package main;

import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import orms.*;

public class search extends CFrame{
	//단축키 Login, Serch, 밑에 3ro
	//Login -> 5분 10초 아래, Search 15분 9초 - 5분 10초 + 20분 - 13분 43초
	JComboBox<String> cb1 = cb("(없음), 가격↑, 가격↓, 별점↑, 별점↓".split(", "), BG(Color.white));
	JComboBox<String> cb2 = cb(("전체," + categoryEntity.findAll().stream().map(e -> e.cname).collect(Collectors.joining(","))).split(","), BG(Color.white));
	
	
	JTextField tf1 = comp(JTextField::new);
	
	JButton b1 = bt("검색", BG(Color.white));
	JPanel p1 = set(new JPanel(new GridLayout(0, 4, 10, 10)),BG(Color.white), BORDER(sp.em(10, 10, 10, 10)));
	JScrollPane sc = comp(JScrollPane::new, BG(Color.white), BORDER(null));
	
	
	public search() {
		sc.setViewportView(p1);
		setFrame("검색", 700, 500);
	}
	
	
	@Override
	protected void desing() {
		JPanel p2 = set(new JPanel(new GridLayout(0, 4, 10, 10)), BG(Color.white), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)));
		p2.add(col(5, fw(lb("정렬조건")), f(cb1)));
		p2.add(col(5, fw(lb("분류조건")), f(cb2)));
		p2.add(col(5, fw(lb("검색어")), f(tf1)));
		p2.add(col(5, f(b1)));
		
		initP1();
		
		add(col(10, fw(p2), f(sc)).setBackColor(Color.white));
	}
	
	
	private void initP1() {
		p1.removeAll();
		productEntity.findAll().stream()
				.filter(e -> e.pname.trim().contains(tf1.getText().trim()))
				.filter(e -> {
					if(cb2.getSelectedIndex() == 0) return true;
					else return cb2.getSelectedIndex() == e.cno;
				})
				.sorted((a ,b) -> {
					if(cb1.getSelectedIndex() == 1) return a.price - b.price;// Math.min(a, b) - Math.max(a, b); // a: 10, b = 20;
					if(cb1.getSelectedIndex() == 2) return b.price - a.price;
					if(cb1.getSelectedIndex() == 3) return Double.compare(a.avgStar(), b.avgStar());
					if(cb1.getSelectedIndex() == 4) return Double.compare(b.avgStar(), a.avgStar());
					return 0;
				}).forEach(p -> card(p));
		if (p1.getComponentCount() == 0) {
			tf1.setText("");
			cb1.setSelectedIndex(0);
			cb2.setSelectedIndex(0);
			initP1();
			sp.exception("검색결과가 없습니다");
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
		img.setPreferredSize(new Dimension(0, 150));
		
		p1.add(col(2, 10, 10,
				f(img),
				lb(p.pname),
				lb(sp.df.format(p.price) + "원"),
				lb("별점 " + new DecimalFormat("#.0").format(p.avgStar()) + " 구매 " + orderEntity.findBy(o -> o.pno.equals(p.pno)).size(), FG(Color.LIGHT_GRAY))
		).setBackColor(Color.white));
	}
	
	@Override
	protected void action() {
		cb1.addActionListener(e -> initP1());
		cb2.addActionListener(e -> initP1());
		b1.addActionListener(e -> initP1());
	}

	public static void main(String[] args) {
		Util.start(new search());
	}
}
