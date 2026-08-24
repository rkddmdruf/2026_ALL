package test.test4;

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
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.*;

import main.Util;

public class game extends CFrame{
	final int SIZE = 15;
	int[][] grid = new int[15][15];
	boolean[][] visit = new boolean[15][15];
	int[] dx = {0,0,2,-2}, dy = {2,-2,0,0};
	List<Color> colors = Arrays.asList(new Color(64, 64, 64), Color.white);
	List<List<JLabel>> labels = new ArrayList<>();
	JTextField tf = comp(JTextField::new, SIZE(0, 0));
	List<ac> ps = new ArrayList<>();
	Point user = new Point(1, 1);
	// 0 벽, 1 길 2 도착지, 3 시작점
	class ac {
		public Color c;
		public Point p = new Point();
		
		public ac() {}
		public ac(Color c, Point p) {
			this.c = c;
			this.p = p;
		}
	}
	public game() {
		dfs(1, 1);
		setFrame("미로", 600, 650, () -> {});
		new Thread(() -> {
			try {
				while (true) {
					Thread.sleep(1000); // 다 사라지고 나서 1초 뒤 다시 시작
					Point start = new Point(user);
					Point end = new Point(13, 13);
					List<Point> path = bfsPath(start, end);
					for(int s = 0; s < path.size(); s++) {
						ps.clear();
						for(int i = 0; i < 5; i++) {
							int befn = s - i;
							if(befn < 0) befn = 0;
							Point p = new Point(path.get(befn));
							ps.add(0, new ac(new Color(130, 190, 250, 200 - i * 40), p));
						}
						path.forEach(e -> SwingUtilities.invokeLater(() -> labels.get(e.x).get(e.y).repaint()));
						Thread.sleep(40);
					}
					for(int i = ps.size(); i > 0; i--) {
						Color c = ps.get(0).c;
						ps.get(ps.size() - 1).c = new Color(c.getRed(), c.getGreen(), c.getBlue(), ps.get(ps.size() - (i == 1 ? 1 : 2)).c.getAlpha());
						ps.remove(0);
						path.forEach(e -> labels.get(e.x).get(e.y).repaint());
						Thread.sleep(40);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	/** start -> end 로 가는 최단 경로를 BFS로 구함 (대각선 없음) */
	private List<Point> bfsPath(Point start, Point end) {
		boolean[][] visited = new boolean[SIZE][SIZE];
		Point[][] parent = new Point[SIZE][SIZE];
		int[] dr = {-1, 1, 0, 0};
		int[] dc = {0, 0, -1, 1};

		Queue<Point> queue = new LinkedList<>();
		queue.add(start);
		visited[start.x][start.y] = true;

		while (!queue.isEmpty()) {
			Point cur = queue.poll();
			if (cur.x == end.x && cur.y == end.y) break;
			for (int d = 0; d < 4; d++) {
				int nx = cur.x + dr[d];
				int ny = cur.y + dc[d];
				if (nx <= 0 || ny <= 0 || nx >= 14 || ny >= 14) continue;
				if (visited[nx][ny] || grid[nx][ny] == 0) continue;
				visited[nx][ny] = true;
				parent[nx][ny] = cur;
				queue.add(new Point(nx, ny));
			}
		}

		LinkedList<Point> path = new LinkedList<>();
		Point cur = new Point(end.x, end.y);
		while (cur != null) {
			path.addFirst(cur);
			if (cur.x == start.x && cur.y == start.y) break;
			cur = parent[cur.x][cur.y];
		}
		return path;
	}
	
	@Override
	protected void desing() {
		JPanel topPanel = set(col(10, fw(lb("방향키로 이동", FONT(sp.font.deriveFont(14f).deriveFont(1)))), lb("미로 탈출", FONT(sp.font.deriveFont(1).deriveFont(20f)))),
				BG(Color.white), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)));
		JPanel mainPanel = new JPanel(new GridLayout(15, 15));
		for(int i = 0; i < SIZE; i++) {
			int index = i;
			List<JLabel> list = new ArrayList<>();
			for(int j = 0; j < SIZE; j++) {
				int jndex = j;
				JLabel l = new JLabel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						Point p = new Point(index, jndex);
						ps.stream().filter(e -> e.p.equals(p)).forEach(e -> {
							g.setColor(e.c);
							g.fillRect(0, 0, getWidth(), getHeight());
						});
						if(user.x == index && user.y == jndex) {
							g.setColor(Color.red);
							g.fillOval((getWidth() / 2) - (getWidth() / 3), (getHeight() / 2) - (getHeight() / 3), (getWidth() / 3) * 2, (getHeight() / 3) * 2);
						}
						if(index == 13 && jndex == 13) {
							g.setColor(Color.green);
							g.fillOval((getWidth() / 2) - (getWidth() / 3), (getHeight() / 2) - (getHeight() / 3), (getWidth() / 3) * 2, (getHeight() / 3) * 2);
						}
					}
				};
				l.setBackground(colors.get(grid[i][j]));
				l.setOpaque(true);
				list.add(l);
				mainPanel.add(l);
			}
			labels.add(list);
		}
		add(set(col(10, 10, 0, fw(topPanel), f(mainPanel), tf), BORDER(sp.em(0, 10, 0, 10))));
	}

	@Override
	protected void action() {
		tf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				int saveX = user.x;
				int saveY = user.y;
				if(k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN) user.x += k - 39;
				if(k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT) user.y += k - 38;
				user.x = (user.x == 0 ? 1 : (user.x == 14 ? 13 : user.x));
				user.y = (user.y == 0 ? 1 : (user.y == 14 ? 13 : user.y));
				if(grid[user.x][user.y] == 0) {
					user.x = saveX;
					user.y = saveY;
				}
				labels.get(saveX).get(saveY).repaint();
				labels.get(user.x).get(user.y).repaint();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if(user.equals(new Point(13, 13))) {
					List<Point> bfs = bfsPath(new Point(1, 1), new Point(13, 13));
					sp.infor("탈출 성고!\n" + bfs.size() + "원이 적립되었습니다.");
					sp.user.point += bfs.size();
					sp.user.save();
					dispose();
				}
			}
		});
	}
	
    private void dfs(int x, int y) {
        visit[x][y] = true;
        grid[x][y] = 1;
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);
        
        for (int d : dirs) {
            int nx = x + dx[d];
            int ny = y + dy[d];
            if (nx <= 0 || ny <= 0 || nx >= SIZE - 1 || ny >= SIZE - 1) continue;
            if (!visit[nx][ny]) {
                grid[x + dx[d] / 2][y + dy[d] / 2] = 1;
                dfs(nx, ny);
            }
        }
    }
	

	public static void main(String[] args) {
		Util.start(new game());
	}
}