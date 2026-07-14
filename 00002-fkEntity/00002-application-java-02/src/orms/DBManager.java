package orms;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.ImageIcon;

import utils.Connections;

public class DBManager {
	private static String url = ("jdbc:mysql://localhost/italba?serverTimezone=Asia/Seoul");
	private static String id = "root";
	private static String pw = "1234";

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, id, pw);
	}

	public static PreparedStatement execute(String str, Object... val) throws SQLException {
		PreparedStatement ps = getConnection().prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		for (int i = 0; i < val.length; i++) {
			if(val[i] instanceof Image) {
				Image image = ((Image) val[i]);
				BufferedImage bi = new BufferedImage(image.getHeight(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = bi.createGraphics();
				g2.drawImage(image, 0, 0, null);
				g2.dispose();
				
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
					ImageIO.write(bi, "png", bos);
				} catch (Exception e) {
					e.printStackTrace();
				}
				ps.setBytes(i + 1, bos.toByteArray());
			}
			else {
				ps.setObject(i + 1, val[i]);
			}
			
		}
		return ps;
	}
}
