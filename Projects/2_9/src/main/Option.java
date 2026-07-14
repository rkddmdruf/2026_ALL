package main;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import orms.areaEntity;
import orms.sub_areaEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Option extends JPanel{
	sub_areaEntity subA = sub_areaEntity.findById(getter.user.sno).get();
	JTextField name = 	tf("이름", BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(8,8,8,8))));
	JTextField id = 	tf("ID", BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(8,8,8,8))));
	JTextField pw = 	tf("PW", BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(8,8,8,8))));
	JTextField arrive = tf("거주지", BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(8,8,8,8))));
	
	JButton acb =	 bt("거주지 변경", BG(Color.white));
	JButton button = bt("수정하기", 	BG(Color.white), SIZE(100, 25));
	
	int sno = getter.user.sno;
	public Option() {
		setLayout(new BorderLayout());
		
		init();
		
		id.setEditable(false);
		arrive.setEditable(false);
		JPanel topPanel = set(row(0, 
				fillWidth(set(new JLabel("  " + getter.user.uname + " 님", getter.getImage("logo/user.png", 55, 55), JLabel.LEFT), FONT(getter.font.deriveFont(20f).deriveFont(1)))), 
				lb("잔액 : " + getter.df.format(getter.user.point) + "원", FONT(getter.font.deriveFont(20f).deriveFont(1)))
			).setBackColor(Color.white), BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(10, 10, 10, 10))));
		
		JPanel mainPanel = col(10, 
				Arrays.asList(setTf(name, null), setTf(id, null), setTf(pw, null), setTf(arrive, button), col(15, 0, 0, fill(button)).setBackColor(Color.white))
				.stream().map(e -> fillWidth(e)).toArray(JComponent[]::new));
		mainPanel.setBorder(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(50, 10, 50, 10)));
		mainPanel.setBackground(Color.white);
		JPanel panel = col(10, fillWidth(topPanel), fill(mainPanel));
		
		action();
		add(fill(panel));
	}
	
	private void action() {
		button.addActionListener(e -> {
			Util.textIsBlank(name, id, pw, arrive);
			getter.user.id = id.getText();
			getter.user.uname = name.getText();
			getter.user.pw = pw.getText();
			getter.user.sno = sno;
			getter.user.save();
			getter.infor("수정이 완료되었습니다.");
		});
		acb.addActionListener(e -> {
			class test extends CFrame{
				JPanel p1 = set(col(0), NAME("광역"), BG(Color.white));
				JPanel p2 = set(col(0), NAME("지역"), BG(Color.white));
				Option o;
				public test(Option o) {
					this.o = o;
					setFrame("거주지 선택", 425, 350, () -> {});
				}
				@Override
				protected void desing() {
					JPanel panel = set(new JPanel(new GridLayout(1, 2, 10, 10)));
					panel.add(setPanel(p1));
					panel.add(setPanel(p2));
					//여기서도 비율이 안맞음
					add(set(
							col(10, lb("거주지 선택",FONT(getter.font.deriveFont(15f).deriveFont(1)), HOA(JLabel.CENTER)), 
									fill(panel)
									),
							BORDER(getter.em(10, 10, 10, 10))
							));
					ButtonGroup butG = new ButtonGroup();
					JToggleButton but = null;
					for(areaEntity a : areaEntity.findAll()) {
						JToggleButton b = comp(JToggleButton::new, TEXT(a.aname), FONT(getter.font.deriveFont(15f)), HOA(JButton.LEFT), BORDER(getter.em(5, 1, 5, 1)));
						b.addActionListener(c -> {
							p2.removeAll();
							sub_areaEntity.findBy(sa -> sa.ano.equals(a.ano))
							.forEach(e -> {
								JButton sb = bt(e.sname ,FONT(b.getFont()), BG(b.getBackground()), BORDER(b.getBorder()), HOA(b.getHorizontalAlignment()));
								sb.addActionListener(action -> { o.setArrive(e.sno); dispose(); });
								p2.add(fill(sb));
							});
							p2.revalidate();
							p2.repaint();
						});
						but = (but == null ? b : but);
						butG.add(b);
						p1.add(fill(b));
					}
					but.setSelected(true);
					but.doClick();
				}

				private JComponent setPanel(JPanel p) {
					return set(col(10,10,10, fillWidth(lb(p1.getName())), fill(new JScrollPane(p)))
							, BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(0, 10, 0, 10))), BG(Color.white));
				}
				@Override
				protected void action() { }
			}
			test form = new test(this);
			form.setVisible(true);
			
		});
	}
	
	private void setArrive(int sno) {
		this.sno = sno;
		sub_areaEntity sa = sub_areaEntity.findById(sno).get();
		arrive.setText(areaEntity.findById(sa.ano).get().aname + "-" + sa.sname);
	}
	
	public void init() {
		sno = getter.user.sno;
		name.setText(getter.user.uname);
		id.setText(getter.user.id);
		pw.setText(getter.user.pw);
		setArrive(sno);
	}
	
	private JPanel setTf(JTextField tf, JButton lastButton) {
		return set(
				row(10, 
						set(new JLabel(), TEXT(tf.getName()), FONT(getter.font.deriveFont(1).deriveFont(14f)), SIZE(60, 35)), 
						fill(tf),
						col(0, 0, 0, lastButton == null ? lb("", SIZE(100, 25)) : acb).setBackColor(Color.white)
						)
				, BG(Color.white));
	}

}
