package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.swing.*;

import orms.*;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class map extends CFrame{
	
	List<sub_areaEntity> list = sub_areaEntity.findAll();
	List<linelistEntity> lineList = linelistEntity.findAll();
	List<areaEntity> aList = areaEntity.findAll();
	List<Point> ps = new ArrayList<>();
	boolean[][] visit = new boolean[800][800];
	Map<Integer, Integer> xMap = new HashMap<>();
	Map<Integer, Integer> yMap = new HashMap<>();
	Map<Integer, List<Point>> guPoints = new LinkedHashMap<>();
	List<Point> colorpul = new ArrayList<>();
	public map() {
		setFrame("test", 820, 820, () -> {});
	}

	@Override
	protected void desing() {
		try {
			Image img = getter.getImage("map.png", 800, 800).getImage();
			ImageIcon icon = new ImageIcon(img);
			BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bfi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			for(int y = 0; y < bfi.getHeight(); y++)
				for(int x = 0; x < bfi.getHeight(); x++)
					if(bfi.getRGB(x, y) != 0) ps.add(new Point(x, y));
			//System.out.println(ps.size() + " , " + (xMap.size() + yMap.size()) + " , " + test);
		} catch (Exception e) {
			
		}
		
		areaEntity.findAll().forEach(a -> settingMap(a));
		
		JLabel label = new JLabel(getter.getImage("map.png", 800, 800)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				System.out.println("sdf");
				Graphics2D g2 = (Graphics2D) g;
				System.out.println(getWidth() + " = " + getHeight());
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.yellow);
				guPoints.get(8).forEach(c -> g2.fillRect(c.x, c.y, 1, 1));
				
				g2.setColor(Color.lightGray);
				ps.forEach(e -> g2.fillRect(e.x, e.y, 1, 1));
				g2.setColor(Color.blue);
				for(int i = 0; i < lineList.size(); i++) {
					linelistEntity l = lineList.get(i);
					sub_areaEntity s1 = sub_areaEntity.findById(l.u).get();
					sub_areaEntity s2 = sub_areaEntity.findById(l.v).get();
					g2.drawLine(s1.sx + 3, s1.sy + 3, s2.sx + 3, s2.sy + 3);
				}
				g2.setColor(Color.red);
				for(int i = 0; i < list.size(); i++) {
					sub_areaEntity s = list.get(i);
					g2.fillOval(s.sx, s.sy, 6, 6);
				}
			}
		};
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
		
		
		for(int i = 0; i < visit.length; i++)
			for(int j = 0; j < visit[i].length; j++)
				visit[i][j] = false;
	}
	
	public static void main(String[] args) {
		Util.start(new map());
	}

}
