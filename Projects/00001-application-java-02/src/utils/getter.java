package utils;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

public class getter {

	public static final String text = "K-TRADE";
	public static final Font font = new Font("맑은 고딕", 0, 12);
	public static final Color color = new Color(95, 130, 220);
	public static final Color backColor = new Color(245, 247, 255);
	public static final Color darkColor = new Color(22, 30, 58);
	
	public static final Border line = BorderFactory.createLineBorder(Color.black);
	public static Border line(Color Color) {
		return BorderFactory.createLineBorder(Color);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	public static Border com(Border outBorder, Border inBorder) {
		return BorderFactory.createCompoundBorder(outBorder, inBorder);
	}
	public static void infor(String str) {
		JOptionPane.showMessageDialog(null, str, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void err(String str) {
		JOptionPane.showMessageDialog(null, str, "x", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static int check(String str) {
		return JOptionPane.showConfirmDialog(null, str, "확인", JOptionPane.YES_NO_OPTION);
	}
}
