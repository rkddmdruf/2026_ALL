package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.*;

import orms.*;

public class Infor extends CFrame {
	
	JLabel img = set(new JLabel() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = sp.anti(g);
			if(product == null) return;
			g2.drawImage(sp.getImage("product/" + product.pno), 0, 0, getWidth(), getHeight(), null);
			
			String s = "heart_" + (likesEntity.findFirst(e -> e.uno.equals(sp.user.uno) && e.pno.equals(product.pno)).isEmpty() ? "off" : "on");
			r.width = 20; r.height = r.width;
			g2.drawImage(sp.getImage(s), r.x = getWidth() - r.width - 10, r.y = getHeight() - r.height - 10, r.width, r.height, null);
		};
	}, BORDER(sp.line));
	
	Rectangle r = new Rectangle();
	
	JLabel l1 = lb("0.0", FONT(sp.font.deriveFont(24f).deriveFont(1)), FG(sp.orange));
	JLabel ml = lb("-", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(17f).deriveFont(1)), SIZE(30,30), BORDER(sp.line));
	JLabel pl = lb("+", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(17f).deriveFont(1)), SIZE(30,30), BORDER(sp.line));
	JLabel cl = lb("1", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(17f).deriveFont(1)), SIZE(40,30), BORDER(sp.line));
	
	CButton b1 = set(new CButton("장바구니"), SIZE(0, 40));
	CButton b2 = set(new CButton("바로 결제"), SIZE(0, 40), BG(sp.orange), FG(Color.white));
	
	JTextArea ta = new JTextArea();
	productEntity product;
	
	JLabel inforImage = new JLabel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = sp.anti(g);
			g2.drawImage(sp.getImage("info/" + product.pno), 0, 0, getWidth(), getHeight(), null);
		}
	};
	
	public Infor(int pno) {
		ta.setEditable(false);
		ta.setLineWrap(true);
		product = productEntity.findById(pno).get();
		
		set(ta, BORDER(sp.line), BG(Color.white), TEXT(product.pcontent), SIZE(0, 50), FONT(sp.font.deriveFont(13f)));
		setFrameg("상세정보", 550, 550, () -> new sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj());
	}

	protected void desing() {
		JPanel p1 = set(new JPanel(new GridLayout(1, 2, 10, 10)), BG(Color.white));
		p1.add(col(0,0,50, f(img)).setBackColor(Color.white));
		p1.add(setP1());
		
		
		JPanel p = set(col(10,
					fw(p1),
					fw(col(5, fw(lb("상세 설명", FONT(sp.font.deriveFont(14f).deriveFont(1)))), fw(ta))).setBackColor(Color.white),
					fw(set(inforImage, SIZE(0, 800)))
				), BORDER(sp.em(15, 15, 15, 10)), BG(Color.white));
		add(set(new JScrollPane(p), BG(Color.white), BORDER(null)));
	}

	public JPanel setP1() {
		return col(20, 
				fw(lb(product.pname, FONT(sp.font.deriveFont(21f).deriveFont(1)))),
				col(5, fw(lb(product.pcompany, FG(Color.LIGHT_GRAY))), fw(lb("종류 : " + detailEntity.findById(product.dno).get().dname, FG(Color.LIGHT_GRAY)))).setBackColor(Color.white),
				fw(lb(sp.df.format(product.pprice) + "원", FG(sp.orange), FONT(sp.font.deriveFont(16f).deriveFont(1)))),
				fw(row(10, lb("☆☆☆☆☆", FG(sp.orange), FONT(sp.font.deriveFont(15f).deriveFont(1))), l1, hg())).setBackColor(Color.white),
				fw(row(0, ml, cl, pl).setBackColor(Color.white)),
				fw(row(15, f(b1), f(b2))).setBackColor(Color.white)
			).setBackColor(Color.white);
	}

	private void setCl(int n) {
		int r = Integer.parseInt(cl.getText());
		if(r + n < 1) return;
		if(r + n > product.pcount) return;
		cl.setText(r + n + "");
		repaint();
	}
	protected void action() {
		img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				likesEntity l = likesEntity.findFirst(e -> e.uno.equals(sp.user.uno) && e.pno.equals(product.pno)).orElse(null);
				if(l == null) {
					likesEntity ll = new likesEntity();
					ll.uno = sp.user.uno;
					ll.pno = product.pno;
					ll.ldate = LocalDate.now();
					ll.save();
				}else {
					l.delete();
				}
				img.revalidate();
				img.repaint();
			}
		});
		
		MouseAdapter ma = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(l1 == e.getSource()) {
					class test extends CFrame{

						public test() {
							setFramed("리뷰", 300, 400, () -> new Infor(product.pno));
						}
						@Override
						protected void desing() {
						}

						@Override
						protected void action() {
						}
					}
					new test();
					dispose();
					return;
				}
				setCl(e.getSource() == pl ? 1 : -1);
			};
		};
		l1.addMouseListener(ma);
		ml.addMouseListener(ma);
		pl.addMouseListener(ma);

		ActionListener ac = e -> {
			class test extends CFrame{

				public test(String s) {
					setFramed(s, 800, 500, () -> new Infor(product.pno));
				}
				@Override
				protected void desing() {
				}

				@Override
				protected void action() {
				}
			}
			new test(e.getSource() == b1 ? "장바구니" : "결재");
			dispose();
		};
		b1.addActionListener(ac);
		b2.addActionListener(ac);
	}
}