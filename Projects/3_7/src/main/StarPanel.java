package main;
import javax.swing.*;
import java.awt.*;

public class StarPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Polygon star = createStar(getWidth() / 2, getHeight() / 2, 150, 60, 20);

        g2.setColor(Color.YELLOW);
        g2.fillPolygon(star);
        g2.setColor(Color.ORANGE);
        g.drawPolygon(star);
    }

    /**
     * @param cx 중심 x
     * @param cy 중심 y
     * @param outerRadius 바깥 꼭짓점 반지름 (뾰족한 끝)
     * @param innerRadius 안쪽 꼭짓점 반지름 (움푹 들어간 부분)
     * @param numPoints   별의 뾰족한 꼭짓점 개수 (5각별이면 5)
     */
    private Polygon createStar(int cx, int cy, int outerRadius, int innerRadius, int numPoints) {
        Polygon star = new Polygon();
        double angleStep = Math.PI / numPoints; // 바깥-안쪽 번갈아 찍으므로 360/(numPoints*2)

        for (int i = 0; i < numPoints * 2; i++) {
            double radius = (i % 2 == 0) ? outerRadius : innerRadius;
            double angle = i * angleStep - Math.PI / 2; // -90도부터 시작 (위쪽 뾰족점이 맨 위로)

            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            star.addPoint(x, y);
        }
        return star;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Star");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new StarPanel());
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}