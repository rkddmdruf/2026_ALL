package demo;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class JsonWriter {

	public static String write(Object obj) {
		return write(obj, false);
	}

	public static String writePretty(Object obj) {
		return write(obj, true);
	}

	private static String write(Object obj, boolean pretty) {
		StringBuilder sb = new StringBuilder();
		writeValue(sb, obj, pretty, 0);
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private static void writeValue(StringBuilder sb, Object obj, boolean pretty, int depth) {
		if (obj == null) {
			sb.append("null");
		} else if (obj instanceof String) {
			writeString(sb, (String) obj);
		} else if (obj instanceof Number || obj instanceof Boolean) {
			sb.append(obj);
		} else if (obj instanceof Enum) {
			writeString(sb, ((Enum<?>) obj).name());
		} else if (obj instanceof Map) {
			writeMap(sb, (Map<String, Object>) obj, pretty, depth);
		} else if (obj instanceof Collection) {
			writeArray(sb, (Collection<Object>) obj, pretty, depth);
		} else if (obj.getClass().isArray()) {
			writeArray(sb, java.util.Arrays.asList((Object[]) obj), pretty, depth);
		} else {
			writeBean(sb, obj, pretty, depth);
		}
	}

	private static void writeMap(StringBuilder sb, Map<String, Object> map, boolean pretty, int depth) {
		if (map.isEmpty()) { sb.append("{}"); return; }
		sb.append('{');
		int count = 0;
		for (Map.Entry<String, Object> e : map.entrySet()) {
			if (count++ > 0) sb.append(',');
			newlineIndent(sb, pretty, depth + 1);
			writeString(sb, e.getKey());
			sb.append(':');
			if (pretty) sb.append(' ');
			writeValue(sb, e.getValue(), pretty, depth + 1);
		}
		newlineIndent(sb, pretty, depth);
		sb.append('}');
	}

	private static void writeArray(StringBuilder sb, Collection<Object> list, boolean pretty, int depth) {
		if (list.isEmpty()) { sb.append("[]"); return; }
		sb.append('[');
		int count = 0;
		for (Object v : list) {
			if (count++ > 0) sb.append(',');
			newlineIndent(sb, pretty, depth + 1);
			writeValue(sb, v, pretty, depth + 1);
		}
		newlineIndent(sb, pretty, depth);
		sb.append(']');
	}

	// Serializes a plain object's public instance fields, in the same style
	// as ProjectEntity/Capacity/CarrierItem: no getters, just public fields.
	private static void writeBean(StringBuilder sb, Object obj, boolean pretty, int depth) {
		sb.append('{');
		int count = 0;
		for (Field f : obj.getClass().getFields()) {
			if (Modifier.isStatic(f.getModifiers())) continue;
			if (count++ > 0) sb.append(',');
			newlineIndent(sb, pretty, depth + 1);
			writeString(sb, f.getName());
			sb.append(':');
			if (pretty) sb.append(' ');
			try {
				writeValue(sb, f.get(obj), pretty, depth + 1);
			} catch (IllegalAccessException e) {
				sb.append("null");
			}
		}
		newlineIndent(sb, pretty, depth);
		sb.append('}');
	}

	private static void writeString(StringBuilder sb, String s) {
		sb.append('"');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '"': sb.append("\\\""); break;
			case '\\': sb.append("\\\\"); break;
			case '\n': sb.append("\\n"); break;
			case '\r': sb.append("\\r"); break;
			case '\t': sb.append("\\t"); break;
			case '\b': sb.append("\\b"); break;
			case '\f': sb.append("\\f"); break;
			default:
				if (c < 0x20) {
					sb.append(String.format("\\u%04x", (int) c));
				} else {
					sb.append(c);
				}
			}
		}
		sb.append('"');
	}

	private static void newlineIndent(StringBuilder sb, boolean pretty, int depth) {
		if (!pretty) return;
		sb.append('\n');
		for (int i = 0; i < depth; i++) sb.append("  ");
	}
}
