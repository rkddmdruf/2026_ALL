package main;

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

public class Game extends CFrame{
	final int SIZE = 15;
	int[][] grid = new int[15][15];
	boolean[][] visit = new boolean[15][15];
	int[] dx = {0,0,2,-2}, dy = {2,-2,0,0};
	List<Color> colors = Arrays.asList(new Color(64, 64, 64), Color.white);
	List<List<JLabel>> labels = new ArrayList<>();
	
	List<Point> testLines = new ArrayList<>();
	List<Point> testLines2 = new ArrayList<>();
	List<Point> test = new ArrayList<>();
	JTextField tf = comp(JTextField::new, SIZE(0, 0));
	
	Point user = new Point(1, 1);

	/** 트레일에 있는 점 하나. alpha는 이 점만의 고유한 밝기라서, 다른 점이 지워져도 안 바뀜. */
	private static class TrailDot {
		Point p;
		float alpha;
		TrailDot(Point p, float alpha) { this.p = p; this.alpha = alpha; }
	}
	List<TrailDot> gaidTrail = new CopyOnWriteArrayList<>();

	// 0 벽, 1 길 2 도착지, 3 시작점
	public Game() {
		dfs(1, 1);
		setFrame("미로", 600, 650, () -> {});
		Point p1 = testLines2.get(0);
		test.add(p1);
		for(int i = 1; i < testLines2.size(); i++) {
			Point p2 = testLines2.get(i);
			test.add(new Point(p1.x + ((p2.x - p1.x) / 2), p1.y + ((p2.y - p1.y) / 2)));
			p1 = new Point(p2.x, p2.y);
			test.add(p2);
		}
		new Thread(() -> {
			try {
				while (true) {
					Point start = new Point(user.x, user.y);
					Point end = new Point(13, 13);
					List<Point> path = bfsPath(start, end);

					List<TrailDot> dots = new ArrayList<>();
					int pathIndex = 0;
					List<Point> prevVisible = new ArrayList<>();

					// 아직 갈 길이 남아있거나(pathIndex < path.size()), 아직 안 사라진 dot이 있는 동안 계속
					while (pathIndex < path.size() || !dots.isEmpty()) {

						// 1) 있던 dot들 전부 한 단계씩 옅어짐
						for (TrailDot d : dots) d.alpha -= 0.2f;
						dots.removeIf(d -> d.alpha <= 0.01f);

						// 2) 길이 남아있으면 맨 앞에 새 dot(가장 진한 색)을 추가
						if (pathIndex < path.size()) {
							dots.add(0, new TrailDot(path.get(pathIndex), 1.0f));
							pathIndex++;
						}

						gaidTrail.clear();
						gaidTrail.addAll(dots);

						List<Point> nowVisible = new ArrayList<>();
						for (TrailDot d : dots) nowVisible.add(d.p);

						// 방금까지 보이던 칸 + 지금 보이는 칸 다 repaint (안 그러면 잔상 남음)
						List<Point> toRepaint = new ArrayList<>(prevVisible);
						toRepaint.addAll(nowVisible);
						SwingUtilities.invokeLater(() -> {
							for (Point pt : toRepaint) labels.get(pt.x).get(pt.y).repaint();
						});

						prevVisible = nowVisible;
						Thread.sleep(20);
					}

					Thread.sleep(1000); // 다 사라지고 나서 1초 뒤 다시 시작
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
				if (nx < 0 || ny < 0 || nx >= SIZE || ny >= SIZE) continue;
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
		JPanel topPanel = set(col(10, fillWidth(lb("방향키로 이동", FONT(getter.font.deriveFont(14f).deriveFont(1)))), lb("미로 탈출", FONT(getter.font.deriveFont(1).deriveFont(20f)))),
				BG(Color.white), BORDER(getter.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)));
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
						if(user.x == index && user.y == jndex) {
								g.setColor(Color.red);
								g.fillOval((getWidth() / 2) - (getWidth() / 3), (getHeight() / 2) - (getHeight() / 3), (getWidth() / 3) * 2, (getHeight() / 3) * 2);
						}
						if(index == 13 && jndex == 13) {
							g.setColor(Color.green);
							g.fillOval((getWidth() / 2) - (getWidth() / 3), (getHeight() / 2) - (getHeight() / 3), (getWidth() / 3) * 2, (getHeight() / 3) * 2);
						}
						// 트레일 그리기: 각 dot이 자기 고유 alpha로 그려짐 (남은 개수와 무관)
						List<TrailDot> snapshot = gaidTrail;
						for (TrailDot d : snapshot) {
							if (d.p.x == index && d.p.y == jndex) {
								g.setColor(new Color(120, 190, 255, (int) (255 * d.alpha)));
								g.fillRect(0, 0, getWidth(), getHeight());
								break;
							}
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
		add(set(col(10, 10, 0, fillWidth(topPanel), fill(mainPanel), tf), BORDER(getter.em(0, 10, 0, 10))));
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
				if(user.y == 0) user.y = 1;
				if(user.y == 14) user.y = 13;
				if(user.x == 0) user.x = 1;
				if(user.x == 14) user.x = 13;
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
					getter.infor("탈출 성고!\n" + testLines2.size() + "원이 적립되었습니다.");
					getter.user.point += testLines2.size();
					getter.user.save();
					dispose();
				}
			}
		});
	}
	
    private void dfs(int x, int y) {
        visit[x][y] = true;
        grid[x][y] = 1;
        testLines.add(new Point(x, y));
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);
        
        if (x == SIZE - 2 && y == SIZE - 2) {
            testLines2 = new ArrayList<>(testLines);
        }
        
        for (int d : dirs) {
            int nx = x + dx[d];
            int ny = y + dy[d];
            if (nx <= 0 || ny <= 0 || nx >= SIZE - 1 || ny >= SIZE - 1) continue;
            if (!visit[nx][ny]) {
                grid[x + dx[d] / 2][y + dy[d] / 2] = 1;
                dfs(nx, ny);
            }
        }
        testLines.remove(testLines.size() - 1);
    }
	

	public static void main(String[] args) {
		Util.start(new Game());
	}
}