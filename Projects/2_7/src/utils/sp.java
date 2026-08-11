package utils;

import java.awt.Color;
import java.awt.Font;
import java.nio.ByteOrder;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import orms.doctorEntity;
import orms.userEntity;


public class sp {
	public static userEntity user;
	public static doctorEntity doctor;
	public static final Color color = new Color(30, 90, 200);
	public static final Color imgBackColor = new Color(245, 245, 240);
	public static final Font font = new Font("맑은 고딕", 0, 12);
	public static final DecimalFormat df = new DecimalFormat("###,###");
	
	
	public static ImageIcon getImage(String s, int w, int h) {
		return new ImageIcon(new ImageIcon("datafiles/" + s + ".png").getImage().getScaledInstance(w, h, 4));
	}
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
