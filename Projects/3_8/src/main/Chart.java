package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
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
	String[] str2 = "1 / 3,2 / 3,3 / 3".split(",");
	int n = 0;
	
	JLabel label;
	JLabel numberL = new JLabel(n + 1 + " / 3");
	JLabel titleL = lb(str[n], FONT(sp.font.deriveFont(16f)));
	
	int n2 = -1;
	
	int nx = 0;
	Timer moveT;
	public Chart() {
		moveT = new Timer(5, e -> {
			nx -= n2 * 2;
			if(nx % 500 == 0) moveT.stop();
			repaint();
		});
		
		for(int i = 0; i < m1.size(); i++) rColor(cs1);
		for(int i = 0; i < m2.size(); i++) rColor(cs2);
		for(int i = 0; i < m3.size(); i++) rColor(cs3);
		setFramed("분석", 500, 500, () -> moveT.stop());
		setResizable(true);
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
				
				double r = getHeight() / 3;
				double sx = getWidth() / 2 - r, sy = getHeight() / 2 - r;
				double start = 0;
				paintz(0, g2, sx, sy, start, r, m1, cs1);
				paintz(1, g2, sx, sy, start, r, m2, cs2);
				paintz(2, g2, sx, sy, start, r, m3, cs3);
			}
			
			private void paintz(int n, Graphics2D g2,double sx, double sy, double start, double r, List<Entry<String, Long>> ms, List<Color> cs) {
				sx += nx + n * getWidth();
				double cx = getWidth() / 2 + nx + n * getWidth(), cy = getHeight() / 2;
				Font font = g2.getFont();
				
				g2.setFont(sp.font.deriveFont(16f));
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString(str[n], (int) cx - fm.stringWidth(str[n]) / 2, (int) fm.getHeight() + 10);
				
				g2.setFont(font);
				fm = g2.getFontMetrics();
				g2.drawString(str2[n], (int) cx - fm.stringWidth(str2[n]) / 2, (int) getHeight() - fm.getHeight() - 10);
				
				for(Entry<String, Long> m : ms) {
					double angle = (360.0 / ms.stream().mapToInt(c -> c.getValue().intValue()).sum()) * m.getValue();
					int x = (int) ((Math.cos(Math.toRadians(-start - angle / 2)) * r / 2) + cx);
					int y = (int) ((Math.sin(Math.toRadians(-start - angle / 2)) * r / 2) + cy);
					
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
		set(label, BG(Color.white)).setOpaque(true);
		add(label);
	}

	
	@Override
	protected void action() {
		MouseAdapter mac = new MouseAdapter() {
			int x = 0;
			
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
				
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				moveT.stop();
				if(Math.abs(x - e.getX()) > 100) {
					n += n2;
					if(n < 0) n = 0;
					if(n > 2) n = 2;
					System.out.println(n);
					moveT.start();
					repaint();
				}
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				if(e.getX() < 0 || e.getX() > 500) return;
				if(x - e.getX() > 0) n2 = 1;
				else n2 = -1;
			}
		};
		label.addMouseMotionListener(mac);
		label.addMouseListener(mac);
	}
	
	public static void main(String[] args) {
		Util.start(new Chart());
	}
}