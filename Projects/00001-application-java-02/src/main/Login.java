package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import utils.BoxPanel;
import utils.CFrame;
import static utils.Properties.*;
import utils.getter;

public class Login extends CFrame{

	JTextField id = comp(JTextField::new, NAME("아이디"));
	JTextField pw = comp(JTextField::new, NAME("패스워드"));
	
	JButton login = comp(JButton::new, TEXT("로그인"), BG(getter.color), FG(Color.white), FONT(getter.font));
	JButton sing_up = comp(JButton::new, TEXT("회원가입"), BG(getter.color), FG(Color.white), FONT(getter.font));
	
	public Login() {
		setFrame("로그인", 275, 225, () -> {});
	}
	@Override
	protected void desing() {
		JLabel topLabel = comp(JLabel::new, TEXT(getter.text), FONT(getter.font.deriveFont(1).deriveFont(35f)), FG(getter.color));
		JPanel tfsPanel = new BoxPanel(BoxPanel.C, 0, 10, 0, label_tfPanel(id), label_tfPanel(pw));
		JPanel buttonPanel = new BoxPanel(BoxPanel.R, 0, 10, 0, BoxPanel.HGAP(), login, sing_up, BoxPanel.HGAP());
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, topLabel, tfsPanel, buttonPanel);
		panel.setBorder(getter.em(15, 5, 10, 5));
		add(panel);
	}

	private JPanel label_tfPanel(JTextField tf) {
		return new BoxPanel(BoxPanel.R, 0, 0, 0, comp(JLabel::new, TEXT(tf.getName()), SIZE(60, 25), FONT(getter.font)), BoxPanel.HGAP(), BoxPanel.fill(tf));
	}
	
	@Override
	protected void action() {
		
	}
	
	public static void main(String[] args) {
		Util.start(new Login());
	}
}
