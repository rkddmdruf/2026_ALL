package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;

import orms.*;

public class Login extends CFrame {
	JTextField t1 = new JTextField();
	JTextField t2 = new JPasswordField() {{ setEchoChar('●'); }};
	JButton b1 = bt("로그인", BG(sp.color), FG(Color.white));
	
	public Login() {
		setFrameg("로그인", 450, 200, () -> new A_Main());
	}

	protected void desing() {
		JLabel l = lb("LOGIN", FG(sp.color), FONT(sp.font.deriveFont(24f).deriveFont(1)));
		add(set(col(10, 
				l,
				f(row(0, lb("아이디", SIZE(100, 27)), f(t1))),
				f(row(0, lb("비밀번호", SIZE(100, 27)), f(t2))),
				f(row(0, lb("", SIZE(100, 30)), f(b1)))
			), BORDER(sp.em(10, 50, 20, 50))));
	}

	protected void action() {
		b1.addActionListener(e -> {
			String s1 = t1.getText();
			String s2 = t2.getText();
			if(s1.isBlank() || s2.isBlank()) throw new RuntimeException("빈칸이 존재합니다.");
			if(s1.equals("admin") && s2.equals("1234")) {
				sp.infor("관리자님 환영합니다.");
				new Admin();
				dispose();
				return;
			}
			userEntity user = userEntity.findFirst(u -> u.id.equals(s1)).orElseThrow(() -> new RuntimeException("아이디가 존재하지 않습니다."));
			if(!user.pw.equals(s2)) throw new RuntimeException("비밀번호가 올바르지 않습니다.");
			sp.user = user;
			sp.infor(sp.user.name + "님 로그인에 성공하였습니다.");
			new A_Main();
			dispose();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Login());
	}
}