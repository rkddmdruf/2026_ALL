package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;

import orms.*;


public class Login extends CFrame{

	JTextField tf1 = comp(JTextField::new);
	JTextField tf2 = comp(JTextField::new);
	
	JButton b1 = bt("로그인");
	
	public Login() {
		setFrame("로그인", 300, 400);
	}
	
	@Override
	protected void desing() {
		JLabel l = lb("iDelivery");
		l.setIcon(sp.getImage("logo/logo.png", 150, 150));
		l.setVerticalTextPosition(JLabel.BOTTOM);
		add(set(col(10, l,
				fw(row(0, lb("ID", SIZE(40, 30)), f(tf1))), 
				fw(row(0, lb("PW", SIZE(40, 30)), f(tf2))), 
				b1
		), BORDER(sp.em(40, 40, 40, 40))).setBackColor(Color.white));
		revalidate();
		repaint();
	}

	@Override
	protected void action() {
		b1.addActionListener(ac -> {
			String s1 = tf1.getText();
			String s2 = tf2.getText();
			
			if(s1.isBlank() || s2.isBlank()) sp.tException("빈칸이 있습니다.");
			
			sp.user = userEntity.findByFrist(e -> e.id.equals(s1) && e.pw.equals(s2)).orElseThrow(() -> {
				tf1.setText("");
				tf2.setText("");
				return sp.exception("회원 정보가 일치하지 않습니다.");
			});
			
			sp.infor(sp.user.uname + "님 환영합니다.");
			new Main();
			dispose();
		});
	}

	
	public static void main(String[] args) {
			SwingUtilities.invokeLater(() -> new Login().setVisible(true));
			Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
			Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
				@Override
				protected void dispatchEvent(AWTEvent event) {
					try {
						super.dispatchEvent(event);
					} catch (Exception e) {
						handle(e);
					}
				}
			});
		
	}
	private static void handle(Throwable throwable) {
		throwable.printStackTrace();
		sp.err(throwable.getMessage());
	}
}
