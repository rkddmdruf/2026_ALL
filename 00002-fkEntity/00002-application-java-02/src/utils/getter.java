package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import orms.Account;
import orms.storeEntity;
import orms.userEntity;

public class getter {

	public static String n = BorderLayout.NORTH;
	public static String s = BorderLayout.SOUTH;
	public static String c = BorderLayout.CENTER;
	public static String w = BorderLayout.WEST;
	public static String e = BorderLayout.EAST;
	
	public static userEntity user = userEntity.findById(1).get();
	public static storeEntity store;
	public static Account account;
	
	public static Font font = new Font("맑은 고딕", 0, 12);
	public static Color color = new Color(255, 60, 20);
	public static Color orangeColor = new Color(255, 125, 0);
	
	public static void infor(String s) {
		JOptionPane.showMessageDialog(null, s, "정보", JOptionPane.INFORMATION_MESSAGE);
	}
	public static void err(String s) {
		JOptionPane.showMessageDialog(null, s, "경고", JOptionPane.ERROR_MESSAGE);
	}
	
	public static boolean inputMs(String s) {
		return JOptionPane.showConfirmDialog(null, s, "질문", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
	public static void main(String[] args) {
	}
}

