package test.test1;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import main.Util;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.io.File;
import java.time.LocalDate;

public class SingUp extends CFrame{
	
	JTextField name = tf("이름");
	JTextField id = tf("아이디");
	JTextField pw = tf("비밀번호");
	JTextField pwc = tf("비밀번호 확인");
	JTextField nick = tf("닉네임");
	JTextField birth = tf("생년월일 예) 2008-05-31");
	JTextField phone = tf("전화번호");
	JTextField email = tf("이메일");
	
	JTextArea ta = comp(JTextArea::new, SIZE(0, 100), BORDER(sp.line));
	JLabel img = lb("프로필 사진", BORDER(sp.line), HOA(JLabel.CENTER), VEA(JLabel.CENTER), SIZE(125, 125));
	JButton select = bt("사진 선택", BG(sp.color), FG(Color.white));
	JButton singUp = bt("회원가입", BG(sp.color), FG(Color.white));
	File phtoFile = null;
	public SingUp() {
		setFramed("회원가입", 400, 550, () -> new Login());
	}

	@Override
	protected void desing() {
		add(set(col(10, lb("회원가입", FONT(sp.font.deriveFont(25f).deriveFont(1))), f(new JScrollPane(panel())), fw(singUp)), BORDER(sp.em(10, 10, 10, 10))));
	}

	private JPanel panel() {
		JPanel p = col(5, col(5, 7, 0, img, select), 
				setTf(name),
				setTf(id),
				setTf(pw),
				setTf(pwc),
				setTf(nick),
				setTf(birth),
				setTf(phone),
				setTf(email),
				col(1, lb("소개글"), f(ta))
				);
		return p;
	}
	
	private JComponent setTf(JTextField t) {
		JPanel p = row(0, lb(t.getName(), SIZE(150, 30), FONT(sp.font)), f(t));
		return p;
	}
	@Override
	protected void action() {
		select.addActionListener(e -> {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg"));
			if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				phtoFile = fc.getSelectedFile();
				img.setIcon(new ImageIcon(new ImageIcon(phtoFile.getPath()).getImage().getScaledInstance(125, 125, 4)));
				img.setText("");
				revalidate();
				repaint();
			}
		});
		
		singUp.addActionListener(e -> {
			if(ib(name) || ib(id) || ib(pw) || ib(pwc) || ib(nick) || ib(phone) || ib(birth) || ib(email) || ib(ta)) throw new RuntimeException("빈칸이 있습니다.");
			LocalDate date = null;
			try {
				date = LocalDate.parse(birth.getText());
			} catch (Exception e2) {
				throw new RuntimeException("생년월일 형식이 올바르지 않습니다.");
			}
			if(!phone.getText().matches("\\d{3}-\\d{4}-\\d{4}"))
				throw new RuntimeException("전화번호 형식이 올바르지 않습니다.");
			if(phtoFile == null)
				throw new RuntimeException("프로필사진을 선택해주세요.");
			sp.info("회원가입이 완료되었습니다.");
			userEntity user = new userEntity();
			user.u_id = id.getText();
			user.u_pw = pw.getText();
			user.u_name = name.getText();
			user.u_nick = nick.getText();
			user.u_birth = date;
			user.u_phone = phone.getText();
			user.u_email = email.getText();
			user.u_intro = ta.getText();
			user.u_follow = "";
			user.save();
			dispose();
		});
	}

	private boolean ib(JTextComponent t) {
		return t.getText().isBlank();
	}
	public static void main(String[] args) {
		Util.start(new SingUp());
	}
}
