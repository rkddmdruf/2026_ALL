package test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;

/**
 * 도형 안쪽에서 라벨 자리를 찾는다. 두 가지 방식을 제공한다.
 *
 *  1) findLabelPoint  - 내접원 중심 (거리 변환)
 *  2) findLabelBox    - 내접 사각형 (히스토그램 최대 직사각형)
 *
 * 글씨는 가로로 긴 사각형이라서 보통 2번이 유리하다.
 * 사각형을 쓰면 "여기에 이만한 글씨가 들어간다"까지 같이 나오기 때문에
 * 글자 크기도 계산으로 정할 수 있다.
 *
 * 무게중심(centroid)을 안 쓰는 이유:
 *  - 전라남도처럼 섬이 많으면 무게중심이 바다로 빠진다.
 *  - 경기도처럼 가운데(서울)가 뚫린 도넛 모양이면 무게중심이 구멍 안에 찍힌다.
 * 두 방식 모두 결과가 항상 도형 내부라서 그런 문제가 없다.
 */
public final class LabelPlacer {

    /** 계산용 비트맵의 긴 변 크기(px). 클수록 정밀하고 느리다. */
    private static final int RASTER_SIZE = 512;
    private static final int PAD = 2;

    private LabelPlacer() {}

    // =============================================================== 내접원

    // chamfer 가중치: 상하좌우 5, 대각선 7 (실제 거리 1 : 1.4의 5배 근사)
    private static final int ORTHO = 5;
    private static final int DIAG = 7;
    private static final int INF = Integer.MAX_VALUE / 4;

    /** 경계에서 가장 멀리 떨어진 지점 = 내접원의 중심. */
    public static Point2D.Double findLabelPoint(Shape shape) {
        Mask m = Mask.of(shape);
        if (m == null) {
            Rectangle2D b = shape.getBounds2D();
            return new Point2D.Double(b.getCenterX(), b.getCenterY());
        }

        int[] dist = new int[m.w * m.h];
        for (int i = 0; i < dist.length; i++) dist[i] = m.in[i] ? INF : 0;
        chamferDistanceTransform(dist, m.w, m.h);

        int best = -1, bestIdx = 0;
        for (int i = 0; i < dist.length; i++) {
            if (dist[i] > best) { best = dist[i]; bestIdx = i; }
        }
        return m.toUser(bestIdx % m.w + 0.5, bestIdx / m.w + 0.5);
    }

    /** 두 번(정방향/역방향) 훑는 chamfer 거리 변환. */
    private static void chamferDistanceTransform(int[] d, int w, int h) {
        for (int y = 0; y < h; y++) {                 // 왼쪽 위 -> 오른쪽 아래
            for (int x = 0; x < w; x++) {
                int i = y * w + x;
                if (d[i] == 0) continue;
                int v = d[i];
                if (x > 0)                  v = Math.min(v, d[i - 1]     + ORTHO);
                if (y > 0)                  v = Math.min(v, d[i - w]     + ORTHO);
                if (y > 0 && x > 0)         v = Math.min(v, d[i - w - 1] + DIAG);
                if (y > 0 && x < w - 1)     v = Math.min(v, d[i - w + 1] + DIAG);
                d[i] = v;
            }
        }
        for (int y = h - 1; y >= 0; y--) {            // 오른쪽 아래 -> 왼쪽 위
            for (int x = w - 1; x >= 0; x--) {
                int i = y * w + x;
                if (d[i] == 0) continue;
                int v = d[i];
                if (x < w - 1)              v = Math.min(v, d[i + 1]     + ORTHO);
                if (y < h - 1)              v = Math.min(v, d[i + w]     + ORTHO);
                if (y < h - 1 && x < w - 1) v = Math.min(v, d[i + w + 1] + DIAG);
                if (y < h - 1 && x > 0)     v = Math.min(v, d[i + w - 1] + DIAG);
                d[i] = v;
            }
        }
    }

    // ============================================================ 내접 사각형

