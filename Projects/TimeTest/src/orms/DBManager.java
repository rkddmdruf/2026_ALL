package orms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DBManager {

	private static final String url = "jdbc:mysql://localhost/idelivery?serverTimezone=Asia/Seoul";
	private static final String id = "root";
	private static final String pw = "1234";
	
	private static Connection c;
	
	static {
		try {
			c = DriverManager.getConnection(url, id, pw);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static PreparedStatement execute(String s, Object...val) throws Exception{
		PreparedStatement ps = c.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
		for(int i = 0; i < val.length; i++) 
			ps.setObject(i + 1, val[i]);
		return ps;
	}
	
}
