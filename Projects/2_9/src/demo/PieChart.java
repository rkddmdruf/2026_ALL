package demo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class PieChart extends JComponent {
	
	static class Pie {
		
		private Color color;
		private String label;
		private double value;
		private Arc2D.Double arc;
		
		public Pie(Color color, String label, double value) {
			this.color = color;
			this.label = label;
			this.value = value;
		}
	
		public void generateArc(double reduce, double total) {
			arc = new Arc2D.Double();
			arc.setAngleStart(reduce * 360.0 / total + 90);
			arc.setAngleExtent(-(value * 360.0 / total));
			setRadius(100);
		}
		
		public void setRadius(int r) {
			arc.setArcByCenter(0, 0, r, arc.getAngleStart(), arc.getAngleExtent(), Arc2D.PIE);
		}
		
		public void paint(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(arc);
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(1.5f));
			g2.draw(arc);
		}
	}
	
	class Pie2 extends Pie {

		public Pie2(Color color, String label, int value) {
			super(color, label, value);
		}

		@Override
		public void paint(Graphics2D g2) {
			super.paint(g2);
			AffineTransform old = g2.getTransform();
			
			FontMetrics fm = g2.getFontMetrics();
			g2.translate(super.arc.getCenterX(), super.arc.getCenterY());
		    g2.rotate(Math.toRadians(-(super.arc.getAngleExtent() / 2 + super.arc.getAngleStart())));
			g2.drawString("10 포인트", 20, 5);
		    g2.setTransform(old);
		}
	}
	
	public List<Pie> pies = new ArrayList<>();
	
	public PieChart(List<Pie> pies, int size) {
		this.pies = pies;
		double total = pies.stream().mapToDouble(pie -> pie.value).sum();
		this.pies.stream().reduce(new Pie(null, null, 0), (a, b) -> {
			b.generateArc(a.value, total);
			return new Pie(null, null, a.value - b.value);
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		pies.forEach(pie -> pie.paint(g2));
	}
	
	public static Color generateRandomColor() {
//		Random rand = new Random();
//		Color c = new Color(rand.nextInt(156) + 100, rand.nextInt(156) + 100, rand.nextInt(156) + 100);
		
		int[] a = {
				0x0000000,
				0x0000000,
				0x0000000,
				0x0000000,
				0x0000000,
				0x0000000,
				0x0000000,
				0x0000000,
		};
		
		//for (int i = 0; i < 8; ++i) if ((a[i] & (1 << i)) > 0) comp[i].on();
		
		return new Color(new Random().nextInt(0x1000000));
		

	}
	
	public static void main(String[] args) {
		
		List<Pie> data = new ArrayList<>();
		data.add(new Pie(generateRandomColor(), "1", 10));
		data.add(new Pie(generateRandomColor(), "2", 10));
		data.add(new Pie(generateRandomColor(), "3", 6));
		data.add(new Pie(generateRandomColor(), "4", 20));
		data.add(new Pie(generateRandomColor(), "5", 10));
		data.add(new Pie(generateRandomColor(), "6", 2));
		data.add(new Pie(generateRandomColor(), "7", 5));
		data.add(new Pie(generateRandomColor(), "8", 3));
		
		JFrame frame = new JFrame();
		PieChart chart = new PieChart(data, 300);
		chart.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				chart.pies.forEach(pie -> pie.setRadius(pie.arc.contains(e.getPoint()) ? 120 : 100));
				frame.repaint();
			}
		});
		
		frame.add(chart);
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
