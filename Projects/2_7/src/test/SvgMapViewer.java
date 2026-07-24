package test;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SVG 지도(<path d="..." id="...">)를 읽어서
 * 각 지역(path)마다 랜덤 색을 입혀 렌더링하는 뷰어.
 *
 * 이 SVG는 커브 없이 M(이동) / L(직선) / Z(닫기) 명령만 사용하므로
 * 별도 라이브러리 없이 직접 파싱 가능.
 *
 * 좌표는 구역별로 나눠서 regionPoints 맵에 들어간다.
 *   "Seoul" -> [ [ (284,169), (285,170), ... ] ]                  서브패스 1개
 *   "Jeonnam" -> [ [본토 좌표...], [섬1 좌표...], [섬2 좌표...] ]   서브패스 36개
 * Path2D는 이 좌표에서 만들어 쓰기 때문에 원본은 한 군데(맵)뿐이다.
 */
public class SvgMapViewer extends JPanel {
	List<Color> colors = Arrays.asList(
			new Color(0xB3AC97),
			new Color(0x98D1A7),
			new Color(0xE5D1AF),
			new Color(0xD6BADD),
			new Color(0xA0D6D6),
			new Color(0xE1C7CC),
			new Color(0xCEB1B0),
			new Color(0x97B5B4),
			new Color(0xCEDBD8),
			new Color(0xDBA1A8),
			new Color(0xB3A4BD),
			new Color(0xC6CEDD),
			new Color(0xD2D4BB),
			new Color(0x9EB7B9),
			new Color(0xAECBE5),
			new Color(0xABC5DC),
			new Color(0xFFFDD0)
			);

    private static class Region {
        String id;
        Path2D.Double path;
        Color color;
    }

    /** 명령 문자(M,L,Z) 또는 숫자(음수/소수 포함) */
    private static final Pattern TOKEN = Pattern.compile("([MLZ])|(-?\\d+(\\.\\d+)?)");

    private final List<Region> regions = new ArrayList<>();

    /**
     * 구역별 좌표.  id -> 서브패스 목록 -> 좌표 목록
     *
     * 안쪽 List가 두 겹인 이유: 한 지역이 여러 덩어리로 나뉠 수 있다.
     * (전라남도의 섬들, 인천의 섬들, 경기도 안에 뚫린 서울 구멍 등)
     * 이걸 한 줄로 펴버리면 섬끼리 선으로 이어져서 도형이 망가진다.
     *
     * 순서는 SVG에 나온 순서 그대로(LinkedHashMap).
     */
    private final Map<String, List<List<Point2D.Double>>> regionPoints = new LinkedHashMap<>();

    private double minX, minY, width, height; // viewBox

