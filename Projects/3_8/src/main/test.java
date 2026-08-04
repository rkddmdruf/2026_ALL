package main;

import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class test {

	public static void main(String[] args) {
	    String pass = "";
	    for (int i = 0; i < 6; i++)
	        pass += (char) ('A' + (int) (Math.random() * 26));
	
	    try {
	        SystemTray tray = SystemTray.getSystemTray();
	
	        TrayIcon icon = new TrayIcon(
	                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
	
	        tray.add(icon);
	        icon.displayMessage(
	                "비밀번호 안내",
	                "pass: " + pass,
	                TrayIcon.MessageType.NONE);
	
	        // 일정 시간 뒤 트레이 아이콘 제거
	        Timer timer = new Timer(8000, e -> tray.remove(icon));
	        timer.setRepeats(false);
	        timer.start();
	
	    } catch (Exception e) {
	        // 시스템 알림을 지원하지 않는 환경의 예외 처리
	        JOptionPane.showMessageDialog(null,
	                "pass: " + pass,
	                "비밀번호 안내",
	                JOptionPane.INFORMATION_MESSAGE);
	    }
	}
}
