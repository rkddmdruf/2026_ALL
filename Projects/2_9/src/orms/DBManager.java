package orms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
	
	private static final String url = "jdbc:mysql://localhost/idelivery?serverTimezone=Asia/Seoul";
	private static final String id = "root";
	private static final String pw = "1234";
	
	private static Connection c;
	static {
		try {
			c = DriverManager.getConnection(url, id, pw);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private static Connection getConnection() throws SQLException{
		return c;
	}
	
	public static PreparedStatement execute(String str, Object...val) throws SQLException{
		PreparedStatement ps = getConnection().prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		for(int i = 0; i < val.length; i++) {
			ps.setObject(i + 1, val[i]);
		}
		return ps;
	}
	
	public static List<List<String>> select(String str, Object...val){
		List<List<String>> list = new ArrayList<>();
		try (var rs = execute(str, val); var re = rs.executeQuery()){
			while(re.next()) {
				List<String> l = new ArrayList<>();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					l.add(re.getObject(i + 1).toString());
				list.add(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
