package test.test1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import main.Util;
import orms.couponEntity;
import orms.rewardEntity;
import utils.*;
import static utils.Properties.*;
import static utils.BoxPanel.*;

public class game extends CFrame {

	JLabel label;
	
	double theta = 0;
	List<rewardEntity> list = rewardEntity.findAll();
	List<Color> colors = Arrays.asList(new Color(255, 99, 132), new Color(54, 162, 235), new Color(255, 206, 86),
			new Color(75, 192, 192), new Color(153, 102, 255));
	CButton button = set(new CButton("룰렛 돌리기"), BG(colors.get(2)), FONT(sp.font.deriveFont(25f).deriveFont(1)));
	JLabel statusLabel = lb("START", FONT(sp.font.deriveFont(25f).deriveFont(1)), HOA(JLabel.CENTER));
	
	Map<Integer, Arc2D.Double> arcs = new LinkedHashMap<>();
	Point point = new Point();
	Timer t1, t2 = new Timer(5000, e -> {
		System.out.println("스톱");
		if(t1 != null) t1.stop();
		List<Integer> key = new ArrayList<>(arcs.keySet());
		for(int i = 0; i < key.size(); i++) {
			int k = key.get(i);
			System.out.println(arcs.get(key.get(i)).contains(point));
			if(arcs.get(k).contains(point)) {
				couponEntity c = new couponEntity();
				c.cpdate = LocalDate.now();
				c.uno = sp.user.uno;
				c.reno = list.get(i).reno;
				c.save();
				sp.infor((i + 1) + "등 축하합니다");
			}
		}
	});
	
	public game() {
		Collections.shuffle(list);
		list = list.subList(0, 5);
		list.sort((a, b) -> java.lang.Double.compare(b.resale, a.resale));
		
		setFramed("룰렛", 500, 500, () -> new sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj());
	}

	@Override
	protected void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				
				Graphics2D g2 = Util.ANTI(g);
				double r = getWidth() / 3.5;

				double start = theta;
				for (int i = 0; i < 5; i++) {
					double angle = -360.0 / 5;
					Arc2D.Double arc = new Arc2D.Double();
					arc.setArcByCenter(getWidth() / 2, getHeight() / 2, r, start, angle, Arc2D.PIE);
					g2.setColor(colors.get(i));
					start += angle;
					g2.fill(arc);
					arcs.put(i, arc);
				}
				
				start = theta;
				
				AffineTransform old = g2.getTransform();
				g2.setFont(sp.font.deriveFont(1).deriveFont(17f));
				g2.translate(getWidth() / 2, getHeight() / 2);
				FontMetrics fm = g2.getFontMetrics();
				g2.rotate(Math.toRadians(-theta));
				for(int i = 0; i < 5; i++) {
					double angle = 360.0 / 5;
					g2.setColor(Color.white);
					g2.rotate(Math.toRadians(angle / 2));
					g2.drawString((i + 1) + "등 " + (int) (list.get(i).resale * 100) + "%", 
							(int) ((r - fm.stringWidth((i + 1) + "등 " + (int) (list.get(i).resale * 100) + "%")) / 2 + 20), 
							-fm.getHeight() / 2 + fm.getAscent());
					g2.rotate(Math.toRadians(angle / 2));
					start += angle;
				}
				g2.rotate(0);
				g2.setTransform(old);
				
				int minR = 80;
				g2.setColor(Color.black);
				g2.fillOval((getWidth() - minR) / 2,  (getHeight() - minR) / 2, minR, minR);
				g2.setFont(g2.getFont().deriveFont(25f));
				g2.setColor(Color.white);
				fm = g2.getFontMetrics();
				g2.drawString("GO", (getWidth() - fm.stringWidth("GO")) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
				
				Polygon p = new Polygon();
				p.addPoint(getWidth() / 2, (int) (getHeight() / 2 - r + 10));
				p.addPoint(getWidth() / 2 - 15, (int) (getHeight() / 2 - r - 30));
				p.addPoint(getWidth() / 2 + 15, (int) (getHeight() / 2 - r - 30));
				
				point.x = getWidth() / 2; point.y = (int) (getHeight() / 2 - r + 10);
				
				g2.setColor(Color.orange);
				g2.fill(p);
				g2.setStroke(new BasicStroke(1.5f));
				g2.setColor(Color.black);
				g2.draw(p);
				
			}
		};
		add(col(0, 10, 30, f(label), button, fw(statusLabel)).setBackColor(Color.white));
	}

	double speed = Math.random() * 30 + 20;
	@Override
	protected void action() {
		button.addActionListener(e -> {
			if(!couponEntity.findBy(c -> c.cpdate.equals(LocalDate.now()) && c.uno.equals(sp.user.uno)).isEmpty()) {
				throw new RuntimeException("오늘은 이미 룰렛을 돌리셨습니다. 내일 돌아와주세요.");
			}
			statusLabel.setText("룰렛회전중");
			t1 = new Timer(1, t -> {
				theta -= speed;
				speed *= 0.985;
				repaint();
			});
			t1.start();
			t2.start();
			t2.setRepeats(false);
			revalidate();
			repaint();
		});
		
	}

	public static void main(String[] args) {
		Util.start(new game());
	}
}
