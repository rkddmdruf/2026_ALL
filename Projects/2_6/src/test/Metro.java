package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import main.*;
import orms.*;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Metro extends CFrame{
	
	Image img = new ImageIcon("datafiles/metro.png").getImage();
	double imgX = img.getWidth(null), imgY = img.getHeight(null);
	List<Integer> bfs = new ArrayList<>();
	JLabel label;
	JLabel bLabel = lb("역을 우클릭하여 출발역을 선택하세요", FG(Color.white), BG(Color.black), HOA(JLabel.CENTER), BORDER(sp.em(10, 10, 10, 10)), FONT(sp.font.deriveFont(13f)));
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<>();
	JPopupMenu menu = new JPopupMenu();
	JButton startB = bt("출발", HOA(JLabel.LEFT)), endB = bt("도착", HOA(JLabel.LEFT));
	
	int start = -1, end = -1, selectN = - 1;
	public Metro() {
		startB.setBorderPainted(false);
		startB.setMargin(new Insets(0, 0, 0, 10));
		endB.setBorderPainted(false);
		endB.setMargin(new Insets(0, 0, 0, 10));
		
		menu.add(startB);
		menu.add(endB);
		
		bLabel.setOpaque(true);
		
		setFrame("경로검색", 600, 850, () -> {});
	}

	@Override
	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				double scaleX = getWidth() / imgX, scaleY = getHeight() / imgY;
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				int r = 12;
				stationEntity.findAll().forEach(e -> ovals.put(e.sno, new Ellipse2D.Double(e.x * scaleX - r, e.y * scaleX - r, r*2, r*2)));
				
				g2.setStroke(new BasicStroke(4f));
				for(int i = 0; i < bfs.size(); i++) {
					g2.setColor(Color.red);
					stationEntity s1 = stationEntity.findById(bfs.get(i)).get();
					stationEntity s2 = stationEntity.findById(bfs.get(Math.max(i - 1, 0))).get();
					g2.draw(new Line2D.Double(s1.x * scaleX, s1.y * scaleY, s2.x * scaleX, s2.y * scaleY));
				}
				
				g2.setColor(Color.orange);
				bfs.stream().map(e -> stationEntity.findById(e).get())
				.forEach(s -> g2.fill(new Ellipse2D.Double(s.x * scaleX - 2, s.y * scaleY - 2, 4, 4)));
				
				if(start != -1) me(g2, start, "출");
				if(end != -1) me(g2, end, "도");
			}
			
			private void me(Graphics2D g2, int n, String s) {
				Ellipse2D.Double o = ovals.get(n);
				g2.setColor(Color.white);
				g2.fill(o);
				g2.setColor(s.equals("출") ? sp.color : Color.red);
				g2.draw(o);
				g2.setFont(sp.font.deriveFont(1).deriveFont(13f));
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString(s, (int) o.x + (fm.stringWidth(s) / 2), (int) o.y + (fm.getHeight()));
			}
		};
		label.add(menu);
		add(col(0, f(label), fw(bLabel)));
	}

	private List<Integer> dijkstar(int start, int end){
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<Integer, Integer>();
		Map<Integer, List<int[]>> map = new HashMap<>();
		for(int line : new int[] {1, 2, 7}) {
			List<stationEntity> list = stationEntity.findBy(e -> e.line.substring(1, 2).equals(String.valueOf(line)));
			for(int i = 0; i < list.size(); i++) {
				stationEntity nowS = list.get(i);
				List<stationEntity> ns = new ArrayList<>();
				if(i - 1 >= 0) ns.add(list.get(i - 1));
				if(i + 1 < list.size()) ns.add(list.get(i + 1));
				for(stationEntity n : ns) {
					map.computeIfAbsent(nowS.sno, k -> new ArrayList<>()).add(new int[] {n.sno, (int) Math.sqrt(Math.pow(nowS.x - n.x, 2) + Math.pow(nowS.y - n.y, 2))});
				}
			}
		}
		for(String s : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> list = stationEntity.findBy(e -> e.name.equals(s));
			stationEntity s1 = list.get(0);
			stationEntity s2 = list.get(1);
			map.computeIfAbsent(s1.sno, k -> new ArrayList<>()).add(new int[] {s2.sno, (int) Math.sqrt(Math.pow(s1.x - s2.x, 2) + Math.pow(s1.y - s2.y, 2))});
			map.computeIfAbsent(s2.sno, k -> new ArrayList<>()).add(new int[] {s1.sno, (int) Math.sqrt(Math.pow(s1.x - s2.x, 2) + Math.pow(s1.y - s2.y, 2))});
		}
		
		PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[1] - b[1]);
		pq.add(new int[] {start, 0});
		dist.put(start, 0);
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for(int[] es : map.getOrDefault(p, new ArrayList<>())) {
				int next = es[0], weight = es[1];
				int newDist = weight + d;
				if(newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
					dist.put(next, newDist);
	                parents.put(next, p);
	                pq.add(new int[]{next, newDist});
				}
			}
		}

		return Stream.iterate(end, n -> n != null, n -> n.equals(start) ? null : parents.get(n))
				.collect(LinkedList::new, LinkedList::addFirst, (a, b) -> a.addAll(b));
	}
	@Override
	protected void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON3) return;
				List<Integer> key = new ArrayList<>(ovals.keySet());
				key.forEach(k -> {
					if(ovals.get(k).contains(e.getPoint())) {
						selectN = k;
						menu.show(label, e.getX(), e.getY());
					}
				});
			}
		});
		ActionListener ac = e -> {
			if(e.getSource() == startB) start = selectN;
			if(e.getSource() == endB  ) end   = selectN;
			selectN = -1;
			menu.setVisible(false);
			if(start != -1 && end != -1) {
				bfs.clear();
				bfs.addAll(dijkstar(start, end));
			}
			repaint();
		};
		startB.addActionListener(ac);
		endB.addActionListener(ac);
	}

	public static void main(String[] args) {
		Util.start(new Metro());
	}
}