    public SvgMapViewer(String svgFilePath) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(svgFilePath));

        Element svgRoot = doc.getDocumentElement();
        String viewBox = svgRoot.getAttribute("viewBox"); // "minX minY width height"
        String[] vb = viewBox.trim().split("\\s+");
        minX = Double.parseDouble(vb[0]);
        minY = Double.parseDouble(vb[1]);
        width = Double.parseDouble(vb[2]);
        height = Double.parseDouble(vb[3]);

        NodeList paths = doc.getElementsByTagName("path");

        for (int i = 0; i < paths.getLength(); i++) {
            Element p = (Element) paths.item(i);
            String d = p.getAttribute("d");
            String id = p.getAttribute("id");
            if (id == null || id.isEmpty()) id = "path" + i;

            // 1) d 문자열 -> 구역별 좌표
            List<List<Point2D.Double>> rings = parsePoints(d);
            regionPoints.put(id, rings);

            // 2) 좌표 -> 그리기용 Path2D
            Region r = new Region();
            r.id = id;
            r.path = toPath(rings);
            r.color = colors.get(i % colors.size());
            regions.add(r);
        }

        setPreferredSize(new Dimension(800, 800));
        setBackground(Color.WHITE);
    }

    // ------------------------------------------------------------ 좌표 파싱

    /**
     * M/L/Z 명령만 사용하는 SVG path 'd' 문자열을 서브패스별 좌표 목록으로 변환.
     * M을 만날 때마다 새 덩어리가 시작되고, L은 그 덩어리에 점을 추가한다.
     * Z(닫기)는 좌표가 없으므로 따로 처리할 게 없다.
     */
    private List<List<Point2D.Double>> parsePoints(String d) {
        List<List<Point2D.Double>> rings = new ArrayList<>();
        List<Point2D.Double> cur = null;
        Matcher m = TOKEN.matcher(d);

        Double pendingX = null;   // x를 읽고 y를 기다리는 중

        while (m.find()) {
            String cmd = m.group(1);
            if (cmd != null) {
                if (cmd.charAt(0) == 'M') {     // 새 덩어리 시작
                    cur = new ArrayList<>();
                    rings.add(cur);
                }
                pendingX = null;
                continue;
            }

            double v = Double.parseDouble(m.group(2));
            if (pendingX == null) {
                pendingX = v;                    // x
            } else {
                if (cur == null) {               // M 없이 숫자부터 나온 경우 방어
                    cur = new ArrayList<>();
                    rings.add(cur);
                }
                cur.add(new Point2D.Double(pendingX, v));   // (x, y) 완성
                pendingX = null;
            }
        }

        rings.removeIf(List::isEmpty);
        return rings;
    }

    /** 좌표 목록 -> Path2D. 각 덩어리는 닫힌 다각형으로 만든다. */
    private Path2D.Double toPath(List<List<Point2D.Double>> rings) {
        Path2D.Double path = new Path2D.Double();
        // SVG 기본 fill-rule이 nonzero. 경기도 안의 서울 구멍이 이 규칙으로 뚫린다.
        path.setWindingRule(Path2D.WIND_NON_ZERO);

        for (List<Point2D.Double> ring : rings) {
            Point2D.Double first = ring.get(0);
            path.moveTo(first.x, first.y);
            for (int i = 1; i < ring.size(); i++) {
                Point2D.Double pt = ring.get(i);
                path.lineTo(pt.x, pt.y);
            }
            path.closePath();
        }
        return path;
    }

    // ------------------------------------------------------------ 좌표 꺼내기

    /** 구역 id 목록 (SVG 순서) */
    public List<String> getRegionIds() {
        return new ArrayList<>(regionPoints.keySet());
    }

    /** 구역 전체 좌표 (덩어리별로 나뉜 상태) */
    public List<List<Point2D.Double>> getPoints(String id) {
        return regionPoints.getOrDefault(id, Collections.emptyList());
    }

    /** 구역에서 가장 큰 덩어리. 섬 빼고 본토만 필요할 때. */
    public List<Point2D.Double> getMainRing(String id) {
        List<Point2D.Double> best = Collections.emptyList();
        for (List<Point2D.Double> ring : getPoints(id)) {
            if (ring.size() > best.size()) best = ring;
        }
        return best;
    }

    /** 구역을 한 줄로 편 좌표. 범위 계산처럼 순서가 상관없을 때만 쓸 것. */
    public List<Point2D.Double> getFlatPoints(String id) {
        List<Point2D.Double> all = new ArrayList<>();
        for (List<Point2D.Double> ring : getPoints(id)) all.addAll(ring);
        return all;
    }

    /** 구역이 차지하는 사각 범위 (SVG 좌표계) */
    public Rectangle2D.Double getBounds(String id) {
        double x1 = Double.MAX_VALUE, y1 = Double.MAX_VALUE;
        double x2 = -Double.MAX_VALUE, y2 = -Double.MAX_VALUE;
        for (List<Point2D.Double> ring : getPoints(id)) {
            for (Point2D.Double pt : ring) {
                x1 = Math.min(x1, pt.x); y1 = Math.min(y1, pt.y);
                x2 = Math.max(x2, pt.x); y2 = Math.max(y2, pt.y);
            }
        }
        if (x1 > x2) return new Rectangle2D.Double();
        return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
    }

    /** 그리기용 도형이 필요할 때 */
    public Path2D.Double getPath(String id) {
        for (Region r : regions) {
            if (r.id.equals(id)) return r.path;
        }
        return null;
    }

    /** 어떻게 나뉘었는지 확인용 */
    public void printRegionSummary() {
        for (Map.Entry<String, List<List<Point2D.Double>>> e : regionPoints.entrySet()) {
            int total = 0;
            for (List<Point2D.Double> ring : e.getValue()) total += ring.size();
            Rectangle2D.Double b = getBounds(e.getKey());
            System.out.printf("%-10s 덩어리 %2d개  좌표 %4d개  범위 x %.0f~%.0f  y %.0f~%.0f%n",
                    e.getKey(), e.getValue().size(), total,
                    b.x, b.x + b.width, b.y, b.y + b.height);
        }
    }

    // ---------------------------------------------------------------- 렌더링

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // viewBox -> 패널 크기에 맞게 스케일
        double scale = Math.min(getWidth() / width, getHeight() / height);
        g2.translate(-minX * scale, -minY * scale);
        g2.scale(scale, scale);

        for (Region r : regions) {
            g2.setColor(r.color);
            g2.fill(r.path);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(0.5f));
            g2.draw(r.path);
        }
        g2.dispose();
    }

    public static void main(String[] args) throws Exception {
        String svgPath = args.length > 0 ? args[0] : "datafiles/backimg/map.svg";

        SwingUtilities.invokeLater(() -> {
            try {
                SvgMapViewer viewer = new SvgMapViewer(svgPath);
                viewer.printRegionSummary();   // 콘솔에서 좌표가 어떻게 나뉘었는지 확인

                JFrame frame = new JFrame("지역별 랜덤 색 지도");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new JScrollPane(viewer));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}