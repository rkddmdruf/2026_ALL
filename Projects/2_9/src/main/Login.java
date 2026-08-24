package main;

import java.awt.*;
import java.awt.geom.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import orms.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;
import utils.*;

public class Login extends CFrame{
	
	JTextField id = tf("ID");
	JTextField pw = tf("PW");
	JButton login = bt("로그인", FONT(sp.font), BG(Color.white));
	
	public Login() {
		set(this.getContentPane(), BORDER(sp.em(30, 30, 40, 30)));
		
		setFrame("로그인", 350, 400, () -> {
			
		});
	}

	@Override
	protected void desing() {
		JLabel logo = new JLabel(sp.getImage("logo/logo.png", 100, 100));
		JLabel label = lb("iDelivery", FONT(sp.font.deriveFont(1).deriveFont(30f)));
		
		add(new BoxPanel(C, 0, 15, 0, logo, label, tfPanel(id), tfPanel(pw), f(login)).setBackColor(Color.white));
	}

	private JPanel tfPanel(JTextField tf) {
		JLabel label = lb(tf.getName(), FONT(sp.font.deriveFont(1).deriveFont(14f)), SIZE(40, 30));
		JPanel p = new BoxPanel(R, 0, 5, 0, label, f(tf)).setBackColor(Color.white);
		return p;
	}
	@Override
	protected void action() {
		login.addActionListener(e -> {
			Util.textIsBlank(id, pw);
			List<userEntity> users = userEntity.findBy(u -> u.id.equals(id.getText()) && u.pw.equals(pw.getText()));
			if(users.isEmpty()) {
				id.setText("");
				pw.setText("");
				throw new RuntimeException("회원정보가 일치하지 않습니다.");
			}
			sp.user = users.get(0);
			sp.infor(sp.user.uname + "님 환영합니다.");
			new Main();
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
