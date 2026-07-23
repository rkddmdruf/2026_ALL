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
import javax.swing.SwingUtilities;

public class PieChart2 extends JComponent {
	
	class Pie {
		private Color color;
		private String label;
		private int value;
		private Arc2D.Double arc;
		
		public Pie(Color color, String label, int value) {
			this.color = color;
			this.label = label;
			this.value = value;
		}
	
		public void generateArc(int reduce, int total) {
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
			AffineTransform o = g2.getTransform();
			g2.translate(w / 2, h / 2);
			g2.fill(arc);
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(1.5f));
			g2.draw(arc);
			g2.setTransform(o);
		}
	}
	
	public List<Pie> pies = new ArrayList<>();
	int w = 0, h = 0;
	public PieChart2(int size) {
		int total = pies.stream().mapToInt(pie -> pie.value).sum();
		this.pies.stream().reduce(new Pie(null, null, 0), (a, b) -> {
			b.generateArc(a.value, total);
			return new Pie(null, null, a.value - b.value);
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		w = getWidth(); h = getHeight();
		pies.forEach(pie -> pie.paint(g2));
	}
	
	public static Color generateRandomColor() {
		return new Color(new Random().nextInt(0x1000000));
	}
	
	public void addPie(Color color, String label, int value) {
		Pie p = new Pie(color, label, value);
		pies.add(p);
		int total = pies.stream().mapToInt(pie -> pie.value).sum();
		this.pies.stream().reduce(new Pie(null, null, 0), (a, b) -> {
			b.generateArc(a.value, total);
			return new Pie(null, null, a.value - b.value);
		});
		repaint();
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame();
		PieChart2 chart = new PieChart2(300);
		chart.addPie(generateRandomColor(), "1", 10);
		chart.addPie(generateRandomColor(), "2", 10);
		chart.addPie(generateRandomColor(), "3", 6);
		chart.addPie(generateRandomColor(), "4", 20);
		chart.addPie(generateRandomColor(), "5", 10);
		chart.addPie(generateRandomColor(), "6", 2);
		chart.addPie(generateRandomColor(), "7", 5);
		chart.addPie(generateRandomColor(), "8", 3);
		
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
