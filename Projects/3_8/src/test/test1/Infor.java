package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.*;

import orms.*;

public class Infor extends CFrame {
	JPanel p = set(new JPanel(new GridLayout(1, 2, 20, 20)), BG(Color.white), BORDER(sp.em(15, 15, 15, 20)));
	JComboBox<String> c1 = cb("", SIZE(0, 30));
	JComboBox<String> c2 = cb("", SIZE(0, 30));
	JComboBox<String> c3 = cb("", SIZE(0, 30));
	
	productEntity pd;
	ProjectEntity pj;
	
	int price = 0;
	JLabel img = set(new JLabel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(pd != null) {
				Graphics2D g2 = sp.anti(g);
				g2.drawImage(new ImageIcon("datafiles/기종/" + pd.pno + ".jfif").getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		};
	}, BORDER(sp.line), SIZE(0, 200));
	JLabel l1 = lb("원", FONT(sp.font.deriveFont(1)));
	JLabel l2 = lb("요금제 선택 안됌", HOA(JLabel.CENTER), BORDER(sp.line));
	JLabel lp = lb("+", FG(sp.color), BORDER(sp.line(sp.color)), SIZE(50, 30), HOA(JLabel.CENTER), FONT(sp.font.deriveFont(22f).deriveFont(1)));
	
	
	JButton b1 = bt("결제하러가기", FONT(sp.font.deriveFont(16f)), FG(Color.white), BG(sp.color));
	rateplanEntity ratep;
	double star = 0d;
	public Infor(int pno) {
		pd = productEntity.findById(pno).get();
		pj = ProjectEntity.findById(pno).get();
		
		pj.capacities.forEach(e -> c1.addItem(e.value + "GB"));
		pj.items.forEach(e -> c2.addItem(e.type));
		pj.installments.forEach(e -> c3.addItem(e.month.toString()));
		
		star += Math.round(starEntity.findBy(e -> e.pno.equals(pno)).stream().mapToDouble(e -> e.scope).average().getAsDouble());
		
		setFrameg("상세정보", 450, 375, () -> new A_Main());
		l1Text();
		img.repaint();
	}

	protected void desing() {
		JPanel starPanel = row(5, IntStream.range(0, 5).mapToObj(e -> {
			return lb(e <= star ? "★" : "☆", FG(Color.orange), FONT(sp.font.deriveFont(16f)));
		}).toArray(JComponent[]::new)).setBackColor(Color.white);
		starPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Review(pd.pno);
				dispose();
			}
		});
		JPanel p1 = set(col(10, fw(img), fw(l1)), BG(Color.white));
		JPanel p2 = set(col(10, 
					fw(lb(pd.pname, FONT(sp.font.deriveFont(20f).deriveFont(1)))),
					fw(row(30, lb("별점 : "), starPanel)).setBackColor(Color.white),
					fw(col(2, fw(lb("용량")), f(c1))).setBackColor(Color.white),
					fw(col(2, fw(lb("통신사")), f(c2))).setBackColor(Color.white),
					fw(col(2, fw(lb("할부")), f(c3))).setBackColor(Color.white),
					fw(col(2, fw(lb("요금제")), f(row(5, f(l2), fh(lp)).setBackColor(Color.white)))).setBackColor(Color.white),
					f(b1)
				), BG(Color.white));
		p.add(p1);
		p.add(p2);
		add(p);
	}

	protected void action() {
		lp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				class test extends CFrame{
					JScrollPane sc = new JScrollPane();
					test(){
						sc.setBackground(Color.white);
						getContentPane().setBackground(Color.white);
						setFrame("요금제 선택", 600, 250);
						sc.getHorizontalScrollBar().setValue(0);
					}
					@Override
					protected void desing() {
						sc.setViewportView(
								set(row(20, 20, 20, 
									rateplanEntity.findBy(e -> e.service.equals(c2.getSelectedItem().toString())).stream().map(e -> {
										BoxPanel p = set(col(0,
													fw(set(col(0, 10, 5, 
															fw(lb(e.rname, FONT(sp.font.deriveFont(13f).deriveFont(1)), FG(sp.color))),
															fw(lb(sp.df.format(e.price) + "원 / 월", FONT(sp.font.deriveFont(13f).deriveFont(1)), FG(sp.color)))
														), BG(Color.white), BORDER(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray)))),
													f(col(5, 3, 0,
															List.of(e.effect.replace("\"", "").split(",")).stream()
															.map(ef -> fw(lb("√ " + ef)))
															.toArray(JComponent[]::new)
														).setBackColor(Color.white))
												), BG(Color.white), BORDER(sp.com(sp.line(sp.color), sp.em(10, 10, 10, 10))), SIZE(200, 0));
										p.addMouseListener(new MouseAdapter() {
											public void mouseClicked(MouseEvent ae) {
												ratep = e;
												l1Text();
												dispose();
											}
										});
										return fh(p);
									}).toArray(JComponent[]::new)), BG(Color.white), BORDER(sp.em(10, 0, 5, 0)))
								);
						add(sc);
					} 
					@Override protected void action() { }
				}
				new test();
			}
		});
		b1.addActionListener(e -> {
			if(ratep == null) throw new RuntimeException("요금제를 선택해주세요");
			new Pay(ratep, pd.pno, Integer.parseInt(c1.getSelectedItem().toString().split("GB")[0]), c2.getSelectedItem().toString(), Integer.parseInt(c3.getSelectedItem().toString()));
			dispose();
		});
	}
	
	private void l1Text() {
		int c1p = pj.capacities.stream().filter(e -> e.value.equals(c1.getSelectedItem().toString().split("GB")[0])).findFirst().get().price;
		int c2p = pj.items.stream().filter(e -> e.type.equals(c2.getSelectedItem().toString())).findFirst().get().price;
		int total = (c1p + c2p) / Integer.parseInt(c3.getSelectedItem().toString());
		
		l1.setText(sp.df.format(total + (ratep != null ? ratep.price : 0)) + "원 / 월");
		l2.setText(ratep == null ? "요그제 선택 안됌" : ratep.rname);
	}
	
	public static void main(String[] args) {
		Util.start(new Infor(1));
	}
}