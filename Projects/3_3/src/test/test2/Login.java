package test.test2;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Util;
import orms.userEntity;

public class Login extends CFrame{
	
	JTextField id = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(!id.getText().isBlank()) return;
			String s = "전화번호, 사용자 이름 또는 이메일";
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.lightGray);
			g.drawString(s, 1, (getHeight() - fm.getHeight()) / 2 + fm.getAscent()); 
		};
	};
	JPasswordField pw = new JPasswordField();
	JButton login = bt("로그인", FONT(sp.font), FG(Color.white), BG(sp.color));
	JLabel singup = lb("가입하기", FONT(sp.font), FG(sp.color));
	public Login() {
		setFrame("로그인", 350,350);
	}

	@Override
	protected void desing() {
		JPanel p1 = set(col(10, f(lb("ITGRAM", FONT(sp.font.deriveFont(35f).deriveFont(1)), HOA(JLabel.CENTER))), f(id), f(pw), f(login)).setBackColor(Color.white)
				, BORDER(sp.com(sp.line, sp.em(15, 40, 40, 40))));
		JPanel p2 = set(row(20, hg(), lb("계정이 없으신가요?   "), singup, hg()).setBackColor(Color.white), BORDER(sp.com(sp.line, sp.em(15, 15, 15, 15))));
		add(set(col(10, f(p1), fw(p2)), BORDER(sp.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		login.addActionListener(e -> {
			String i = id.getText();
			String p = String.valueOf(pw.getPassword());
			if(i.isBlank() || p.isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			
			List<userEntity> users = userEntity.findBy(u -> u.u_id.equals(i) && u.u_pw.equals(p));
			if(users.isEmpty()) throw new RuntimeException("일치하는 회원이 없습니다.");
			sp.user = users.get(0);
			sp.info(sp.user.u_name + "님 환영합니다.");
			//new Main();
			dispose();
		});
		singup.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				new SingUp();
				dispose();
			};
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
