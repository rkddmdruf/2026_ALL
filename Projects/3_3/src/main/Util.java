package main;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import orms.userEntity;
import utils.sp;

public class Util {
	
	private static Map<Integer, List<Integer>> myFollow= new LinkedHashMap<>();
	private static Map<Integer, List<Integer>> myFollowing = new LinkedHashMap<>();
	static { followSetting(); }
	
	public static Map<Integer, List<Integer>> myFollow() {
		return Map.copyOf(myFollow);
	}
	public static Map<Integer, List<Integer>> myFollowing() {
		return Map.copyOf(myFollowing);
	}
	
	public static Image bImage() {
		Image img = new ImageIcon("datafiles/icons/plus.png").getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bf.getWidth(); y++) 
				for(int x = 0; x < bf.getWidth(); x++) 
					if((bf.getRGB(x, y) & 0xFFFFFF) > 0x222222) bf.setRGB(x, y, 0);
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	private static void followSetting() {
		userEntity.findAll().forEach(us -> {
			List<Integer> list = Arrays.stream(us.u_follow.split(",")).map(e -> Integer.parseInt(e)).collect(Collectors.toList());
			list.forEach(u -> myFollow.computeIfAbsent(u, k -> new ArrayList<>()).add(us.u_no));
			list.forEach(u -> myFollowing.computeIfAbsent(us.u_no, k -> new ArrayList<>()).add(u));
		});
	}
	
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
	
	private static void handle(Throwable t) {
		t.printStackTrace();
		JOptionPane.showMessageDialog(null, t.getMessage(), "경고", JOptionPane.ERROR_MESSAGE);
	}
}
