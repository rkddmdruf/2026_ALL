package orms;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProjectEntity {
	public Integer pno;
	public List<Capacity> capacities;
	public List<CarrierItem> items;
	public List<Installment> installments;

	public static class Capacity {
		public String value;
		public Integer price;
		@Override
		public String toString() {
			return "Capacity [value=" + value + ", price=" + price + "]";
		}
	}

	public static class CarrierItem {
		public String type;
		public Integer price;
		@Override
		public String toString() {
			return "CarrierItem [type=" + type + ", price=" + price + "]";
		}
	}

	public static class Installment {
		public Integer month;

		@Override
		public String toString() {
			return "Installment [month=" + month + "]";
		}
	}
	
	public Optional<Capacity> find1(Predicate<Capacity> pre) {
		return capacities.stream().filter(pre).findFirst();
	}
	public Optional<CarrierItem> find2(Predicate<CarrierItem> pre) {
		return items.stream().filter(pre).findFirst();
	}
	public Optional<Installment> find3(Predicate<Installment> pre) {
		return installments.stream().filter(pre).findFirst();
	}
	
	public static final Map<Integer, ProjectEntity> cache = new HashMap<>();
	static { reload(); };

	@Override
	public String toString() {
		return "ProjectEntity [pno=" + pno + ", capacities=" + capacities + ", items=" + items + ", installments="
				+ installments + "]";
	}

	@SuppressWarnings("unchecked")
	public static void reload() {
		cache.clear();
		try {
			String json = Files.readString(Path.of("datafiles/project.json"));

			ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
			engine.put("jsonText", json);
			Object result = engine.eval("Java.asJSONCompatible(JSON.parse(jsonText))");

			List<Object> list = (List<Object>) result;
			for (Object o : list) {
				Map<String, Object> m = (Map<String, Object>) o;
				var e = new ProjectEntity();
				e.pno = num(m.get("pno"));

				e.capacities = new ArrayList<>();
				for (Object co : (List<Object>) m.get("capacities")) {
					Map<String, Object> cm = (Map<String, Object>) co;
					var c = new Capacity();
					c.value = (String) cm.get("value");
					c.price = num(cm.get("price"));
					e.capacities.add(c);
				}

				e.items = new ArrayList<>();
				for (Object io : (List<Object>) m.get("items")) {
					Map<String, Object> im = (Map<String, Object>) io;
					var it = new CarrierItem();
					it.type = (String) im.get("type");
					it.price = num(im.get("price"));
					e.items.add(it);
				}

				e.installments = new ArrayList<>();
				for (Object mo : (List<Object>) m.get("installments")) {
					Map<String, Object> mm = (Map<String, Object>) mo;
					var ins = new Installment();
					ins.month = num(mm.get("month"));
					e.installments.add(ins);
				}

				cache.put(e.pno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Integer num(Object o) {
		return o == null ? null : ((Number) o).intValue();
	}

	public static Optional<ProjectEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<ProjectEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<ProjectEntity> findBy(Predicate<ProjectEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<ProjectEntity> findFirst(Predicate<ProjectEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		if (pno == null) {
			pno = cache.keySet().stream().mapToInt(Integer::intValue).max().orElse(0) + 1;
		}
		cache.put(pno, this);
		writeToFile();
	}

	public void delete() {
		cache.remove(pno);
		writeToFile();
	}

	private static void writeToFile() {
		try {
			List<ProjectEntity> list = findAll();
			list.sort((a, b) -> a.pno - b.pno);
			String json = JsonWriter.writePretty(list);
			Files.writeString(Path.of("datafiles/project.json"), json);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
	}
}
