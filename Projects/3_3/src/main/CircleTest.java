package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import utils.CFrame;
import utils.sp;

import static utils.BoxPanel.*;
import static utils.Properties.*;

public class CircleTest extends CFrame{

	public CircleTest() {
		setFrame("동그라미 테스트", 500, 200);
	}

	@Override
	protected void desing() {
		File dir = new File("datafiles/profile");
		File[] found = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".jpg"));
		List<File> files = new ArrayList<>(found == null ? List.of() : List.of(found));
		Collections.shuffle(files);

		List<JComponent> circles = new ArrayList<>();
		circles.add(myStory());
		for (File f : files.subList(0, Math.min(10, files.size())))
			circles.add(new JLabel(sp.circleImage(f.getPath(), 80)));

		JPanel rowPanel = row(15, circles.toArray(new JComponent[0]));
		rowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		JScrollPane sc = new JScrollPane(rowPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sc.setBorder(null);

		Point[] origin = { null };
		rowPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				origin[0] = e.getPoint();
				rowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				origin[0] = null;
				rowPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
		});
		rowPanel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (origin[0] == null) return;
				JScrollBar bar = sc.getHorizontalScrollBar();
				bar.setValue(bar.getValue() + origin[0].x - e.getX());
				origin[0] = e.getPoint();
			}
		});

		add(sc);
	}

	private static Image tinted(String path, int size, Color color) {
		try {
			BufferedImage src = ImageIO.read(new File(path));
			BufferedImage out = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = out.createGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(src, 0, 0, size, size, null);
			g.setComposite(AlphaComposite.SrcIn);
			g.setColor(color);
			g.fillRect(0, 0, size, size);
			g.dispose();
			return out;
		} catch (Exception e) {
			return null;
		}
	}

	private JPanel myStory() {
		int size = 80;
		int plus = size / 3;
		Image plusIcon = tinted("datafiles/icons/plus.png", plus, sp.color);
		JLabel circle = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(Color.white);
				g2.fillOval(0, 0, size - 1, size - 1);
				g2.setColor(sp.color);
				g2.setStroke(new BasicStroke(2f));
				g2.drawOval(1, 1, size - 3, size - 3);

				int offset = (size - plus) / 2;
				g2.drawImage(plusIcon, offset, offset, plus, plus, null);
				g2.dispose();
			}
		};
		set(circle, SIZE(size, size));
		circle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		circle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sp.info("스토리 추가 폼으로 이동합니다.");
				// TODO: new StoryAdd(); dispose();
			}
		});

		JLabel label = lb("내 스토리", FONT(sp.font), HOA(JLabel.CENTER));
		return col(5, circle, label);
	}

	@Override
	protected void action() {
	}

	public static void main(String[] args) {
		Util.start(new CircleTest());
	}
}
