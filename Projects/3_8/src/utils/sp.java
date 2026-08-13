package utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import orms.userEntity;


public class sp {
	public static userEntity user;
	public static Integer pno; // 마이페이지에서 리뷰작성으로 넘길 제품 번호
	public static Color color = new Color(0, 120, 0);
	public static Font font = new Font("맑은 고딕", 0, 12);
	public static DecimalFormat df = new DecimalFormat("###,###");
	
	public static Border line = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	
	public static Border com(Border outB, Border inB) {
		return BorderFactory.createCompoundBorder(outB, inB);
	}
	
	public static Graphics2D anti(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		return g2;
	}
	public static void infor(String s) {
		JOptionPane.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	public static ImageIcon getImage(String s, int w, int h) {
		if(!s.contains("/")) s = "기종/" + s;
		return new ImageIcon(new ImageIcon("datafiles/" + s + ".jfif").getImage().getScaledInstance(w, h, 4));
	}
}
