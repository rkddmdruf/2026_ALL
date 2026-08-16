package test.test3;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import main.Util;
import orms.*;

public class A_Login extends CFrame {
	JTextField tf1 = new JTextField() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = sp.anti(g);
			if(tf1.getText().isBlank()) {
				g2.setColor(Color.LIGHT_GRAY);
				g2.drawString("전화번호, 사용자 이름 또는 이메일", 2, (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent());
			}
		};
	};
	JTextField tf2 = new JPasswordField();
	JButton b1 = bt("로그인", BG(sp.color), FG(Color.white), SIZE(0, 30));
	JLabel l1 = lb("가입하기", FG(sp.color), HOA(JLabel.LEFT));
	
	public A_Login() {
		setFrame("로그인", 300, 300);
	}

	protected void desing() {
		JPanel p1 = set(col(10,
					lb("ITGRAM", FONT(sp.font.deriveFont(25f).deriveFont(1)), BORDER(sp.em(20, 0, 20, 0))),
					f(col(5, f(tf1), f(tf2)).setBackColor(Color.white)),
					fw(b1)
				), BORDER(sp.com(sp.line, sp.em(10, 30, 20, 30))), BG(Color.white));
		JPanel p2 = set(row(10,
					fw(lb("계정이 없으신가요?", HOA(JLabel.RIGHT))),
					fw(l1)
				), BG(Color.white), BORDER(sp.com(sp.line, sp.em(15, 15, 15, 15))));
		add(set(col(10, 
				f(p1),
				fw(p2)
				), BORDER(sp.em(10, 10, 10, 10))));
	}

	protected void action() {
		l1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SingUp();
				dispose();
			}
		});
		
		b1.addActionListener(e -> {
			String s1 = tf1.getText();
			String s2 = tf2.getText();
			if(s1.isBlank() || s2.isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			sp.user = userEntity.findFirst(c -> c.u_id.equals(s1) && c.u_pw.equals(s2)).orElseThrow(() -> new RuntimeException("일치하는 회원이 없습니다."));
			sp.info(sp.user.u_name + "님 환영합니다.");
			new A_Main();
			dispose();
			
		});
		
	}
	
	public static void main(String[] args) {
		Util.start(new A_Login());
	}
}