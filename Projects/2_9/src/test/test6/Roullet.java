package test.test6;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

import main.Util;
import orms.*;

public class Roullet extends CFrame {
	List<Arc2D.Double> arcs = Arrays.asList(null,null,null,null,null);
	List<chanceitemEntity> items = chanceitemEntity.findAll();
	List<Color> colors = List.of(rc(),rc(),rc(),rc(),rc());
	double rand = Math.random() * 360;
	JLabel label;
	Point p = new Point();
	JButton b1 = bt("경품 뽑기! (" + sp.user.chance + "회 남음)", FONT(sp.font.deriveFont(20f).deriveFont(1)), BG(Color.white));
	private Color rc() {
		return new Color((int) ( Math.random() * 0x1000000));
	}
	
	public Roullet() {
		getContentPane().setBackground(new JButton().getBackground());
		setFrame("룰렛", 600, 500);
	}

	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				double r = getWidth() / 3.5;
				
				double start = rand;
				for(int i = 0; i < items.size(); i++) {
					double angle = 360d * items.get(i).chance;
					Arc2D.Double a = new Arc2D.Double();
					a.setArcByCenter(getWidth() / 2, getHeight() / 2, r, start, angle, Arc2D.PIE);
					arcs.set(i, a);
					g2.setColor(colors.get(i));
					g2.fill(a);
					g2.setColor(Color.white);
					g2.draw(a);
					start += angle;
				}
				
				AffineTransform old = g2.getTransform();
				
				g2.setColor(Color.white);
				g2.setFont(sp.font.deriveFont(14f).deriveFont(1));
				g2.translate(getWidth() / 2, getHeight() / 2);
				g2.rotate(Math.toRadians(-rand));
				for(int i = 0; i < items.size(); i++) {
					double angle = -360d * items.get(i).chance;
					g2.rotate(Math.toRadians(angle / 2));
					g2.drawString(items.get(i).ciname, 50, 6);
					g2.rotate(Math.toRadians(angle / 2));
				}
				g2.setTransform(old);
				
				g2.setColor(Color.LIGHT_GRAY);
				g2.drawOval((int) (getWidth() / 2 - r), (int) (getHeight() / 2 - r), (int) (r * 2), (int) r * 2);
				
				g2.setColor(Color.red);
				int w = getWidth() / 2, h = getHeight() / 2;
				int rr = (int) r;
				int[] x = {w - 10, w, w + 10}, y = {h - rr - 10, h - rr + 20, h - rr - 10};
				g2.fillPolygon(x, y, 3);
				
				p.x = x[1]; p.y = y[1];
			}
		};
		label.setBorder(sp.line);
		label.setBackground(Color.white);
		label.setOpaque(true);
		add(set(col(0, f(label), fw(b1)), BORDER(sp.em(10, 10, 10, 10))));
	}

	double speed = 0;
	javax.swing.Timer timer;
	protected void action() {
		b1.addActionListener(ae -> {
			b1.setEnabled(false);
			speed = Math.random() * 40 + 30;
			timer = new javax.swing.Timer(1, e -> {
				rand -= speed;
				speed *= 0.99;
				if(speed < 0.1) {
					timer.stop();
					sp.infor("축하합니다!\n" + items.get(arcs.indexOf(arcs.stream().filter(a -> a.contains(p)).findFirst().get())).ciname + "에 당첨되셨습니다.");
					sp.user.chance -= 1;
					sp.user.save();
					b1.setEnabled(true);
					b1.setText("경품 뽑기! (" + sp.user.chance + "회 남음)");
				}
				repaint();
			});
			timer.start();
		});
	}
	public static void main(String[] args) {
		Util.start(new Roullet());
	}
}