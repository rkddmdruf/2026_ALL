package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;

import orms.categoryEntity;
import orms.orderEntity;
import orms.productEntity;
import orms.reviewEntity;
import orms.reviewEntity2;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Product extends JPanel{
	JComboBox<String> order = cb("(없음),가격↑,가격↓,별점↑,별점↓", NAME("정렬조건"), BG(Color.white));
	JComboBox<String> category = cb(categoryEntity.findAll().stream().map(e -> e.cname).collect(Collectors.joining(",")), NAME("분류조건"), BG(Color.white));
	JTextField tf = tf("검색어");
	JButton search = bt("검색", BG(Color.white));
	
	public Product() {
		setLayout(new BorderLayout());
		
		JPanel topPanel = set(rowF(10, 
				nameCom(order), nameCom(category), nameCom(tf), search).setBackColor(Color.white),
				BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(10, 10, 10, 10))));
		JButton but = new JButton();
		JPanel panel = col(20, fillWidth(topPanel), fill(new JScrollPane(mainPanel()) {{ setBorder(null); }}));
		add(panel);
	}

	private JPanel mainPanel() {
		JPanel panel = set(new JPanel(new GridLayout(0, 4, 10, 10)), BORDER(getter.em(0, 10, 0, 10)));
		
		List<productEntity> list = productEntity.findAll();
		
		for(int i = 0; i < list.size(); i++) {
			productEntity pro = list.get(i);
			double star = Math.round(reviewEntity2.findAll(pro.pno).stream().mapToInt(e -> e.star).average().getAsDouble() * 10d) / 10d;
			int buyed = orderEntity.findBy(e -> e.pno.equals(pro.pno)).stream().mapToInt(e -> e.quantity).sum();
			
			JLabel image = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(pro.img, 0, 0, getWidth(), getHeight(), null);
				}
			};
			image.setPreferredSize(new Dimension(160, 180));
			JPanel p = set(colF(15, image, 
					lb(pro.pname, FONT(getter.font.deriveFont(14f).deriveFont(1))),
					lb(getter.df.format(pro.price) + "원", FONT(getter.font.deriveFont(1).deriveFont(14f))),
					lb("별" + star + " 구매 " + buyed, FONT(getter.font), FG(Color.LIGHT_GRAY))
					).setBackColor(Color.white), BORDER(getter.eLine(Color.LIGHT_GRAY, 2, 5, 10, 5)));
			panel.add(p);
		}
		return panel;
	}
	private JPanel nameCom(JComponent c) {
		c.setPreferredSize(new Dimension(0, 30));
		JPanel p = col(1, fillWidth(lb(c.getName())), fill(c)).setBackColor(Color.white);
		return p;
	}
}
