package test.test2;

import main.Util;
import orms.areaEntity;
import orms.linelistEntity;
import orms.sub_areaEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Maps2 extends CFrame{
	List<Point> lines = new ArrayList<>();
	Map<Integer, List<Point>> guPoints = new LinkedHashMap<>();
	
	sub_areaEntity pstation, userStation = sub_areaEntity.findById(getter.user.sno).get();
	
	public Maps2(int pno) {
		pstation = sub_areaEntity.findById(pno).get();
		settingMap();
		setFrame("test", 800 + 20, 800 + 20, () -> {});
	}
	
	@Override
	protected void desing() {
		List<sub_areaEntity> sList = sub_areaEntity.findAll();
		Map<Integer, Integer> linesMap = new LinkedHashMap<>();
		linelistEntity.findAll().forEach(e -> {
			linelistEntity.findBy(c -> c.u.equals(e.u));
		});
		JLabel label = new JLabel(getter.getImage("map.png", 800, 800)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.black);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				guPoints.keySet().stream().forEach(e -> {
					if(e == 8) g2.setColor(Color.yellow);
					else g2.setColor(new Color(64,64,64));
					guPoints.get(e).forEach(c -> g2.fillRect(c.x, c.y, 1, 1));
				});
				
				g2.setColor(Color.blue);
				linelistEntity.findAll().forEach(l -> {
					sub_areaEntity s1 = sub_areaEntity.findById(l.u).get();
					sub_areaEntity s2 = sub_areaEntity.findById(l.v).get();
					g2.drawLine(s1.sx + 2, s1.sy + 2, s2.sx + 2, s2.sy + 2);
				});
				g2.setColor(Color.red);
				sList.forEach(e -> g2.fillOval(e.sx, e.sy, 4, 4));
				g2.setColor(Color.lightGray);
				lines.forEach(e -> g2.fillRect(e.x, e.y, 1, 1));
				
				g2.drawImage(new ImageIcon("datafiles/logo/start.png").getImage(), pstation.sx - 18, pstation.sy - 35, 40, 40, null);
				g2.drawImage(new ImageIcon("datafiles/logo/destination.png").getImage(), userStation.sx - 23, userStation.sy - 37, 50, 50, null);
			}
		};
		label.setBackground(Color.white);
		label.setOpaque(true);
		label.setBorder(getter.line(Color.LIGHT_GRAY));
		add(set(col(0, fill(label)), BORDER(getter.em(10, 10, 10, 10))));
	}

	private void settingMap() {
		Image img = getter.getImage("map.png", 800, 800).getImage();
		try {
			BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bfi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			for(int y = 0; y < bfi.getHeight(); y++)
				for(int x = 0; x < bfi.getWidth(); x++)
					if(bfi.getRGB(x, y) != 0) lines.add(new Point(x, y));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		int[] dx = {1,-1,0,0}, dy = {0,0,-1,1};
		boolean[][] visit = new boolean[800][800];
		
		areaEntity.findAll().forEach(a -> {
			guPoints.put(a.ano, new ArrayList<>());
			
			Queue<Point> q = new LinkedList<>();
			q.add(new Point(a.ax, a.ay));
			visit[a.ax][a.ay] = true;
			guPoints.get(a.ano).add(new Point(a.ax, a.ay));
			
			while(!q.isEmpty()) {
				Point p = q.poll();
				int x = p.x, y = p.y;
				for(int i = 0; i < 4; i++) {
					int nx = x + dx[i], ny = y + dy[i];
					if (!visit[nx][ny]) {
						visit[nx][ny] = true;
						if(lines.stream().filter(e -> e.x == nx && e.y == ny).collect(Collectors.toList()).isEmpty()) 
						{
							guPoints.get(a.ano).add(new Point(nx, ny));
							q.offer(new Point(nx, ny));
						}
					}
				}
			}
			for(int i = 0; i < visit.length; i++) Arrays.fill(visit[i], false);
		});
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		Util.start(new Maps2(1));
	}
}
