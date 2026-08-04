package demo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

	private final String s;
	private int i;

	private JsonParser(String s) {
		this.s = s;
	}

	public static Object parse(String json) {
		JsonParser p = new JsonParser(json);
		p.skipWs();
		Object v = p.parseValue();
		p.skipWs();
		return v;
	}

	private Object parseValue() {
		skipWs();
		char c = s.charAt(i);
		switch (c) {
		case '{': return parseObject();
		case '[': return parseArray();
		case '"': return parseString();
		case 't': i += 4; return Boolean.TRUE;
		case 'f': i += 5; return Boolean.FALSE;
		case 'n': i += 4; return null;
		default: return parseNumber();
		}
	}

	private Map<String, Object> parseObject() {
		Map<String, Object> map = new LinkedHashMap<>();
		i++;
		skipWs();
		if (s.charAt(i) == '}') { i++; return map; }
		while (true) {
			skipWs();
			String key = parseString();
			skipWs();
			i++; // ':'
			Object value = parseValue();
			map.put(key, value);
			skipWs();
			char c = s.charAt(i++);
			if (c == '}') break;
		}
		return map;
	}

	private List<Object> parseArray() {
		List<Object> list = new ArrayList<>();
		i++;
		skipWs();
		if (s.charAt(i) == ']') { i++; return list; }
		while (true) {
			list.add(parseValue());
			skipWs();
			char c = s.charAt(i++);
			if (c == ']') break;
		}
		return list;
	}

	private String parseString() {
		StringBuilder sb = new StringBuilder();
		i++; // opening quote
		while (true) {
			char c = s.charAt(i++);
			if (c == '"') break;
			if (c == '\\') {
				char e = s.charAt(i++);
				switch (e) {
				case 'n': sb.append('\n'); break;
				case 't': sb.append('\t'); break;
				case 'r': sb.append('\r'); break;
				case 'b': sb.append('\b'); break;
				case 'f': sb.append('\f'); break;
				case 'u':
					sb.append((char) Integer.parseInt(s.substring(i, i + 4), 16));
					i += 4;
					break;
				default: sb.append(e);
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	private Double parseNumber() {
		int start = i;
		while (i < s.length() && "-+.eE0123456789".indexOf(s.charAt(i)) >= 0) i++;
		return Double.parseDouble(s.substring(start, i));
	}

	private void skipWs() {
		while (i < s.length() && Character.isWhitespace(s.charAt(i))) i++;
	}
}
