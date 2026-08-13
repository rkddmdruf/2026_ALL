package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

import main.windowAlarm;
import orms.*;

public class Pay extends CFrame {
	JLabel l1 = lb("sdf", FG(Color.white), BG(sp.color), FONT(sp.font.deriveFont(1).deriveFont(16f)));
	JLabel l2 = lb("결제", FG(Color.white), BG(sp.color), HOA(JLabel.CENTER), FONT(sp.font.deriveFont(14f)), BORDER(sp.em(10, 15, 10, 15)));
	
	productEntity p;
	ProjectEntity project;
	Integer gb, moment;
	String ef;
	rateplanEntity ratep;
	
	int price1, price2;
	public Pay(rateplanEntity ratep, int pno, int gb, String ef, int moment) {
		project = ProjectEntity.findById(pno).get();
		this.ratep = ratep;
		this.gb = gb;
		this.ef = ef;
		this.moment = moment;
		
		price1 = project.capacities.stream().filter(e -> e.value.equals(this.gb.toString())).findFirst().get().price;
		price2 = project.items.stream().filter(e -> e.type.equals(ef)).findFirst().get().price;
		
		p = productEntity.findById(pno).get();
		
		l1.setText(sp.df.format(((price1 + price2) / moment) + ratep.price) + "원 / 월");
		
		l2.setOpaque(true);
		setFrameg("결제", 350, 550, () -> new Infor(pno));
	}

	protected void desing() {
		JPanel p1 = col(5, 
					fw(lb("결제 상세 내역", FONT(sp.font.deriveFont(16f).deriveFont(1)), FG(sp.color))),
					fw(lb(LocalDate.now() + " 기준", FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f))))
				);
		p1.setOpaque(false);
		BoxPanel p2 = set(col(5,
				fw(lb(" ▶ 기기 정보", FONT(sp.font.deriveFont(13f)), FG(sp.color))),
				fs("기종", p.pname),
				fs("선택 용량", gb),
				fs("통신사", ef),
				fs("할부 기간", moment),
				fs("개통일", LocalDate.now()),
				fs("약정 종료일", LocalDate.now().plusMonths(moment)),
				set(col(10, 5, 5,
						fw(lb(" ▶ 요금제 정보", FONT(sp.font.deriveFont(13f)), FG(sp.color))),
						fs("선택 요금제", ratep.rname),
						fs("요금제 금액", sp.df.format(ratep.price) + "원 / 월")
						).setBackColor(Color.white), BORDER(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY))),
				fw(lb(" ▶ 가격 내역", FONT(sp.font.deriveFont(13f)), FG(sp.color))),
				fs("용량가격", sp.df.format(price1) + "원"),
				fs("통신사 가격", sp.df.format(price2) + "원"),
				fs("단말기 총 출고가", sp.df.format(price1 + price2) + "원")
			), BORDER(sp.com(sp.line, sp.em(15, 10, 10, 10))), BG(Color.white));
		JPanel p3 = set(col(3, 
					fw(lb("월 납부금 (단말 할부 + 요금제)", FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY))),
					fw(l1)
				), BG(sp.color), BORDER(sp.em(5, 10, 15, 10)));
		add(set(col(10, fw(p1), f(p2), fw(p3), fw(l2)), BG(Util.setA(sp.color, 10)), BORDER(sp.em(15, 15, 5, 15))));
	}

	private JComponent fs(String t, Object s) {
		return fw(row(0, 
					lb(t, FONT(sp.font), FG(t.equals("단말기 총 출고가") ? sp.color : Color.LIGHT_GRAY)), 
					hg(), 
					lb(s.toString(), FONT(sp.font.deriveFont(1)), FG(t.equals("단말기 총 출고가") ? sp.color : Color.black)))
				).setBackColor(Color.white);
	}
	
	protected void action() {
		l2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				class test extends CFrame{
					JTextField tf = comp(JTextField::new, SIZE(0, 30));
					JButton but = bt("결제", FG(Color.white), BG(sp.color));
					List<Integer> list = new ArrayList<>();
					String str = "";
					public test() {
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
							l.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent e) {
									tf.setText(tf.getText() + l.getText());
								}
							});
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
								ordersEntity o = new ordersEntity();
								o.sprice = project.capacities.stream().filter(c -> c.value.equals(gb.toString())).findFirst().get().price;
								o.cprice = ratep.price;
								o.price = price1 + price2;
								o.mprice = ((price1 + price2) / moment) + ratep.price;
								o.opening_date = LocalDate.now();
								o.uno = sp.user.uno;
								o.pno = p.pno;
								o.rno = 1;
								o.save();
								sp.infor("결제 완료");
							}
						});
					}
				}
				new test();
			}
		});
	}
}