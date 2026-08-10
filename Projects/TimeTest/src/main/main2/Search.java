package main.main2;

import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import main.Util;
import orms.*;

public class Search extends CFrame{
	JComboBox<String> cb1 = cb("(없음), 가격↑, 가격↓, 별점↑, 별점↓".split(", "), BG(Color.white));
	JComboBox<String> cb2 = cb(("전체," + categoryEntity.findAll().stream().map(e -> e.cname).collect(Collectors.joining(","))).split(","), BG(Color.white));
	JTextField tf1 = comp(JTextField::new);
	JButton b1 = bt("검색", BG(Color.white));
	
	JPanel p1 = set(new JPanel(new GridLayout(0, 4, 10, 10)), BORDER(sp.em(10, 10, 10, 10)));
	JScrollPane sc = set(new JScrollPane(), BORDER(null));
	public Search() {
		sc.setViewportView(new JPanel(new BorderLayout()) {{
			add(p1, BorderLayout.NORTH);
		}});
		setFrame("검색", 700, 500);
	}
	@Override
	protected void desing() {
		JPanel p2 = set(new JPanel(new GridLayout(1, 4, 10, 10)), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)), BG(Color.white));
		p2.add(col(5, fw(lb("정렬조건")), f(cb1)).setBackColor(Color.white));
		p2.add(col(5, fw(lb("분류조건")), f(cb2)).setBackColor(Color.white));
		p2.add(col(5, fw(lb("검색어")), f(tf1)).setBackColor(Color.white));
		p2.add(col(5, f(b1)).setBackColor(Color.white));
		
		initP1();
		
		add(col(10, fw(p2), f(sc)));
	}

	private void initP1() {
		p1.removeAll();
		productEntity.findAll().stream()
			.filter(e -> e.pname.replace(" ", "").contains(tf1.getText().replace(" ", "")))
			.filter(e -> cb2.getSelectedIndex() == 0 || cb2.getSelectedIndex() == e.cno)
			.sorted((a, b) -> {
				if(cb1.getSelectedIndex() == 1) return a.price - b.price;
				if(cb1.getSelectedIndex() == 2) return b.price - a.price;
				if(cb1.getSelectedIndex() == 3) return Double.compare(a.avgStar(), b.avgStar());
				if(cb1.getSelectedIndex() == 4) return Double.compare(b.avgStar(), a.avgStar());
				
				return 0;
			}).forEach(p -> card(p));
		if(p1.getComponentCount() == 0) {
			tf1.setText("");
			sp.err("검색 결과가 없습니다");
			initP1();
		}
		revalidate();
		repaint();
	}
	
	private void card(productEntity p) {
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(p.img, 0, 0, getWidth(), getHeight(), null);
			}
		};
		set(img, SIZE(125, 125));
		p1.add(set(col(2, 10, 10,
					f(img),
					fw(lb(p.pname)),
					fw(lb(sp.df.format(p.price) + "원")),
					fw(lb("별점 " + new DecimalFormat("#.0").format(p.avgStar()) + " 구매 " + orderEntity.findBy(e -> e.pno.equals(p.pno)).size(), FG(Color.LIGHT_GRAY)))
				).setBackColor(Color.white), BORDER(sp.eLine(Color.LIGHT_GRAY, 0, 2, 0, 2))));
	}
	
	@Override
	protected void action() {
		cb1.addActionListener(e -> initP1());
		cb2.addActionListener(e -> initP1());
		b1.addActionListener(e -> initP1());
	}
	
	public static void main(String[] args) {
		Util.start(new Search());
	}
	
}
