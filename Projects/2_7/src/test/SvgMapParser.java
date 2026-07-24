package test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * map.svg를 읽어서 MapRegion 목록으로 바꿔주는 파서.
 * 외부 라이브러리 없이 JDK 기본 DOM 파서만 사용한다.
 *
 * 지원 명령: M m L l H h V v C c Q q Z z  (이 지도는 M/L/Z만 사용)
 * 미지원: A(호) - 필요해지면 여기만 확장하면 된다.
 */
public class SvgMapParser {

    /** SVG의 id -> 화면에 그릴 한글 이름. 이름을 바꾸고 싶으면 여기만 고치면 된다. */
    public static final Map<String, String> KOREAN_NAMES = new LinkedHashMap<>();
    static {
        KOREAN_NAMES.put("Seoul",     "서울특별시");
        KOREAN_NAMES.put("Incheon",   "인천광역시");
        KOREAN_NAMES.put("Gyeonggi",  "경기도");
        KOREAN_NAMES.put("Gangwon",   "강원도");
        KOREAN_NAMES.put("Chungbuk",  "충청북도");
        KOREAN_NAMES.put("Chungnam",  "충청남도");
        KOREAN_NAMES.put("Sejong",    "세종시");
        KOREAN_NAMES.put("Daejeon",   "대전광역시");
        KOREAN_NAMES.put("Jeonbuk",   "전라북도");
        KOREAN_NAMES.put("Jeonnam",   "전라남도");
        KOREAN_NAMES.put("Gwangju",   "광주광역시");
        KOREAN_NAMES.put("Gyeongbuk", "경상북도");
        KOREAN_NAMES.put("Gyeongnam", "경상남도");
        KOREAN_NAMES.put("Daegu",     "대구광역시");
        KOREAN_NAMES.put("Ulsan",     "울산광역시");
        KOREAN_NAMES.put("Busan",     "부산광역시");
        KOREAN_NAMES.put("Jeju",      "제주도");
    }

    private final Rectangle2D.Double viewBox;
    private final List<MapRegion> regions;

    private SvgMapParser(Rectangle2D.Double viewBox, List<MapRegion> regions) {
        this.viewBox = viewBox;
        this.regions = regions;
    }

    public Rectangle2D.Double getViewBox() { return viewBox; }
    public List<MapRegion> getRegions() { return regions; }

    // ------------------------------------------------------------------ 읽기

    public static SvgMapParser parse(File file) throws Exception {
        DocumentBuilder db = newBuilder();
        return build(db.parse(file));
    }

    public static SvgMapParser parse(InputStream in) throws Exception {
        DocumentBuilder db = newBuilder();
        return build(db.parse(in));
    }

