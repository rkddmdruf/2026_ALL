package test.test6;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.swing.*;

import main.Util;
import orms.*;
import java.util.List;

public class Game extends CFrame {
	int wall = 50_000;
	int[][] maze = new int[15][15];
	int posR = 1, posC = 1;
	List<int[]> dist = List.of(new int[] {0, -1}, new int[] {-1, 0}, new int[] {0, 1}, new int[] {1, 0});
	Thread thread;
	public Game() {
		for(int[] row : maze) Arrays.fill(row, wall);
		createMaze(1, 1);
		setFramed("미로", 500, 600, () -> {
			new Main().setVisible(true);
			thread.interrupt();
		});
		thread = new Thread(() -> {
			try {
				while(posR != 13 && posC != 13) {
					Thread.sleep(1000);
					path(posR, posC, new boolean[15][15], 5);
					while(maze[13][13] >= 0) {
						for(int[] row : maze) for(int i = 0; i < 15 ;i++) if(row[i] != wall) row[i]--;
						Thread.sleep(40);
						repaint();
					}
					repaint();
				}
				path(1, 1, new boolean[15][15], 5);
				sp.infor("탈출 성공!\n" + maze[14][14] + "원이 적립었습니다.");
				sp.user.point += maze[14][14];
				dispose();
			} catch (Exception e) {
				e.printStackTrace();
				thread.interrupt();
			}
		});
		thread.start();
	}

	protected void desing() {
		JPanel p = new JPanel(new GridLayout(15, 15));
		for(int x = 0; x < 15; x++) {
			for(int y = 0; y < 15; y++) {
				p.add(label(x, y));
			}
		}
		
		add(set(col(10,
				set(col(10, fw(lb("방향키로 이동", FONT(sp.font.deriveFont(14f)))), lb("미로 탈출", FONT(sp.font.deriveFont(20f).deriveFont(1)))), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)), BG(Color.white)),
				f(p)
				), BORDER(sp.em(10, 10, 10, 10))));
	}
	
	private void createMaze(int x, int y) {
		maze[x][y] = 0;
		
		List<int[]> clone = new ArrayList<>(dist);
		Collections.shuffle(clone);
		
		for(int[] d : clone) {
			int nextR = x + d[0] * 2;
			int nextC = y + d[1] * 2;
			if(nextR < 0 || nextR >= 15 || nextC < 0 || nextC >= 15) continue;
			if(maze[nextR][nextC] != wall) continue;
			maze[x + d[0]][y + d[1]] = 0;
			createMaze(nextR, nextC);
		}
	}
	
	private boolean path(int x, int y, boolean[][] visit, int step) {
		visit[x][y] = true;
		
		if(x == 13 && y == 13) {
			maze[x][y] = step;
			return true;
		}
		
		for(int[] d : dist) {
			int nextR = x + d[0];
			int nextC = y + d[1];
			if(nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14) continue;
			if(maze[nextR][nextC] == wall) continue;
			if(visit[nextR][nextC]) continue;
			
			if(path(nextR, nextC, visit, step + 1)) {
				maze[x + d[0]][y + d[1]] = step;
				return true;
			}
		}
		return false;
	}
	
	private JLabel label(int x, int y) {
		return new JLabel() {
			int r;
			@Override
			protected void paintComponent(Graphics g) {
				r = getWidth() / 3;
				int value = maze[x][y];
				super.paintComponent(g);
				if((value >= 0 && value < 5)) {
					g.setColor(new Color(160, 222, 253, value * 45));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				if(value == wall) {
					g.setColor(Color.gray.darker().darker());
					g.fillRect(0, 0, getWidth(), getHeight());
				}
				if((x == posR && y == posC) || (x == 13 && y == 13)) {
					g.setColor(x == 13 && y == 13 ? Color.green : Color.red);
					g.fillOval(getWidth() / 2 - r, getHeight() / 2 - r, r * 2, r * 2);
				}
			}
		};
	}

	protected void action() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode() - KeyEvent.VK_LEFT;
				if(k < 0 || k > 4) return;
				int[] d = dist.get(k).clone();
				int nr = posR + d[0], nc = posC + d[1];
				if(maze[nr][nc] <= 10_000) { posR = nr; posC = nc; }
				repaint();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Game());
	}
}