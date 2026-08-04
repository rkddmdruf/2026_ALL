package main;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.util.Optional;

public class Login extends CFrame{
	JTextField id = tf("아이디", SIZE(300, 26));
	JPasswordField pw = comp(JPasswordField::new, NAME("비밀번호"), SIZE(300, 26));
	CButton login = comp(CButton::new, TEXT("로그인"), FG(Color.white), BG(sp.color), SIZE(0, 32));

	boolean admin = false;

	public Login() {
		pw.setEchoChar('●');
		login.r = 0;
		setFramed("로그인", 485, 230, () -> {
			if(admin) new Admin();
			else new Main();
		});
	}

	@Override
	protected void desing() {
		add(set(col(18,
				lb("LOGIN", FG(sp.color), FONT(sp.font.deriveFont(28f).deriveFont(1))),
				field(id),
				field(pw),
				row(10, hg(70), f(login)).setBackColor(Color.white)
				).setBackColor(Color.white), BORDER(sp.em(25, 50, 25, 60))));
	}

	private BoxPanel field(JTextField t) {
		return row(10, set(L(t.getName(), 70), FONT(sp.font.deriveFont(1))), f(t)).setBackColor(Color.white);
	}

	@Override
	protected void action() {
		login.addActionListener(e -> {
			String i = id.getText();
			String p = String.valueOf(pw.getPassword());
			if(i.isBlank()) {
				throw new RuntimeException("아이디를 입력해주세요.");
			}
			if(p.isBlank()) {
				throw new RuntimeException("비밀번호를 입력해주세요.");
			}
			if(i.equals("admin") && p.equals("1234")) {
				admin = true;
				sp.infor("관리자님 환영합니다.");
				dispose();
				return;
			}
			Optional<userEntity> user = userEntity.findFirst(u -> u.id.equals(i) && u.pw.equals(p));
			if(user.isEmpty()) {
				id.setText("");
				pw.setText("");
				throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			sp.user = user.get();
			sp.infor("환영합니다");
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
