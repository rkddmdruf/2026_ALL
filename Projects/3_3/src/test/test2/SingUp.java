package test.test2;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import main.Util;
import orms.userEntity;
public class SingUp extends CFrame{
	JLabel imgLabel = lb("프로필 사진", HOA(JLabel.CENTER), VEA(JLabel.CENTER), BORDER(sp.line), SIZE(125, 125));
	JButton imgSelect = bt("사진 선택", FG(Color.white), BG(sp.color));
	
	JTextField t1 = tf("이름");
	JTextField t2 = tf("아이디");
	JTextField t3 = tf("비밀번호");
	JTextField t4 = tf("비밀번호 확인");
	JTextField t5 = tf("닉네임");
	JTextField t6 = tf("생년월일 예) 2008-05-31");
	JTextField t7 = tf("전화번호");
	JTextField t8 = tf("이메일");
	JTextArea ta = comp(JTextArea::new, NAME("소개글"));
	
	JButton b1 = bt("회원가입", FG(Color.white), BG(sp.color), FONT(sp.font));
	
	File file;
	public SingUp() {
		setFramed("회원가입", 450, 600, () -> new Login());
	}

	@Override
	protected void action() {
		imgSelect.addActionListener(e -> {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("이미지 파일"));
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				file = fc.getSelectedFile();
				imgLabel.setIcon(new ImageIcon(new ImageIcon(file.getPath()).getImage().getScaledInstance(125, 125, 4)));
				imgLabel.setText("");
			}
		});
		
		b1.addActionListener(e -> {
			if(isBlanks(t1, t2,t3,t4,t5,t6,t7,t8,ta)) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			LocalDate date = null;
			try {
				date = LocalDate.parse(t6.getText());
			} catch (Exception e2) {
				throw new RuntimeException("생년월일 형식이 올바르지 않습니다.");
			}
			if(!t7.getText().matches("\\d{3}-\\d{4}-\\\\d{4}")) {
				throw new RuntimeException("전화번호 형식이 올바르지 않습니다.");
			}
			if(file == null || file.exists()) throw new RuntimeException("프로필사진을 선택하주세요.");
			userEntity user = new userEntity();
			user.u_name = t1.getText();
			user.u_id = t2.getText();
			user.u_pw = t3.getText();
			user.u_nick = t5.getText();
			user.u_birth = date;
			user.u_phone = t7.getText();
			user.u_email = t8.getText();
			user.save();
			
			sp.info("회원가입이 완료 되었습니다.");
			dispose();
		});
	}
	
	@Override
	protected void desing() {
		JScrollPane sc = new JScrollPane(getMainPanel());
		add(set(col(10, lb("회원가입", FONT(sp.font.deriveFont(25f).deriveFont(1))), f(sc), fw(b1)), BORDER(sp.em(10, 10, 10, 10))));
	}

	private JPanel getMainPanel() {
		return col(5, fw(col(5, imgLabel, imgSelect)), field(t1), field(t2), field(t3), field(t4), field(t5), field(t6) ,field(t7),field(t8) ,field(ta));
	}
	
	private JPanel field(JTextComponent t) {
		if(t instanceof JTextArea) return col(1, lb(t.getName()), fw(set(t, BORDER(sp.line),SIZE(0, 100))));
		return row(0, lb(t.getName(), SIZE(150, 30)), f(t));
	}
	

	private boolean isBlanks(JTextComponent...ts) {
		return Arrays.asList(ts).stream().filter(t -> t.getText().isBlank()).collect(Collectors.toList()).isEmpty();
	}
	public static void main(String[] args) {
		Util.start(new SingUp());
	}
}
