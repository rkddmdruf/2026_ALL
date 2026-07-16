package uitls;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import orms.userEntity;

public class getter {
	public static userEntity user;
	public static Color color = new Color(30, 90, 200);
	public static Font font = new Font("맑은 고딕", 0, 12);
	public static DecimalFormat df = new DecimalFormat("###,###");
	
	public static ImageIcon getImage(String string, int w, int h) {
		return new ImageIcon(new ImageIcon("datafiles/" + string + ".png").getImage().getScaledInstance(w, h, 4));
	}
	public static Border line = BorderFactory.createLineBorder(Color.black);
	public static Border line(Color color) {
		return BorderFactory.createLineBorder(color);
	}
	public static Border em(int t, int l, int b, int r) {
		return BorderFactory.createEmptyBorder(t, l, b, r);
	}
	public static Border eLine(Color color, int t, int l, int b, int r) {
		return BorderFactory.createCompoundBorder(line(color), em(t, l, b, r));
	}
	public static Border com(Border ob, Border inb) {
		return BorderFactory.createCompoundBorder(ob, inb);
	}
	
	public static void infor(String str) {
		JOptionPane.showMessageDialog(null, str, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void err(String str) {
		JOptionPane.showMessageDialog(null, str, "경고", JOptionPane.ERROR_MESSAGE);
	}
}
