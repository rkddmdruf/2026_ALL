package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import utils.getter;

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
	
	public static Color setAlpha(Color color, int n) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), n);
	}
	
	public static void handle(Throwable t) {
		t.printStackTrace();
		getter.err(t.getMessage());
	}
}
