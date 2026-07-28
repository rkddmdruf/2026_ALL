package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import orms.userEntity;

public class Login extends CFrame{
	JTextField id = set(new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
		};
	}, SIZE(0, 40));
	
	JPasswordField pw = comp(JPasswordField::new, SIZE(0, 40));

	JButton login = bt("로그인", FONT(sp.font.deriveFont(1).deriveFont(14f)), BG(sp.color), FG(Color.white), SIZE(0, 40));
	JLabel signupLink = lb("가입하기", FONT(sp.font.deriveFont(1)), FG(sp.color), HOA(JLabel.CENTER));

	public Login() {
		pw.setEchoChar('*');
		setFrame("로그인", 350, 350);
	}

	@Override
	protected void desing() {
		JLabel title = lb("ITGRAM", FONT(sp.font.deriveFont(1).deriveFont(34f)), HOA(JLabel.CENTER), BORDER(sp.em(0, 0, 20, 0)));
		JPanel bottom = set(row(5, lb("계정이 없으신가요?    ", FONT(sp.font), HOA(JLabel.CENTER)), signupLink).setBackColor(Color.white), BORDER(sp.em(20,75,20,20)));
		
		add(set(col(10, f(set(col(10, fw(title), fw(id), fw(pw), fw(login)).setBackColor(Color.white), BORDER(sp.em(30, 30, 30, 30)))), fw(bottom)), BORDER(sp.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		signupLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		signupLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SingUp();
				dispose();
			}
		});

		login.addActionListener(e -> {
			if (id.getText().isBlank() || pw.getPassword().length == 0)
				throw new RuntimeException("빈칸이 있습니다.");

			String input = id.getText();
			String password = String.valueOf(pw.getPassword());
			
			List<userEntity> users = userEntity.findBy(u ->
					(u.u_id.equals(input) || u.u_phone.equals(input) || u.u_email.equals(input)) && u.u_pw.equals(password));

			if (users.isEmpty())
				throw new RuntimeException("일치하는 회원이 없습니다.");

			userEntity user = users.get(0);
			sp.info(user.u_name + "님 환영합니다.");
			sp.user = user;
			new Main();
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
