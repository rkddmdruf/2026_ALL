package test;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 지도를 그리는 패널.
 *  - 각 시/도를 랜덤 색으로 채우고
 *  - 도형 안쪽에서 계산한 자리에 한글 이름을 찍는다.
 *
 * 라벨 자리는 전부 계산으로 정한다.
 *   1) LabelPlacer 로 도형 안에 들어가는 자리를 찾고 (내접 사각형 또는 내접원)
 *   2) 그 자리에 맞춰 글자 크기를 정하고
 *   3) 그래도 이웃 라벨과 겹치면 위/아래/옆으로 자동으로 비켜준다.
 */
public class KoreaMapPanel extends JPanel {

    /** 라벨 자리를 잡는 방식. */
    public enum LabelMode {
        /** 내접 사각형 - 글씨 모양(가로로 김)에 맞춰서 자리와 크기를 같이 구한다. */
        RECT("내접 사각형"),
        /** 내접원 중심 - 경계에서 가장 먼 점. 자리만 구한다. */
        CIRCLE("내접원");

        private final String label;
        LabelMode(String label) { this.label = label; }
        @Override public String toString() { return label; }
    }

    /**
     * 자동 결과가 마음에 안 드는 곳만 손으로 밀고 싶을 때 쓰는 표(기본은 비어 있음).
     * 단위는 SVG 좌표(viewBox 기준), 값은 (dx, dy).
     * 예: LABEL_NUDGE.put("Seoul", new Point2D.Double(0, -8));
     */
    private static final Map<String, Point2D.Double> LABEL_NUDGE = new HashMap<>();

    private static final Color BACKGROUND = new Color(0xF7F7F5);
    private static final Color BORDER     = Color.WHITE;
    private static final Color TEXT       = new Color(0x33, 0x33, 0x38);
    private static final Color TEXT_HALO  = new Color(255, 255, 255, 225);
    private static final Color BOX_GUIDE  = new Color(0xE0, 0x33, 0x55, 170);

    private static final float MIN_FONT = 9f;
    private static final float MAX_FONT = 24f;
    private static final float REF_SIZE = 100f;   // 글자 비율 잴 때 쓰는 기준 크기

    private final Rectangle2D.Double viewBox;
    private final List<MapRegion> regions;

    private LabelMode labelMode = LabelMode.RECT;
    private boolean showLabels = true;
    private boolean showLabelBox = false;
    private boolean pastel = true;

    // SVG 좌표계 계산 결과 (창 크기와 무관해서 한 번만 구하면 된다)
    private final Map<String, Rectangle2D.Double> fitBoxes = new HashMap<>();

    // 화면 좌표로 변환해둔 캐시 (창 크기가 바뀔 때만 다시 계산)
    private final Map<String, Shape> deviceShapes = new HashMap<>();
    private final Map<String, Point2D.Double> devicePoints = new HashMap<>();
    private final List<LabelBox> labelBoxes = new ArrayList<>();
    private AffineTransform lastTransform;
    private double lastScale = 1;
    private boolean labelLayoutDirty = true;

    private final Font labelFontBase;
    private final Random random = new Random();

    public KoreaMapPanel(Rectangle2D.Double viewBox, List<MapRegion> regions) {
        this.viewBox = viewBox;
        this.regions = regions;
        this.labelFontBase = pickKoreanFont();
        setBackground(BACKGROUND);

        // 내접원 기준점은 글씨와 무관하니 미리 계산해둔다.
        for (MapRegion r : regions) {
            Point2D.Double p = LabelPlacer.findLabelPoint(r.getShape());
            Point2D.Double nudge = LABEL_NUDGE.get(r.getId());
            if (nudge != null) { p.x += nudge.x; p.y += nudge.y; }
            r.setLabelPoint(p);
        }
        randomizeColors();
    }

    // ------------------------------------------------------------------ 색

    /** 모든 지역 색을 새로 뽑는다. */
    public void randomizeColors() {
        // 황금비(0.618)만큼 색상(hue)을 돌리면 뽑을 때마다 색이 확실히 갈린다.
        // 배정 순서를 섞어서 매번 다른 배치가 나오게 함.
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < regions.size(); i++) order.add(i);
        Collections.shuffle(order, random);

