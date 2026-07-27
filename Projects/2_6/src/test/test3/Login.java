package test.test3;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import main.Util;
import orms.userEntity;
import uitls.*;
import static uitls.BoxPanel.*;
import static uitls.Properties.*;

public class Login extends CFrame{
	
	JTextField id = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.gray);
				g.drawString("ID", 1, (getHeight() - getFontMetrics(getFont()).getHeight()) / 2 + getFontMetrics(getFont()).getAscent());
			}
		};
	};
	JPasswordField pw = new JPasswordField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(pw.getPassword().length == 0) {
				g.setColor(Color.gray);
				g.drawString("PW", 1, (getHeight() - getFontMetrics(getFont()).getHeight()) / 2 + getFontMetrics(getFont()).getAscent());
			}
		};
	};
	CButton login = set(new CButton("로그인"), FG(Color.white));
	public Login() {
		pw.setEchoChar('●');
		setFrame("로그인", 325, 200, () -> { });
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				new Main3().setVisible(true);
			}
		});
	}

	@Override
	protected void desing() {
		TitledBorder tBorder = new TitledBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black), "LOGIN", TitledBorder.TOP, TitledBorder.CENTER, getter.font.deriveFont(1).deriveFont(20f));
		JPanel panel = set(colF(10, setPanel(id), setPanel(pw), setPanel(login)), BORDER(getter.com(tBorder, getter.em(20, 20, 20, 20))), BG(Color.white));
		add(panel);
	}

	private JPanel setPanel(JComponent t) {
		String imgs = t == id ? "user" : t == pw ? "lock" : "";
		System.out.println(imgs);
		JLabel imgL = lb("", SIZE(25, 25), ICON(getter.getImage("icon/" + imgs, 25, 25)));
		return row(10, imgL, f(t)).setBackColor(Color.white);
	}
	@Override
	protected void action() {
		login.addActionListener(e -> {
			String i = id.getText(), p = String.copyValueOf(pw.getPassword());
			if(i.isBlank() || p.isBlank()) {
				throw new RuntimeException("빈칸이 존재합니다.");
			}
			if(i.equals("admin") && p.equals("1234")) {
				getter.infor("관리자님 환영합니다.");
			}
			List<userEntity> users = userEntity.findBy(c -> c.id.equals(i) && c.pw.equals(p));
			if(users.isEmpty()) {
				throw new RuntimeException("해당 유저가 존재하지 않습니다.");
			}
			getter.user = users.get(0);
			getter.infor(getter.user.name + "님 환영합니다");
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new Login());
	}
}
