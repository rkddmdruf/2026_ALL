package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import orms.ProjectEntity;
import orms.categoryEntity;
import orms.productEntity;
import orms.rateplanEntity;


public class Pay extends CFrame{
	LocalDate now = LocalDate.now();
	JButton button = bt("결제하기", SIZE(0, 30), BG(sp.color), FG(Color.white));
	ProjectEntity project;
	int storeg;
	String company;
	int moment;
	rateplanEntity ratep;
	public Pay(ProjectEntity project, rateplanEntity ratep, int storeg, String company, int moment) {
		this.ratep = ratep;
		this.project = project;
		this.storeg = storeg;
		this.company = company;
		this.moment = moment;
		setFrame("결제", 325, 550);
	}

	static class field {
		public String name;
		public String value;
		
		private field(String name, Object value) {
			this.name = name;
			this.value = value.toString();
		}
		
		public static field get(String name, Object value) {
			return new field(name, value);
		}
	}
	
	@Override
	protected void desing() {
		//TOP
		JPanel pt = col(5, fw(lb("결제 상세 내역", FG(sp.color), FONT(sp.font.deriveFont(15f)))), fw(lb(now.toString() + "기준", FG(Color.LIGHT_GRAY))));
		
		//CENTER
		int sPrice = project.find1(e -> e.value.equals(String.valueOf(storeg))).get().price;
		int cPrice = project.find2(e -> e.type.equals(company)).get().price;
		int tPrice = sPrice + cPrice;
		JPanel p1 = col(5, 
						card("기기 정보", 
							field.get("기종", productEntity.findById(project.pno).get().pname), 
							field.get("선택 용량", storeg), 
							field.get("통신사", company), 
							field.get("할부 기간", moment), 
							field.get("개통일", now), 
							field.get("약정 종료일", now.plusMonths(moment))
						)
					).setBackColor(Color.white);
		JPanel p2 = col(5,
						card("요금제 정보",
							field.get("선택 요금제", ratep.rname), 
							field.get("요금제 금액", sp.df.format(ratep.price) + "원 / 월")	
						)
					).setBackColor(Color.white);
		set(p2, BORDER(sp.com(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.lightGray), sp.em(5, 0, 5, 0))));
		JPanel p3 = col(5,
				card("가격 내역",
					field.get("용량 가격", sp.df.format(sPrice) + "원"), 
					field.get("통신사 가격", sp.df.format(cPrice) + "원"), 
					field.get("단말기 총 출고가", sp.df.format(tPrice) + "원")
				)
			).setBackColor(Color.white);
		JPanel pc = set(col(5, p1, p2, p3), BG(Color.white), BORDER(sp.com(sp.line, sp.em(10, 10, 10, 10))));
		JPanel p = (JPanel) ((JPanel) p3.getComponent(1)).getComponent(7);
		for(Component c : p.getComponents()) {
			c.setForeground(sp.color);
		}
		//BOTTOM
		JPanel priceInfo = col(3, 
					fw(lb("월 납부금(달말 할부 + 요금제)", FG(Color.white), FONT(sp.font.deriveFont(11f)))), 
					fw(lb(sp.df.format(((tPrice / moment) + ratep.price)) + "원 / 월", FG(Color.white), FONT(sp.font.deriveFont(15f).deriveFont(1))))
				);
		set(priceInfo, BORDER(sp.em(5, 10, 10, 10)), BG(sp.color));
		JPanel pb = col(10, fw(priceInfo), fw(button));
		
		add(set(col(7, fw(pt), f(pc), fw(pb)), BORDER(sp.em(10, 10, 2, 10))));
	}

	private JPanel card(String title, field...fields) {
		List<JComponent> list = new ArrayList<>();
		list.add(fw(lb("▶ " + title, FONT(sp.font.deriveFont(13f)), FG(sp.color))));
		for(field f : fields) {
			list.add(fw(row(0, fw(lb(f.name, FG(Color.LIGHT_GRAY))), fw(lb(f.value, HOA(JLabel.RIGHT)))).setBackColor(Color.white)));
		}
		return col(5, list.toArray(JComponent[]::new)).setBackColor(Color.white);
	}
	
	@Override
	protected void action() {
		button.addActionListener(e -> {
			class form extends CFrame{
				JTextField tf = comp(JTextField::new, SIZE(0, 30));
				JButton but = bt("결제", FG(Color.white), BG(sp.color));
				List<Integer> list = new ArrayList<>();
				String str = "";
				public form() {
					for(int n = (int) ((Math.random() * 26) + 65); list.size() < 26; n = (int) ((Math.random() * 26) + 65)) {
						if(!list.contains(n)) list.add(n);
					}
					
					setFrame("비밀번호", 350, 400);
					Collections.shuffle(list);
					for(Integer n : list.subList(0, 6))
						str += String.valueOf(((char) n.intValue()));
					new windowAlarm(str);
				}
				
				@Override
				protected void desing() {
					JPanel gp = new JPanel(new GridLayout(6, 5, 20, 10));
					
					for(Integer n : list) {
						JLabel l = lb(String.valueOf(((char) n.intValue())),FONT(sp.font.deriveFont(15f).deriveFont(1)), FG(sp.color), HOA(JLabel.CENTER));
						gp.add(l);
					}
					set(gp, BG(Color.white));
					add(set(col(10, 
								f(gp), 
								row(15, lb("pass:"), fw(tf), hg(20)).setBackColor(Color.white), 
								fw(row(0, hg(30), fw(but), hg(30)).setBackColor(Color.white))
							) , BG(Color.white), BORDER(sp.em(30, 30, 10, 30))));
				}

				@Override
				protected void action() {
					but.addActionListener(e -> {
						if(tf.getText().equals(str)) {
							System.out.println("결제 완료");
						}
					});
				}
			}
			new form();
		});
	}
}
