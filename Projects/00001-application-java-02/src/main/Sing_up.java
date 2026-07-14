package main;

import javax.swing.*;

import utils.BoxPanel;
import utils.CFrame;
import utils.getter;

import static utils.Properties.*;

import java.awt.Color;
import java.awt.Dimension;

public class Sing_up extends CFrame{
	
	JTextField name = comp(JTextField::new, NAME("이름"));
	JTextField id = comp(JTextField::new, NAME("아이디"));
	JTextField pw = comp(JTextField::new, NAME("비밀번호"));
	JTextField pwCheck = comp(JTextField::new, NAME("비밀번호 확인"));
	JTextField birth = comp(JTextField::new, NAME("생년월일 (YYYY-MM-DD)"));
	JTextField phone = comp(JTextField::new, NAME("전화번호"));
	
	JCheckBox krw = comp(JCheckBox::new, TEXT("KRW 계좌 (필수)"), BG(getter.backColor), FONT(getter.font));
	JCheckBox usd = comp(JCheckBox::new, TEXT("USD 계좌"), BG(getter.backColor), FONT(getter.font));
	
	JButton checkButton = comp(JButton::new, TEXT("중복확인"), FONT(getter.font), BG(getter.color), FG(Color.white));
	JButton singup = comp(JButton::new, TEXT("가입하기"), FONT(getter.font), BG(getter.color), FG(Color.white));
	public Sing_up() {
		krw.setSelected(true);
		krw.setEnabled(false);
		setFrame("회원가입", 400, 350, () -> {});
	}

	@Override
	protected void desing() {
		JPanel p = new BoxPanel(BoxPanel.C, 0, 10, 0, getRowPanel(name), getRowPanel(id).addz(checkButton), getRowPanel(pw), getRowPanel(pwCheck), getRowPanel(birth), getRowPanel(phone)
				, BoxPanel.fillWidth(krw), BoxPanel.fillWidth(usd)
				, singup);
		p.setBorder(getter.em(10, 15, 10, 15));
		add(p);
	}

	private BoxPanel getRowPanel(JTextField tf) {
		return new BoxPanel(BoxPanel.R, 0, 3, 0, 
				BoxPanel.fill(comp(JLabel::new, TEXT(tf.getName()), FONT(getter.font)))
				, BoxPanel.HGAP()
				, set(tf, SIZE((350 - 30) / 2, 25), BORDER(getter.line(Color.LIGHT_GRAY))));
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		Util.start(new Sing_up());
	}
}
