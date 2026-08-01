package utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;


import orms.userEntity;

public class sp {

	public static Font font = new Font("맑은 고딕", 0, 12);
	public static userEntity user = userEntity.findById(1).get();
	public static Color color = new Color(0, 150, 250);
	public static Color lineColor = new Color(255, 30, 80);
	public static Border line = BorderFactory.createLineBorder(Color.lightGray);

	public static ImageIcon circleImage(int uno, int size) {
		File file = new File("datafiles/profile/" + uno + ".jpg");
		if (!file.exists()) return null;
		try {
			BufferedImage src = ImageIO.read(file);
			BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = out.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setClip(new Ellipse2D.Float(0, 0, size, size));
			g.drawImage(src, 0, 0, size, size, null);
			g.dispose();
			return new ImageIcon(out);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static Image circleLine(ImageIcon imgi, Color color) {
		Image img = imgi.getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setStroke(new BasicStroke(2));
			g2.setColor(color);
			g2.drawImage(img, 4, 4, bf.getWidth() - 8, bf.getHeight() - 8, null);
			g2.drawOval(1, 1, bf.getWidth() - 3, bf.getHeight() - 3);
			g2.dispose();
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	public static ImageIcon getImage(String s, int w, int h) {
		String str = s + (s.contains("icons/") ? ".png" : ".jpg");
		return new ImageIcon(new ImageIcon("datafiles/" + str).getImage().getScaledInstance(w, h, 4));
	}
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	
	public static Border em(int t, int l, int b, int r) { 
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	
	public static Border com(Border b1, Border b2) {
		return BorderFactory.createCompoundBorder(b1, b2);
	}
	
	public static void errs(String s) throws RuntimeException{
		throw new RuntimeException(s);
	}
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
	public static void info(String s) {
		JOptionPane.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
}
