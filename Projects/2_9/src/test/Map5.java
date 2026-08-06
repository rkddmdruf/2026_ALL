package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import main.Util;
import orms.areaEntity;
import orms.linelistEntity;
import orms.productEntity;
import orms.sub_areaEntity;

import static utils.BoxPanel.*;
import static utils.Properties.*;
import utils.*;

public class Map5 extends CFrame{
	BufferedImage img;
	productEntity product;
	int nowArea = 0;
	double step = 0;
	
	List<sub_areaEntity> path;
	
	public Map5(int pno) {
		product = productEntity.findById(pno).get();
		path = dijkstar(124, getter.user.sno);
		setting();
		setFrame("배송", 820, 820, () -> {});
	}
	
	@Override
	protected void desing() {
		JLabel label = new JLabel() {
			protected void paintComponent(java.awt.Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.drawImage(img, 0, 0, null);
				
				g2.setColor(Color.blue);
				linelistEntity.findAll().forEach(e -> line(g2, sub_areaEntity.findById(e.u).get(), sub_areaEntity.findById(e.v).get()));
				
				g2.setColor(Color.green);
				g2.setStroke(new BasicStroke(2f));
				for(int i = 0; i < nowArea; i++) line(g2, path.get(i), path.get(Math.min(i + 1, path.size() - 1)));
				
				var s1 = path.get(nowArea);
				var s2 = path.get(Math.min(nowArea + 1, path.size() - 1));
				int curX = (int) (s1.sx + (s2.sx - s1.sx) * step);
				int curY = (int) (s1.sy + (s2.sy - s1.sy) * step);
				g2.drawLine(s1.sx, s1.sy, curX, curY);
				
				g2.setColor(Color.red);
				int r= 2;
				sub_areaEntity.findAll().forEach(e -> g2.fillOval(e.sx - r, e.sy - r, r*2, r*2) );
				sub_areaEntity imgS1 = sub_areaEntity.findById(product.sno).get();
				sub_areaEntity imgS2 = sub_areaEntity.findById(getter.user.sno).get();
				g2.drawImage(getter.getImage("logo/start.png", 40, 40).getImage(), imgS1.sx-20, imgS1.sy - 40, null);
				g2.drawImage(getter.getImage("logo/destination.png", 50, 50).getImage(), imgS2.sx-24, imgS2.sy - 40, null);
			};
			
			private void line(Graphics2D g2, sub_areaEntity s1, sub_areaEntity s2) {
				g2.drawLine(s1.sx, s1.sy, s2.sx, s2.sy);
			}
		};
		
		label.setBorder(getter.line(Color.LIGHT_GRAY));
		label.setBackground(Color.white);
		label.setOpaque(true);
		add(set(col(0, fill(label)), BORDER(getter.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		new Thread(() -> {
			try {
				while(true) {
					if(step >= 1) {
						areaEntity a1 = areaEntity.findById(path.get(nowArea).ano).get();
						changeColor(img, a1.ax, a1.ay,Color.gray.darker().darker().getRGB());
						nowArea++; 
						step = 0;
						areaEntity a2 = areaEntity.findById(path.get(nowArea).ano).get();
						changeColor(img, a2.ax, a2.ay,Color.yellow.getRGB());
					}
					else step += 0.01;
					if(nowArea >= path.size() - 1) break;
					repaint();
					Thread.sleep(16);
				}
				SwingUtilities.invokeLater(() -> {
					getter.infor("배송이 완료되었습니다.");
					dispose();
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	private List<sub_areaEntity> dijkstar(Integer start, Integer end){
		Map<Integer, List<int[]>> map = new HashMap<>();
		linelistEntity.findAll().forEach(e -> {
			sub_areaEntity s1 = sub_areaEntity.findById(e.u).get();
			sub_areaEntity s2 = sub_areaEntity.findById(e.v).get();
			int n = (int) Math.hypot(s1.sx - s2.sx, s1.sy - s2.sy);
			map.computeIfAbsent(e.u, k -> new ArrayList<>()).add(new int[] {e.v, n});
			map.computeIfAbsent(e.v, k -> new ArrayList<>()).add(new int[] {e.u, n});
		});
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, Integer> parents = new HashMap<>();
		
		PriorityQueue<int[]> pq = new PriorityQueue<int[]>((a, b) -> a[1] - b[1]);
		dist.put(start, 0);
		pq.add(new int[] { start, 0 });
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for(int[] curs : map.getOrDefault(p, List.of())) {
				int next = curs[0], weight = curs[1];
				int newDist = d + weight;
				if(newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
					dist.put(next, newDist);
					parents.put(next, p);
					pq.add(new int[] {next, newDist});
				}
			}
		}
		LinkedList<sub_areaEntity> path = new LinkedList<>();
		for(Integer n = end; n != null; n = parents.getOrDefault(n, null)) path.addFirst(sub_areaEntity.findById(n).get());
		return path;
	}
	
	private void setting() {
		Image img = getter.getImage("map.png", 800, 800).getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			this.img = bf;
			areaEntity.findAll().forEach(a -> {
				changeColor(bf, a.ax, a.ay, Color.gray.darker().darker().getRGB());
			});
			areaEntity a = areaEntity.findById(sub_areaEntity.findById(product.sno).get().ano).get();
			changeColor(bf, a.ax, a.ay,Color.yellow.getRGB());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void changeColor(BufferedImage img, int startX, int startY, int newRGB) {
	    int targetRGB = img.getRGB(startX, startY);
	    if (targetRGB == newRGB) return;
	    
	    Deque<Point> stack = new ArrayDeque<>();
	    stack.push(new Point(startX, startY));

	    while (!stack.isEmpty()) {
	        Point p = stack.pop();
	        int x = p.x, y = p.y;
	        if (x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) continue;
	        if (img.getRGB(x, y) != targetRGB) continue; // 경계선(검은색)이거나 이미 채워짐

	        img.setRGB(x, y, newRGB);
	        
	        stack.push(new Point(x + 1, y));
	        stack.push(new Point(x - 1, y));
	        stack.push(new Point(x, y + 1));
	        stack.push(new Point(x, y - 1));
	    }
	}
	
	public static void main(String[] args) {
		Util.start(new Map5(1));
	}
}
