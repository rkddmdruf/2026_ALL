package test.test5;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Util;
import utils.CFrame;
import utils.getter;

public class Game extends CFrame{
	final int wall = 50_000;
	int[][] maze = new int[15][15];
	int posR = 1;
	int posC = 1;
	List<int[]> dirs = List.of(new int[] {0, -1}, new int[] {-1, 0}, new int[] {0, 1}, new int[] {1, 0});
	public Game() {
		for(int[] row : maze) Arrays.fill(row, wall);
		dfs(1, 1);
		setFrame("미로", 500, 600, () -> {});
	}
	@Override
	protected void desing() {
		JPanel panel = new JPanel(new GridLayout(15, 15));
		panel.setBackground(Color.white);
		
		for(int i = 0; i < 15; i++)
			for(int j = 0; j < 15; j++)
				panel.add(label(i, j));
		
		new Thread(() -> {
			try {
				while(posR != 13 || posC != 13) {
					Thread.sleep(1000);
					path(posR, posC, new boolean[15][15], 5);
					while(maze[13][13] != 0) {
						if(posR == 13 && posC == 13) break;
						for(int[] row : maze) for(int i = 0; i < row.length; i++) if (row[i] > 0 && row[i] != wall) row[i]--;
						repaint();
						Thread.sleep(45);
					}
				}
				
				path(1, 1, new boolean[15][15], 5);
				int point = maze[13][13];
				getter.infor("탈출 성공!\n " + point + "원이 적립되었습니다.");
				getter.user.point += point;
				getter.user.save();
				dispose();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
		add(panel);
	}

	private JLabel label(int x, int y) {
		return new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				Arc2D.Double arc = new Arc2D.Double();
				arc.setArcByCenter(getWidth() / 2, getHeight() / 2, getWidth() / 3, 0, 360, Arc2D.PIE);
				int value = maze[x][y];
				if(value == wall) {
					g2.setColor(Color.black.brighter());
					g2.fillRect(0, 0, getWidth(), getHeight());
				}else if(value <= 5) {
					g2.setColor(new Color(158, 222, 253, value * 50));
					g2.fillRect(0, 0, getWidth(), getHeight());
				}
				
				if(x == posR && y == posC) {
					g2.setColor(Color.red);
					g2.fill(arc);
				}
				if(x == 13 && y == 13) {
					g2.setColor(Color.green);
					g2.fill(arc);
				}
			}
		};
	}
	private boolean path(int r, int c, boolean[][] visit, int step) {
		
		visit[r][c] = true;
		
		if(r == 13 && c == 13) {
			maze[r][c] = step;
			return true;
		}
		
		for(int[] dir : dirs) {
			int nextR = r + dir[0];
			int nextC = c + dir[1];
			if(nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14) continue;
			if(maze[nextR][nextC] == wall) continue;
			if(visit[nextR][nextC]) continue;

			if(path(nextR, nextC, visit, step+1)) {
				if(step >= 5) maze[r][c] = step;
				return true;
			}
		}
		return false;
	}
	
	private void dfs(int r, int c) {
		maze[r][c] = 0;
		
		List<int[]> clone = new ArrayList<>(List.copyOf(dirs));
		Collections.shuffle(clone);
		
		for(int[] dir : clone) {
			int nextR = r + dir[0] * 2;
			int nextC = c + dir[1] * 2;
			if(nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14) continue;
			if(maze[nextR][nextC] != wall) continue;
			maze[r + dir[0]][c + dir[1]] = 0;
			dfs(nextR, nextC);
		}
	}
	@Override
	protected void action() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getExtendedKeyCode() - KeyEvent.VK_LEFT;
				if(k < 0 || k >= dirs.size()) return;
				int r = posR + dirs.get(k)[0];
				int c = posC + dirs.get(k)[1];
				if(maze[r][c] != wall){
					posR = posR + dirs.get(k)[0];
					posC = posC + dirs.get(k)[1];
					System.out.println(posR + ", " + posC);
				}
				repaint();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Game());
	}

}
