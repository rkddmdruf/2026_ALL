package test.test6;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;

import main.Util;
import orms.*;

public class Login extends CFrame {
	JTextField t1 = comp(JTextField::new, BORDER(sp.eLine(Color.LIGHT_GRAY, 5, 5, 5, 5)));
	JTextField t2 = comp(JTextField::new, BORDER(sp.eLine(Color.LIGHT_GRAY, 5, 5, 5, 5)));
	
	JButton b1 = bt("로그인", BG(Color.white));
	
	public Login() {
		setFrame("로그인", 300, 325);
	}

	protected void desing() {
		JLabel l = lb("iDelivery", ICON(sp.getImage("logo/logo.png", 100, 100)), FONT(sp.font.deriveFont(20f).deriveFont(1)));
		l.setHorizontalAlignment(JLabel.CENTER);
		l.setVerticalAlignment(JLabel.TOP);
		l.setHorizontalTextPosition(JLabel.CENTER);
		l.setVerticalTextPosition(JLabel.BOTTOM);
		add(set(col(10, f(l), 
				fw(row(10, lb("ID", SIZE(30, 30)), f(t1))).setBackColor(Color.white),
				fw(row(10, lb("PW", SIZE(30, 30)), f(t2))).setBackColor(Color.white),
				fw(b1)
				), BORDER(sp.em(30, 30, 30, 30)), BG(Color.white)));
	}

	protected void action() {
		b1.addActionListener(e -> {
			String s1 = t1.getText();
			String s2 = t2.getText();
			if(s1.isBlank() || s2.isBlank()) throw new RuntimeException("빈칸이 있습니다.");
			
			sp.user = userEntity.findFirst(c -> c.id.equals(s1) && c.pw.equals(s2)).orElseThrow(() -> {
				t1.setText("");
				t2.setText("");
				t1.requestFocus();
				return new RuntimeException("회원 정보가 일치하지 않습니다.");
			});
			
			sp.infor(sp.user.uname + "님 환영합니다.");
			new Main().setVisible(true);;
			dispose();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Login());
	}
}