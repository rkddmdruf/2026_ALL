package utils;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import orms.userEntity;

public class getter {

	public static final Font font = new Font("맑은 고딕", 0, 12);
	public static final Color color = new Color(230, 240, 250);
	public static final Border line = BorderFactory.createLineBorder(Color.black);
	public static userEntity user = userEntity.findById(1).get();
	public static final DecimalFormat df = new DecimalFormat("###,###");
	
	
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon("datafiles/" + s).getImage().getScaledInstance(w, h, 4));
	}
	public static Border line(Color Color) {
		return BorderFactory.createLineBorder(Color);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	public static Border eLine(Color color, int t, int l, int b, int r) {
		return com(line(color), em(t, l, b, r));
	}
	public static Border com(Border outBorder, Border inBorder) {
		return BorderFactory.createCompoundBorder(outBorder, inBorder);
	}
	public static void infor(String str) {
		JOptionPane.showMessageDialog(null, str, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void err(String str) {
		JOptionPane.showMessageDialog(null, str, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	public static int check(String str) {
		return JOptionPane.showConfirmDialog(null, str, "확인", JOptionPane.YES_NO_OPTION);
	}
	
	public static Integer qution(String str) {
		JTextField tf = new JTextField();
		if(JOptionPane.showConfirmDialog(null, new Object[] {"수량을 입력해주세요.", tf}, "질문", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.OK_OPTION)
			try {
				Integer n = Integer.parseInt(tf.getText());
				return n;
			} catch (Exception e) {
				if(str != null && !str.isBlank()) throw new RuntimeException(str);
			}
		return null;
	}
}
