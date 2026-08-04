package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.*;

import orms.categoryEntity;
import orms.ordersEntity;
import orms.productEntity;
import orms.starEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Chart extends CFrame{
	
	List<Entry<String, Long>> m1 = ordersEntity.findAll().stream().map(e -> productEntity.findById(e.pno).get())
			.collect(Collectors.groupingBy(n -> categoryEntity.findById(n.cno).get().cname, Collectors.counting()))
			.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).collect(Collectors.toList());
	
	List<Entry<String, Long>> m2 = ordersEntity.findAll().stream()
			.collect(Collectors.groupingBy(n -> productEntity.findById(n.pno).get().pname, Collectors.counting()))
			.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).collect(Collectors.toList());
	
	List<Entry<String, Long>> m3 = starEntity.findAll().stream().map(e -> productEntity.findById(e.pno).get())
			.collect(Collectors.groupingBy(n -> productEntity.findById(n.pno).get().pname, Collectors.counting()))
			.entrySet().stream().sorted((a, b) -> Long.compare(b.getValue(), a.getValue())).collect(Collectors.toList());
	
	List<Color> cs1 = new ArrayList<>();
	List<Color> cs2 = new ArrayList<>();
	List<Color> cs3 = new ArrayList<>();
	
	String[] str = "카테고리별 판매 분석,상품 판매 분석,리뷰 분석".split(",");
	int n = 0;
	
	JLabel label;
	JLabel numberL = new JLabel(n + 1 + " / 3");
	JLabel titleL = lb(str[n], FONT(sp.font.deriveFont(16f)));
	
	int n2 = -1;
	
	int nx = 0;
	Timer moveT = new Timer(15, e -> {
		nx -= 1;
		repaint();
	});
	public Chart() {
		for(int i = 0; i < m1.size(); i++) rColor(cs1);
		for(int i = 0; i < m2.size(); i++) rColor(cs2);
		for(int i = 0; i < m3.size(); i++) rColor(cs3);
		setFrame("분석", 500, 500);
		setResizable(true);
		moveT.start();
	}
	
	private void rColor(List<Color> colors) {
		colors.add(new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
	}
	@Override
	protected void desing() {
		label = new JLabel() {
			int gap;
			@Override
			protected void paintComponent(Graphics g) {
				gap = getWidth();
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				double r = getHeight() / 2.5;
				double sx = getWidth() / 2 - r, sy = getHeight() / 2 - r;
				double start = 0;
				paintz(0, g2, sx, sy, start, r, m1, cs1);
				paintz(1, g2, sx, sy, start, r, m2, cs2);
				paintz(2, g2, sx, sy, start, r, m3, cs3);
			}
			
			private void paintz(int n, Graphics2D g2,double sx, double sy, double start, double r, List<Entry<String, Long>> ms, List<Color> cs) {
				sx += nx;
				for(Entry<String, Long> m : ms) {
					double angle = (360.0 / ms.stream().mapToInt(c -> c.getValue().intValue()).sum()) * m.getValue();
					int x = (int) (Math.cos(Math.toRadians(-start - angle / 2)) * r / 2) + getWidth() / 2;
					int y = (int) (Math.sin(Math.toRadians(-start - angle / 2)) * r / 2) + getHeight() / 2;
					
					Arc2D.Double arc = new Arc2D.Double(sx, sy, r * 2, r * 2, start, angle, Arc2D.PIE);
					g2.setColor(cs.get(ms.indexOf(m)));
					g2.fill(arc);
					
					String str = m.getKey() + " (" + m.getValue() + ")";
					g2.setColor(Color.black);
					g2.drawString(str, x - g2.getFontMetrics().stringWidth(str) / 2, y);
					
					start += angle;
				}
				start = 0;
			}
		};
		add(col(10, 10, 25, titleL, f(label), numberL).setBackColor(Color.white));
	}

	Timer timer = new Timer(100, e -> {
		n = n + n2;
		System.out.println(n);
	});
	@Override
	protected void action() {
		timer.setRepeats(false);
		MouseAdapter mac = new MouseAdapter() {
			int x = 0;
			int value = 0;
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				if(value <= 50) timer.stop();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.getX() > 0 && x - e.getX() > 0) {
					timer.stop();
					timer.start();
					n2 = 1;
				}
				if(e.getX() < getWidth() && x - e.getX() < 0) {
					timer.stop();
					timer.start();
					n2 = -1;
				}
			}
		};
		label.addMouseMotionListener(mac);
		label.addMouseListener(mac);
	}
	
	public static void main(String[] args) {
		Util.start(new Chart());
	}
}