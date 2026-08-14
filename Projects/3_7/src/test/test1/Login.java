package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;

import main.Util;
import orms.*;

public class Login extends CFrame {
	JTextField t1 = comp(JTextField::new);
	JTextField t2 = comp(JPasswordField::new);
	
	CButton b1 = set(new CButton("로그인"), BG(sp.blue), FG(Color.white));
	int pno;
	public Login(int pno) {
		this.pno = pno;
		((JPasswordField) t2).setEchoChar('●');
		setFrameg("로그인", 300, 350, () -> new sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj());
	}

	protected void desing() {
		BoxPanel p1 = set(new BoxPanel(C, 15,10,10, col(5, fw(lb("아이디")), f(t1)).setBackColor(Color.white), col(5, fw(lb("비밀번호")), f(t2)).setBackColor(Color.white)) {
			int r = 20;
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(getBackground());
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, r, r);
				
				super.paintComponent(g);
			};
		}.setBackColor(Color.white), BORDER(sp.em(0, 17, 0, 16)));
		p1.setOpaque(false);
		BoxPanel panel = set(col(25, 25, 25, 
					col(0, lb("WLCOME BACK", FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(10f))), lb("LOGIN", FONT(sp.font.deriveFont(27f).deriveFont(1)))),
					f(p1),
					f(b1)
				), BORDER(sp.em(0, 15, 0, 10)));
		add(panel);
	}

	protected void action() {
		b1.addActionListener(e -> {
			String s1 = t1.getText();
			String s2 = t2.getText();
			if(s1.isBlank() || s2.isBlank()) throw new RuntimeException("빈칸이 있습니다.");
			
			if(s1.equals("admin") && s2.equals("1234")) {
				sp.infor("관리자님 환영합니다.");
				dispose();
				return;
			}
			sp.user = userEntity.findFirst(c -> c.uid.equals(s1) && c.upw.equals(s2)).orElseThrow(() -> {
				t1.setText("");
				t2.setText("");
				return new RuntimeException("아이디 또는 비밀번호가 틀렸습니다.");
			});
			
			sp.infor(sp.user.uname + "님 환영합니다.");
			if(pno <= 0) {
				new Infor(pno);
			}else new sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj();
			dispose();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Login(0));
	}
}