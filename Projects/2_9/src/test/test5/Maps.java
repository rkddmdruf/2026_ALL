package test.test5;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.stream.Stream;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Util;
import orms.areaEntity;
import orms.linelistEntity;
import orms.productEntity;
import orms.sub_areaEntity;
import utils.CFrame;
import utils.getter;

public class Maps extends CFrame{
	List<linelistEntity> lines = linelistEntity.findAll();
	BufferedImage img; 
	productEntity product;
	List<Integer> dik = new ArrayList<>();
	public Maps(int pno) {
		product = productEntity.findById(pno).get();
		dik.addAll(dijkstar(product.sno, getter.user.sno));
		setImage();
		setFrame("배송", 820, 820, () -> {});
	}

	private void setImage() {
		Image image = getter.getImage("map.png", 800, 800).getImage();
		try {
			img = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = this.img.createGraphics();
			g2.drawImage(image, 0, 0, null);
			g2.dispose();
			
			areaEntity.findAll().forEach(a -> {
				repaintColor(img, a.ax, a.ay, Color.gray.darker().darker().getRGB());
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void desing() {
		JLabel l = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				lines.forEach(e -> {
					sub_areaEntity s1 = sub_areaEntity.findById(e.u).get();
					sub_areaEntity s2 = sub_areaEntity.findById(e.v).get();
					g2.setColor(Color.red);
					g2.fillOval(s1.sx - 2, s1.sy - 2, 4, 4);
					g2.fillOval(s2.sx - 2, s2.sy - 2, 4, 4);
				});			
				
				lines.forEach(e -> {
					sub_areaEntity s1 = sub_areaEntity.findById(e.u).get();
					sub_areaEntity s2 = sub_areaEntity.findById(e.v).get();
					g2.setColor(Color.blue);
					g2.drawLine(s1.sx, s1.sy, s2.sx, s2.sy);
				});
				sub_areaEntity sp = sub_areaEntity.findById(product.sno).get();
				sub_areaEntity su = sub_areaEntity.findById(getter.user.sno).get();
				g2.drawImage(new ImageIcon("logo/destination.png").getImage(), sp.sx -27, sp.sy- 37, 40, 40, null);
				g2.drawImage(new ImageIcon("logo/start.png").getImage(), 0, 0, 50, 50, null);
			}
		};
		add(l);
	}

	@Override
	protected void action() {
		
	}

	private List<Integer> dijkstar(int start, int end){
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<>();
		Map<Integer, List<int[]>> map = new HashMap<Integer, List<int[]>>();
		linelistEntity.findAll().forEach(e -> {
			sub_areaEntity s1 = sub_areaEntity.findById(e.u).get();
			sub_areaEntity s2 = sub_areaEntity.findById(e.v).get();
			
			map.computeIfAbsent(s1.sno, k -> new ArrayList<>()).add(new int[] {s2.sno, (int) Math.sqrt(Math.pow(s1.sx - s2.sy, 2) + Math.pow(s1.sx - s2.sy, 2))});
			map.computeIfAbsent(s2.sno, k -> new ArrayList<>()).add(new int[] {s1.sno, (int) Math.sqrt(Math.pow(s1.sx - s2.sy, 2) + Math.pow(s1.sx - s2.sy, 2))});
		});
		
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		pq.add(new int[] {start, 0});
		dist.put(start, 0);
		
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
					pq.add(new int[] {next, newDist});
				}
			}
		}
		
		return Stream.iterate(end, n -> n != null, n -> n.equals(start) ? null : parents.get(n)).collect(LinkedList::new, LinkedList::addFirst, (a, b) -> a.addAll(b));
	}
	
	private void repaintColor(BufferedImage img, int sx, int sy, int rgb) {
		int target = img.getRGB(sx, sy);
		if(target == rgb) return;
		
		Stack<Point> stack = new Stack<>();
		stack.add(new Point(sx, sy));
		
		while(!stack.isEmpty()) {
			Point p = stack.pop();
			int x = p.x, y = p.y;
			if(x < 0 || y < 0 || x >= img.getWidth() || y >= img.getHeight()) continue;
			if(img.getRGB(x, y) != target) continue;
			
			img.setRGB(x, y, rgb);
			stack.push(new Point(x + 1, y));
			stack.push(new Point(x - 1, y));
			stack.push(new Point(x, y + 1));
			stack.push(new Point(x, y - 1));
		}
	}
	public static void main(String[] args) {
		Util.start(new Maps(1));
	}
}
