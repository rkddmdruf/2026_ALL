package test.test3.test3_1;

import javax.swing.*;

import main.Util;
import orms.stationEntity;
import test.test3.Main3;
import test.test3.scdule;
import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;
import static java.lang.Integer.MAX_VALUE;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class metro extends CFrame {

	Image img = new ImageIcon("datafiles/metro.png").getImage();
	double imgX = img.getWidth(null), imgY = img.getHeight(null), scaleX = 1, scaleY = 1, step;
	JLabel label, bLabel = lb("역을 우클릭하여 출발역을 선택하세요", FG(Color.white), BG(Color.black), HOA(JLabel.CENTER), VEA(JLabel.CENTER), BORDER(sp.em(10, 10, 10, 10)));
	List<Integer> path = new ArrayList<>();
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<>();
	int start = -1, end = -1, selectN = -1, total, idx, onOff = -1;
	JPopupMenu menu = new JPopupMenu();
	JButton startB = bt("출발", HOA(JButton.LEFT)), endB = bt("도착", HOA(JButton.LEFT));
	Rectangle resetRect = new Rectangle();
	Thread th;

	Runnable run = () -> {
		try {
			while (idx < path.size() - 1) {
				if ((step += 0.02) >= 1) { step = 0; idx++; }
				label.repaint();
				Thread.sleep(16);
			}
			for (onOff = 0; onOff < 8; onOff++) { repaint(); Thread.sleep(250); }
			set(bLabel, FG(Color.yellow));
		} catch (Exception e) { }
	};

	public metro(String s, String e) {
		start = sno(s); end = sno(e);
		for (JButton b : new JButton[] { startB, endB }) {
			b.setBorderPainted(false);
			b.setMargin(new Insets(0, 0, 0, 10));
			menu.add(b);
		}
		bLabel.setOpaque(true);
		setFrame("경로 검색", 550, 800, () -> { stop(); new Main3().setVisible(true); });
		if (start != -1 && end != -1) play();
	}

	int sno(String n) { return n.isBlank() ? -1 : stationEntity.findBy(e -> e.name.equals(n)).get(0).sno; }

	stationEntity st(int i) { return stationEntity.findById(path.get(i)).get(); }

	void stop() { if (th != null) th.interrupt(); }

	void play() {
		stop();
		path = dijkstra(start, end);
		step = 0; idx = 0; onOff = -1;
		set(bLabel, FG(Color.white));
		double km = total * 0.05;
		bLabel.setText("출발: " + st(0).name + " → 도착: " + st(path.size() - 1).name + " ( " + (path.size() - 1) + "구간 ) 약 "
				+ new DecimalFormat("#.##").format(km) + " km | 약 " + (int) Math.ceil(km / 40 * 60) + " 분");
		(th = new Thread(run)).start();
	}

	@Override
	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				scaleX = getWidth() / imgX; scaleY = getHeight() / imgY;

				FontMetrics fm = getFontMetrics(g2.getFont());
				resetRect.setBounds(getWidth() - 10 - fm.stringWidth("초기화"), 10, fm.stringWidth("초기화"), fm.getHeight());
				g2.setColor(Color.red);
				g2.drawString("초기화", resetRect.x, resetRect.y + fm.getHeight());

				stationEntity.findAll().forEach(e -> ovals.put(e.sno, new Ellipse2D.Double(e.x * scaleX - 12, e.y * scaleY - 12, 24, 24)));

				g2.setStroke(new BasicStroke(4f));
				for (int i = 0; i < path.size() - 1; i++) {
					stationEntity s1 = st(i), s2 = st(i + 1);
					g2.setColor(Color.red);
					g2.draw(new Line2D.Double(s1.x * scaleX, s1.y * scaleY, s2.x * scaleX, s2.y * scaleY));
					g2.setColor(Color.orange);
					g2.fill(new Ellipse2D.Double(s1.x * scaleX - 2, s1.y * scaleY - 2, 4, 4));
				}

				seD(g2, start, "출", sp.color);
				seD(g2, end, "도", Color.red);

				boolean moving = idx < path.size() - 1;
				if (path.isEmpty() || (!moving && onOff % 2 != 0)) return;
				stationEntity s1 = st(Math.min(idx, path.size() - 2)), s2 = st(Math.min(idx, path.size() - 2) + 1);
				double t = moving ? step : 1;
				drawTrain(g2, (s1.x + (s2.x - s1.x) * t) * scaleX, (s1.y + (s2.y - s1.y) * t) * scaleY,
						Math.atan2(s2.y - s1.y, s2.x - s1.x));
			}

			private void drawTrain(Graphics2D g2, double x, double y, double angle) {
				g2.translate(x, y);
				g2.rotate(angle + Math.PI / 2);
				g2.drawImage(Util.train, -4, -20, 8, 40, null);
			}

			private void seD(Graphics2D g2, int sno, String s, Color c) {
				if (sno == -1) return;
				Ellipse2D.Double oval = ovals.get(sno);
				g2.setColor(Color.white);
				g2.setStroke(new BasicStroke(1.5f));
				g2.fill(oval);
				g2.setColor(c);
				g2.draw(oval);
				g2.setFont(sp.font.deriveFont(1, 13f));
				FontMetrics fm = getFontMetrics(g2.getFont());
				g2.drawString(s, (int) (oval.x + fm.stringWidth(s) / 2), (int) oval.y + fm.getHeight());
			}
		};
		add(col(0, f(label), fw(bLabel)));
	}

	public List<Integer> dijkstra(int s, int e) {
		Map<Integer, List<int[]>> map = new HashMap<>();
		for (char c : "127".toCharArray()) {
			List<stationEntity> list = stationEntity.findBy(x -> x.line.charAt(1) == c);
			for (int i = 0; i + 1 < list.size(); i++) link(map, list.get(i), list.get(i + 1));
		}
		for (String n : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> ss = stationEntity.findBy(x -> x.name.equals(n));
			link(map, ss.get(0), ss.get(1));
		}

		Map<Integer, Integer> parents = new HashMap<>(), dist = new HashMap<>();
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		pq.add(new int[] { s, 0 });
		dist.put(s, 0);
		while (!pq.isEmpty()) {
			int[] cur = pq.poll();
			if (cur[1] > dist.getOrDefault(cur[0], MAX_VALUE)) continue;
			if (cur[0] == e) break;
			for (int[] eg : map.getOrDefault(cur[0], List.of())) {
				int nd = cur[1] + eg[1];
				if (nd >= dist.getOrDefault(eg[0], MAX_VALUE)) continue;
				dist.put(eg[0], nd);
				parents.put(eg[0], cur[0]);
				pq.add(new int[] { eg[0], nd });
			}
		}
		total = dist.get(e);

		LinkedList<Integer> p = new LinkedList<>();
		for (Integer n = e; n != null; n = parents.get(n)) p.addFirst(n);
		return p;
	}

	private void link(Map<Integer, List<int[]>> map, stationEntity a, stationEntity b) {
		int w = (int) Math.hypot(a.x - b.x, a.y - b.y);
		map.computeIfAbsent(a.sno, k -> new ArrayList<>()).add(new int[] { b.sno, w });
		map.computeIfAbsent(b.sno, k -> new ArrayList<>()).add(new int[] { a.sno, w });
	}

	@Override
	protected void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (resetRect.contains(e.getPoint())) {
					stop();
					path = new ArrayList<>();
					start = end = -1; step = 0; idx = 0; onOff = -1;
					set(bLabel, FG(Color.white), TEXT("역을 우클릭하여 출발역을 선택하세요"));
				}
				if (e.getButton() == MouseEvent.BUTTON3 && (start == -1 || end == -1))
					ovals.forEach((k, v) -> {
						if (!v.contains(e.getPoint())) return;
						selectN = k;
						menu.show(label, e.getX(), e.getY());
					});
				repaint();
			}
		});

		ActionListener ac = e -> {
			if (e.getSource() == startB) start = selectN; else end = selectN;
			menu.setVisible(false);
			selectN = -1;
			if (start != -1 && end != -1) play();
			repaint();
		};
		startB.addActionListener(ac);
		endB.addActionListener(ac);

		bLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (bLabel.getForeground() != Color.yellow) return;
				new scdule(start, end).setVisible(true);
				dispose();
			}
		});
	}

	public static void main(String[] args) {
		Util.start(new metro("", ""));
	}
}