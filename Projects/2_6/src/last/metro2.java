/** 
 * 
 * 대략 30줄 정도 지웠고, 크게 바뀐거는
 * 1. Thread -> swing timer로 바꿈
 * 2. paintComponent에서 매번 findAll로 ovals 다시 채우던거 크기 바뀔 때만 계산하는거로 바꿈
 * 3. 경로를 sno 리스트로 들고 매번 findById 하던거 다익스트라 끝날 때 엔티티 리스트로 한 번만 조회해서 저장하는거로 바꿈
 * 4. 생성자는 다익스트라, 우클릭은 dfs 쓰던거 search 함수 하나로 통일했고
 * 이정도? 코드 이해 안가는 부분 있으면 물어봐
 * 
 * **/


package last;

import javax.swing.*;
import javax.swing.Timer;

import main.Util;
import orms.stationEntity;
import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class metro2 extends CFrame {

	final Image img = new ImageIcon("datafiles/metro.png").getImage();
	final double imgW = img.getWidth(null), imgH = img.getHeight(null);

	JLabel mapLabel;
	final JLabel bLabel = lb("역을 우클릭하여 출발역을 선택하세요",
			FG(Color.white), BG(Color.black), HOA(JLabel.CENTER), VEA(JLabel.CENTER), BORDER(sp.em(10, 10, 10, 10)));

	int start = -1, end = -1, selectN = -1;
	List<stationEntity> path = List.of();  
	int totalPix = 0;                       

	Timer anim;
	double step = 0;   
	int seg = 0;      
	int pause = 0;     
	int blink = -1;    

	final JPopupMenu menu = new JPopupMenu();
	final JButton startB = bt("출발", HOA(JButton.LEFT));
	final JButton endB = bt("도착", HOA(JButton.LEFT));

	final Map<Integer, Ellipse2D.Double> ovals = new HashMap<>();
	int lastW = -1, lastH = -1;
	static final double R = 12;

	static Map<Integer, List<int[]>> graph;

	public metro2(String startName, String endName) {
		for (JButton b : new JButton[]{startB, endB}) {
			b.setBorderPainted(false);
			b.setMargin(new Insets(0, 0, 0, 10));
			menu.add(b);
		}
		bLabel.setOpaque(true);
		setFrame("경로 검색2", 550, 800, () -> {});

		if (!startName.isBlank()) start = stationEntity.findBy(e -> e.name.equals(startName)).get(0).sno;
		if (!endName.isBlank())   end   = stationEntity.findBy(e -> e.name.equals(endName)).get(0).sno;
		if (start != -1 && end != -1) search();
	}

	void search() {
		path = dijkstra(start, end);
		double km = totalPix * 0.05;
		bLabel.setText("출발: " + path.get(0).name + " → 도착: " + path.get(path.size() - 1).name
				+ " ( " + (path.size() - 1) + "구간 ) 약 " + new DecimalFormat("#.##").format(km)
				+ " km | 약 " + (int) Math.ceil(km / 40 * 60) + " 분");
		startAnim();
	}

	static Map<Integer, List<int[]>> graph() {
		if (graph != null) return graph;
		graph = new HashMap<>();
		for (int line : new int[]{1, 2, 7}) {
			List<stationEntity> list = stationEntity.findBy(e -> e.line.charAt(1) - '0' == line);
			for (int i = 0; i + 1 < list.size(); i++) link(list.get(i), list.get(i + 1));
		}
		for (String name : "석남,부평구청,인천시청".split(",")) {          // 환승 연결
			List<stationEntity> ss = stationEntity.findBy(e -> e.name.equals(name));
			link(ss.get(0), ss.get(1));
		}
		return graph;
	}

	static void link(stationEntity a, stationEntity b) {
		int w = dist(a, b);
		graph.computeIfAbsent(a.sno, k -> new ArrayList<>()).add(new int[]{b.sno, w});
		graph.computeIfAbsent(b.sno, k -> new ArrayList<>()).add(new int[]{a.sno, w});
	}

	static int dist(stationEntity a, stationEntity b) {
		return (int) Math.hypot(a.x - b.x, a.y - b.y);
	}

	List<stationEntity> dijkstra(int s, int e) {
		Map<Integer, Integer> dist = new HashMap<>(), prev = new HashMap<>();
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		pq.add(new int[]{s, 0});
		dist.put(s, 0);

		while (!pq.isEmpty()) {
			int[] cur = pq.poll();
			if (cur[1] > dist.getOrDefault(cur[0], Integer.MAX_VALUE)) continue;
			if (cur[0] == e) break;
			for (int[] eg : graph().getOrDefault(cur[0], List.of())) {
				int nd = cur[1] + eg[1];
				if (nd < dist.getOrDefault(eg[0], Integer.MAX_VALUE)) {
					dist.put(eg[0], nd);
					prev.put(eg[0], cur[0]);
					pq.add(new int[]{eg[0], nd});
				}
			}
		}
		totalPix = dist.get(e);

		LinkedList<stationEntity> p = new LinkedList<>();       
		for (Integer n = e; n != null; n = n.equals(s) ? null : prev.get(n))
			p.addFirst(stationEntity.findById(n).get());
		return p;
	}


	void startAnim() {
		if (anim != null) anim.stop();                          
		step = 0; seg = 0; pause = 0; blink = -1;
		anim = new Timer(16, e -> {
			if (pause > 0) { pause--; return; }                 // 환승 구간 일시정지
			if (blink >= 0) {                                   // 도착 후 깜빡임
				if (++blink >= 16) {
					anim.stop();
					set(bLabel, FG(Color.yellow));
				}
				mapLabel.repaint();
				return;
			}
			step += 0.02;
			if (step >= 1) {
				step = 0;
				seg++;
				if (seg >= path.size() - 1) {                   // 최종 도착 
					seg = path.size() - 2; step = 1;
					blink = 0;
					anim.setDelay(250);
				} else if (isTransfer(seg)) {
					pause = 45;                                 
				}
			}
			mapLabel.repaint();
		});
		anim.start();
	}

	boolean isTransfer(int i) {
		return path.get(i).name.equals(path.get(i + 1).name);  
	}

	@Override
	protected void desing() {
		mapLabel = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);

				double sx = getWidth() / imgW, sy = getHeight() / imgH;
				updateOvals(sx, sy);

				// 경로선
				g2.setStroke(new BasicStroke(4f));
				for (int i = 0; i + 1 < path.size(); i++) {
					stationEntity a = path.get(i), b = path.get(i + 1);
					g2.setColor(Color.red);
					g2.draw(new Line2D.Double(a.x * sx, a.y * sy, b.x * sx, b.y * sy));
					g2.setColor(Color.orange);
					g2.fill(new Ellipse2D.Double(a.x * sx - 2, a.y * sy - 2, 4, 4));
				}

				if (start != -1) mark(g2, start, "출", sp.color);
				if (end != -1)   mark(g2, end, "도", Color.red);

				if (path.isEmpty()) return;
				if (blink >= 0 && blink % 2 != 0) return;
				stationEntity a = path.get(seg), b = path.get(seg + 1);
				double x = (a.x + (b.x - a.x) * step) * sx;
				double y = (a.y + (b.y - a.y) * step) * sy;
				drawTrain(g2, x, y, Math.atan2(b.y - a.y, b.x - a.x));
			}

			void drawTrain(Graphics2D g, double x, double y, double angle) {
				Graphics2D g2 = (Graphics2D) g.create();        
				g2.translate(x, y);
				g2.rotate(angle + Math.PI / 2);
				g2.drawImage(Util.train, -4, -20, 8, 40, null);
				g2.dispose();
			}

			void mark(Graphics2D g2, int sno, String s, Color c) {
				Ellipse2D.Double o = ovals.get(sno);
				g2.setColor(Color.white);
				g2.setStroke(new BasicStroke(1.5f));
				g2.fill(o);
				g2.setColor(c);
				g2.draw(o);
				g2.setFont(sp.font.deriveFont(Font.BOLD, 13f));
				FontMetrics fm = getFontMetrics(g2.getFont());
				g2.drawString(s, (int) (o.x + fm.stringWidth(s) / 2), (int) o.y + fm.getHeight());
			}
		};
		add(col(0, f(mapLabel), fw(bLabel)));
	}

	void updateOvals(double sx, double sy) {
		if (mapLabel.getWidth() == lastW && mapLabel.getHeight() == lastH) return;
		lastW = mapLabel.getWidth(); lastH = mapLabel.getHeight();
		ovals.clear();
		stationEntity.findAll().forEach(e ->
				ovals.put(e.sno, new Ellipse2D.Double(e.x * sx - R, e.y * sy - R, R * 2, R * 2)));
	}


	@Override
	protected void action() {
		mapLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3) return;
				ovals.entrySet().stream()
						.filter(en -> en.getValue().contains(e.getPoint()))
						.findFirst()
						.ifPresent(en -> {
							selectN = en.getKey();
							menu.show(mapLabel, e.getX(), e.getY());
						});
			}
		});

		ActionListener ac = e -> {
			if (e.getSource() == startB) start = selectN; else end = selectN;
			selectN = -1;
			if (start != -1 && end != -1) search();
			repaint();
		};
		startB.addActionListener(ac);
		endB.addActionListener(ac);

		bLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}

	public static void main(String[] args) {
		Util.start(new metro2("", ""));
	}
}