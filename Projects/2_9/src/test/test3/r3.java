package test.test3;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import main.Util;
import orms.chanceitemEntity;

import static utils.BoxPanel.*;
import utils.*;
import static utils.Properties.*;

public class r3 extends CFrame{
	double rand = Math.random() * 360;
	List<chanceitemEntity> items = chanceitemEntity.findAll();
	List<Color> color = IntStream.range(0, 5).mapToObj(e -> new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))).collect(Collectors.toList());
	List<Arc2D.Double> arcs = Arrays.asList(null, null, null, null, null);
	JButton b = bt("경품 뽑기! (" + sp.user.chance + "회 남음)", FONT(sp.font.deriveFont(20f).deriveFont(1)), BG(Color.white));
	JLabel label;
	Point point = new Point();
	
	public r3() {
		setFrame("경품", 600, 500, () -> {});
	}

	@Override
	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int os = 300;
				int w = getWidth(), h = getHeight(), sx = w / 2 - os / 2, sy = h / 2 - os / 2;
				g2.setColor(Color.LIGHT_GRAY);
				g2.fillOval(sx - 3, sy - 3, os + 6, os + 6);
				double start = rand;
				for(int i = 0; i < items.size(); i++) {
					double angle = -360.0 * items.get(i).chance;
					Arc2D.Double a = new Arc2D.Double(sx, sy, os, os, start, angle, Arc2D.PIE);
					g2.setColor(color.get(i));
					g2.fill(a);
					arcs.set(i, a);
					start += angle;
				}
				
				start = rand;
				int cx = w / 2, cy = h / 2, r = os / 2;
				g2.setColor(Color.white);
				for(int i = 0; i < items.size(); i++) {
					double angle = -360.0 * items.get(i).chance;
					
					double rad = Math.toRadians(start);
					int x = (int) (cx + r * Math.cos(rad));
					int y = (int) (cy - r * Math.sin(rad));
					g2.drawLine(cx, cy, x, y);
					start += angle;
				}
				
				start = rand;
				AffineTransform old = g2.getTransform();
				g2.translate(cx, cy);
				g2.rotate(Math.toRadians(-rand));
				g2.setFont(sp.font.deriveFont(15f).deriveFont(1));
				for(int i = 0; i < items.size(); i++) {
					double angle = 360.0 * items.get(i).chance;
					g2.rotate(Math.toRadians(angle / 2));
					g2.drawString(items.get(i).ciname, 50, getFontMetrics(getFont()).getHeight() / 2);
					g2.rotate(Math.toRadians(angle / 2));
					start += angle;
				}
				g2.setTransform(old);
				int[] x = {cx - 12, cx, cx + 12}, y = {sy - 10, sy + 20, sy - 10};
				g2.setColor(Color.red);
				g2.fillPolygon(x, y, 3);
				point.x = x[1];point.y = y[1];
			}
		};
		label.setBackground(Color.white);
		label.setOpaque(true);
		label.setBorder(sp.line(Color.LIGHT_GRAY));
		add(set(col(0, f(label), fw(b)), BORDER(sp.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		b.addActionListener(e -> {
			b.setEnabled(false);
			new Thread(() -> {
				try {
					double speed = 0.000000001;
					double max = (Math.random() * 3 + 1) / 100.0;
					while(speed < max) {
						long millis = (long)(speed * 1000);
			            int nanos = (int)((speed * 1_000_000_000) % 1_000_000);

			            rand--;
			            SwingUtilities.invokeLater(() -> label.repaint());
			            Thread.sleep(millis, nanos);
			            rand--;
			            SwingUtilities.invokeLater(() -> label.repaint());
			            speed *= 1.01;
					}
					SwingUtilities.invokeLater(() -> {
						int n = arcs.indexOf(arcs.stream().filter(c -> c.contains(point)).findFirst().get());
						sp.infor("축하합니다!\n" + items.get(n).ciname + "에 당첨되셨습니다!");
						sp.user.chance -= 1;
						sp.user.point += Integer.parseInt(items.get(n).ciname.split(" ")[0].replace(",", ""));
						sp.user.save();
						b.setEnabled(true);
					});
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}).start();;
		});
	}
	
	public static void main(String[] args) {
		Util.start(new r3());
	}

}
