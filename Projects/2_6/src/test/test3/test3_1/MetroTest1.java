package test.test3.test3_1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.*;

import main.Util;
import orms.*;
import test.test3.scdule;

public class MetroTest1 extends CFrame {
	
	List<Integer> path = new ArrayList<Integer>();
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<Integer, Ellipse2D.Double>();
	Image img = new ImageIcon("datafiles/metro.png").getImage();
	double imgX = img.getWidth(null), imgY = img.getHeight(null), step = 0;
	int start = -1, end = start, total = -1, idx = 0, onOff = -1, selectN = -1;
	JLabel bLabel = lb("역을 우클릭하여 출발역을 선택하세요", FG(Color.white), BG(Color.black), FONT(sp.font.deriveFont(13f)), BORDER(sp.em(10, 10, 10, 10)), HOA(JLabel.CENTER));
	JLabel l;
	Thread thread;
	JButton startB = bt("출발", HOA(JButton.LEFT)), endB = bt("도착", HOA(JButton.LEFT));
	JPopupMenu menu = new JPopupMenu();
	Runnable run = () -> {
		try {
			System.out.println(path.size());
			while (idx < path.size() - 1) {
				if ((step += 0.02) >= 1) { step = 0; idx++; }
				l.repaint();
				Thread.sleep(16);
			}
			for (onOff = 0; onOff < 8; onOff++) { repaint(); Thread.sleep(250); }
			set(bLabel, FG(Color.yellow));
		} catch (Exception e) { }
	};
	public MetroTest1(int s, int e) {
		start = s; end = e;
		for (JButton b : new JButton[] { startB, endB }) {
			b.setBorderPainted(false);
			b.setMargin(new Insets(0, 0, 0, 10));
			menu.add(b);
		}
		bLabel.setOpaque(true);
		setFrame("경로검색", 550, 800, () -> {});
		if(start != -1 && end != -1) play();
	}
	
	protected void desing() {
		l = new JLabel() {
			int r = 12;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				stationEntity.findAll().forEach(e -> ovals.put(e.sno, new Ellipse2D.Double(sx(e.x) - r, sy(e.y) - r, r * 2, r * 2)));
				
				if(path.isEmpty()) return;
				
				g2.setStroke(new BasicStroke(4f));
				g2.setColor(Color.red);
				for(int i = 0; i + 1 < path.size(); i++) {
					stationEntity s1 = st(path.get(i));
					stationEntity s2 = st(path.get(i + 1));
					g2.drawLine(sx(s1.x), sy(s1.y), sx(s2.x), sy(s2.y));
				}
				g2.setColor(Color.orange);
				for(int i = 0; i < path.size(); i++) {
					stationEntity s1 = st(path.get(i));
					g2.fillOval(sx(s1.x) -4, sy(s1.y) - 2, 4, 4);
				}
				
				seD(g2, start, "출", sp.color);
				seD(g2, end, "도", Color.red);
				
				boolean moving = idx < path.size() - 1;
				if (path.isEmpty() || (!moving && onOff % 2 != 0)) return;
				stationEntity s1 = st(path.get(Math.min(idx, path.size() - 2))); 
				stationEntity s2 = st(path.get(Math.min(idx + 1, path.size() - 1))); 
				double t = moving ? step : 1;
				drawTrain(g2, sx((s1.x + (s2.x - s1.x) * t)), sy((s1.y + (s2.y - s1.y) * t)),
						Math.atan2(s2.y - s1.y, s2.x - s1.x));
			}
			
			private void drawTrain(Graphics2D g2, double x, double y, double angle) {
				g2.translate(x, y);
				g2.rotate(angle + Math.PI / 2);
				g2.drawImage(Util.train, -4, -20, 8, 40, null);
			}
			
			private void seD(Graphics2D g2,int sno, String s, Color c) {
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

			private int sx(double x) {
				return (int) (x * (getWidth() / imgX));
			}
			private int sy(double y) {
				return (int) (y * (getHeight() / imgY));
			}
		};
		add(col(0, f(l), fw(bLabel)));
	}

	@Override
	protected void action() {
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if(thread != null) thread.interrupt();
					path = new ArrayList<>();
					start = end = -1; step = 0; idx = 0; onOff = -1;
					set(bLabel, FG(Color.white), TEXT("역을 우클릭하여 출발역을 선택하세요"));
				}
				if (e.getButton() == MouseEvent.BUTTON3 && (start == -1 || end == -1))
					ovals.forEach((k, v) -> {
						if (!v.contains(e.getPoint())) return;
						selectN = k;
						menu.show(l, e.getX(), e.getY());
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
	void play() {
		threadStop();
		path = dfs(start, end);
		step = 0; idx = 0; onOff = -1;
		set(bLabel, FG(Color.white));
		double km = total * 0.05;
		bLabel.setText("출발: " + st(path.get(0)).name + " → 도착: " + st(path.size() - 1).name + " ( " + (path.size() - 1) + "구간 ) 약 "
				+ new DecimalFormat("#.##").format(km) + " km | 약 " + (int) Math.ceil(km / 40 * 60) + " 분");
		(thread = new Thread(run)).start();
	}

	private void threadStop() {
		if(thread != null) thread.interrupt();
	}

	void link(Map<Integer, List<int[]>> map, stationEntity a, stationEntity b) {
		int w = (int) Math.hypot(a.x - b.x, a.y - b.y);
		map.computeIfAbsent(a.sno, k -> new ArrayList<>()).add(new int[] {b.sno, w});
		map.computeIfAbsent(b.sno, k -> new ArrayList<>()).add(new int[] {a.sno, w});
	}
	
	private stationEntity st(int sno) {
		return stationEntity.findById(sno).get();
	}
	private List<Integer> dfs(int start, int end) {
		Map<Integer, List<int[]>> map = new HashMap<Integer, List<int[]>>();
		for(char c : "127".toCharArray()) {
			List<stationEntity> list = stationEntity.findBy(e -> e.line.charAt(1) == c);
			for(int i = 0; i + 1 < list.size(); i++) link(map, list.get(i), list.get(i + 1));
		}
		for(String s : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> ss = stationEntity.findBy(e -> e.name.equals(s));
			link(map, ss.get(0), ss.get(1));
		}
		Map<Integer, Integer> parents = new HashMap<Integer, Integer>(), dist = new HashMap<Integer, Integer>();
		
		PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a , b) -> a[1] - b[1]);
		dist.put(start, 0);
		pq.add(new int[] {start, 0});
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for(int[] egs : map.getOrDefault(p, List.of())) {
				int next = egs[0], weight = egs[1];
				int newDist = d + weight;
				if(newDist >= dist.getOrDefault(next, Integer.MAX_VALUE)) continue;
				dist.put(next, newDist);
				parents.put(next, p);
				pq.add(new int[] {next, newDist});
			}
		}
		
		total = dist.get(end);
		LinkedList<Integer> path = new LinkedList<>();
		for(Integer n = end; n != null; n = parents.getOrDefault(n, null)) path.addFirst(n);
		return path;
	}

	
	public static void main(String[] args) {
		Util.start(new MetroTest1(1, 5));
	}
}