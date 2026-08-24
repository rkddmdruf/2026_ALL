package test.test4;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import main.Util;
import orms.areaEntity;
import orms.linelistEntity;
import orms.productEntity;
import orms.sub_areaEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class map extends CFrame{
	List<Point> lines = new ArrayList<>();
	Map<Integer, List<Point>> map = new LinkedHashMap<>();
	List<linelistEntity> lineList = linelistEntity.findAll();
	List<Integer> bfs = new ArrayList<>();
	
	sub_areaEntity startStation, userStation = sub_areaEntity.findById(sp.user.sno).get();
	JLabel label;
	int selectStation = 1, productStation = 0;
	double step = 0d;
	int test;
	public map(int pno) {
		System.out.println("fsd");
		startStation =	sub_areaEntity.findById(productEntity.findById(pno).get().sno).get();
		bfs = dijkstra(54, 19);
		System.out.println(bfs);
		selectStation = sub_areaEntity.findById(bfs.get(0)).get().ano;
		System.out.println("fds");
		new Thread(() -> {
			try {
				while(true) {
					test++;
					Thread.sleep(1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		setting();
		System.out.println(test);
		setFrame("배송", 820, 820, () -> {});
	}

	
	private List<Integer> dijkstra(Integer start, Integer end) {
		Map<Integer, List<int[]>> map = new LinkedHashMap<>();
		linelistEntity.findAll().forEach(e -> {
			sub_areaEntity s1 = sub_areaEntity.findById(e.u).get();
			sub_areaEntity s2 = sub_areaEntity.findById(e.v).get();
			int n = (int) (Math.sqrt(Math.pow(s1.sx - s2.sx, 2) + Math.pow(s1.sy - s2.sy, 2)));
			map.computeIfAbsent(e.u, k -> new ArrayList<>()).add(new int[] {e.v, n});
			map.computeIfAbsent(e.v, k -> new ArrayList<>()).add(new int[] {e.u, n});
		});
		
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, Integer> parents = new HashMap<>();
		
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		dist.put(start, 0);
		pq.add(new int[] {start, 0});
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for(int[] edge : map.getOrDefault(p, new ArrayList<>())) {
				int next = edge[0], weight = edge[1];
				int newDist = d + weight;
				if(newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
					dist.put(next, newDist);
					parents.put(next, p);
					pq.add(new int[] {next, newDist});
				}
			}
		}
		
		LinkedList<Integer> path = new LinkedList<>();
	    Integer cur = end;
	    while (cur != null) {
	        path.addFirst(cur);
	        cur = (cur == start) ? null : parents.get(cur);
	    }
	    return path;
	}


	@Override
	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(Color.LIGHT_GRAY);
				lines.forEach(e -> g2.fillRect(e.x, e.y, 1, 1));
				map.keySet().forEach(e -> {
					g2.setColor(e.equals(selectStation) ? Color.yellow : Color.gray.darker().darker());
					map.get(e).forEach(c -> g2.fillRect(c.x, c.y, 1, 1));
				});
				g2.setColor(Color.blue);
				lineList.forEach(e -> {
					sub_areaEntity s1 = sub_areaEntity.findById(e.u).get();
					sub_areaEntity s2 = sub_areaEntity.findById(e.v).get();
					g2.drawLine(s1.sx, s1.sy, s2.sx,s2.sy);
				});
				
				g2.setStroke(new BasicStroke(2f));
				g2.setColor(Color.green);
				for(int i = 0; i < productStation; i++) {
					sub_areaEntity s1 = sub_areaEntity.findById(bfs.get(i)).get();
					sub_areaEntity s2 = sub_areaEntity.findById(bfs.get(i + 1)).get();
					g2.drawLine(s1.sx, s1.sy, s2.sx, s2.sy);
				}
				
				sub_areaEntity s1 = sub_areaEntity.findById(bfs.get(productStation)).get();
				sub_areaEntity s2 = sub_areaEntity.findById(bfs.get(productStation + 1)).get();
				int curX = (int) (s1.sx + (s2.sx - s1.sx) * step);
				int curY = (int) (s1.sy + (s2.sy - s1.sy) * step);
				g2.drawLine(s1.sx, s1.sy, curX, curY);
				
				
				g2.setColor(Color.red);
				lineList.forEach(e -> {
					sub_areaEntity ss1 = sub_areaEntity.findById(e.u).get();
					sub_areaEntity ss2 = sub_areaEntity.findById(e.v).get();
					g2.fillOval(ss1.sx-2, ss1.sy-2, 4, 4);
					g2.fillOval(ss2.sx-2, ss2.sy-2, 4, 4);
				});
				
				sub_areaEntity imgS1 = sub_areaEntity.findById(startStation.sno).get();
				sub_areaEntity imgS2 = sub_areaEntity.findById(userStation.sno).get();
				g2.drawImage(new ImageIcon("datafiles/logo/start.png").getImage(), imgS1.sx-20, imgS1.sy - 40, 40, 40, null);
				g2.drawImage(new ImageIcon("datafiles/logo/destination.png").getImage(), imgS2.sx-24, imgS2.sy - 40, 50, 50, null);
				
			}
		};
		set(label, BG(Color.white), BORDER(sp.line(Color.LIGHT_GRAY)));
		label.setOpaque(true);
		add(set(colF(0, label), BORDER(sp.em(10, 10, 10, 10))));
	}

	private void setting() {
		Image img = sp.getImage("map.png", 800, 800).getImage();
		try {
			BufferedImage b = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = b.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			for(int y = 0; y < b.getHeight(); y++)
				for(int x = 0; x < b.getWidth(); x++)
					if(b.getRGB(x, y) != 0) lines.add(new Point(x, y));
		} catch (Exception e) {
			e.printStackTrace();
		}
		int[] dx = {1,0,-1,0}, dy = {0,1,0,-1};
		boolean[][] visit = new boolean[800][800];
		areaEntity.findAll().forEach(e -> {
			map.put(e.ano, new ArrayList<>());
			Queue<Point> q = new LinkedList<>();
			q.add(new Point(e.ax, e.ay));
			visit[e.ax][e.ay] = true;
			map.get(e.ano).add(new Point(e.ax, e.ay));
			while(!q.isEmpty()) {
				Point p = q.poll();
				int x = p.x, y = p.y;
				for(int i = 0; i < 4; i++) {
					int nx = x + dx[i], ny = y + dy[i];
					if(!visit[nx][ny]) {
						visit[nx][ny] = true;
						if(lines.stream().filter(c -> c.x == nx && c.y == ny).collect(Collectors.toList()).isEmpty()) 
						{
							map.get(e.ano).add(new Point(nx, ny));
							q.offer(new Point(nx, ny));
						}
					}
				}
			}
		});
	}
	
	@Override
	protected void action() {
		new Thread(() -> {
			try {
				while(true) {
					if(step < 1) step += 0.1;
					else {
						step = 0;
						productStation ++;
						selectStation = sub_areaEntity.findById(bfs.get(productStation)).get().ano;
					};
					if(productStation >= bfs.size() -1) break;
					SwingUtilities.invokeLater(() -> label.repaint());
					Thread.sleep(90);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		Util.start(new map(1));
	}
}
