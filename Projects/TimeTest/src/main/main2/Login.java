package main.main2;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;

import javax.swing.*;

import main.Util;
import orms.userEntity;

public class Login extends CFrame{

	JTextField t1 = comp(JTextField::new);
	JTextField t2 = comp(JTextField::new);
	
	JButton b1 = bt("로그인", BG(Color.white));
	public Login() {
		setFrame("로그인", 300, 300);
	}
	
	@Override
	protected void desing() {
		JLabel l = lb("iDelivery", FONT(sp.font.deriveFont(20f).deriveFont(1)));
		l.setIcon(sp.getImage("logo/logo.png", 80, 80));
		l.setHorizontalTextPosition(JLabel.CENTER);
		l.setVerticalTextPosition(JLabel.BOTTOM);
		
		add(set(col(10, l, 
					fw(row(0, lb("ID", SIZE(40, 30)), f(t1))),
					fw(row(0, lb("PW", SIZE(40, 30)), f(t2))),
					fw(b1)
				), BORDER(sp.em(30, 30, 40, 30))).setBackColor(Color.white));
	}

	@Override
	protected void action() {
		b1.addActionListener(ac -> {
			String s1 = t1.getText();
			String s2 = t2.getText();
			if(s1.isBlank() || s2.isBlank()) sp.exception("빈칸이 있습니다");
			
			sp.user = userEntity.findByFrist(e -> e.id.equals(s1) && e.pw.equals(s2)).orElseThrow(() -> {
				t1.setText("");
				t2.setText("");
				return sp.exception("회원 정보가 일치하지 않습니다.");
			});
			sp.infor(sp.user.uname +"님 환영합니다");
			dispose();
		});
	}
	

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
