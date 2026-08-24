package test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

import main.Util;
import orms.chanceitemEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class R5 extends CFrame{
	List<chanceitemEntity> items = chanceitemEntity.findAll();
	double rand = Math.random() * 360;
	List<Color> colors = IntStream.range(0, 5).mapToObj(e -> new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))).collect(Collectors.toList());
	JLabel label;
	Point point = new Point();
	List<Arc2D.Double> arcs = Arrays.asList(new Arc2D.Double(), new Arc2D.Double(), new Arc2D.Double(), new Arc2D.Double(), new Arc2D.Double());
	
	JButton button = bt("경품 뽑기! (9회 남음)", FONT(sp.font.deriveFont(17f).deriveFont(1)), BG(Color.white));
	Timer timer;
	public R5() {
		setFrame("경품", 600, 500, () -> {});
	}

	@Override
	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int w = getWidth() / 2, h = getHeight() / 2, r = (int) (getWidth() / 3.5);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				double start = rand;
				for(int i = 0; i < items.size(); i++) {
					double angle = -360d * items.get(i).chance;
					Arc2D.Double a = new Arc2D.Double(w - r, h - r, r*2,r*2,start, angle, Arc2D.PIE);
					arcs.set(i, a);
					start+= angle;
					
					g2.setColor(Color.white);
					g2.draw(a);
					g2.setColor(colors.get(i));
					g2.fill(a);
				}
				
				start = rand;
				AffineTransform old = g2.getTransform();
				g2.translate(w, h);
				g2.rotate(Math.toRadians(-rand));
				g2.setColor(Color.white);
				g2.setFont(sp.font.deriveFont(15f).deriveFont(1));
				for(chanceitemEntity item : items) {
					double angle = 360d * item.chance;
					g2.rotate(Math.toRadians(angle / 2));
					g2.drawString(item.ciname, 50, 6);
					g2.rotate(Math.toRadians(angle / 2));
					start += angle;
				}
				g2.setTransform(old);
				
				g2.setColor(Color.LIGHT_GRAY);
				g2.setStroke(new BasicStroke(2f));
				g2.drawOval(w - r - 2, h - r - 2, r * 2 + 2, r*2 + 2);
				
				int[] x = {w - 15, w, w + 15}, y = {h - r - 10, h - r + 25, h - r - 10};
				g2.setColor(Color.red);
				g2.fillPolygon(x, y, 3);
				
				point.x = x[1]; point.y = y[1];
			}
		};
		label.setBackground(Color.white);
		label.setBorder(sp.line(Color.LIGHT_GRAY));
		label.setOpaque(true);
		add(set(col(0,f(label), fw(button)), BORDER(sp.em(10, 10, 10, 10))));
	}
	
	double speed = 0;
	@Override
	protected void action() {
		button.addActionListener(ac -> {
			button.setEnabled(false);
			speed = Math.random() * 40 + 30;
			timer = new Timer(1, e -> {
				rand -= speed;
				speed *= 0.99;
				repaint();
				if(speed < 0.1) {
					button.setEnabled(true);
					System.out.println(items.get(arcs.indexOf(arcs.stream().filter(t -> t.contains(point)).findFirst().get())).ciname);
					timer.stop();
				}
			});
			timer.start();
		});
	}

	public static void main(String[] args) {
		Util.start(new R5());
	}
}
