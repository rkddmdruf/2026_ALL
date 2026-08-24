package main;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import orms.chanceitemEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;

public class Roulette extends CFrame{
	
	JButton b = bt("경품 뽑기! (" + sp.user.chance + "회 남음)", FONT(sp.font.deriveFont(20f).deriveFont(1)), BG(Color.white), SIZE(0, 30)); 

	Point2D.Double point = new Point2D.Double(0, 0);
	List<Arc2D.Double> arcs = new ArrayList<>();
	List<Color> colors = IntStream.range(0, 5)
			.mapToObj(e -> new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)))
			.collect(Collectors.toList());
	double r = Math.random();
	JLabel label;
	
	public Roulette() {
		setFrame("경품", 600, 550, () -> {});
	}
	
	@Override 
	protected void desing() {
		label = new JLabel() {
			double start = r;
			double end = 0d;
			List<Double> ds = new ArrayList<>();
			List<Double> de = new ArrayList<>();
			List<chanceitemEntity> angle = chanceitemEntity.findAll();
			@Override
			protected void paintComponent(Graphics g) {
				ds.clear();
				de.clear();
				arcs.clear();
				start = r;
				end = 0d;
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(Color.lightGray);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int w = getWidth(), h = getHeight();
				int sx = (w / 2) - (w / 3);
				int sy = (h / 3) - (h / 4);
				int ox = (w / 3) * 2;
				g2.fillOval(sx - 3, sy - 3, ox + 6, ox + 6);
				
				AffineTransform old = g2.getTransform(); // ✅ 저장
				angle.forEach(e -> {
					ds.add(start += end);
					de.add(end = -360.0 * e.chance);
				});
				for(int i = 0; i < angle.size(); i++) {
					double s = ds.get(i), e = de.get(i);
					Arc2D.Double arc = new Arc2D.Double(sx, sy, ox, ox, s, e, Arc2D.PIE);
					g2.setColor(colors.get(i));
					g2.fill(arc);
					arcs.add(arc);
				}
				g2.translate(sx + (ox / 2), sy + (ox / 2));
				g2.setFont(sp.font.deriveFont(17f).deriveFont(1));
				g2.rotate(Math.toRadians(-r));
				g2.setColor(Color.white);
				for(int i = 0; i < angle.size(); i++) {
					double s = ds.get(i), e = -de.get(i);
					g2.rotate(Math.toRadians(-e / -2));
					g2.drawString(angle.get(i).ciname, 50, (getFontMetrics(getFont()).getDescent() + getFontMetrics(getFont()).getAscent()) / 2);//
					g2.rotate(Math.toRadians(-e / -2));
				}
				g2.setTransform(old); // ✅ 복구 (이게 핵심)
				
				int[] x = {w / 2 - 15, w / 2, w / 2 + 15};
				int[] y = {sy - 10, sy + 20, sy - 10};
				g2.setColor(Color.red);
				g2.fillPolygon(x, y, 3);
				point.x = getWidth() / 2;
				point.y = sy + 20;
			}
		};
		label.setBackground(Color.white);
		label.setOpaque(true);
		label.setBorder(sp.line(Color.lightGray));
		add(set(col(0, f(label), fw(b)), BORDER(sp.em(10, 10, 10, 10))));
		
	}

	@Override
	protected void action() {

		b.addActionListener(ac -> {
			b.setEnabled(false);
			new Thread(() -> {
			    try {
			        double speed = 0.000000001;		// 처음 빠름 (sleep 짧음)
			        double max = (Math.random() * 3 + 1) / 100.0;	// 최대 느림 (sleep 길어짐)
			        System.out.println(max);
			        
			        while (speed < max) {
			            long millis = (long)(speed * 1000);
			            int nanos = (int)((speed * 1_000_000_000) % 1_000_000);

			            r--;
			            SwingUtilities.invokeLater(() -> label.repaint());
			            Thread.sleep(millis, nanos);
			            r--;
			            SwingUtilities.invokeLater(() -> label.repaint());
			            speed *= 1.01;
			        }
			        
			        SwingUtilities.invokeLater(() -> {
			        	int index = arcs.indexOf(arcs.stream().filter(e -> e.contains(point)).findFirst().get());
			        	if(index != -1) {
			        		chanceitemEntity c = chanceitemEntity.findById(index + 1).get();
			        		sp.infor("축하합니다!\n" + c.ciname + "에 당첨되셨습니다!");
			        		sp.user.point += Integer.parseInt(c.ciname.split(" ")[0].replace(",", ""));
			        		sp.user.chance--;
			        		sp.user.save();
			        		b.setText("경품 뽑기! (" + sp.user.chance + "회 남음)");
			        	}
			        });
			        b.setEnabled(true);
			    } catch (Exception e) {
			        e.printStackTrace();
			    }
			}).start();
		});
	}

	public static void main(String[] args) {
		Util.start(new Roulette());
	}
}