    private static DocumentBuilder newBuilder() throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        // 네임스페이스를 끄면 태그명을 그냥 "path"로 찾을 수 있다.
        f.setNamespaceAware(false);
        f.setValidating(false);
        // 외부 DTD 조회 시도 방지 (오프라인에서 파싱이 멈추는 것 방지)
        f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        return f.newDocumentBuilder();
    }

    private static SvgMapParser build(Document doc) {
        Element svg = doc.getDocumentElement();
        Rectangle2D.Double vb = parseViewBox(svg);

        List<MapRegion> list = new ArrayList<>();
        NodeList paths = doc.getElementsByTagName("path");
        for (int i = 0; i < paths.getLength(); i++) {
            Element p = (Element) paths.item(i);
            String id = p.getAttribute("id");
            String en = p.getAttribute("data-name");
            String d = p.getAttribute("d");
            if (d == null || d.isEmpty()) continue;
            if (id == null || id.isEmpty()) id = "path" + i;

            Path2D.Double shape = parsePathData(d);
            // SVG 기본 fill-rule은 nonzero. (구멍 뚫린 도형 처리)
            shape.setWindingRule(Path2D.WIND_NON_ZERO);

            String label = KOREAN_NAMES.getOrDefault(id, en != null && !en.isEmpty() ? en : id);
            list.add(new MapRegion(id, en, label, shape));
        }
        return new SvgMapParser(vb, list);
    }

    private static Rectangle2D.Double parseViewBox(Element svg) {
        String vb = svg.getAttribute("viewBox");
        if (vb != null && !vb.trim().isEmpty()) {
            String[] t = vb.trim().split("[\\s,]+");
            if (t.length == 4) {
                return new Rectangle2D.Double(
                        Double.parseDouble(t[0]), Double.parseDouble(t[1]),
                        Double.parseDouble(t[2]), Double.parseDouble(t[3]));
            }
        }
        // viewBox가 없으면 width/height로 대체
        double w = parseLength(svg.getAttribute("width"), 800);
        double h = parseLength(svg.getAttribute("height"), 600);
        return new Rectangle2D.Double(0, 0, w, h);
    }

    private static double parseLength(String s, double def) {
        if (s == null) return def;
        s = s.replaceAll("[^0-9.\\-]", "");
        if (s.isEmpty()) return def;
        return Double.parseDouble(s);
    }

    // --------------------------------------------------------- path 데이터 파싱

    public static Path2D.Double parsePathData(String d) {
        Path2D.Double path = new Path2D.Double();
        PathTokenizer t = new PathTokenizer(d);

        double curX = 0, curY = 0;   // 현재 점
        double startX = 0, startY = 0; // 서브패스 시작점
        char cmd = 0;

        while (t.hasNext()) {
            if (t.peekIsCommand()) {
                cmd = t.nextCommand();
            } else if (cmd == 'M') {
                cmd = 'L';   // M 뒤에 좌표가 이어지면 L로 취급 (SVG 규칙)
            } else if (cmd == 'm') {
                cmd = 'l';
            } else if (cmd == 0) {
                break;       // 명령 없이 숫자로 시작하면 잘못된 데이터
            }

            switch (cmd) {
                case 'M': case 'm': {
                    double x = t.nextNumber(), y = t.nextNumber();
                    if (cmd == 'm') { x += curX; y += curY; }
                    path.moveTo(x, y);
                    curX = startX = x;
                    curY = startY = y;
                    break;
                }
                case 'L': case 'l': {
                    double x = t.nextNumber(), y = t.nextNumber();
                    if (cmd == 'l') { x += curX; y += curY; }
                    path.lineTo(x, y);
                    curX = x; curY = y;
                    break;
                }
                case 'H': case 'h': {
                    double x = t.nextNumber();
                    if (cmd == 'h') x += curX;
                    path.lineTo(x, curY);
                    curX = x;
                    break;
                }
                case 'V': case 'v': {
                    double y = t.nextNumber();
                    if (cmd == 'v') y += curY;
                    path.lineTo(curX, y);
                    curY = y;
                    break;
                }
                case 'C': case 'c': {
                    double x1 = t.nextNumber(), y1 = t.nextNumber();
                    double x2 = t.nextNumber(), y2 = t.nextNumber();
                    double x  = t.nextNumber(), y  = t.nextNumber();
                    if (cmd == 'c') {
                        x1 += curX; y1 += curY; x2 += curX; y2 += curY; x += curX; y += curY;
                    }
                    path.curveTo(x1, y1, x2, y2, x, y);
                    curX = x; curY = y;
                    break;
                }
                case 'Q': case 'q': {
                    double x1 = t.nextNumber(), y1 = t.nextNumber();
                    double x  = t.nextNumber(), y  = t.nextNumber();
                    if (cmd == 'q') { x1 += curX; y1 += curY; x += curX; y += curY; }
                    path.quadTo(x1, y1, x, y);
                    curX = x; curY = y;
                    break;
                }
                case 'Z': case 'z': {
                    path.closePath();
                    curX = startX; curY = startY;
                    break;
                }
                default:
                    throw new IllegalArgumentException("지원하지 않는 path 명령: " + cmd);
            }
        }
        return path;
    }

    /** path의 d 문자열을 명령문자/숫자 단위로 잘라주는 간단한 토크나이저. */
    private static class PathTokenizer {
        private final String s;
        private int i = 0;

        PathTokenizer(String s) { this.s = s; }

        private void skipSeparators() {
            while (i < s.length()) {
                char c = s.charAt(i);
                if (c == ' ' || c == ',' || c == '\t' || c == '\n' || c == '\r') i++;
                else break;
            }
        }

        boolean hasNext() { skipSeparators(); return i < s.length(); }

        boolean peekIsCommand() {
            skipSeparators();
            if (i >= s.length()) return false;
            char c = s.charAt(i);
            return Character.isLetter(c) && c != 'e' && c != 'E';
        }

        char nextCommand() { skipSeparators(); return s.charAt(i++); }

        double nextNumber() {
            skipSeparators();
            int start = i;
            if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) i++;
            while (i < s.length()) {
                char c = s.charAt(i);
                if (Character.isDigit(c) || c == '.') { i++; }
                else if (c == 'e' || c == 'E') {        // 지수 표기
                    i++;
                    if (i < s.length() && (s.charAt(i) == '+' || s.charAt(i) == '-')) i++;
                } else break;
            }
            if (start == i) throw new IllegalArgumentException("숫자를 읽을 수 없음 (위치 " + i + ")");
            return Double.parseDouble(s.substring(start, i));
        }
    }
}
