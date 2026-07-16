package main;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import orms.userEntity;

import static uitls.BoxPanel.*;
import static uitls.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import uitls.*;

public class Login extends CFrame{
	JTextField id = set(new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("ID", 1, (getHeight() - getFontMetrics(getFont()).getHeight()) / 2 + getFontMetrics(getFont()).getAscent());
			}
		};
	}, NAME("user"));
	JPasswordField pw = set(new JPasswordField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("PW", 1, (getHeight() - getFontMetrics(getFont()).getHeight()) / 2 + getFontMetrics(getFont()).getAscent());
			}
		};
	}, NAME("lock"));
	CButton login = comp(CButton::new, TEXT("로그인"), NAME("d"), BG(getter.color), FG(Color.white));
	
	public Login() {
		pw.setEchoChar('●');
		login.arc = 15;
		setFrame("로그인", 325, 200, () -> {});
	}

	@Override
	protected void desing() {
		TitledBorder tBorder = new TitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black), "LOGIN", TitledBorder.TOP, TitledBorder.CENTER, getter.font.deriveFont(1).deriveFont(23f));
		JPanel panel = set(colF(10, setPanel(id), setPanel(pw), setPanel(login))
				, BG(Color.white), BORDER(getter.em(20, 15, 0, 20)));
		add(set(col(5, f(panel)), BORDER(getter.com(getter.em(0, 2, 30, 2), tBorder)), BG(Color.white)));
	}

	private JPanel setPanel(JComponent c) {
		JPanel p = row(20, set(new JLabel(getter.getImage("icon/" + c.getName(), 25, 25)), SIZE(25, 25)), f(c)).setBackColor(Color.white);
		return p;
	}
	@Override
	protected void action() {
		login.addActionListener(e -> {
			String i = id.getText(), p = String.copyValueOf(pw.getPassword());
			if(i.isBlank() || p.isBlank()) {
				throw new RuntimeException("빈칸이 존재합니다.");
			}
			if(i.equals("admin") && p.equals("1234")) {
				getter.infor("관리자님 환영합니다.");
			}
			List<userEntity> users = userEntity.findBy(c -> c.id.equals(i) && c.pw.equals(p));
			if(users.isEmpty()) {
				throw new RuntimeException("해당 유저가 존재하지 않습니다.");
			}
			getter.user = users.get(0);
			getter.infor(getter.user.name + "님 환영합니다");
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
