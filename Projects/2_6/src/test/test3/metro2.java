package test.test3;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.*;
import orms.stationEntity;
import uitls.*;
import static uitls.BoxPanel.*;
import static uitls.Properties.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Stream;

public class metro2 extends CFrame{

	Image img = new ImageIcon("datafiles/metro.png").getImage();
	JLabel label;
	JLabel bLabel = lb("역을 우클릭하여 출발역을 선택하세요", FONT(getter.font.deriveFont(14f)), FG(Color.white), BG(Color.black), BORDER(getter.em(5, 5, 5, 5)), HOA(JLabel.CENTER));
	Map<Integer, Ellipse2D.Double> ovals = new LinkedHashMap<>();
	List<Integer> bfs = new ArrayList<>();
	public metro2() {
		bLabel.setOpaque(true);
		System.out.println(bfs = dijkstar(1, 34));
		setFrame("경로 검색", 600, 800, () -> {});
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
				
				double scaleX = (double) getWidth() / img.getWidth(null);
				double scaleY = (double) getHeight() / img.getHeight(null);
				int r = 12;
				stationEntity.findAll().forEach(e -> ovals.put(e.sno, new Ellipse2D.Double(e.x * scaleX - r, e.y * scaleY - r, r*2, r*2)));
				
				for(int i = 0; i < bfs.size() - 1; i++) {
					stationEntity s1 = stationEntity.findById(bfs.get(i)).get();
					stationEntity s2 = stationEntity.findById(bfs.get(i + 1)).get();
					g2.setColor(Color.red);
					g2.setStroke(new BasicStroke(4f));
					g2.draw(new Line2D.Double(s1.x * scaleX, s1.y * scaleY, s2.x * scaleX, s2.y * scaleY));
					g2.setColor(Color.orange);
					g2.fill(new Ellipse2D.Double(s1.x * scaleX - 2.5, s1.y * scaleY - 2.5, 5, 5));
				}
			}
		};
		add(col(0, f(label), fw(bLabel)));
	}

	public List<Integer> dijkstar(int start, int end){
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, List<int[]>> map = new HashMap<>();
		for(int lineI : new int[] {1, 2, 7}) {
			List<stationEntity> list = stationEntity.findBy(e -> {
				int n = Integer.parseInt(e.line.substring(1));
				return n >= lineI * 1000 && n <= (lineI + 1) * 1000;
			});
			for(int i = 0; i < list.size(); i++) {
				stationEntity nowStation = list.get(i);
				List<stationEntity> ns = new ArrayList<>();
				if(i - 1 >= 0) ns.add(list.get(i - 1));
				if(i + 1 < list.size()) ns.add(list.get(i + 1));
				for(stationEntity s : ns) {
					map.computeIfAbsent(nowStation.sno, k -> new ArrayList<>())
					.add(new int[] {s.sno, (int) Math.sqrt(Math.pow(nowStation.x - s.x, 2) + Math.pow(nowStation.y - s.y, 2))});
				}
			}
		}
		for(String str : "석남,인천시청,부평구청".split(",")) {
			List<stationEntity> s = stationEntity.findBy(e -> e.name.equals(str));
			map.computeIfAbsent(s.get(0).sno, k -> new ArrayList<>()).add(new int[] {s.get(1).sno, (int) Math.sqrt(Math.pow(s.get(0).x - s.get(1).x, 2) + Math.pow(s.get(0).y - s.get(1).y, 2))});
			map.computeIfAbsent(s.get(1).sno, k -> new ArrayList<>()).add(new int[] {s.get(0).sno, (int) Math.sqrt(Math.pow(s.get(0).x - s.get(1).x, 2) + Math.pow(s.get(0).y - s.get(1).y, 2))});
		}
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		dist.put(start, 0);
		pq.add(new int[] {start, 0});
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for(int[] es : map.getOrDefault(p, new ArrayList<>())) {
				int next = es[0], weight = es[1];
				int newDist = d + weight;
				if(newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
	                dist.put(next, newDist);
	                parents.put(next, p);
	                pq.add(new int[]{next, newDist});
				}
			}
		}
		
		
		return Stream.iterate(end, c -> c != null, c -> parents.getOrDefault(c, null)).collect(LinkedList::new, LinkedList::addFirst, (a, b) -> a.addAll(b));
	}
	@Override
	protected void action() {
		
	}
	
	public static void main(String[] args) {
		Util.start(new metro2());
	}
}
