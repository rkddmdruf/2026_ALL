package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class ProductFrame extends CFrame{
	
	CButton b1 = set(new CButton("수정하기"), BG(sp.blue), FG(Color.white), SIZE(100, 30), FONT(sp.font));
	CButton b2 = set(new CButton("취소"), BG(Color.LIGHT_GRAY), FG(Color.white), SIZE(100, 30), FONT(sp.font));
	public ProductFrame(int pno) {
		setFrame("상품 수정", 450, 600);
	}

	@Override
	protected void desing() {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = Util.ANTI(g);
				g2.setColor(Color.white);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				super.paintComponent(g);
			}
		};
		
		JPanel buttonPanel = row(20, hg(),b1,b2,hg());
		panel.setOpaque(false);
		add(set(col(5, f(panel), fw(buttonPanel)), BORDER(sp.em(10, 10, 2, 10))));
	}

	@Override
	protected void action() {
		
	}

	public static void main(String[] args) {
		Util.start(new ProductFrame(1));
	}
}
