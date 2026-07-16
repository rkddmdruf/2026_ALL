package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uitls.getter;

public class Util {
	public static Image logo;
	static { setImage(); }
	private static void setImage() {
		Image img = new ImageIcon("datafiles/logo.png").getImage();
		try {
			BufferedImage bfi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bfi.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bfi.getHeight(); y++) 
				for(int x = 0; x < bfi.getWidth(); x++) {
					int rgb = bfi.getRGB(x, y);//ABCDEF
					if((rgb & 0xFFFFFF) >= 0xCCCCCC) bfi.setRGB(x, y, 0x000000);
				}
			logo = bfi;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void start(JFrame f) {
		SwingUtilities.invokeLater(() -> f.setVisible(true));
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
			@Override
			protected void dispatchEvent(AWTEvent event) {
				try {
					super.dispatchEvent(event);
				} catch (Exception e) {
					handle(e);
				}
			}
		});
	}
	
	public static Color setAlpha(Color color, int n) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
	}
	private static void handle(Throwable t) {
		t.printStackTrace();
		getter.err(t.getMessage());
	}
}
