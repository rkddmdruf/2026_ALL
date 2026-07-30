package test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JFrame {

	final int WALL = 500_000_000;
	
	int posR = 1;
	int posC = 1;

	List<int[]> dirs = new ArrayList<>();
	
	int[][] maze;
	
	public Game() {
		setSize(700, 700);
		setTitle("미로");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		dirs.add(new int[]{0, -1});
		dirs.add(new int[]{-1, 0});
		dirs.add(new int[]{0, 1});
		dirs.add(new int[]{1, 0});
	
		maze = generateMaze();
		
		design(maze);
		
		dfs(posR, posC, maze);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getExtendedKeyCode() - KeyEvent.VK_LEFT;
				if(k < 0 || k >= dirs.size()) return;
				int nextR = posR + dirs.get(k)[0];
				int nextC = posC + dirs.get(k)[1];
				if (maze[nextR][nextC] != WALL) {
					posR = nextR;
					posC = nextC;
				}
				repaint();
			}
		});
		
		new Thread(() -> {
			try {
				while(true) {
					Thread.sleep(1000);
			    	path(posR, posC, maze, new boolean[15][15], 5);
					while(maze[13][13] != 0) {
						for (int[] row : maze)
							for (int j = 0; j < row.length; ++j)
								if (row[j] > 0 && row[j] != WALL) --row[j];
						repaint();
						Thread.sleep(50);
					}
				}
			} catch (Exception e) {}
		}).start();;
	}
	
	
	public void design(int[][] maze) {
		
		JPanel panel = new JPanel(new GridLayout(15, 15));
		
		for (int i = 0; i < maze.length; ++i) {
			for (int j = 0; j < maze.length; ++j) {
				panel.add(generateCell(i, j));
			}
		}

		add(panel, BorderLayout.CENTER);
	}
	
	private JPanel generateCell(int r, int c) {
		return new JPanel() {
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				Arc2D.Double arc = new Arc2D.Double();
				arc.setArcByCenter(getWidth() / 2.0, getWidth() / 2.0, (getWidth() / 3) * 2, 0, 360, Arc2D.PIE);
				int value = maze[r][c];
				if (value == WALL) {
					g2.setColor(Color.black);
					g2.fillRect(0, 0, getWidth(), getHeight());
				} else if (value <= 5){
					g2.setColor(new Color(158, 222, 253, 50 * value));
					g2.fillRect(0, 0, getWidth(), getHeight());
				}
				if (r == posR && c == posC) {
					g2.setColor(Color.red);
					g2.fill(arc);
				}
				if (r == 13 && c == 13) {
					g2.setColor(Color.green);
					g2.fill(arc);
				}
			};
		};
	}
	

	private int[][] generateMaze() {
		int[][] maze = new int[15][15];
		for (var row : maze) Arrays.fill(row, WALL);
		dfs(1, 1, maze);
		return maze;
	}
	
	private boolean path(int r, int c, int[][] maze, boolean[][] visited, int step) {
		
	    visited[r][c] = true;

	    if (r == 13 && c == 13) {
	        maze[r][c] = step;
	        return true;
	    }

	    for (int[] dir : dirs) {
	        int nextR = r + dir[0];
	        int nextC = c + dir[1];
	        if (nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14) continue;
	        if (maze[nextR][nextC] == WALL) continue;
	        if (visited[nextR][nextC]) continue;

	        if (path(nextR, nextC, maze, visited, step + 1)) {
	            if (step >= 5) maze[r][c] = step;
	            return true;
	        }
	    }
	    return false;
	}
	
	private void dfs(int r, int c, int[][] maze) {
		
		maze[r][c] = 0;
		
		List<int[]> cloned = new ArrayList<>(dirs);
		Collections.shuffle(cloned);
		
		for (int[] dir : cloned) {
			int nextR = r + dir[0] * 2;
			int nextC = c + dir[1] * 2;
			if (nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14) continue;
			if (maze[nextR][nextC] != WALL) continue;
			maze[r + dir[0]][c + dir[1]] = 0;
			dfs(nextR, nextC, maze);
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setVisible(true);
	}
}
