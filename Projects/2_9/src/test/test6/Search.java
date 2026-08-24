package test.test6;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.stream.Collectors;

import javax.swing.*;

import orms.*;

public class Search extends JPanel{
	JComboBox<String> cb1 = cb("(없음),가격↑,가격↓,별점↑,별점↓", BG(Color.white));
	JComboBox<String> cb2 = cb("전체," + categoryEntity.findAll().stream().map(e -> e.cname).collect(Collectors.joining(",")), BG(Color.white));
	JTextField t1 = comp(JTextField::new);
	JButton b1 = bt("검색", BG(Color.white));
	JPanel panel = set(new JPanel(new GridLayout(0, 4, 10, 10)), BORDER(sp.em(10, 10, 10, 10)));
	JScrollPane sc = set(new JScrollPane(new JPanel(new BorderLayout()) {{ add(panel, BorderLayout.NORTH); }}), BORDER(null));
	
	public Search(JFrame f) {
		setLayout(new BorderLayout(10, 10));
		
		cb1.addActionListener(e -> {
			reload();
		});
		cb2.addActionListener(e -> {
			reload();
		});
		b1.addActionListener(e -> {
			reload();
		});
		
		JPanel top = set(new JPanel(new GridLayout(1, 4, 10, 10)), BG(Color.white), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)));
		top.add(col(2, fw(lb("정렬조건")), f(cb1)).setBackColor(Color.white));
		top.add(col(2, fw(lb("분류조건")), f(cb2)).setBackColor(Color.white));
		top.add(col(2, fw(lb("검색어")), f(t1)).setBackColor(Color.white));
		top.add(b1);
		
		add(top, BorderLayout.NORTH);
		add(sc);
		
		reload();
	}

	private void reload() {
		panel.removeAll();
		productEntity.findAll().stream()
		.filter(e -> e.pname.replace(" ", "").contains(t1.getText().replace(" ", "")))
		.filter(e -> cb2.getSelectedIndex() == 0 || cb2.getSelectedIndex() == e.cno.intValue())
		.sorted((a, b) -> {
			int index = cb1.getSelectedIndex();
			if(index == 1) return Integer.compare(a.price, b.price);
			if(index == 2) return Integer.compare(b.price, a.price);
			if(index == 3) return Double.compare(a.review(), b.review());
			if(index == 4) return Double.compare(b.review(), a.review());
			return a.pno - b.pno;
		})
		.forEach(e -> {
			JLabel img = set(new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(e.img, 0, 0,getWidth(), getHeight(), null);
				}
			}, SIZE(0, 150));
			
			panel.add(set(col(12, f(img), 
					fw(lb(e.pname, FONT(sp.font.deriveFont(13f)))), 
					fw(lb(sp.df.format(e.price) + "원", FONT(sp.font.deriveFont(13f)))),
					fw(lb("별점 " + new DecimalFormat().format(e.review()) + "  구매" + orderEntity.findBy(c -> e.pno.equals(c.pno)).size(), FG(Color.LIGHT_GRAY)))
					), BORDER(sp.eLine(Color.LIGHT_GRAY, 2, 2, 2, 2)), BG(Color.white)));
			var p = panel.getComponent(panel.getComponentCount() - 1);
			p.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent ee) {
					System.out.println(e.pno);
				}
			});
		});
		if(panel.getComponentCount() == 0) {
			sp.err("검색결과가 없습니다.");
			cb1.setSelectedIndex(0);
			cb2.setSelectedIndex(0);
			t1.setText("");
			reload();
		}
		revalidate();
		repaint();
	}
}