package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import orms.*;

public class Roullet extends CFrame {
	List<Arc2D.Double> arcs = new ArrayList<>();
	List<chanceitemEntity> item = chanceitemEntity.findAll();
	List<Color> colors = Arrays.asList(rc(),rc(),rc(),rc(),rc());
	JLabel l;
	JButton b1 = bt("", FONT(sp.font.deriveFont(16f).deriveFont(1)), BG(Color.white));
	
	Point point = new Point();
	double rand = Math.random() * 360;
	
	private Color rc() {
		return new Color((int) (Math.random() * 0x1000000));
	}
	public Roullet() {
		setFrame("경품", 500, 500);
	}

	protected void desing() {
		setText();
		l = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int w = getWidth() / 2, h = getHeight() / 2, r = (int) (getWidth() / 2.5);
				double start = rand;
				for(int i = 0; i < 5; i++) {
					double angle = 360 * item.get(i).chance;
					g2.setColor(colors.get(i));
					Arc2D.Double a = new Arc2D.Double(w - r, h - r, r * 2, r * 2, start, angle, Arc2D.PIE);
					g2.fill(a);
					g2.setColor(Color.white);
					g2.draw(a);
					arcs.set(i, a);
					start += angle;
					
				}
				
				AffineTransform o = g2.getTransform();
				g2.translate(w, h);
				g2.setFont(sp.font.deriveFont(15f).deriveFont(1));
				g2.setColor(Color.white);
				g2.rotate(Math.toRadians(-rand));
				for(int i = 0; i < 5; i++) {
					double angle = -360 * item.get(i).chance;
					g2.rotate(Math.toRadians(angle / 2));
					g2.drawString(item.get(i).ciname, 50, 6);
					g2.rotate(Math.toRadians(angle / 2));
				}
				
				g2.setTransform(o);
				
				g2.setColor(Color.red);
				int[] x = {w - 15, w , w + 15}, y = {h - r - 10, h - r + 10, h - r - 10};
				g2.fillPolygon(x, y, 3);
				
				point.x = x[1]; point.y = y[1];
			}
			
		};
		l.setBorder(sp.line);
		l.setBackground(Color.white);
		l.setOpaque(true);
		add(set(col(10, f(l), fw(b1)).setBackColor(Color.white), BORDER(sp.em(10, 10, 10, 10))));
	}

	private void setText() {
		b1.setText("경품 뽑기! (" + sp.user.chance + "회 남음)");
	}
	
	double speed = 0;
	Timer timer;
	protected void action() {
		b1.addActionListener(e -> {
			b1.setEnabled(false);
			speed = Math.random() * 20 + 30;
			timer = new Timer(15, t -> {
				rand -= speed;
				speed *= 0.995;
				if(speed < 0.1) {
					sp.infor("축하합니다!\n" + item.get(arcs.indexOf(arcs.stream().filter(ac -> ac.contains(point)).findFirst().get())).ciname.split(" ")[0].replace(",", "") + "에 당첨되셨습니다.");;
					sp.user.point += Integer.parseInt(item.get(arcs.indexOf(arcs.stream().filter(ac -> ac.contains(point)).findFirst().get())).ciname.split(" ")[0].replace(",", ""));
					sp.user.chance--;
					sp.user.save();
					b1.setEnabled(true);
					timer.stop();
				}
				setText();
				repaint();
			});
			timer.start();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Roullet());
	}
}