package utils;

import java.awt.Color;

import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import orms.userEntity;

public class sp {

	public static Font font = new Font("맑은 고딕", 0, 12);
	public static DecimalFormat df = new DecimalFormat("#,###");
	public static Color black = new Color(230, 240, 250);
	public static Integer fn = 0;
	
	public static userEntity user = userEntity.findById(1).get();
	public static Border line = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	public static Border com(Border ob, Border ib) {
		return BorderFactory.createCompoundBorder(ob, ib);
	}
	public static Border eLine(Color color, int t, int l, int b, int r) {
		return BorderFactory.createCompoundBorder(line(color), em(t, l, b, r));
	}
	
	public static void tException(String s) {
		throw exception(s);
	}
	
	public static RuntimeException exception(String e) {
		return new RuntimeException(e);
	}
	
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	public static void infor(String s) {
		JOptionPane.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon("datafiles/" + s).getImage().getScaledInstance(w, h, 4));
	}
}
