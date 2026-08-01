package main;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import orms.categoryEntity;
import orms.detailEntity;
import orms.likesEntity;
import orms.productEntity;
import utils.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static utils.BoxPanel.*;

public class ProductInfor extends CFrame{
	
	JButton shoppingBasket = comp(CButton::new, TEXT("장바구니"));
	JButton buy = comp(CButton::new, TEXT("바로 결제"), FG(Color.white), BG(sp.orange));
	
	JLabel minus = 	lb("-", FONT(sp.font.deriveFont(1)	.deriveFont(20f))	, BG(Color.white), VEA(JLabel.CENTER), HOA(JLabel.CENTER), BORDER(sp.line)), 
			plus = 	lb("+", FONT(sp.font.deriveFont(1)	.deriveFont(20f))	, BG(Color.white), VEA(JLabel.CENTER), HOA(JLabel.CENTER), BORDER(sp.line)),
			count = lb("1", FONT(sp.font.deriveFont(20f).deriveFont(1))		, BG(Color.white), VEA(JLabel.CENTER), HOA(JLabel.CENTER), BORDER(sp.line));
	productEntity product;
	detailEntity detail;
	
	Rectangle rect;
	
	public ProductInfor(int pno) {
		count.setOpaque(true);
		plus.setOpaque(true);
		minus.setOpaque(true);
		product = productEntity.findById(pno).get();
		detail = detailEntity.findById(product.dno).get();
		setFrame("상품 상제정보", 625, 700);
	}

	@Override
	protected void desing() {
		JPanel p1 = set(new JPanel(new GridLayout(1, 2, 20, 20)), BG(Color.white));
		JLabel p1l = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/product/" + product.pno + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
				g.drawImage(new ImageIcon("datafiles/heart_off.png").getImage(), getWidth() - 40, getHeight() - 40, 30, 30, null);
				rect = new Rectangle(getWidth() - 40, getHeight() - 40, 30, 30);
			}
		};
		p1l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(rect.contains(e.getPoint())) {
					if(likesEntity.findBy(l -> l.uno.equals(sp.user.uno) && l.pno.equals(product.pno)).isEmpty()) {
						System.out.println("on");
					}else {
						System.out.println("off");
					}
				}
			}
		});
		p1l.setBorder(sp.line);
		JPanel p1p = col(20, 
				fw(lb(product.pname, FONT(sp.font.deriveFont(24f).deriveFont(1)))),
				fw(col(2, fw(lb(product.pcompany, FG(Color.LIGHT_GRAY))), fw(lb("종류 : " + categoryEntity.findById(detail.cno).get().cname + " / " + detail.dname, FG(Color.LIGHT_GRAY))))).setBackColor(Color.white),
				fw(lb(sp.df.format(product.pprice) + "원", FG(sp.orange), FONT(sp.font.deriveFont(20f).deriveFont(1)))),
				fw(starPanel()),
				numberPanel(),
				buttonPanel()
				).setBackColor(Color.white);
		p1.add(p1l); p1.add(p1p);
		
		
		JTextArea ta = set(new JTextArea(product.pcontent), SIZE(0, 50));
		ta.setLineWrap(true);
		ta.setBorder(sp.line);
		ta.setFocusable(false);
		ta.setCursor(Cursor.getDefaultCursor());
		
		JPanel p2 = col(5, fw(lb("상세 설명", HOA(JLabel.LEFT), FONT(sp.font.deriveFont(1).deriveFont(16f)))), fw(ta)).setBackColor(Color.white);
		JLabel img = set(new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/info/" + product.pno + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
			}
		}, SIZE(0, 800));
		
		JPanel mainPanel = set(new JPanel(new BorderLayout(20, 20)), BG(Color.white), BORDER(sp.em(10, 20, 10, 0)));
		mainPanel.add(p1, BorderLayout.NORTH);
		mainPanel.add(p2);
		mainPanel.add(img, BorderLayout.SOUTH);
		add(set(new JScrollPane(mainPanel), BG(Color.white), BORDER(null)));
	}
	
	private JPanel starPanel() {
		return row(15, lb("☆ ☆ ☆ ☆ ☆",FG(sp.orange),FONT(sp.font.deriveFont(20f))), fw(lb("0.0", FG(sp.orange), FONT(sp.font.deriveFont(20f).deriveFont(1))))).setBackColor(Color.white);
	}
	private JPanel numberPanel() {
		minus.setPreferredSize(new Dimension(30, 30));
		count.setPreferredSize(new Dimension(45, 30));
		plus.setPreferredSize(new Dimension(30, 30));
		return row(0, minus, count, plus).setBackColor(Color.white);
		
	}
	private JPanel buttonPanel() {
		
		JPanel p = set(new JPanel(new GridLayout(1, 2, 15, 15)), BG(Color.white), BORDER(sp.em(0, 0, 0, 20)), SIZE(0, 40));
		p.add(shoppingBasket); p.add(buy);
		return p;
	}
	
	@Override
	protected void action() {
		MouseAdapter ma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = Integer.parseInt(count.getText());
				if(e.getSource() == minus && i > 1) count.setText(String.valueOf(i - 1)); 
				if(e.getSource() == plus && i < product.pcount) count.setText(String.valueOf(i + 1));
				repaint();
			}
		};
		
		minus.addMouseListener(ma);
		plus.addMouseListener(ma);
	}

	public static void main(String[] args) {
		Util.start(new ProductInfor(1));
	}
}
