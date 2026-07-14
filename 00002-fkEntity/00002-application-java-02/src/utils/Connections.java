package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;


public class Connections {

	private static Connection getCon() throws SQLException{
		return DriverManager.getConnection("jdbc:mysql://localhost/italba?serverTimezone=Asia/Seoul", "root", "1234");
	}
	
	private static PreparedStatement getPs(String string, Object...val) throws SQLException{
		PreparedStatement ps = getCon().prepareStatement(string);
		for(int i = 0; i < val.length; i++) 
			ps.setObject(i + 1, val[i]);
		return ps;
	}
	
	public static List<Data> select(String string, Object...val){
		List<Data> list=  new ArrayList<>();
		try {
			ResultSet re = getPs(string, val).executeQuery();
			while(re.next()) {
				Data data = new Data();
				for(int i = 0; i < re.getMetaData().getColumnCount(); i++)
					data.add(re.getObject(i + 1));
				list.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
