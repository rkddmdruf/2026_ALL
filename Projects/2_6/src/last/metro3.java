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

public class metro3 extends CFrame {

	Image img = new ImageIcon("datafiles/metro.png").getImage();
	JLabel mapLabel;
	JLabel bLabel = lb("역을 우클릭하여 출발역을 선택하세요", FG(Color.white), BG(Color.black), HOA(JLabel.CENTER), VEA(JLabel.CENTER), BORDER(sp.em(10, 10, 10, 10)));

	int start = -1, end = -1, selectN = -1, totalPix = 0;
	List<stationEntity> path = List.of();
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<>();

	Timer anim;
	double step = 0;
	int seg = 0, pause = 0, blink = -1;

	JPopupMenu menu = new JPopupMenu();
	JButton startB = bt("출발", HOA(JButton.LEFT)), endB = bt("도착", HOA(JButton.LEFT));

	public metro3(String s, String e) {
		for (JButton b : new JButton[]{startB, endB}) {
			b.setBorderPainted(false);
			b.setMargin(new Insets(0, 0, 0, 10));
			menu.add(b);
		}
		bLabel.setOpaque(true);
		setFrame("경로 검색3", 550, 800, () -> {});
		if (!s.isBlank()) start = stationEntity.findBy(x -> x.name.equals(s)).get(0).sno;
		if (!e.isBlank()) end = stationEntity.findBy(x -> x.name.equals(e)).get(0).sno;
		if (start != -1 && end != -1) search();
	}

	void search() {
		path = dijkstra(start, end);
		for(int i = 0; i < path.size(); i++) {
			var s1 = path.get(i);
			var s2 = path.get(Math.min(path.size() - 1, i + 1));
			
			System.out.println(s1.name);
			if(s1.name.equals(s2.name) && i != path.size() - 1) {
				System.out.println("[" + s1.line.substring(0, 2) + "->" + s2.line.substring(0, 2) + "환승]");
			}
		}
		double km = totalPix * 0.05;
		bLabel.setText("출발: " + path.get(0).name + " → 도착: " + path.get(path.size() - 1).name
				+ " ( " + (path.size() - 1) + "구간 ) 약 " + new DecimalFormat("#.##").format(km)
				+ " km | 약 " + (int) Math.ceil(km / 40 * 60) + " 분");
		if (anim != null) anim.stop();
		step = 0; seg = 0; pause = 0; blink = -1;
		anim = new Timer(16, ev -> {
			if (pause > 0) { pause--; return; }
			if (blink >= 0) {
				if (++blink >= 8) { anim.stop(); set(bLabel, FG(Color.yellow)); }
			} else if ((step += 0.02) >= 1) {
				step = 0;
				if (++seg >= path.size() - 1) { seg = path.size() - 2; step = 1; blink = 0; anim.setDelay(250); }
				else if (path.get(seg).name.equals(path.get(seg + 1).name)) pause = 45;   // 환승 정지
			}
			mapLabel.repaint();
		});
		anim.start();
	}

	List<stationEntity> dijkstra(int s, int e) {
		Map<Integer, List<int[]>> g = new HashMap<>();
		for (int l : new int[]{1, 2, 7}) {
			List<stationEntity> ls = stationEntity.findBy(x -> x.line.charAt(1) - '0' == l);
			for (int i = 0; i + 1 < ls.size(); i++) link(g, ls.get(i), ls.get(i + 1));
		}
		for (String n : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> ss = stationEntity.findBy(x -> x.name.equals(n));
			link(g, ss.get(0), ss.get(1));
		}

		Map<Integer, Integer> dist = new HashMap<>(), prev = new HashMap<>();
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		pq.add(new int[]{s, 0});
		dist.put(s, 0);
		while (!pq.isEmpty()) {
			int[] c = pq.poll();
			if (c[1] > dist.getOrDefault(c[0], Integer.MAX_VALUE)) continue;
			if (c[0] == e) break;
			for (int[] eg : g.getOrDefault(c[0], List.of())) {
				int nd = c[1] + eg[1];
				if (nd < dist.getOrDefault(eg[0], Integer.MAX_VALUE)) {
					dist.put(eg[0], nd);
					prev.put(eg[0], c[0]);
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

	void link(Map<Integer, List<int[]>> g, stationEntity a, stationEntity b) {
		int w = (int) Math.hypot(a.x - b.x, a.y - b.y);
		g.computeIfAbsent(a.sno, k -> new ArrayList<>()).add(new int[]{b.sno, w});
		g.computeIfAbsent(b.sno, k -> new ArrayList<>()).add(new int[]{a.sno, w});
	}

	@Override
	protected void desing() {
		mapLabel = new JLabel() {
			@Override
			protected void paintComponent(Graphics gr) {
				super.paintComponent(gr);
				Graphics2D g2 = (Graphics2D) gr;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				double sx = getWidth() / (double) img.getWidth(null), sy = getHeight() / (double) img.getHeight(null), r = 12;

				stationEntity.findAll().forEach(x -> ovals.put(x.sno, new Ellipse2D.Double(x.x * sx - r, x.y * sy - r, r*2, r*2)));

				g2.setStroke(new BasicStroke(4f));
				for (int i = 0; i + 1 < path.size(); i++) {
					stationEntity a = path.get(i), b = path.get(i + 1);
					g2.setColor(Color.red);
					g2.draw(new Line2D.Double(a.x * sx, a.y * sy, b.x * sx, b.y * sy));
					g2.setColor(Color.orange);
					g2.fill(new Ellipse2D.Double(a.x * sx - 2, a.y * sy - 2, 4, 4));
				}

				if (start != -1) mark(g2, start, "출", sp.color);
				if (end != -1) mark(g2, end, "도", Color.red);

				if (path.isEmpty() || (blink >= 0 && blink % 2 != 0)) return;
				stationEntity a = path.get(seg), b = path.get(seg + 1);
				Graphics2D t = (Graphics2D) g2.create();
				t.translate((a.x + (b.x - a.x) * step) * sx, (a.y + (b.y - a.y) * step) * sy);
				t.rotate(Math.atan2(b.y - a.y, b.x - a.x) + Math.PI / 2);
				t.drawImage(Util.train, -4, -20, 8, 40, null);
				t.dispose();
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

	@Override
	protected void action() {
		mapLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() != MouseEvent.BUTTON3) return;
				for (var en : ovals.entrySet())
					if (en.getValue().contains(e.getPoint())) {
						selectN = en.getKey();
						menu.show(mapLabel, e.getX(), e.getY());
						break;
					}
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
	}

	public static void main(String[] args) {
		Util.start(new metro3("", ""));
	}
}