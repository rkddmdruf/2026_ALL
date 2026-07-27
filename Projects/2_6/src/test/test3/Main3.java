package test.test3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.management.RuntimeErrorException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import main.Util;
import orms.stationEntity;
import uitls.*;
import static uitls.BoxPanel.*;
import static uitls.Properties.*;

public class Main3 extends CFrame{
	JLabel l1 = lb("현재시간: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")), FONT(getter.font.deriveFont(1).deriveFont(16f)));
	JTextField t1 = tf("출발지");
	JTextField t2 = tf("도착지");
	CButton b1  = comp(CButton::new, TEXT("경로검색"), FG(Color.white));
	CButton b2 = set(new CButton(getter.user == null ? "로그인" : "로그아웃", getter.getImage("icon/login", 40, 40)), FG(Color.white));
	CButton b3 = set(new CButton("마이페이지", getter.getImage("icon/user", 40, 40)), FG(Color.white));
	
	public Main3() {
		b3.setEnabled(getter.user != null);
		setFrame("메인", 800, 475, () -> {System.exit(0);});
	}

	@Override
	protected void desing() {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/main.png").getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
		set(panel, BORDER(getter.em(100, 70, 100, 70)));
		panel.add(setMainPanel());
		add(panel);
	}

	private JPanel setMainPanel() {
		int arc = 40;
		var panel = new JPanel(new BorderLayout(10, 10)) {
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
		set(panel, BORDER(getter.em(10,20,10,50)));
		
		JPanel p1 = new BoxPanel(C, 0, 20, 0,
				fw(l1),
				row(10, 10, 0, lb(t1.getName(), SIZE(50, 22)), f(t1)).opf(),
				row(10, 10, 0, lb(t2.getName(), SIZE(50, 22)), f(t2)).opf(),
				f(b1)
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
		p1.setBorder(getter.em(15, 15, 15, 15));
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 20));
		buttonPanel.setOpaque(false);
		buttonPanel.add(b2);
		buttonPanel.add(b3);
		JPanel mainPanel = row(50, f(p1), buttonPanel);
		mainPanel.setOpaque(false);
		panel.add(mainPanel);
		
		panel.add(lb("", ICON(new ImageIcon(Util.logo.getScaledInstance(200, 50, 4)))), BorderLayout.NORTH);
		return panel;
	}
	
	@Override
	protected void action() {
		b1.addActionListener(e -> {
			if(getter.user == null) {
				getter.err("로그인이 되어있지 않습니다.");
				new Login().setVisible(true);
				dispose();
				return;
			}
			if(t1.getText().isBlank() && t2.getText().isBlank()) {
				new metro("", "").setVisible(true);
				dispose();
				return;
			}
			if(stationEntity.findBy(s -> t1.getText().equals(s.name)).isEmpty() || stationEntity.findBy(s -> t2.getText().equals(s.name)).isEmpty()) {
				throw new RuntimeException("역명을 확인해 주세요.");
			};
			new metro(t1.getText(), t2.getText()).setVisible(true);
			dispose();
			return;
		});
		b2.addActionListener(e -> {
			if(getter.user == null) {
				new Login().setVisible(true);
				dispose();
				return;
			}else {
				getter.user = null;
				getter.infor("로그아웃되어있습니다.");
				b2.setText("로그인");
				b3.setEnabled(false);
			}
		});
		new Thread(() -> {
			try {
				while(true) {
					l1.setText("현재시간: " + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();
	}

	public static void main(String[] args) {
		Util.start(new Main3());
	}
}
