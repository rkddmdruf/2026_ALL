package orms;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import javax.script.*;

public class JsonWriter {
	private static final ScriptEngine E = new ScriptEngineManager().getEngineByName("nashorn");
	private static final String CONV =
		"function conv(o){if(o==null)return null;" +
		"if(o instanceof java.util.Map){var r={},i=o.entrySet().iterator();" +
		"while(i.hasNext()){var e=i.next();r[e.getKey()]=conv(e.getValue());}return r;}" +
		"if(o instanceof java.util.Collection){var a=[],i=o.iterator();" +
		"while(i.hasNext())a.push(conv(i.next()));return a;}" +
		"if(o instanceof java.lang.Number)return o.doubleValue();" +
		"if(o instanceof java.lang.Boolean)return o.booleanValue();return String(o);}";

	public static String write(Object o) { return stringify(o, "null"); }
	public static String writePretty(Object o) { return stringify(o, "null,2"); }

	private static String stringify(Object obj, String opt) {
		try {
			E.eval(CONV);
			E.put("data", toMap(obj));
			return (String) E.eval("JSON.stringify(conv(data)," + opt + ")");
		} catch (Exception e) { throw new RuntimeException(e); }
	}

	private static Object toMap(Object o) {
		if (o == null || o instanceof String || o instanceof Number || o instanceof Boolean) return o;
		if (o instanceof Enum) return ((Enum<?>) o).name();
		if (o instanceof Collection) {
			List<Object> l = new ArrayList<>();
			for (Object v : (Collection<?>) o) l.add(toMap(v));
			return l;
		}
		if (o instanceof Map) {
			Map<String, Object> m = new LinkedHashMap<>();
			((Map<?, ?>) o).forEach((k, v) -> m.put(String.valueOf(k), toMap(v)));
			return m;
		}
		Map<String, Object> m = new LinkedHashMap<>();
		for (Field f : o.getClass().getFields()) {
			if (Modifier.isStatic(f.getModifiers())) continue;
			try { m.put(f.getName(), toMap(f.get(o))); } catch (Exception ignore) {}
		}
		return m;
	}
}