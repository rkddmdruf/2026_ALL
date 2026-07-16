package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.*;

import orms.*;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Maps extends CFrame{
	
	List<sub_areaEntity>  ovalList = sub_areaEntity.findAll();
	List<linelistEntity> lineList = linelistEntity.findAll();
	List<areaEntity> aList = areaEntity.findAll();
	List<Point> ps = new ArrayList<>();
	List<Integer> sted;
	boolean[][] visit = new boolean[800][800];
	Map<Integer, List<Point>> guPoints = new LinkedHashMap<>();
	
	sub_areaEntity startStation, userStation = sub_areaEntity.findById(getter.user.sno).get();
	int nowStation;
	int productStation = 0;      // 지금 몇 번째 구간(sted[i] -> sted[i+1])을 그리는 중인지
	int productLine = 0;         // 그 구간 안에서 몇 스텝째인지 (0 ~ STEPS)
	final int STEPS = 40; 
	
	public Maps(int pno) {
		startStation = sub_areaEntity.findById(productEntity.findById(pno).get().sno).get();
		sted = dijkstra(startStation.sno, userStation.sno);
		System.out.println(sted);
		setFrame("test", 820, 820, () -> {});
	}

	@Override
	protected void desing() {
		try {
			Image img = getter.getImage("map.png", 800, 800).getImage();
			BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bfi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			for(int y = 0; y < bfi.getHeight(); y++)
				for(int x = 0; x < bfi.getHeight(); x++)
					if(bfi.getRGB(x, y) != 0) ps.add(new Point(x, y));
		} catch (Exception e) {
			
		}
		
		areaEntity.findAll().forEach(a -> settingMap(a));
		
		
		JLabel label = new JLabel(getter.getImage("map.png", 800, 800)) {
			@Override
			protected void paintComponent(Graphics g) {
				int n = 0;
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				for(Integer key : guPoints.keySet()) {
					if(key == nowStation) g2.setColor(Color.yellow);
					else g2.setColor(new Color(64, 64, 64));
					guPoints.get(key).forEach(e -> g2.fillRect(e.x, e.y, 1, 1));
				}
				
				g2.setColor(Color.lightGray);
				ps.forEach(e -> g2.fillRect(e.x, e.y, 1, 1));
				
				
				g2.setColor(Color.blue);
				for(int i = 0; i < lineList.size(); i++) {
					linelistEntity l = lineList.get(i);
					sub_areaEntity s1 = sub_areaEntity.findById(l.u).get();
					sub_areaEntity s2 = sub_areaEntity.findById(l.v).get();
					g2.drawLine(s1.sx + 3, s1.sy + 3, s2.sx + 3, s2.sy + 3);
				}
				g2.setColor(Color.green);

				g2.setStroke(new BasicStroke(3f));
				// 이미 다 그려진 구간들은 완성된 실선으로
				for (int i = 0; i < productStation; i++) {
					sub_areaEntity s1 = sub_areaEntity.findById(sted.get(i)).get();
					sub_areaEntity s2 = sub_areaEntity.findById(sted.get(i + 1)).get();
					g2.drawLine(s1.sx + 3, s1.sy + 3, s2.sx + 3, s2.sy + 3);
				}

				// 지금 그려지는 중인 구간 (진행률만큼만)
				if (productStation < sted.size() - 1) {
					sub_areaEntity s1 = sub_areaEntity.findById(sted.get(productStation)).get();
					sub_areaEntity s2 = sub_areaEntity.findById(sted.get(productStation + 1)).get();

					double ratio = productLine / (double) STEPS; // 0.0 ~ 1.0
					int curX = (int) (s1.sx + (s2.sx - s1.sx) * ratio);
					int curY = (int) (s1.sy + (s2.sy - s1.sy) * ratio);

					g2.drawLine(s1.sx + 3, s1.sy + 3, curX + 3, curY + 3);   // s1에서 지금 지점까지만 그려짐
				}
				
				g2.setColor(Color.red);
				for(int i = 0; i < ovalList.size(); i++) {
					sub_areaEntity s = ovalList.get(i);
					g2.fillOval(s.sx, s.sy, 6, 6);
				}
				g2.setColor(Color.cyan);
				g2.drawImage(new ImageIcon("datafiles/logo/start.png").getImage(), startStation.sx - 17, startStation.sy - 37, 40, 40, null);
				
				g2.drawImage(new ImageIcon("datafiles/logo/destination.png").getImage(), userStation.sx - 22, userStation.sy - 37, 50, 50, null);
				
			}
		};
		new Thread(() -> {
			try {
				while (true) {
					if (productStation < sted.size() - 1) {
						productLine++;
						if (productLine > STEPS) {
							productLine = 0;
							productStation++;
						}
					}else {
						//끝 액션
					}
					SwingUtilities.invokeLater(() -> label.repaint());
					Thread.sleep(30);
				}
			} catch (Exception e2) {
			}
		}).start();
		label.setBorder(getter.line(Color.LIGHT_GRAY));
		label.setBackground(Color.white);
		label.setOpaque(true);
		JPanel p = colF(0, label);
		p.setBorder(getter.em(10, 10, 10, 10));
		add(p);
	}

	@Override
	protected void action() {
		
	}
	
	private void settingMap(areaEntity a) {
		int[] dx = {1, -1, 0, 0}, dy = {0, 0, 1, -1};
		Queue<Point> q = new LinkedList<>();
		q.add(new Point(a.ax, a.ay));
		visit[a.ax][a.ay] = true;
		
		while(!q.isEmpty()) {
			Point p = q.poll();
			int x = p.x, y = p.y;
			for(int i = 0; i < 4; i++) {
				int nx = x + dx[i], ny = y + dy[i];
				if (nx >= 0 && ny >= 0 && nx < 800 && ny < 800) {
		            if (!visit[nx][ny]) {
		                visit[nx][ny] = true;
		                if(ps.stream().filter(e -> e.x == nx && e.y == ny).collect(Collectors.toList()).isEmpty()) 
		                {
		                	guPoints.computeIfAbsent(a.ano, k -> new ArrayList<>()).add(new Point(nx, ny));
		                	q.offer(new Point(nx, ny));
		                }
		            }
		        }
			}
		}
		IntStream.range(0, visit.length).forEach(e -> Arrays.fill(visit[e], false));
	}
	
	static List<Integer> dijkstra(int start, int end) {
		Map<Integer, List<int[]>> map = new LinkedHashMap<>();
	    linelistEntity.findAll().forEach(c -> {
	        sub_areaEntity s1 = sub_areaEntity.findById(c.u).get();
	        sub_areaEntity s2 = sub_areaEntity.findById(c.v).get();
	        int dist = (int) Math.sqrt(Math.pow(s1.sx - s2.sx, 2) + Math.pow(s1.sy - s2.sy, 2));

	        map.computeIfAbsent(c.u, k -> new ArrayList<>()).add(new int[]{c.v, dist});
	        map.computeIfAbsent(c.v, k -> new ArrayList<>()).add(new int[]{c.u, dist});
	    });

	    Map<Integer, Integer> dist = new HashMap<>();     // 지금까지 알려진 최단 거리
	    Map<Integer, Integer> parent = new HashMap<>();
	    PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // [노드, 거리] 중 거리 오름차순

	    dist.put(start, 0);
	    pq.add(new int[]{start, 0});

	    while (!pq.isEmpty()) {
	        int[] cur = pq.poll();
	        int p = cur[0], d = cur[1];

	        if (d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue; // 이미 더 짧은 걸로 처리된 노드면 skip
	        if (p == end) break;

	        for (int[] edge : map.getOrDefault(p, new ArrayList<>())) {
	            int next = edge[0], weight = edge[1];
	            int newDist = d + weight;
	            if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
	                dist.put(next, newDist);
	                parent.put(next, p);
	                pq.add(new int[]{next, newDist});
	            }
	        }
	    }

	    if (!dist.containsKey(end)) return new ArrayList<>(); // 경로 없음

	    LinkedList<Integer> path = new LinkedList<>();
	    Integer cur = end;
	    while (cur != null) {
	        path.addFirst(cur);
	        cur = (cur == start) ? null : parent.get(cur);
	    }
	    return path;
	}
	
	public static void main(String[] args) {
		Util.start(new Maps(1));
	}

}
