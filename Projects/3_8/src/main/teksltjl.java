package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.*;
import javax.swing.Timer;

public class teksltjl {

	public teksltjl() {
		showToast("ABCDE");
	}
	
	public static void main(String[] args) {
		new teksltjl();
	}
	
	void showToast(String pass) {
	    int w = 360, h = 100;
	    JWindow toast = new JWindow();
	    toast.setAlwaysOnTop(true);
	    toast.setFocusableWindowState(false);
	    toast.setSize(w, h);
	    toast.setShape(new RoundRectangle2D.Double(0, 0, w, h, 8, 8));

	    JPanel panel = new JPanel(null) {
	        protected void paintComponent(Graphics g) {
	            Graphics2D g2 = (Graphics2D) g;
	            g2.setRenderingHint(
	                    RenderingHints.KEY_ANTIALIASING,
	                    RenderingHints.VALUE_ANTIALIAS_ON);

	            // 검은 배경
	            g2.setColor(new Color(38, 38, 38));
	            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

	            // 왼쪽 초록색 선
	            g2.setColor(new Color(55, 165, 70));
	            g2.fillRect(0, 0, 5, getHeight());

	            // 초록색 원
	            g2.fillOval(15, 29, 44, 44);

	            // 원 내부 P
	            g2.setColor(Color.WHITE);
	            g2.setFont(new Font("Arial", Font.BOLD, 20));
	            g2.drawString("P", 29, 59);
	        }
	    };

	    panel.setOpaque(false);

	    JLabel title = new JLabel("비밀 번호 안내");
	    title.setBounds(75, 17, 200, 25);
	    title.setForeground(new Color(220, 220, 220));
	    title.setFont(new Font("맑은 고딕", Font.BOLD, 14));

	    JLabel message = new JLabel("pass: " + pass);
	    message.setBounds(75, 45, 220, 30);
	    message.setForeground(Color.WHITE);
	    message.setFont(new Font("Arial", Font.BOLD, 17));

	    JButton close = new JButton("×");
	    close.setBounds(320, 5, 35, 35);
	    close.setForeground(new Color(170, 170, 170));
	    close.setFont(new Font("Arial", Font.PLAIN, 22));
	    close.setBorder(null);
	    close.setFocusPainted(false);
	    close.setContentAreaFilled(false);
	    close.addActionListener(e -> toast.dispose());

	    panel.add(title);
	    panel.add(message);
	    panel.add(close);
	    toast.setContentPane(panel);

	    // 작업표시줄을 제외한 화면 오른쪽 아래에 배치
	    Rectangle screen = GraphicsEnvironment
	            .getLocalGraphicsEnvironment()
	            .getMaximumWindowBounds();

	    toast.setLocation(
	            screen.x + screen.width - w - 10,
	            screen.y + screen.height - h - 10
	    );

	    toast.setVisible(true);

	    // 10초 후 자동 종료
	    Timer timer = new Timer(10000, e -> toast.dispose());
	    timer.setRepeats(false);
	    timer.start();
	}
}
