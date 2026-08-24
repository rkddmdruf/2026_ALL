package test;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;

import javax.swing.*;

import com.sun.jdi.Value;

import java.util.List;
import main.Util;
import orms.*;

public class metrot extends CFrame {
	
	stationEntity se1 = stationEntity.findById(1).get();
	stationEntity se2 = stationEntity.findById(30).get();
	
	Image img = new ImageIcon("datafiles/metro.png").getImage();
	
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<Integer, Ellipse2D.Double>();
	JLabel label = new JLabel();
	JLabel bLabel = new JLabel("역을 우클릭하여 출발역을 선택하세요.");
	
	Rectangle resetR;
	
	int start = -1, end = start;
	List<Integer> path = new ArrayList<Integer>();
	public metrot() {
		start = 1; end = 40;
		System.out.println(path = dikjstar(start, end));
		setFrame("경로 검색", 550, 800, () -> {});
	}

	protected void desing() {
		label = new JLabel() {
			int r = 12;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				
				FontMetrics fm = g2.getFontMetrics();
				g2.setColor(Color.red);
				g2.drawString("초기화", getWidth() - 50, 10);
				resetR = new Rectangle(getWidth() - 50, 10, fm.stringWidth("초기화"), fm.getHeight());
				
				double scaleX = ((double) getWidth()) / img.getWidth(null);
				double scaleY = ((double) getHeight()) / img.getHeight(null);
				
				stationEntity.findAll().forEach(e -> ovals.put(e.sno, new Ellipse2D.Double(e.x * scaleX - r, e.y * scaleY - r, r * 2, r * 2)));
				
				g2.setStroke(new BasicStroke(4f));
				
				
				if(start != -1) se(g2, "출");
				if(end != -1) se(g2, "도");
			}
			
			private void se(Graphics2D g, String s) {
				g.setFont(sp.font.deriveFont(13f).deriveFont(1));
				FontMetrics fm = g.getFontMetrics();
				Ellipse2D.Double e = ovals.get(s.equals("출") ? start : end);
				g.setColor(Color.white);
				g.fill(e);
				g.setColor(s.equals("출") ? sp.color : Color.red);
				g.draw(e);
				g.drawString(s, (int) (e.x + (fm.stringWidth(s)) / 2), (int) (e.y + fm.getHeight()));
				
			}
		};
		add(label);
	}
	
	private List<Integer> dikjstar(int start, int end) {
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, List<int[]>> map = new HashMap<>();
		for(int i : new int[] {1, 2, 7}) {
			Function<String, Integer> f = value -> Integer.parseInt(value.substring(1, 2));
			List<stationEntity> list = stationEntity.findBy(e -> f.apply(e.line).equals(i));
			for(int s = 0; s < list.size(); s++) {
				stationEntity nows = list.get(s);
				List<stationEntity> ns = new ArrayList<>();
				if(s - 1 >= 0) ns.add(list.get(s - 1));
				if(s + 1 < list.size() - 1) ns.add(list.get(s + 1));
				for(stationEntity n : ns) {
					map.computeIfAbsent(nows.sno, k -> new ArrayList<>()).add(new int[] {n.sno, (int) Math.hypot(nows.x - n.x, nows.y - n.y)});
				}
			}
		}
		for(String s : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> ss = stationEntity.findBy(e -> e.name.equals(s));
			stationEntity s1 = ss.get(0), s2 = ss.get(1);
			int weight = (int) Math.hypot(s1.x - s2.x, s1.y - s2.y);
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
		
		List<Integer> path = new ArrayList<Integer>();
		for(Integer n = end; n != null; n = parents.getOrDefault(n, null)) path.add(n);
		return path;
	}

	protected void action() {
	}
	
	public static void main(String[] args) {
		Util.start(new metrot());
	}
}