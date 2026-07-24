package utils;

import java.awt.Color;
import java.awt.Font;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import orms.userEntity;


public class getter {
	public static userEntity user = userEntity.findById(1).get();
	public static final Color color = new Color(30, 90, 200);
	public static final Font font = new Font("맑은 고딕", 0, 12);
	public static final DecimalFormat df = new DecimalFormat("###,###");
	
	public static final Border line = BorderFactory.createLineBorder(Color.black);
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	
	public static void inf(String s) {
		JOptionPane.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
}
