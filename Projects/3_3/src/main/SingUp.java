package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import orms.userEntity;

public class SingUp extends CFrame{

	JScrollPane sc = new JScrollPane();

	JLabel photo = lb("프로필 사진", FONT(sp.font.deriveFont(14f)), HOA(JLabel.CENTER), VEA(JLabel.CENTER), SIZE(110, 110), BORDER(sp.line(Color.LIGHT_GRAY)));
	JButton photoBt = bt("사진 선택", FONT(sp.font.deriveFont(1)), BG(sp.color), FG(Color.white));
	File photoFile;

	JTextField name = tf("이름");
	JTextField id = tf("아이디");
	JTextField pw = tf("비밀번호");
	JTextField pw2 = tf("비밀번호 확인");
	JTextField nick = tf("닉네임");
	JTextField birth = tf("생년월일 예) 2008-05-31");
	JTextField phone = tf("전화번호");
	JTextField email = tf("이메일");
	JTextArea intro = new JTextArea(4, 20);

	JButton signup = bt("회원가입", FONT(sp.font.deriveFont(1).deriveFont(16f)), BG(sp.color), FG(Color.white));

	public SingUp() {
		setFrames("회원가입", 500, 650, () -> new Login());
	}

	@Override
	protected void desing() {

		intro.setLineWrap(true);
		intro.setWrapStyleWord(true);
		intro.setFont(sp.font);
		intro.setBorder(sp.line(Color.LIGHT_GRAY));
		JScrollPane introSc = new JScrollPane(intro);
		introSc.setPreferredSize(new Dimension(300, 100));
		introSc.setBorder(sp.line);
		JPanel form = col(6,
				col(4, photo, photoBt),
				tfPanel(name), tfPanel(id), tfPanel(pw), tfPanel(pw2), tfPanel(nick), tfPanel(birth), tfPanel(phone), tfPanel(email),
				fw(lb("소개글", FONT(sp.font.deriveFont(1)), HOA(JLabel.CENTER))), f(introSc));
		sc.setViewportView(form);

		add(set(col(10, fw(lb("회원가입", FONT(sp.font.deriveFont(1).deriveFont(24f)), HOA(JLabel.CENTER), VEA(JLabel.CENTER))), f(sc), fw(signup)), BORDER(sp.em(10, 10, 10, 10))));
	}

	private JPanel tfPanel(JTextField tf) {
		JLabel label = lb(tf.getName(), FONT(sp.font.deriveFont(1).deriveFont(12f)), SIZE(150, 30));
		return row(0, 10, 0, label, f(tf));
	}

	private boolean isBlank(JTextField tf) {
		return tf.getText().isBlank();
	}

	@Override
	protected void action() {
		photoBt.addActionListener(e -> {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg"));
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				photoFile = fc.getSelectedFile();
				photo.setIcon(new ImageIcon(new ImageIcon(photoFile.getPath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
				photo.setText("");
			}
		});

		signup.addActionListener(e -> {
			if (isBlank(name) || isBlank(id) || isBlank(pw) || isBlank(pw2) || isBlank(nick)
					|| birth.getText().isBlank() || isBlank(phone) || isBlank(email) || intro.getText().isBlank())
				throw new RuntimeException("빈칸이 있습니다.");

			LocalDate date = null;
			try {
				date = LocalDate.parse(birth.getText());
			} catch (Exception ex) {
				throw new RuntimeException("생년월일 형식이 올바르지 않습니다.");
			}
			boolean phoneOk = phone.getText().matches("\\d{3}-\\d{4}-\\d{4}");
			if (date == null || !phoneOk)
				throw new RuntimeException("전화번호 형식이 올바르지 않습니다.");

			if (photoFile == null)
				throw new RuntimeException("프로필사진을 선택하주세요.");

			userEntity u = new userEntity();
			u.u_name = name.getText();
			u.u_id = id.getText();
			u.u_pw = pw.getText();
			u.u_nick = nick.getText();
			u.u_birth = date;
			u.u_phone = phone.getText();
			u.u_email = email.getText();
			u.u_intro = intro.getText();
			u.u_follow = "";

			sp.info("회원가입이 완료되었습니다.");
			new Login();
			dispose();
		});
	}

	public static void main(String[] args) {
		Util.start(new SingUp());
	}
}
