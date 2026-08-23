package test.test4;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import main.Util;
import orms.stationEntity;
import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Stream;

public class metro extends CFrame{

	Image img = new ImageIcon("datafiles/metro.png").getImage();
	double imgX = img.getWidth(null);
	double imgY = img.getHeight(null);
	JLabel label, bLabel = lb("역을 우클릭하여 출발역을 선택하세요", FG(Color.white), BG(Color.black), HOA(JLabel.CENTER), VEA(JLabel.CENTER), BORDER(sp.em(10, 10, 10, 10)));
	List<Integer> bfs = new ArrayList<>();
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<>();
	int start = -1, end = -1, selectN = -1;
	JPopupMenu menu = new JPopupMenu();
	JButton startB = bt("출발", HOA(JButton.LEFT));
	JButton endB = bt("도착", HOA(JButton.LEFT));
	List<Integer> totalPix = Arrays.asList(0);
	double step = 0;
	int stationNumber = 0;
	int onOff = -1;

	Thread thread = new Thread(() -> {
		try {
			while(true) {
			    step += 0.02;
			    if(step >= 1) {
			        step = 0;
			        stationNumber++;
			        if(stationNumber >= bfs.size() - 1) {
			            break;
			        }
			    }
			    label.repaint();
			    Thread.sleep(16);
			}
			repaint();
			for(int i = 0; i < 8; i++) {
				onOff ++;
				repaint();
				Thread.sleep(250);
			}
			set(bLabel, FG(Color.yellow));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	});

	public metro(String start, String end) {
		if(!start.isBlank()) this.start = stationEntity.findBy(e -> e.name.equals(start)).get(0).sno;
		if(!end.isBlank()) this.end = stationEntity.findBy(e -> e.name.equals(end)).get(0).sno;
		startB.setBorderPainted(false);
		startB.setMargin(new Insets(0, 0, 0, 10));

		endB.setBorderPainted(false);
		endB.setMargin(new Insets(0, 0, 0, 10));

		menu.add(startB);
		menu.add(endB);

		bLabel.setOpaque(true);
		setFrame("경로 검색", 550, 800, () -> {});
		if(!start.isBlank() && !end.isBlank()) {
			bfs = dijkstar(this.start, this.end);
			settingBLabel();
			thread.start();
		}
	}

	@Override
	protected void desing() {
		label = new JLabel() {
			double r = 12;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);

				double scaleX = getWidth() / imgX;
				double scaleY = getHeight() / imgY;

				stationEntity.findAll().forEach(e -> ovals.put(e.sno, new Ellipse2D.Double(e.x * scaleX - r, e.y * scaleY - r, r*2, r*2)));

				g2.setStroke(new BasicStroke(4f));
				for(int i = 0; i < bfs.size() - 1; i++) {
					stationEntity s1 = stationEntity.findById(bfs.get(i)).get();
					stationEntity s2 = stationEntity.findById(bfs.get(i + 1)).get();
					g2.setColor(Color.red);
					g2.draw(new Line2D.Double(s1.x * scaleX, s1.y * scaleY, s2.x * scaleX, s2.y * scaleY));
					g2.setColor(Color.orange);
					g2.fill(new Ellipse2D.Double(s1.x * scaleX - 2, s1.y * scaleY - 2, 4, 4));
				}

				if(start != -1) seD(g2, "출");
				if(end != -1) seD(g2, "도");

		        if(!bfs.isEmpty() && stationNumber < bfs.size() - 1) {
		            stationEntity s1 = stationEntity.findById(bfs.get(stationNumber)).get();
		            stationEntity s2 = stationEntity.findById(bfs.get(stationNumber + 1)).get();

		            double x = (s1.x + (s2.x - s1.x) * step) * scaleX;
		            double y = (s1.y + (s2.y - s1.y) * step) * scaleY;

		            drawTrain(g2, x, y, Math.atan2(s2.y - s1.y, s2.x - s1.x));
		        }

		        if(onOff == -1 || onOff % 2 != 0) return;
		        stationEntity s1 = stationEntity.findById(bfs.get(bfs.size() - 2)).get();
		        stationEntity s2 = stationEntity.findById(bfs.get(bfs.size() - 1)).get();

		        double x = (s1.x + (s2.x - s1.x)) * scaleX;
		        double y = (s1.y + (s2.y - s1.y)) * scaleY;

		        drawTrain(g2, x, y, Math.atan2(s2.y - s1.y, s2.x - s1.x));
			}

			private void drawTrain(Graphics2D g2, double x, double y, double angle) {
				g2.translate(x, y);
				g2.rotate(angle + Math.PI / 2);

				int w = 8;
				int h = 40;
				g2.drawImage(Util.train, -w/2, -h/2, w, h, null);
			}

			private void seD(Graphics2D g2, String s) {
				Ellipse2D.Double oval = ovals.get(s.equals("출") ? start : end);
				g2.setColor(Color.white);
				g2.setStroke(new BasicStroke(1.5f));
				g2.fill(oval);
				g2.setColor(s.equals("출") ? sp.color : Color.red);
				g2.draw(oval);
				g2.setFont(sp.font.deriveFont(1).deriveFont(13f));
				g2.drawString(s, (int) (oval.x + getFontMetrics(g2.getFont()).stringWidth(s) / 2), (int) oval.y + getFontMetrics(g2.getFont()).getHeight());

			}
		};
		add(col(0, f(label), fw(bLabel)));
	}

	public List<Integer> dijkstar(int start, int end) {
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, List<int[]>> map = new HashMap<>();
		for(int i : new int[] {1, 2, 7}) {
			Function<String, Integer> f = value -> Integer.parseInt(value.substring(1, 2));
			List<stationEntity> list = stationEntity.findBy(e -> f.apply(e.line).equals(i));
			for(int s = 0; s < list.size(); s ++) {
				stationEntity nowStation = list.get(s);
				List<stationEntity> ns = new ArrayList<>();
				if(s - 1 >= 0) ns.add(list.get(s -1));
				if(s + 1 < list.size()) ns.add(list.get(s + 1));
				for(stationEntity n : ns) {
					map.computeIfAbsent(nowStation.sno, k -> new ArrayList<>()).add(new int[] {n.sno, distance(nowStation, n)});
				}
			}
		}
		for(String s : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> ss = stationEntity.findBy(e -> e.name.equals(s));
			stationEntity s1 = ss.get(0), s2 = ss.get(1);
			int weight = distance(s1, s2);
			map.computeIfAbsent(s1.sno, k -> new ArrayList<>()).add(new int[] {s2.sno, weight});
			map.computeIfAbsent(s2.sno, k -> new ArrayList<>()).add(new int[] {s1.sno, weight});
		}

		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		pq.add(new int[] {start, 0});
		dist.put(start, 0);

		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for(int[] eg : map.getOrDefault(p, new ArrayList<>())){
				int next = eg[0], weight = eg[1];
				int newDist = d + weight;
				if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
					dist.put(next, newDist);
					parents.put(next, p);
					pq.add(new int[]{next, newDist});
				}
			}
		}
		totalPix.set(0, dist.get(end));
	    return Stream
	    		.iterate(end, n -> n != null, n -> n.equals(start) ? null : parents.get(n))
	    		.collect(LinkedList::new, LinkedList::addFirst, (a, b) -> a.addAll(b));
	}

	private int distance(stationEntity a, stationEntity b) {
		return (int) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}

	@Override
	protected void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				repaint();
				if(e.getButton() != MouseEvent.BUTTON3) return;
				for(int i : ovals.keySet()) {
					if(ovals.get(i).contains(e.getPoint())) {
						selectN = i;
						if(start != -1 && end != -1) break;
						menu.setVisible(true);
						menu.show(label, e.getX(), e.getY());
					}
				}
			}
		});
		ActionListener ac = e -> {
			if(e.getSource() == startB) start = selectN;
			else end = selectN;
			menu.setVisible(false);
			selectN = -1;
			if(start != -1 && end != -1) {
				bfs = Util.bfs(start, end, totalPix);
				settingBLabel();
				thread.start();
			}
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

	private void settingBLabel() {
		if(bfs.isEmpty()) return;
		double dist = (totalPix.get(0) * 0.05);
		bLabel.setText("출발: " + stationEntity.findById(start).get().name + " → 도착: " + stationEntity.findById(end).get().name +
				" ( " + (bfs.size() - 1) + "구간 ) 약 " + new DecimalFormat("#.##").format(dist) + " km | 약 " + (int) Math.ceil(dist / 40*60)+ " 분");
	}
	
	public static void main(String[] args) {
		Util.start(new metro("", ""));
	}
}
