package demo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import main.Util;
import utils.CFrame;

public class test extends CFrame{
	{
		setFrame("테스트", 800, 800, () -> {});
	}
	public static void main(String[] args) {
		Util.start(new test());
	}

	static void fill(BufferedImage img, int x, int y, int oldColor, int newColor) {
	    if (x < 0 || y < 0 ||
	        x >= img.getWidth() || y >= img.getHeight() ||
	        img.getRGB(x, y) != oldColor) return;

	    img.setRGB(x, y, newColor);

	    fill(img, x + 1, y, oldColor, newColor);
	    fill(img, x - 1, y, oldColor, newColor);
	    fill(img, x, y + 1, oldColor, newColor);
	    fill(img, x, y - 1, oldColor, newColor);
	}
	
	@Override
	protected void desing() {
		add(new JLabel(new ImageIcon(get())));
	}
	
	static void fill(BufferedImage img, int sx, int sy, int newColor) {
	    int oldColor = img.getRGB(sx, sy);
	    if (oldColor == newColor) return;

	    ArrayDeque<Point> stack = new ArrayDeque<>();
	    stack.push(new Point(sx, sy));

	    while (!stack.isEmpty()) {
	        Point p = stack.pop();
	        int x = p.x;
	        int y = p.y;

	        if (x < 0 || y < 0 ||
	            x >= img.getWidth() || y >= img.getHeight() ||
	            img.getRGB(x, y) != oldColor) {
	            continue;
	        }

	        img.setRGB(x, y, newColor);

	        stack.push(new Point(x + 1, y));
	        stack.push(new Point(x - 1, y));
	        stack.push(new Point(x, y + 1));
	        stack.push(new Point(x, y - 1));
	    }
	}
	
	public Image get() {
		Image img = new ImageIcon(new ImageIcon("datafiles/map.png").getImage().getScaledInstance(800, 800, 4)).getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			int oldColor = bf.getRGB(232, 420);
			fill(bf, 232, 420, oldColor, Color.YELLOW.getRGB());
			
			img = bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}

}
