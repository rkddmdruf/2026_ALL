package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import orms.stationEntity;
import uitls.getter;

public class Util {
	public static Image logo;
	public static Image train;
	static { setImage(); }
	
	public static List<Integer> bfs(int start, int end, List<Integer> totalPix){
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<>(); 
		Map<Integer, List<int[]>> map = new LinkedHashMap<>();
		List<Integer> visit = new ArrayList<>();
		for(int i : new int[]{1, 2, 7}) {
			int line1 = i * 1000, line2 = (i + 1) * 1000;
			List<stationEntity> list = stationEntity.findAll().stream().filter(e -> {
				int n = Integer.parseInt(e.line.substring(1));
				return line1 < n && line2 > n;
			}).collect(Collectors.toList());
			for(int s = 0; s < list.size(); s++) {
				stationEntity nowStation = list.get(s);
				List<Integer> ns = new ArrayList<>();
				if(s - 1 >= 0) ns.add(s - 1);
				if(s + 1 < list.size()) ns.add(s + 1);
				for(Integer n : ns) {
					stationEntity station = list.get(n);
					map.computeIfAbsent(nowStation.sno, k -> new ArrayList<>())
					.add(new int[] {station.sno, (int) Math.sqrt(Math.pow(nowStation.x - station.x, 2) + Math.pow(nowStation.y - station.y, 2))});
				}
			}
		}
		for(String s : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> s1 = stationEntity.findBy(e -> e.name.equals(s));
			map.get(s1.get(0).sno).add(new int[] {s1.get(1).sno, (int) Math.sqrt(Math.pow(s1.get(0).x - s1.get(1).x, 2) + Math.pow(s1.get(0).y - s1.get(1).y, 2))});
			map.get(s1.get(1).sno).add(new int[] {s1.get(0).sno, (int) Math.sqrt(Math.pow(s1.get(0).x - s1.get(1).x, 2) + Math.pow(s1.get(0).y - s1.get(1).y, 2))});
		}
		
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		dist.put(start, 0);
		pq.add(new int[] {start, 0});
		
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for (int[] edge : map.getOrDefault(p, new ArrayList<>())) {
	            int next = edge[0], weight = edge[1];
	            int newDist = d + weight;
	            if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
	                dist.put(next, newDist);
	                parents.put(next, p);
	                pq.add(new int[]{next, newDist});
	            }
	        }
		}
		totalPix.set(0, dist.get(end));
		LinkedList<Integer> path = new LinkedList<>();
	    Integer cur = end;
	    while (cur != null) {
	        path.addFirst(cur);
	        cur = (cur == start) ? null : parents.get(cur);
	    }
	    
	    return path;
	}
	
	private static void setImage() {
		Image img = new ImageIcon("datafiles/logo.png").getImage();
		try {
			BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bfi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bfi.getHeight(); y++) 
				for(int x = 0; x < bfi.getWidth(); x++) {
					int rgb = bfi.getRGB(x, y);//ABCDEF
					if((rgb & 0xFFFFFF) >= 0xCCCCCC) bfi.setRGB(x, y, 0x000000);
				}
			logo = bfi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		img = new ImageIcon("datafiles/icon/train.png").getImage();
		try {
			int top = img.getHeight(null);
			int left = img.getWidth(null);
			int right = 0;
			int bottom = 0;
			BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2 = bfi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bfi.getHeight(); y++) 
				for(int x = 0; x < bfi.getWidth(); x++) {
					int rgb = bfi.getRGB(x, y);//ABCDEF
					if((rgb & 0xFFFFFF) < 0xCCCCCC) {
			            if (x < left) left = x;
			            if (x > right) right = x;
			            if (y < top) top = y;
			            if (y > bottom) bottom = y;
			        }
					if((rgb & 0xFFFFFF) >= 0xCCCCCC) bfi.setRGB(x, y, 0x000000);
				}
			BufferedImage cropped = bfi.getSubimage(
			        left,
			        top,
			        right - left + 1,
			        bottom - top + 1
			);
			train = cropped;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void start(JFrame f) {
		SwingUtilities.invokeLater(() -> f.setVisible(true));
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent event) {
				try {
					super.dispatchEvent(event);
				} catch (Exception e) {
					handle(e);
				}
			}
		});
	}
	
	public static Color setAlpha(Color color, int n) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
	}
	private static void handle(Throwable t) {
		t.printStackTrace();
		getter.err(t.getMessage());
	}
}
