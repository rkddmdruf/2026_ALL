package test.test1;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.Util;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Login extends CFrame{
	
	JTextField id = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("전화번호, 사용자 이름 또는 이메일", 1, (getHeight() - g.getFontMetrics().getHeight()) / 2 + g.getFontMetrics().getAscent());
			}
		};
	};
	JPasswordField pw = new JPasswordField();
	JButton login = bt("로그인", FG(Color.white), BG(sp.color));
	JLabel sing = lb("가입하기", FG(sp.color));
	public Login() {
		pw.setEchoChar('*');
		setFrame("로그인", 400, 400);
	}

	@Override
	protected void desing() {
		JPanel p1 = set(col(10, lb("ITGRAM", FONT(sp.font.deriveFont(35f).deriveFont(1)), BORDER(sp.em(0, 0, 30, 0))), f(id), f(pw), f(login)), BORDER(sp.com(sp.line, sp.em(40,40,40,40)))).setBackColor(Color.white);
		JPanel p2 = set(row(10, hg(), lb("계정이 없으신가요?"), sing, hg()), BORDER(sp.com(sp.line, sp.em(15, 15, 15, 15)))).setBackColor(Color.white);
		add(set(col(10, f(p1), fw(p2)), BORDER(sp.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		login.addActionListener(e -> {
			String i = id.getText();
			String p = String.valueOf(pw.getPassword());
			
			if(i.isBlank() || p.isBlank()) throw new RuntimeException("빈칸이 있습니다.");
			List<userEntity> user = userEntity.findBy(u -> u.u_id.equals(i) && u.u_pw.equals(p));
			if(user.isEmpty()) throw new RuntimeException("일치하는 외원이 없습니다.");
			sp.user = user.get(0);
			sp.info(sp.user.u_name + "님 환영합니다");
			//new Main();
			dispose();
		});
		
		sing.addMouseListener(new MouseAdapter() {
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
