package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.*;

import main.Util;

import static utils.BoxPanel.*;
import static utils.Properties.*;
import utils.*;

public class Game5 extends CFrame{
	static final int SIZE = 15;
	int[] dx = {2, -2, 0, 0}, dy = {0, 0, 2, -2};
	int[][] grid = new int[SIZE][SIZE];
	boolean[][] visit = new boolean[SIZE][SIZE];
	Color[] colors = new Color[] {Color.gray.darker(), Color.white};
	Point user = new Point(1, 1);
	List<Point> bfs = new ArrayList<>();
	int[][] 가이드 = new int[SIZE][SIZE];
	
	public Game5() {
		setting(1, 1);
		bfs = bfs(new Point(1, 1));
		new Thread(() -> {
			try {
				while(!user.equals(new Point(13, 13))) {
					int n = 0;
					for(int i = 0; i < SIZE; i++) Arrays.fill(가이드[i], -1);
					Thread.sleep(1000);
					List<Point> path = bfs(user);
					while(가이드[SIZE - 2][SIZE - 2] != 0) {
						for(int[] row : 가이드) for(int i = 0; i < row.length; i++) row[i] -= 1;
						if(path.size() > n)
							가이드[path.get(n).x][path.get(n++).y] = 5;
						repaint();
						Thread.sleep(20);
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		setFrame("미로", 600, 700, () -> {});
	}

	private void setting(int x, int y) {
		visit[x][y] = true;
		grid[x][y] = 1;
		List<Integer> n = Arrays.asList(0, 1, 2, 3);
		Collections.shuffle(n);
		
		for(int d : n) {
			int nx = x + dx[d];
			int ny = y + dy[d];
			if(nx <= 0 || ny <= 0 || nx >= SIZE - 1 || ny >= SIZE - 1) continue;
			if(!visit[nx][ny]) {
				grid[x + dx[d] / 2][y + dy[d] / 2] = 1;
				setting(nx, ny);
			}
		}
	}

	private List<Point> bfs(Point start){
		Point end = new Point(SIZE - 2, SIZE - 2);
		for(int i = 0; i < visit.length; i++) Arrays.fill(visit[i], false);
		Point[][] parents = new Point[SIZE][SIZE];
		Queue<Point> q = new LinkedList<Point>();
		q.add(new Point(start.x, start.y));
		visit[start.x][start.y] = true;
		
		while(!q.isEmpty()) {
			Point p = q.poll();
			int x = p.x, y = p.y;
			if(p.equals(end)) break;
			for(int i = 0; i < 4; i++) {
				int nx = x + dx[i] / 2, ny = y + dy[i] / 2;
				if(nx <= 0 || ny <= 0 || nx >= SIZE - 1 || ny >= SIZE - 1) continue;
				if(visit[nx][ny] || grid[nx][ny] == 0) continue;
				visit[nx][ny] = true;
				q.add(new Point(nx, ny));
				parents[nx][ny] = p;
			}
		}
		
		LinkedList<Point> path = new LinkedList<>();
		Point p = end;
		while(p != null) {
			path.addFirst(p);
			if(p.equals(start)) break;
			p = parents[p.x][p.y];
		}
		return path;
	}
	
	@Override
	protected void desing() {
		JPanel topPanel = set(col(5, fw(lb("방향키로 이동", HOA(JLabel.LEFT), FONT(sp.font)))
				, lb("미로 탈출", FONT(sp.font.deriveFont(20f).deriveFont(1)))).setBackColor(Color.white), BORDER(sp.eLine(Color.LIGHT_GRAY, 5, 5, 5, 5)));
		JPanel panel = new JPanel(new GridLayout(15, 15, 0, 0));
		
		for(int yy = 0; yy < SIZE; yy++) {
			int y = yy;
			for(int xx = 0; xx < SIZE; xx++) {
				int x = xx;
				JLabel label = new JLabel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						Point point = new Point(x, y);
						int w = getWidth() / 2, h = getHeight() / 2, r = (w * 2 / 3);
						if(가이드[x][y] > 0) {
							g.setColor(new Color(0, 0, 255, 가이드[x][y] * 51));
							g.fillRect(0, 0, getWidth(), getHeight());
						}
						if(point.equals(new Point(SIZE-2, SIZE-2))) {
							g.setColor(Color.green);
							g.fillOval(w - r, h - r, r * 2, r * 2);
						}
						if(user.equals(point)) {
							g.setColor(Color.red);
							g.fillOval(w - r, h - r, r * 2, r * 2);
						}
					}
				};
				label.setBackground(colors[grid[x][y]]);
				label.setOpaque(true);
				panel.add(label);
			}
		}
		
		add(set(col(10, topPanel, f(panel)) ,BORDER(sp.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int saveX = user.x, saveY = user.y, k = e.getKeyCode();
				if(KeyEvent.VK_UP == k || KeyEvent.VK_DOWN == k) user.y += k - 39;
				if(KeyEvent.VK_RIGHT == k || KeyEvent.VK_LEFT == k) user.x += k - 38;
				if(grid[user.x][user.y] == 0) { user.x = saveX; user.y = saveY; }
				repaint();
			}
		});
	}

	public static void main(String[] args) {
		Util.start(new Game5());
	}
}
