package test;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

/**
 * SVG의 &lt;path&gt; 하나 = 행정구역 하나.
 */
public class MapRegion {

    private final String id;          // SVG의 id (예: "Seoul")
    private final String englishName; // SVG의 data-name (예: "Seoul")
    private final String label;       // 화면에 그릴 한글 이름
    private final Path2D.Double shape;// SVG 좌표계(viewBox 기준) 도형

    private Point2D.Double labelPoint; // 라벨을 찍을 지점 (SVG 좌표계)
    private Color color = Color.LIGHT_GRAY;

    public MapRegion(String id, String englishName, String label, Path2D.Double shape) {
        this.id = id;
        this.englishName = englishName;
        this.label = label;
        this.shape = shape;
    }

    public String getId() { return id; }
    public String getEnglishName() { return englishName; }
    public String getLabel() { return label; }
    public Shape getShape() { return shape; }

    public Point2D.Double getLabelPoint() { return labelPoint; }
    public void setLabelPoint(Point2D.Double p) { this.labelPoint = p; }

    public Color getColor() { return color; }
    public void setColor(Color c) { this.color = c; }

    @Override
    public String toString() {
        return id + "(" + label + ")";
    }
}
