package main;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.swing.*;

import utils.*;

public class Main extends CFrame{
	JLabel timeLabel = lb("현재시간: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), FONT(sp.font.deriveFont(1).deriveFont(16f)));
	JTextField tf1 = tf("출발지", TEXT("서구청"));
	JTextField tf2 = tf("도착지", TEXT("경입교대입구"));
	CButton search  = comp(CButton::new, TEXT("경로검색"), FG(Color.white));
	CButton login = set(new CButton("로그인", sp.getImage("icon/login", 40, 40)));
	CButton myHome = set(new CButton("마이페이지", sp.getImage("icon/user", 40, 40)));
	
	public Main() {
		setFrame("메인", 800, 500, () -> {});
	}
	
	@Override
	protected void desing() {
		
		JPanel paintPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/main.png").getImage(), 0, 0, getWidth() + 16, getHeight() + 39, null);
			}
		};
		paintPanel.setBorder(sp.em(100, 50, 100, 50));
		
		paintPanel.add(mainPanel());
		
		add(paintPanel);
	}
	
	private JPanel mainPanel() {
		int arc = 40;
		JPanel panel = new JPanel(new BorderLayout(15, 15)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                            RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Util.setAlpha(Color.white, 180));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
				g2.setColor(Color.gray);
				g2.drawRoundRect(0, 0, getWidth()-1, getHeight() -1, arc, arc);
			}
		};
		panel.setOpaque(false);
		panel.setBorder(sp.em(20, 25, 20, 25));
		
		JPanel p1 = new BoxPanel(C, 0, 20, 0,
				fw(timeLabel),
				row(10, 10, 0, lb(tf1.getName(), SIZE(50, 22)), f(tf1)).opf(),
				row(10, 10, 0, lb(tf2.getName(), SIZE(50, 22)), f(tf2)).opf(),
				f(search)
				) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                            RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Util.setAlpha(Color.white, 180));
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
				g2.setColor(Color.gray);
				g2.drawRoundRect(0, 0, getWidth()-1, getHeight() -1, arc, arc);
			}
		};
		p1.setOpaque(false);
		p1.setBorder(sp.em(15, 15, 15, 15));
		
		JPanel p = row(20);
		
		
		panel.add(set(new JLabel(new ImageIcon(Util.logo.getScaledInstance(180, 50, 4))), HOA(JLabel.LEFT)), BorderLayout.NORTH);
		JPanel mainPanel = row(20, f(p1), hg(100), f(login), f(myHome));
		mainPanel.setOpaque(false);
		panel.add(mainPanel);
		return panel;
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Util.start(new Main());
	}
	
}
