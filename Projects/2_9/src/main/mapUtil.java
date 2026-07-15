package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import orms.areaEntity;
import utils.getter;

public class mapUtil {
	static boolean[][] visit = new boolean[800][800];
	static List<Point> ps = new ArrayList<>();
	static Map<Integer, List<Point>> guPoints = new LinkedHashMap<>();
	
	public static void main(String[] args) {
		setPs();
		areaEntity.findAll().forEach(a -> settingMap(a));
		
		String result = "package utils \n"
				+ "public class mapEntity {\n"
				+ "static { setting(); }"
				+ "static Map<Integer, List<Point>> guPoints = new LinkedHashMap<>();\n"
				+ "static List<Point> ps = new ArrayList<>();"
				+ "private static void setting(){\n"
				+ "$map;\n"
				+ "$lines;"
				+ "}"
				+ "}";
		guPoints.computeIfAbsent(1, k -> new ArrayList<>()).add(new Point(1, 1));
		String str1 = "" + guPoints.keySet().stream()
				.map(e -> guPoints.get(e).stream().map(c -> "guPoints.computeIfAbsent(" + e + ", k -> new ArrayList<>()).add(new Point(" + c.x +  ", " + c.y + "));\n")
						.collect(Collectors.joining())
				).collect(Collectors.joining());
		String str2 = ps.stream().map(e -> "ps.add(new Point(" + e.x + ", " + e.y + ");\n").collect(Collectors.joining());
		result = result.replace("$map", str1).replace("$lines", str2);
		
		try {
			Path outDir = Path.of("src/utils");
			Files.writeString(outDir.resolve("mapEntity.java"), result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void setPs() {
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
	}
	private static void settingMap(areaEntity a) {
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
}
