package test.test4;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.*;
import test.test4.game.ac;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game2 extends CFrame{
	public static final int SIZE = 15;
	int[] dx = {2, -2, 0, 0}, dy = {0, 0, -2, 2};
	int[][] grid = new int[SIZE][SIZE];
	boolean[][] visit = new boolean[SIZE][SIZE];
	Color[] colors = {Color.gray.darker().darker(), Color.white};
	Point user = new Point(1, 1);
	int[][] 가이드 = new int[SIZE][SIZE];
	
	public Game2() {
		for(int i = 0; i < 15; i++) Arrays.fill(가이드[i], -1);
		setting(1, 1);
		setFrame("미로", 517, 620, () -> {});
		new Thread(() -> {
			try {
				while(true) {
					for(int i = 0; i < 가이드.length; i++) Arrays.fill(가이드[i], -1);
					Thread.sleep(1000);
					List<Point> path = bfs(user);
					int n = 0;
					while(가이드[13][13] != 0) {
						for(int[] row : 가이드) for(int i = 0; i < SIZE; i++) row[i] -= 1;
						if(path.size() > n)
							가이드[path.get(n).x][path.get(n++).y] = 5;
						repaint();
						Thread.sleep(20);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();;
	}
	
	private void setting(int x, int y) {
        visit[x][y] = true;
        grid[x][y] = 1;
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);
        
        for (int d : dirs) {
            int nx = x + dx[d];
            int ny = y + dy[d];
            if (nx <= 0 || ny <= 0 || nx >= SIZE - 1 || ny >= SIZE - 1) continue;
            if (!visit[nx][ny]) {
                grid[nx / 2][ny / 2] = 1;
                setting(nx, ny);
            }
        }
    }

	public List<Point> bfs(Point start) {
		Point end = new Point(SIZE-2, SIZE-2);
		for(int i = 0; i < visit.length; i++) Arrays.fill(visit[i], false);
		Point[][] parents = new Point[SIZE][SIZE];
		Queue<Point> q = new LinkedList<>();
		visit[start.x][start.y] = true;
		q.add(new Point(start.x, start.y));
		while(!q.isEmpty()) {
			Point p = q.poll();
			if(p.x == end.x && p.y == end.y) break;
			for(int i = 0; i < 4; i++) {
				int nx = p.x + dx[i] / 2, ny = p.y + dy[i] / 2;
				if(nx <= 0 || nx >= SIZE-1 || ny <= 0 || ny >= SIZE-1) continue;
				if(visit[nx][ny] || grid[nx][ny] == 0) continue;
				visit[nx][ny] = true;
				q.add(new Point(nx, ny));
				parents[nx][ny] = p;
			}
		}
		
		LinkedList<Point> path = new LinkedList<>();
		Point cur = new Point(end.x, end.y);
		while (cur != null) {
			path.addFirst(cur);
			if (cur.x == start.x && cur.y == start.y) break;
			cur = parents[cur.x][cur.y];
		}
		return path;
	}
	@Override
	protected void desing() {
		JPanel topPanel = set(col(10
				, fillWidth(lb("방향키로 이동", FONT(getter.font.deriveFont(14f))))
				, lb("미로 탈출", FONT(getter.font.deriveFont(1).deriveFont(18f))))
				, BG(Color.white), BORDER(getter.eLine(Color.lightGray, 10, 10, 10, 10)));
		JPanel panel = set(new JPanel(new GridLayout(15, 15, 0, 0)), BORDER(getter.line(Color.LIGHT_GRAY)));
		for(int yy = 0; yy < SIZE; yy++) {
			int y = yy;
			for(int xx = 0; xx < SIZE; xx++) {
				int x = xx;
				JLabel label = new JLabel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						int w = getWidth() / 2, h = getHeight() / 2, r = (w * 2 / 3);
						if(가이드[x][y] > 0) {
							g.setColor(new Color(0, 0, 255, 가이드[x][y] * 50));
							g.fillRect(0, 0, getWidth(), getHeight());
						}
						if(user.x == x && user.y == y) {
							g.setColor(Color.red);
							g.fillOval(w - r, h - r, r*2, r*2);
						}
						if(x == 13 && y == 13) {
							g.setColor(Color.green);
							g.fillOval(w - r, h - r, r*2, r*2);
						}
					}
				};
				set(label, BG(colors[grid[x][y]]));
				label.setOpaque(true);
				panel.add(label);
			}
		}
		
		add(set(col(10, fillWidth(topPanel), fill(panel)), BORDER(getter.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int saveX = user.x, saveY = user.y;
				if(KeyEvent.VK_UP == e.getKeyCode() || KeyEvent.VK_DOWN == e.getKeyCode()) user.y += e.getKeyCode() - 39;
				if(KeyEvent.VK_LEFT == e.getKeyCode() || KeyEvent.VK_RIGHT == e.getKeyCode()) user.x += e.getKeyCode() - 38;
				if(grid[user.x][user.y] == 0) { user.x = saveX; user.y = saveY; }
				repaint();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Game2());
	}
}
