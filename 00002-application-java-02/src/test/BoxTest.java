package test;


import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import static utils.BoxUI.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public class BoxTest extends JFrame {

	JLabel title = new JLabel("회원정보");
	

	JPanel profileImage = new JPanel();
	
	JTextField nameInput = new JTextField(15);
	JTextField idInput = new JTextField(15);
	JTextField pwInput = new JTextField(15);
	JTextField birthInput = new JTextField(15);
	JTextField phoneInput = new JTextField(15);
	JTextField emailInput = new JTextField(15);
	JTextField rejoinInput = new JTextField(15);
	
	JComboBox<String> rejoinCombo = new JComboBox<>();
	
	JButton save = new JButton("변경사항 저장");
	
	public BoxTest() {
		setVisible(true);
		
//		lb1 = createComponent(JLable.class, "name", new Font(33), new border);
//		lb1 = createComponent(JLable.class, "name", new Font(33), new border);
//		lb1 = createComponent(JLable.class, "name", new Font(33), new border);
//		lb1 = createComponent(JLable.class, "name", new Font(33), new border);
		
		profileImage.setBackground(Color.red);
		
		JPanel header = fillWidth(box(ROW, 15, 5, 15, fill(new JButton("1")), fill(new JButton("2")), fill(new JButton("3"))));
		JPanel info = fill(box(COL, 0, 5, 5,
				fill(nameInput),
				fill(box(ROW, 5, 5, 0, L("아이디",	125),	fill(idInput))),
				fill(box(ROW, 5, 5, 0, L("비밀번호",	125),	fill(pwInput))),
				fill(box(ROW, 5, 5, 0, L("생년월일",	125),	fill(birthInput))),
				fill(box(ROW, 5, 5, 0, L("연락처",	125),	fill(phoneInput))),
				fill(box(ROW, 5, 5, 0, L("이메일",	125),	fill(emailInput))),
				fill(box(ROW, 5, 5, 0, L("지역",		125),	fill(rejoinCombo)))
		));
		
		profileImage.setPreferredSize(new Dimension(100, 50));
		
		JPanel body = fill(box(ROW, 15, 15, 15, box(COL, 5, 5, 5, profileImage, VGAP()), info));
		
		add(box(COL, 15, 15, 5, header, body), BorderLayout.CENTER);
		
		pack();
	}
	
	public static void main(String[] args) {
		new BoxTest();
	}

}
