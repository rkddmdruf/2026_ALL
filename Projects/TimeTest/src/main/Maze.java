package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import orms.*;

public class Maze extends CFrame {
	JPanel p = new JPanel(new GridLayout(15, 15, 0, 0));
	int[][] maze = new int[15][15];
	List<int[]> dirs = new ArrayList<>(List.of(new int[] {0, -1}, new int[] {-1, 0}, new int[] {0, 1}, new int[] {1, 0}));
	final int wall = 50_000;
	
	int posR = 1, posC = 1;
	Thread t;
	public Maze() {
		for(int[] row : maze) for(int i = 0; i < row.length; i++) Arrays.fill(row, wall);
		dfs(1, 1);
		setFramed("미로", 500, 600, () -> t.interrupt());
		
		
		path(posR, posC, new boolean[15][15], 5);
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				if(maze[i][j] != wall)
					System.out.println(maze[i][j]);
			}
			System.out.println();
		}
		
		t = new Thread(() -> {
			try {
				while(posR != 13 && posC != 13) {
					Thread.sleep(1000);
					path(posR, posC, new boolean[15][15], 1);
					while(true) {
						for(int[] row : maze) for(int i = 0; i < 15; i++) if(row[i] != wall) row[i]--;
						if(maze[13][13] <= 0)
							break;
						repaint();
						Thread.sleep(30);
					}
				}
				path(1, 1, new boolean[15][15], 5);
				int points = maze[13][13];
				sp.infor("탈출 성공\n" + points + "원이 적립되었습니다.");
				sp.user.point += points;
				sp.user.save();
				dispose();
			} catch (Exception e) {
				t.interrupt();
			}
		});
		t.start();
	}

	private void dfs(int r, int c) {
		maze[r][c] = 0;
		
		List<int[]> clone = new ArrayList<>(dirs);
		Collections.shuffle(clone);
		
		for(int[] dir : clone) {
			int nextR = r + dir[0] * 2;
			int nextC = c + dir[1] * 2;
			if(nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14 ) continue;
			if(maze[nextR][nextC] != wall) continue; 
			maze[r + dir[0]][c + dir[1]] = 0;
			dfs(nextR, nextC);
		}
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
			if(nextR < 0 || nextR >= 14 || nextC < 0 || nextC >= 14 ) continue;
			if(maze[nextR][nextC] == wall) continue; 
			if(visit[nextR][nextC]) continue;
			
			if(path(nextR, nextC, visit, step + 1)) {
				maze[r + dir[0]][c + dir[1]] = step;
				return true;
			}
		}
		return false;
	}

	protected void desing() {
		p.setBorder(sp.line);
		JPanel p1 = col(10, fw(lb("방향기로 이동", FONT(sp.font.deriveFont(1).deriveFont(13f)))), lb("미로 탈출", FONT(sp.font.deriveFont(1).deriveFont(16f)))).setBackColor(Color.white);
		set(p1, BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)));
		for(int i = 0; i < 15; i++) {
			for(int j = 0; j < 15; j++) {
				p.add(label(i, j));
			}
		}
		add(set(col(10, fw(p1), f(p)), BORDER(sp.em(10, 10, 10, 10))).setBackColor(Color.white));
	}
	

	private JLabel label(int i, int j) {
		return new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int value = maze[i][j];
				Arc2D.Double a = new Arc2D.Double();
				a.setArcByCenter(getWidth() / 2, getHeight() / 2, getWidth() / 3, 0, 360, Arc2D.PIE);
				if(value == wall) {
					g2.setColor(Color.black);
					g2.fillRect(0, 0, getWidth(), getHeight());
				}
				if(value >= 0 && value < 5) {
					g2.setColor(new Color(160, 222, 253, value * 50));
					g2.fillRect(0, 0, getWidth(), getHeight());
				}
				if(i == 13 && j == 13) {
					g2.setColor(Color.green);
					g2.fill(a);
				}
				if(i == posR && j == posC) {
					g2.setColor(Color.red);
					g2.fill(a);
				}
			}
		};
	}

	protected void action() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode() - KeyEvent.VK_LEFT;
				if(k < 0  || k > dirs.size()) return;					
				posR += dirs.get(k)[0]; posC += dirs.get(k)[1];
				if(maze[posR][posC] == wall) {posR -= dirs.get(k)[0]; posC -= dirs.get(k)[1];}
				repaint();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Maze());
	}
}
