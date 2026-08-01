package main;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;


public class Login extends CFrame{
	JTextField id = tf("아이디");
	JPasswordField pw = comp(JPasswordField::new, NAME("비밀번호"));
	JButton login = comp(CButton::new, TEXT("로그인"), FG(Color.white), SIZE(0, 40), BG(sp.blue));
	public Login() {
		pw.setEchoChar('●');
		setFramed("로그인", 325, 375, () -> new Main());
	}

	@Override
	protected void desing() {
		JPanel panel = set( new BoxPanel(C, 0,10,0, setTF(id), setTF(pw)) {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.white);
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				super.paintComponent(g);
			}
		}, BORDER(sp.em(15, 20, 10, 20)));
		panel.setOpaque(false);
		add(set(col(30, 
				col(0, lb("WELCOME BACK", FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(10f))), lb("LOGIN", FONT(sp.font.deriveFont(35f).deriveFont(1)))),
				f(panel),
				fw(login)
				), BORDER(sp.em(30, 20, 30, 10))));
	}
	
	private JPanel setTF(JTextField t) {
		JPanel p = col(10, fw(lb(t.getName(), FONT(sp.font.deriveFont(1)))), f(t));
		p.setOpaque(false);
		return p;
	}

	@Override
	protected void action() {
		login.addActionListener(e -> {
			String i = id.getText();
			String p = String.valueOf(pw.getPassword());
			if(i.isBlank() || p.isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			if(i.equals("admin") && p.equals("1234")) {
				sp.infor("관리자님 환영힙니다.");
				dispose();
			}
			List<userEntity> users = userEntity.findBy(u -> u.uid.equals(i) && u.upw.equals(p));
			if(users.isEmpty()) {
				id.setText("");
				pw.setText("");
				throw new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
			}
			sp.user = users.get(0);
			sp.infor(sp.user.uname + "님 환영합니다.");
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
