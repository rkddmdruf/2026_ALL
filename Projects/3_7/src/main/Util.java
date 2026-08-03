package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import utils.sp;

public class Util {

	public static void start(JFrame f) {
		SwingUtilities.invokeLater(() -> f.setVisible(true));
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent event) {
				try {
					super.dispatchEvent(event);
				} catch (Exception e2) {
					handle(e2);
				}
			}
		});
	}
	
	public static Graphics2D ANTI(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		return g2;
	}
	public static Image getSearch() {
		Image img = new ImageIcon("datafiles/search.png").getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bf.getHeight(); y++)
				for(int x = 0; x < bf.getWidth(); x++)
					if((bf.getRGB(x, y) & 0xFFFFFF) != 0) {
						bf.setRGB(x, y, 0xFFFFFFFF);
					}else {
						bf.setRGB(x, y, 0x00000000);
					}
			
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	public static void roundComp(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
	}
	
	private static void handle(Throwable t) {
		t.printStackTrace();
		sp.err(t.getMessage());
	}
}
