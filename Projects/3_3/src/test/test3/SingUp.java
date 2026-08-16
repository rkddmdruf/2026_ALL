package test.test3;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Util;
import orms.*;

public class SingUp extends CFrame {
	JLabel l1 = lb("프로필 사진", HOA(JLabel.CENTER), BORDER(sp.line(Color.black)), SIZE(140, 140));
	
	JButton b1 = bt("사진 선택", FG(Color.white), BG(sp.color));
	JButton b2 = bt("회원가입", FG(Color.white), BG(sp.color));
	
	JTextField t1 = comp(JTextField::new);
	JTextField t2 = comp(JTextField::new);
	JTextField t3 = comp(JTextField::new);
	JTextField t4 = comp(JTextField::new);
	JTextField t5 = comp(JTextField::new);
	JTextField t6 = comp(JTextField::new);
	JTextField t7 = comp(JTextField::new);
	JTextField t8 = comp(JTextField::new);
	
	JTextArea ta = set(new JTextArea(), SIZE(0, 100), BORDER(sp.line));
	
	File file = null;
	public SingUp() {
		setFramed("회원가입", 500, 625, () -> new A_Login());
	}

	protected void desing() {
		JScrollPane sc = new JScrollPane();
		JPanel p = set(col(10,
					l1,
					b1,
					fw(row(0, lb("이름", SIZE(150, 30)), f(t1))),
					fw(row(0, lb("아이디", SIZE(150, 30)), f(t2))),
					fw(row(0, lb("비밀번호", SIZE(150, 30)), f(t3))),
					fw(row(0, lb("비밀번호 확인", SIZE(150, 30)), f(t4))),
					fw(row(0, lb("닉네임", SIZE(150, 30)), f(t5))),
					fw(row(0, lb("생년월일 예) 1508-05-31", SIZE(150, 30)), f(t6))),
					fw(row(0, lb("전화번호", SIZE(150, 30)), f(t7))),
					fw(row(0, lb("이메일", SIZE(150, 30)), f(t8))),
					fw(col(0, lb("소개글"), f(ta)))
				));
		sc.setViewportView(p);
		add(set(col(10,
				fw(lb("회원가입", FONT(sp.font.deriveFont(24f).deriveFont(1))))
				, f(sc)
				, fw(b2)
				), BORDER(sp.em(10, 10, 10, 10))));
	}

	protected void action() {
		b1.addActionListener(e -> {
			JFileChooser fs = new JFileChooser();
			fs.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg"));
			if(fs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = fs.getSelectedFile();
				l1.setIcon(new ImageIcon(new ImageIcon(file.getPath()).getImage().getScaledInstance(140, 140, 4)));
				l1.repaint();
			}
		});
		b2.addActionListener(e -> {
			String s1 = t1.getText();
			String s2 = t2.getText();
			String s3 = t3.getText();
			String s4 = t4.getText();
			String s5 = t5.getText();
			String s6 = t6.getText();
			String s7 = t7.getText();
			String s8 = t8.getText();
			String s9 = ta.getText();
			if(s1.isBlank() || s2.isBlank() || s3.isBlank() || s4.isBlank() || s5.isBlank() || s6.isBlank() || s7.isBlank() || s8.isBlank() || s9.isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			
			LocalDate date = null;
			try {
				date = LocalDate.parse(s6);
			} catch (Exception e2) {
				throw new RuntimeException("생년월일 형식이 올바르지 않습니다.");
			}
			
			if(!s7.matches("\\d{3}-\\d{4}-\\d{4}")) {
				throw new RuntimeException("전화번호 형식이 올바르지 않습니다.");
			}
			if(file == null) {
				throw new RuntimeException("프로필 사진을 선택해주세요.");
			}
			sp.info("회원가입이 완료되었습니다.");
			dispose();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new SingUp());
	}
}