package test.test2;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import main.Util;
import orms.chanceitemEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Roulette40_50 extends CFrame{
	JButton button = bt("경품 뽑기! (" + getter.user.chance + "회 남음)", FONT(getter.font.deriveFont(20f).deriveFont(1)), BG(Color.white));
	List<Arc2D.Double> arcs = Arrays.asList(null, null, null,null,null);
	Point2D.Double point = new Point2D.Double();
	List<chanceitemEntity> items = chanceitemEntity.findAll();
	double rand = Math.random() * 360;
	JLabel label;
	
	public Roulette40_50() {
		setFrame("경품", 600, 500, () -> {});
	}

	@Override
	protected void desing() {
		label = new JLabel(){
			List<Color> colors = IntStream.range(0, 5).mapToObj(e -> new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))).collect(Collectors.toList());
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.lightGray);
				int w = getWidth(), h = getHeight();
				int os = 300;
				int sx = (w / 2) - (os / 2), sy = (h / 2) - (os / 2);
				g2.fillOval(sx, sy, os, os);
				double start = rand;
				for(int i = 0; i < 5; i++) {
					int off = 2;
					double angle = -360 * items.get(i).chance;
					Arc2D.Double arc = new Arc2D.Double(sx + off, sy + off, os - off*2, os - off*2, start, angle, Arc2D.PIE);
					g2.setColor(colors.get(i));
					arcs.set(i, arc);
					g2.fill(arc);
					start += angle;
				}
				
			    g2.setColor(Color.WHITE);

			    start = rand;
			    int cx = w / 2, cy = h / 2, r = os/2;
			    for (int i = 0; i < items.size(); i++) {
			        double angle = -360.0 * items.get(i).chance;

			        double rad = Math.toRadians(start);

			        int x = (int) (cx + r * Math.cos(rad));
			        int y = (int) (cy - r * Math.sin(rad));

			        g2.drawLine(cx, cy, x, y); // ✅ 중심 기준으로 변경
			        start += angle;
			    }
				
			    AffineTransform oldF = g2.getTransform();
				g2.translate(sx + (os/2), sy + (os/2));
				g2.setColor(Color.white);
				g2.setFont(getter.font.deriveFont(13f).deriveFont(1));
				g2.rotate(Math.toRadians(-rand));
				for(int i = 0; i < 5; i++) {
					double angle = 360 * items.get(i).chance;
					g2.rotate(Math.toRadians(angle / 2));
					g2.drawString(items.get(i).ciname, 40, getFontMetrics(getFont()).getHeight() / 2);
					g2.rotate(Math.toRadians(angle / 2));
				}
				g2.setTransform(oldF);
				
				g2.setColor(Color.red);
				int[] xx = {w / 2 -15, w / 2, w / 2 + 15},
						yy = {sy - 10, sy + 20, sy - 10};
				g2.fillPolygon(xx, yy, 3);
				point.x = xx[1];
				point.y = yy[1];
			}
		};
		label.setBackground(Color.white);
		label.setOpaque(true);
		label.setBorder(getter.line(Color.lightGray));
		add(set(col(0,fill(label), fillWidth(button)), BORDER(getter.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		button.addActionListener(ac -> {
			button.setEnabled(false);
			new Thread(() -> {
				try {
					double speed = 0.000000001;		// 처음 빠름 (sleep 짧음)
			        double max = (Math.random() * 3 + 1) / 100.0;	// 최대 느림 (sleep 길어짐)
			        
			        while (speed < max) {
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
			        	chanceitemEntity item = items.get(arcs.indexOf(arcs.stream().filter(e -> e.contains(point)).findFirst().get()));
			        	getter.infor("축하합니다!\n" + item.ciname + "에 당첨되셨습니다.");
			        	getter.user.chance -= 1;
			        	getter.user.point += Integer.parseInt(item.ciname.split(" ")[0].replace(",", ""));
			        	getter.user.save();
			        	button.setText("경품 뽑기! (" + getter.user.chance + "회 남음)");
			        	button.setEnabled(true);
			        });
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Roulette40_50());
	}

}
