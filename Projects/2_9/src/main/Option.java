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
	sub_areaEntity subA = sub_areaEntity.findById(sp.user.sno).get();
	JTextField name = 	tf("이름", BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(8,8,8,8))));
	JTextField id = 	tf("ID", BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(8,8,8,8))));
	JTextField pw = 	tf("PW", BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(8,8,8,8))));
	JTextField arrive = tf("거주지", BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(8,8,8,8))));
	
	JButton acb =	 bt("거주지 변경", BG(Color.white));
	JButton button = bt("수정하기", 	BG(Color.white), SIZE(100, 25));
	
	int sno = sp.user.sno;
	public Option() {
		setLayout(new BorderLayout());
		
		init();
		
		id.setEditable(false);
		arrive.setEditable(false);
		JPanel topPanel = set(row(0, 
				fw(set(new JLabel("  " + sp.user.uname + " 님", sp.getImage("logo/user.png", 55, 55), JLabel.LEFT), FONT(sp.font.deriveFont(20f).deriveFont(1)))), 
				lb("잔액 : " + sp.df.format(sp.user.point) + "원", FONT(sp.font.deriveFont(20f).deriveFont(1)))
			).setBackColor(Color.white), BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(10, 10, 10, 10))));
		
		JPanel mainPanel = col(10, 
				Arrays.asList(setTf(name, null), setTf(id, null), setTf(pw, null), setTf(arrive, button), col(15, 0, 0, f(button)).setBackColor(Color.white))
				.stream().map(e -> fw(e)).toArray(JComponent[]::new));
		mainPanel.setBorder(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(50, 10, 50, 10)));
		mainPanel.setBackground(Color.white);
		JPanel panel = col(10, fw(topPanel), f(mainPanel));
		
		action();
		add(f(panel));
	}
	
	private void action() {
		button.addActionListener(e -> {
			Util.textIsBlank(name, id, pw, arrive);
			sp.user.id = id.getText();
			sp.user.uname = name.getText();
			sp.user.pw = pw.getText();
			sp.user.sno = sno;
			sp.user.save();
			sp.infor("수정이 완료되었습니다.");
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
							col(10, lb("거주지 선택",FONT(sp.font.deriveFont(15f).deriveFont(1)), HOA(JLabel.CENTER)), 
									f(panel)
									),
							BORDER(sp.em(10, 10, 10, 10))
							));
					ButtonGroup butG = new ButtonGroup();
					JToggleButton but = null;
					for(areaEntity a : areaEntity.findAll()) {
						JToggleButton b = comp(JToggleButton::new, TEXT(a.aname), FONT(sp.font.deriveFont(15f)), HOA(JButton.LEFT), BORDER(sp.em(5, 1, 5, 1)));
						b.addActionListener(c -> {
							p2.removeAll();
							sub_areaEntity.findBy(sa -> sa.ano.equals(a.ano))
							.forEach(e -> {
								JButton sb = bt(e.sname ,FONT(b.getFont()), BG(b.getBackground()), BORDER(b.getBorder()), HOA(b.getHorizontalAlignment()));
								sb.addActionListener(action -> { o.setArrive(e.sno); dispose(); });
								p2.add(f(sb));
							});
							p2.revalidate();
							p2.repaint();
						});
						but = (but == null ? b : but);
						butG.add(b);
						p1.add(f(b));
					}
					but.setSelected(true);
					but.doClick();
				}

				private JComponent setPanel(JPanel p) {
					return set(col(10,10,10, fw(lb(p1.getName())), f(new JScrollPane(p)))
							, BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(0, 10, 0, 10))), BG(Color.white));
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
		sno = sp.user.sno;
		name.setText(sp.user.uname);
		id.setText(sp.user.id);
		pw.setText(sp.user.pw);
		setArrive(sno);
	}
	
	private JPanel setTf(JTextField tf, JButton lastButton) {
		return set(
				row(10, 
						set(new JLabel(), TEXT(tf.getName()), FONT(sp.font.deriveFont(1).deriveFont(14f)), SIZE(60, 35)), 
						f(tf),
						col(0, 0, 0, lastButton == null ? lb("", SIZE(100, 25)) : acb).setBackColor(Color.white)
						)
				, BG(Color.white));
	}

}
