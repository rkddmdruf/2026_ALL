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
	
	JTextField tf = comp(JTextField::new, SIZE(0, 0));
	
	Point user = new Point(1, 1);
	Point gaid = new Point(-1, -1);
	// 0 벽, 1 길 2 도착지, 3 시작점
	public Game() {
		dfs(1, 1);
		setFrame("미로", 600, 650, () -> {});
		List<Point> test = new ArrayList<>();
		Point p1 = testLines2.get(0);
		test.add(p1);
		for(int i = 1; i < testLines2.size(); i++) {
			Point p2 = testLines2.get(i);
			test.add(new Point(p1.x + ((p2.x - p1.x) / 2), p1.y + ((p2.y - p1.y) / 2)));
			p1 = new Point(p2.x, p2.y);
			test.add(p2);
		}
		new Thread(() ->{
			while(true) {
				try {
					Thread.sleep(1000);
					for(int i = 0; i < test.size(); i++) {
						if(i == 0) Thread.sleep(100);
						Point p = test.get(i);
						gaid = p;
						SwingUtilities.invokeLater(() -> labels.get(p.x).get(p.y).repaint());
						Thread.sleep(10);
					}
					gaid = new Point(-1, -1);
					labels.forEach(e -> e.forEach(c -> c.repaint()));
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}).start();;
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
						if(!gaid.equals(new Point(-1, -1)) && (index == gaid.x && gaid.y == jndex)) {
							g.setColor(Color.blue);
							g.fillRect(0, 0, getWidth(), getHeight());
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
		});
	}
	
    private void dfs(int x, int y) {
        visit[x][y] = true;
        grid[x][y] = 1;
        testLines.add(new Point(x, y));
        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);
        
        if (x == SIZE - 2 && y == SIZE - 2) {
            testLines2 = new ArrayList<>(testLines); // 복사
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
