package main;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * 전체 배열은 15x15.
 *   - 맨 바깥 테두리(0행, 14행, 0열, 14열)는 전부 벽으로 막음
 *   - 안쪽 13x13 (인덱스 1~13) 영역에 DFS로 미로를 만듦
 *   - 안쪽 좌표 기준 (0,0) ~ (12,12) 가 하나의 경로로 이어짐
 *     (perfect maze라서 모든 칸이 어차피 다 이어져 있음)
 *
 * 안쪽 13x13은 "셀 + 벽" 표현이라 실제 이동 가능한 칸은 7x7 (짝수 좌표: 0,2,4,6,8,10,12)
 * 이고, 홀수 좌표는 칸 사이의 벽 자리.
 */
public class MazePanel extends JPanel {

    public static final int OUTER_SIZE = 15;                 // 전체 배열 크기
    public static final int INNER_SIZE = OUTER_SIZE - 2;      // 13 (테두리 벽 뺀 안쪽)
    public static final int CELLS = (INNER_SIZE + 1) / 2;     // 7 (실제 이동 가능한 칸 수)

    private final int[][] grid; // OUTER_SIZE x OUTER_SIZE, 0=길, 1=벽

    private static final Color WALL_COLOR = new Color(45, 52, 65);
    private static final Color PATH_COLOR = Color.WHITE;

    public MazePanel(long seed) {
        this.grid = generateMaze(seed);
        setPreferredSize(new Dimension(620, 620));
        setBackground(WALL_COLOR);
    }

    public static int[][] generateMaze(long seed) {
        int[][] grid = new int[OUTER_SIZE][OUTER_SIZE];
        for (int[] row : grid) Arrays.fill(row, 1); // 전체를 벽으로 시작 (테두리 포함)

        // 안쪽 13x13 영역만 DFS로 뚫는다 (offset 1만큼 안쪽으로)
        int[][] inner = carveInner(seed); // INNER_SIZE x INNER_SIZE, 0=길 1=벽

        for (int r = 0; r < INNER_SIZE; r++) {
            for (int c = 0; c < INNER_SIZE; c++) {
                grid[r + 1][c + 1] = inner[r][c];
            }
        }
        return grid;
    }

    /** 안쪽 13x13(=7x7칸)만 DFS 재귀 백트래킹으로 미로 생성 */
    private static int[][] carveInner(long seed) {
        int[][] inner = new int[INNER_SIZE][INNER_SIZE];
        for (int[] row : inner) Arrays.fill(row, 1);

        boolean[][] visited = new boolean[CELLS][CELLS];
        Random rnd = new Random(seed);

        int[] dr = {-1, 1, 0, 0}; // 대각선 없음: 상/하/좌/우
        int[] dc = {0, 0, -1, 1};

        Deque<int[]> stack = new ArrayDeque<>();
        visited[0][0] = true;
        inner[0][0] = 0; // 시작 칸 (안쪽 좌표 0,0)
        stack.push(new int[]{0, 0});

        while (!stack.isEmpty()) {
            int[] cur = stack.peek();
            int cellR = cur[0], cellC = cur[1];

            List<Integer> dirs = new ArrayList<>(List.of(0, 1, 2, 3));
            Collections.shuffle(dirs, rnd);

            int chosen = -1, ncR = -1, ncC = -1;
            for (int d : dirs) {
                int tr = cellR + dr[d];
                int tc = cellC + dc[d];
                if (tr >= 0 && tr < CELLS && tc >= 0 && tc < CELLS && !visited[tr][tc]) {
                    chosen = d; ncR = tr; ncC = tc;
                    break;
                }
            }

            if (chosen == -1) {
                stack.pop();
                continue;
            }

            // 셀 좌표(cellR,cellC) -> 안쪽 배열 좌표는 *2 (짝수 좌표가 칸)
            int wallR = cellR * 2 + dr[chosen];
            int wallC = cellC * 2 + dc[chosen];
            inner[wallR][wallC] = 0;              // 두 칸 사이 벽 뚫기
            inner[ncR * 2][ncC * 2] = 0;          // 다음 칸도 길로 표시

            visited[ncR][ncC] = true;
            stack.push(new int[]{ncR, ncC});
        }

        return inner;
    }

    public int[][] getGrid() {
        return grid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render((Graphics2D) g, getWidth(), getHeight());
    }

    public void render(Graphics2D g2, int width, int height) {
        int cell = Math.min(width, height) / OUTER_SIZE;
        int offsetX = (width - cell * OUTER_SIZE) / 2;
        int offsetY = (height - cell * OUTER_SIZE) / 2;

        for (int r = 0; r < OUTER_SIZE; r++) {
            for (int c = 0; c < OUTER_SIZE; c++) {
                g2.setColor(grid[r][c] == 1 ? WALL_COLOR : PATH_COLOR);
                g2.fillRect(offsetX + c * cell, offsetY + r * cell, cell, cell);
            }
        }
    }

    public void printGrid() {
        for (int[] row : grid) {
            StringBuilder sb = new StringBuilder();
            for (int v : row) sb.append(v == 1 ? '#' : ' ');
            System.out.println(sb);
        }
    }

    public static void main(String[] args) throws Exception {
        MazePanel panel = new MazePanel(System.currentTimeMillis());
        panel.printGrid();

        JFrame frame = new JFrame("15x15 (테두리 벽 + 안쪽 13x13 미로)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}