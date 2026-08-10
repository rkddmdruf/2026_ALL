package orms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntityGenerator {
	static List<List<String>> data = new ArrayList<>();
	static Map<String, List<List<String>>> grouped;
	static String template = "";
	
	public static void initData() {
		
		try (var ps = DBManager.execute("select table_name, column_name, column_type, extra \r\n"
				+ "from information_schema.COLUMNS \r\n"
				+ "where table_schema = database()\r\n"
				+ "order by table_name, ordinal_position"); var re = ps.executeQuery();){
			while(re.next()) {
				List<String> list = new LinkedList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++) {
					list.add(re.getObject(i + 1).toString());
				}
				data.add(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static String JavaType(String mst) {
		String s = mst.toLowerCase();
		if(s.contains("int")) return "Integer";
		if(s.contains("var") || s.contains("text")) return "String";
		if(s.contains("deci") || s.contains("double")) return "Double";
		if(s.contains("datetime")) return "LocalDateTime";
		if(s.contains("date")) return "LocalDate";
		if(s.contains("time")) return "LocalTime";
		return mst;
	}
	static String rs_fieldType(String mst) {
		String s = mst.toLowerCase();
		if(s.contains("int")) return "Int";
		if(s.contains("var") || s.contains("text")) return "String";
		if(s.contains("deci") || s.contains("double")) return "Double";
		if(s.contains("datetime")) return "Timestamp";
		if(s.contains("time")) return "Time";
		if(s.contains("date")) return "Date";
		return mst;
	}
	
	private static void replaceTemplate() throws IOException{
		for(List<List<String>> info : grouped.values()) {
			String class_name = info.get(0).get(0);
			String id = info.get(0).get(1);
			String fields = info.stream().map(row -> String.format("public %s %s;", JavaType(row.get(2)), row.get(1)))
					.collect(Collectors.joining("\n"));
			String rs_fields = info.stream()
					.map(row -> String.format("e.%s= re.get%s(\"%s\");", row.get(1), rs_fieldType(row.get(2)), row.get(1)))
					.collect(Collectors.joining("\n"));
			String update = info.stream().filter(e -> e.get(1) != info.get(0).get(1)).map(row -> String.format("%s = ?", row.get(1)))
					.collect(Collectors.joining(","));
			String sql_insert = info.stream().filter(row -> row.get(3).isBlank()).map(row -> row.get(1))
					.collect(Collectors.joining(","));
			String result = template.replace("${class_name}", class_name).replace("${fields}", fields)
					.replace("${rs_fields}", rs_fields).replace("${id}", id).replace("${update}", update)
					.replace("${sql_insert}", sql_insert);
			
			Path outDir = Path.of("src/orms");
			Files.writeString(outDir.resolve(class_name + "Entity.java"), result);
		}
	}
	public static void main(String[] args) throws IOException {
		initData();
		template = Files.readString(Path.of("src/orms/ex.txt"));
		grouped = data.stream().collect(Collectors.groupingBy(row -> row.get(0)));
		replaceTemplate();
	}
}
