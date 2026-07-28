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
		public Arc2D.Double arc;
		
		public Pie(Color color, String label, double value) {
			this.color = color;
			this.label = label;
			this.value = value;
		}
	
		public void generateArc(double reduce, double total, int size) {
			arc = new Arc2D.Double();
			arc.setAngleStart(reduce * 360.0 / total);
			arc.setAngleExtent(-(value * 360.0 / total));
			setRadius(size);
		}
		
		public void setRadius(int r) {
			arc.setArcByCenter(0, 0, r, arc.getAngleStart(), arc.getAngleExtent(), Arc2D.PIE);
		}
		
		public void paint(Graphics2D g2, int w, int h) {
			g2.setColor(color);
			arc.setArcByCenter(w / 2, h / 2, arc.getWidth() / 2, arc.getAngleStart(), arc.getAngleExtent(), Arc2D.PIE);
			g2.fill(arc);
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(1.5f));
			g2.draw(arc);
		}
		
		public void paintLabel(Graphics2D g2) {
			g2.rotate(Math.toRadians((360.0 * value) / 2));
			g2.drawString(label, 50, (g2.getFontMetrics(g2.getFont()).getHeight()) / 2);
			g2.rotate(Math.toRadians((360.0 * value) / 2));
		}
	}
	
	public List<Pie> pies = new ArrayList<>();
	public double rand;
	public PieChart(List<Pie> pies, int size, double rand) {
		this.pies = pies;
		this.rand = rand;
		double total = pies.stream().mapToDouble(pie -> pie.value).sum();
		double randValue = rand / 360.0 * total;
		this.pies.stream().reduce(new Pie(null, null, randValue), (a, b) -> {
			b.generateArc(a.value, total, size);
			return new Pie(null, null, a.value - b.value);
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform old = g2.getTransform();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.white);
		g2.fillRect(0, 0, getWidth(), getHeight());
		g2.setColor(Color.gray);
		g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		pies.forEach(pie -> pie.paint(g2, getWidth(), getHeight()));
		g2.translate(getWidth() / 2, getHeight() / 2);
		g2.rotate(Math.toRadians(-rand));
		g2.setColor(Color.white);
		pies.forEach(pie -> pie.paintLabel(g2));
		g2.rotate(0);
		g2.setTransform(old);
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
}
