package orms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

	private static final String url = "jdbc:mysql://localhost/skillmall?serverTimezone=Asia/Seoul";
	private static final String id = "root";
	private static final String pw = "1234";
	
	static Connection c;
	static {
		try {
			c = DriverManager.getConnection(url, id ,pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PreparedStatement execute(String str, Object...val) throws Exception{
		PreparedStatement ps = c.prepareStatement(str);
		for(int i = 0; i < val.length; i++) {
			ps.setObject(i + 1, val[i]);
		}
		return ps;
	}
	
	public static List<List<String>> select(String str, Object...val){
		List<List<String>> list = new ArrayList<>();
		try (var re = execute(str, val).executeQuery()){
			while(re.next()) {
				List<String> data = new ArrayList<>();
				list.add(data);
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					data.add(re.getObject(i + 1).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
