package main;

import javax.swing.*;

import orms.DBManager;
import orms.categoryEntity;
import orms.productEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class Home extends JPanel{
	List<Shape> charts = new ArrayList<>();
	List<Color> colors = new ArrayList<>();
	
	public Home() {
		setLayout(new BorderLayout());
		setBackground(Color.red);
		JPanel rowPanel = row(10, adverPanel(), cashPanel(), goToPanel());
		JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
		panel.add(rowPanel);
		panel.add(chartPanel());
		add(fill(panel));
	}
	
	public JPanel adverPanel() {
		JPanel p = 
			col(10, 5, 10, 
				fillWidth(lb("광고", FONT(getter.font.deriveFont(16f).deriveFont(1)))), 
				fill(set(new JLabel(getter.getImage("advertise/1.jpg", 100, 100))))
			).setBackColor(Color.white);
		p.setBorder(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(0, 10, 0, 10)));
		return p;
	}
	
	public JPanel cashPanel() {
		return panel(getter.getImage("logo/cash.png", 50, 50), "충전소", "포인트 충전하기");
	}
	
	public JPanel goToPanel() {
		JPanel p1 = panel(getter.getImage("logo/maze.png", 50, 50), "미로", "포인트 적립");
		p1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Maps(1).setVisible(true);
			}
		});
		JPanel p2 = panel(getter.getImage("logo/roulette.png", 50, 50), "경품", "룰렛 뽑기");
		JPanel panel = col(
					10, 5, 10, fillWidth(lb("바로가기", FONT(getter.font.deriveFont(1).deriveFont(16f)))),
					fill(row(10,
							set(p1, BORDER(getter.line), BG(Color.white)),
							set(p2, BORDER(getter.line), BG(Color.white))
							).setBackColor(Color.white))
				).setBackColor(Color.white);
		panel.setBorder(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(0, 10, 0, 10)));
		panel.setBackground(Color.white);
		return panel;
	}
	
	public JPanel chartPanel() {
		//List<Integer> values = categoryEntity.findAll().stream().map(e -> (int) productEntity.findBy(c -> c.cno == e.cno).stream().count()).collect(Collectors.toList());
		categoryEntity.findAll().forEach(e -> colors.add(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
		List<Integer> values = new ArrayList<>();
		DBManager.select("SELECT *, count(product.cno) as c FROM idelivery.`order`\r\n"
				+ "join product on product.cno = `order`.pno\r\n"
				+ "group by product.cno").forEach(e -> {
					values.add(Integer.parseInt(e.get(e.size() - 1)));
				});
		/*
		 * Map<Integer, Integer> map = new HashMap<>();

for (Product p : productEntity.findAll()) {
    map.put(p.cno, map.getOrDefault(p.cno, 0) + 1);
}

List<Integer> values = new ArrayList<>();
for (Category c : categoryEntity.findAll()) {
    values.add(map.getOrDefault(c.cno, 0));
}*/
		JLabel chart = new JLabel() {
			int size = values.stream().mapToInt(e -> e).sum();
			double r = 360.0 / size;
			double start = 90, end = 0;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int x = getWidth() - 150, y = (getHeight() - 120) / 2;
				g2.setColor(Color.white);
				g2.fillRect(0, 0, getWidth(), getHeight());
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				for(int i = 0; i < values.size(); i++) {
					g2.setColor(colors.get(i));
					start += end;
					end = - (r * values.get(i));
					Arc2D.Double arc1 = new Arc2D.Double(x, y, 120, 120, start - 2, end + 2, Arc2D.PIE);
					g2.fill(arc1);
				}
				g2.setColor(Color.white);
				g2.fillOval(x + 25, y + 25, 70, 70);
				start = 90; end = 0;
				charts.clear();
			}
		};
		
		JPanel categoryPanel = new JPanel(new GridLayout(categoryEntity.findAll().size(), 1, 10, 10));
		categoryPanel.setBorder(getter.em(30, 0, 20, 0));
		categoryPanel.setBackground(Color.white);
		categoryEntity.findAll().forEach(e -> {
			JLabel label = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setColor(Color.black);
					g2.setFont(getter.font.deriveFont(10f));
					while(getFontMetrics(getFont()).getHeight() < getHeight())
						g2.setFont(getter.font.deriveFont(Float.valueOf(getFont().getSize())));
					g2.drawString(e.cname, getHeight() + 5, (getFontMetrics(getFont()).getHeight() / 2) - 1);
					g2.setColor(colors.get(e.cno - 1));
					g2.fillRect(0, 0, getHeight(), getHeight());
				}
			};
			categoryPanel.add(label);
		});
		JPanel panel = col(2, 
				fillWidth(comp(JLabel::new, TEXT("사람들이 많이 구매하는 카테고리"), FONT(getter.font.deriveFont(1).deriveFont(14f)))),
				fill(row(10, fill(chart), fill(categoryPanel)).setBackColor(Color.white))
				).setBackColor(Color.white);
		panel.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		return panel;
	}
	
	private JPanel panel(ImageIcon img, String title, String subTitle) {
		JPanel titlesPanel = 
				col(0, 2, 10, 
						comp(JLabel::new, TEXT(title), FONT(getter.font.deriveFont(1).deriveFont(16f))), 
					comp(JLabel::new, TEXT(subTitle), FG(Color.LIGHT_GRAY), FONT(getter.font.deriveFont(1).deriveFont(13f)))
				).setBackColor(Color.white);
		JPanel p = set(col(0, fill(new JLabel(img)), fillWidth(titlesPanel)), BORDER(getter.line(Color.LIGHT_GRAY))).setBackColor(Color.white);
		return p;
	}
}