    /**
     * 도형 안에 들어가는 가장 큰 축정렬 사각형을 찾는다.
     *
     * @param aspect 넣으려는 글씨의 가로/세로 비. 예를 들어 "경상북도"가 가로 4 세로 1 이면 4.0.
     *               이 값을 주면 "가장 넓은 사각형"이 아니라
     *               "그 글씨를 가장 크게 넣을 수 있는 사각형"을 고른다.
     *               0 이하를 주면 그냥 면적이 최대인 사각형.
     * @return SVG 좌표계 사각형. 못 찾으면 null.
     *
     * 원리: 한 줄씩 내려오면서 "각 칸 위로 흰 칸이 몇 개 쌓였나"를 세면
     *       막대그래프가 된다. 막대그래프 안의 최대 직사각형은 스택으로 O(n)에 구한다.
     *       이걸 모든 행에 대해 반복하면 전체 최대 직사각형이 나온다. -> O(가로 x 세로)
     */
    public static Rectangle2D.Double findLabelBox(Shape shape, double aspect) {
        Mask m = Mask.of(shape);
        if (m == null) return null;

        int w = m.w, h = m.h;
        int[] heights = new int[w];
        int[] stack = new int[w + 1];

        double bestScore = -1;
        int bx = 0, by = 0, bw = 0, bh = 0;

        for (int y = 0; y < h; y++) {
            // 이 행 기준으로 위쪽에 연속으로 몇 칸이 채워져 있는지
            for (int x = 0; x < w; x++) {
                heights[x] = m.in[y * w + x] ? heights[x] + 1 : 0;
            }

            // 막대그래프에서 최대 직사각형 (스택 방식)
            int sp = 0;
            for (int x = 0; x <= w; x++) {
                int cur = (x == w) ? 0 : heights[x];
                while (sp > 0 && heights[stack[sp - 1]] >= cur) {
                    int barH = heights[stack[--sp]];
                    if (barH == 0) continue;
                    int left = (sp == 0) ? 0 : stack[sp - 1] + 1;
                    int barW = x - left;

                    double score = (aspect > 0)
                            // 이 사각형에 글씨를 넣었을 때 가능한 글자 높이
                            ? Math.min(barW / aspect, barH)
                            : (double) barW * barH;

                    if (score > bestScore) {
                        bestScore = score;
                        bx = left; by = y - barH + 1; bw = barW; bh = barH;
                    }
                }
                stack[sp++] = x;
            }
        }
        if (bestScore < 0) return null;

        // 비트맵은 한 칸 단위라서 가장자리가 경계에 딱 붙는다.
        // 반 칸씩 안으로 넣어서 확실히 도형 내부가 되게 한다.
        double pad = Math.min(1.0, Math.min(bw, bh) / 4.0);
        Point2D.Double p0 = m.toUser(bx + pad, by + pad);
        Point2D.Double p1 = m.toUser(bx + bw - pad, by + bh - pad);
        return new Rectangle2D.Double(p0.x, p0.y, p1.x - p0.x, p1.y - p0.y);
    }

    // ================================================================= 공통

    /** 도형을 흑백 비트맵으로 구운 것. 구멍/섬 처리는 Java2D가 알아서 해준다. */
    private static class Mask {
        final boolean[] in;
        final int w, h;
        final double scale, originX, originY;

        private Mask(boolean[] in, int w, int h, double scale, double ox, double oy) {
            this.in = in; this.w = w; this.h = h;
            this.scale = scale; this.originX = ox; this.originY = oy;
        }

        static Mask of(Shape shape) {
            Rectangle2D b = shape.getBounds2D();
            if (b.getWidth() <= 0 || b.getHeight() <= 0) return null;

            double scale = RASTER_SIZE / Math.max(b.getWidth(), b.getHeight());
            int w = (int) Math.ceil(b.getWidth() * scale) + PAD * 2;
            int h = (int) Math.ceil(b.getHeight() * scale) + PAD * 2;

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = img.createGraphics();
            // 안티에일리어싱을 끄면 픽셀이 0 아니면 255라서 판정이 깔끔하다.
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_OFF);
            AffineTransform at = new AffineTransform();
            at.translate(PAD - b.getX() * scale, PAD - b.getY() * scale);
            at.scale(scale, scale);
            g.setColor(Color.WHITE);
            g.fill(at.createTransformedShape(shape));
            g.dispose();

            boolean[] in = new boolean[w * h];
            Raster raster = img.getRaster();
            boolean any = false;
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    boolean v = raster.getSample(x, y, 0) > 127;
                    in[y * w + x] = v;
                    any |= v;
                }
            }
            if (!any) return null;   // 너무 작아서 한 픽셀도 안 찍힌 경우

            return new Mask(in, w, h, scale, b.getX(), b.getY());
        }

        /** 비트맵 좌표 -> SVG 좌표 */
        Point2D.Double toUser(double px, double py) {
            return new Point2D.Double(
                    (px - PAD) / scale + originX,
                    (py - PAD) / scale + originY);
        }
    }
}