        float hue = random.nextFloat();
        for (Integer idx : order) {
            float sat, bri;
            if (pastel) {
                sat = 0.18f + random.nextFloat() * 0.20f;   // 연한 파스텔
                bri = 0.88f + random.nextFloat() * 0.10f;
            } else {
                sat = 0.55f + random.nextFloat() * 0.35f;   // 선명한 색
                bri = 0.75f + random.nextFloat() * 0.20f;
            }
            regions.get(idx).setColor(Color.getHSBColor(hue, sat, bri));
            hue = (hue + 0.618033988f) % 1f;
        }
        repaint();
    }

    // --------------------------------------------------------------- 설정

    public void setPastel(boolean pastel) { this.pastel = pastel; randomizeColors(); }
    public boolean isPastel() { return pastel; }

    public void setShowLabels(boolean show) { this.showLabels = show; repaint(); }
    public boolean isShowLabels() { return showLabels; }

    /** 라벨이 어느 사각형/점을 기준으로 앉았는지 눈으로 확인할 때. */
    public void setShowLabelBox(boolean show) { this.showLabelBox = show; repaint(); }
    public boolean isShowLabelBox() { return showLabelBox; }

    public void setLabelMode(LabelMode mode) {
        if (mode == null || mode == labelMode) return;
        this.labelMode = mode;
        this.labelLayoutDirty = true;
        repaint();
    }
    public LabelMode getLabelMode() { return labelMode; }

    // --------------------------------------------------------------- 그리기

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        updateTransform();

        // 1) 면 채우기 + 경계선
        g2.setStroke(new BasicStroke((float) Math.max(0.6, lastScale * 0.9),
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (MapRegion r : regions) {
            Shape s = deviceShapes.get(r.getId());
            if (s == null) continue;
            g2.setColor(r.getColor());
            g2.fill(s);
            g2.setColor(BORDER);
            g2.draw(s);
        }

        // 2) 글씨
        if (showLabels) {
            if (labelLayoutDirty) layoutLabels(g2.getFontRenderContext());
            if (showLabelBox) drawGuides(g2);
            for (LabelBox b : labelBoxes) drawLabel(g2, b);
        }
        g2.dispose();
    }

    /** 흰 테두리를 두른 글씨 - 어떤 색 위에 올라와도, 서로 겹쳐도 읽힌다. */
    private void drawLabel(Graphics2D g2, LabelBox b) {
        GlyphVector gv = b.font.createGlyphVector(g2.getFontRenderContext(), b.text);
        Rectangle2D vb = gv.getVisualBounds();
        float x = (float) (b.cx - vb.getWidth() / 2 - vb.getX());
        float y = (float) (b.cy - vb.getHeight() / 2 - vb.getY());
        Shape outline = gv.getOutline(x, y);

        g2.setStroke(new BasicStroke(b.font.getSize2D() * 0.22f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(TEXT_HALO);
        g2.draw(outline);
        g2.setColor(TEXT);
        g2.fill(outline);
    }

    /** 계산된 내접 사각형 / 내접원 기준점을 화면에 보여준다(확인용). */
    private void drawGuides(Graphics2D g2) {
        g2.setColor(BOX_GUIDE);
        g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10f, new float[]{3f, 3f}, 0f));
        for (MapRegion r : regions) {
            if (labelMode == LabelMode.RECT) {
                Rectangle2D.Double box = fitBoxes.get(r.getId());
                if (box == null) continue;
                g2.draw(lastTransform.createTransformedShape(box));
            } else {
                Point2D.Double p = devicePoints.get(r.getId());
                if (p == null) continue;
                g2.draw(new Ellipse2D.Double(p.x - 3, p.y - 3, 6, 6));
            }
        }
    }

    // ---------------------------------------------------- 라벨 자동 배치

    /** 자리를 잡고, 글자 크기를 정하고, 겹치면 비켜 앉히는 계산. */
    private void layoutLabels(FontRenderContext frc) {
        labelBoxes.clear();
        float base = (float) Math.max(MIN_FONT, Math.min(MAX_FONT, lastScale * 18));
        // 글씨 하한도 지도 크기를 따라간다. 안 그러면 창을 키웠을 때
        // 광역시 이름만 9pt로 남아서 깨져 보인다.
        float floor = Math.max(MIN_FONT, base * 0.68f);

        // 넓은 지역부터 그린다. 겹쳤을 때 작은 광역시 이름이 위로 올라오게 하려는 것.
        List<MapRegion> order = new ArrayList<>(regions);
        order.sort(Comparator.comparingDouble(this::deviceArea).reversed());

        for (MapRegion r : order) {
            Shape s = deviceShapes.get(r.getId());
            if (s == null) continue;

            // 글자 모양의 가로세로 비 (폰트 크기와 상관없이 일정하다)
            Rectangle2D ink = labelFontBase.deriveFont(REF_SIZE)
                    .createGlyphVector(frc, r.getLabel()).getVisualBounds();
            double aspect = ink.getWidth() / ink.getHeight();
            double inkPerPt = ink.getHeight() / REF_SIZE;   // 폰트 1pt당 실제 글자 높이

            double ax, ay;      // 기준점(화면 좌표)
            float size;

            if (labelMode == LabelMode.RECT) {
                Rectangle2D.Double box = fitBox(r, aspect);
                if (box == null) {                       // 너무 작은 도형
                    Point2D.Double p = devicePoints.get(r.getId());
                    ax = p.x; ay = p.y; size = floor;
                } else {
                    Point2D.Double c = new Point2D.Double(box.getCenterX(), box.getCenterY());
                    lastTransform.transform(c, c);
                    ax = c.x; ay = c.y;

                    // 이 사각형에 딱 들어가는 글자 크기 (90%만 써서 여백을 남김)
                    double byHeight = box.height * lastScale / inkPerPt;
                    double byWidth  = box.width * lastScale / (inkPerPt * aspect);
                    size = (float) Math.min(base, Math.min(byHeight, byWidth) * 0.9);
                    size = Math.max(floor, size);
                }
            } else {
                Point2D.Double p = devicePoints.get(r.getId());
                ax = p.x; ay = p.y;
                // 내접원 방식은 크기 정보가 없으니 지역 폭에 맞을 때까지 줄인다.
                Rectangle2D rb = s.getBounds2D();
                size = base;
                while (size > floor
                        && aspect * inkPerPt * size > rb.getWidth() * 0.95) {
                    size -= 0.5f;
                }
                size = Math.max(floor, size);   // 0.5씩 빼다가 하한을 넘어가지 않게
            }

            // 계산된 자리에 그대로 앉힌다. 이웃과 겹쳐도 손대지 않는다.
            labelBoxes.add(new LabelBox(r.getLabel(), labelFontBase.deriveFont(size), ax, ay));
        }
        labelLayoutDirty = false;
    }

    /** 내접 사각형은 글씨 비율에만 좌우되므로 지역당 한 번만 구한다. */
    private Rectangle2D.Double fitBox(MapRegion r, double aspect) {
        if (!fitBoxes.containsKey(r.getId())) {
            fitBoxes.put(r.getId(), LabelPlacer.findLabelBox(r.getShape(), aspect));
        }
        return fitBoxes.get(r.getId());
    }

    private double deviceArea(MapRegion r) {
        Shape s = deviceShapes.get(r.getId());
        if (s == null) return 0;
        Rectangle2D b = s.getBounds2D();
        return b.getWidth() * b.getHeight();
    }

    /** 창 크기에 맞춰 지도를 가운데 정렬해서 꽉 채우는 변환을 만든다. */
    private void updateTransform() {
        int margin = 12;
        double sx = (getWidth() - margin * 2) / viewBox.width;
        double sy = (getHeight() - margin * 2) / viewBox.height;
        double s = Math.max(0.01, Math.min(sx, sy));
        double tx = (getWidth() - viewBox.width * s) / 2 - viewBox.x * s;
        double ty = (getHeight() - viewBox.height * s) / 2 - viewBox.y * s;
        AffineTransform at = new AffineTransform(s, 0, 0, s, tx, ty);

        if (at.equals(lastTransform)) return;    // 크기가 그대로면 캐시 재사용
        lastTransform = at;
        lastScale = s;
        deviceShapes.clear();
        devicePoints.clear();
        for (MapRegion r : regions) {
            deviceShapes.put(r.getId(), at.createTransformedShape(r.getShape()));
            Point2D.Double dst = new Point2D.Double();
            at.transform(r.getLabelPoint(), dst);
            devicePoints.put(r.getId(), dst);
        }
        labelLayoutDirty = true;
    }

    /** 시스템에 있는 한글 폰트를 순서대로 찾아본다. */
    private static Font pickKoreanFont() {
        String[] candidates = {
                "Malgun Gothic", "맑은 고딕",           // Windows
                "Apple SD Gothic Neo", "AppleGothic",  // macOS
                "Noto Sans CJK KR", "Noto Sans KR",    // Linux
                "NanumGothic", "나눔고딕"
        };
        List<String> available = Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String name : candidates) {
            if (available.contains(name)) return new Font(name, Font.BOLD, 13);
        }
        return new Font(Font.SANS_SERIF, Font.BOLD, 13);
    }

    /** 화면에 실제로 찍을 글씨 한 덩어리. */
    private static class LabelBox {
        final String text;
        final Font font;
        final double cx, cy;   // 글씨 중심

        LabelBox(String text, Font font, double cx, double cy) {
            this.text = text; this.font = font;
            this.cx = cx; this.cy = cy;
        }
    }
}
