package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Maze {
    static final int SIZE = 15;
    static int[][] grid = new int[SIZE][SIZE];
    static boolean[][] visited = new boolean[SIZE][SIZE];

    static int[] dx = {0, 0, 2, -2};
    static int[] dy = {2, -2, 0, 0};

    static Random rand = new Random();

    static final List<Color> colors = Arrays.asList(Color.black, Color.white);
    public static void main(String[] args) {
        // 전부 벽
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(grid[i], 0);
        }

        // DFS 시작
        dfs(1, 1);

        print();
        JFrame f = new JFrame("fds");
        JPanel panel = new JPanel(new GridLayout(15, 15));
        for(int i = 0; i < 15; i++) {
        	for(int j = 0; j < 15; j++) {
        		JLabel l = new JLabel();
        		l.setBackground(colors.get(grid[i][j]));
        		l.setOpaque(true);
        		panel.add(l);
        	}
        }
        f.add(panel);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(500, 500);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
    }

    static void dfs(int x, int y) {
        visited[x][y] = true;
        grid[x][y] = 1;

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);

        for (int d : dirs) {
            int nx = x + dx[d];
            int ny = y + dy[d];

            // 범위 체크
            if (nx <= 0 || ny <= 0 || nx >= SIZE - 1 || ny >= SIZE - 1)
                continue;

            if (!visited[nx][ny]) {
                // 🔥 중간 벽 뚫기
                grid[x + dx[d] / 2][y + dy[d] / 2] = 1;

                dfs(nx, ny);
            }
        }
    }

    static void print() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }
}