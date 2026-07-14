package main;

import java.awt.AWTEvent;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import utils.getter;

public class Util {

	public static void textIsBlank(JTextField...fields) {
		for(JTextField tf : fields) {
			if(tf.getText().isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
		}
	}
	
	public static void main(String[] args) {
		
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
	private static void handle(Throwable throwable) {
		throwable.printStackTrace();
		getter.err(throwable.getMessage());
	}
}
