package demo;

import static utils.BoxPanel.col;
import static utils.BoxPanel.f;
import static utils.BoxPanel.fw;
import static utils.Properties.BG;
import static utils.Properties.BORDER;
import static utils.Properties.FONT;
import static utils.Properties.bt;
import static utils.Properties.set;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JLabel;

import demo.PieChart.Pie;
import main.Util;
import orms.chanceitemEntity;
import utils.CFrame;
import utils.sp;

public class Roullet extends CFrame{
	double rand = Math.random() * 360;
	List<chanceitemEntity> items = chanceitemEntity.findAll();
	List<Color> color = IntStream.range(0, 5).mapToObj(e -> new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256))).collect(Collectors.toList());
	List<Arc2D.Double> arcs = Arrays.asList(null, null, null, null, null);
	JButton b = bt("경품 뽑기! (" + sp.user.chance + "회 남음)", FONT(sp.font.deriveFont(20f).deriveFont(1)), BG(Color.white));
	Point point = new Point();
	PieChart pcs;
	javax.swing.Timer timer;
	
	double speed = 10;
	
	public Roullet() {
		setFrame("경품", 600, 500, () -> {});
	}

	@Override
	protected void desing() {
		List<Pie> pies = new ArrayList<>();
		items.forEach(e -> {
			pies.add(new Pie(PieChart.generateRandomColor(), e.ciname, e.chance));
		});
		pcs = new PieChart(pies, 150, rand) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int sx = getWidth() / 2, sy = getHeight() / 2 - 150;
				int[] x = {sx - 12, sx, sx + 12}, y = {sy - 10, sy + 20, sy - 10};
				g.setColor(Color.red);
				g.fillPolygon(x, y, 3);
				point = new Point(x[1], y[1]);
			}
		};
		pcs.setBackground(Color.white);
		pcs.setOpaque(true);
		add(set(col(0, f(pcs), fw(b)), BORDER(sp.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		pcs.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(pcs.pies.get(0).arc.contains(e.getPoint()));
			}
		});
		
		b.addActionListener(ac -> {
			b.setEnabled(false);
			speed = (Math.random() * 20) + 30;
			timer = new javax.swing.Timer(1, e -> {
				speed *= 0.994;
				pcs.rand -= speed;
				pcs.pies.forEach(a -> {
					a.arc.setAngleStart(a.arc.getAngleStart() - speed);
				});
				pcs.repaint();
				if(speed < 0.1) {
					timer.stop();
					int n = -1;
					for(int i = 0; i < 5; i++)
						if(pcs.pies.get(i).arc.contains(point)) {
							n = i;
							break;
						}
					sp.infor("축하합니다!\n" + items.get(n).ciname + "에 당첨되셨습니다!");
					sp.user.chance -= 1;
					sp.user.point += Integer.parseInt(items.get(n).ciname.split(" ")[0].replace(",", ""));
					sp.user.save();
					b.setEnabled(true);
				}
			});
			timer.start();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Roullet());
	}
}
