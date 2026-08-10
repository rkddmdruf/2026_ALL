package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class CFrame extends JFrame{
	
	public void setFrame(String s, int w, int h) {
		setIconImage(sp.getImage("logo/logo.png", 100, 100).getImage());
		setSize(w + 16, h + 39);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle(s);
		
		desing();
		action();
	}
	
	public void setFramed(String s, int w, int h, Runnable r) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			}
		});
		setFrame(s, w, h);
	}
	public void setFrames(String s, int w, int h, Runnable r) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				r.run();
			}
		});
		setFrame(s, w, h);
	}
	
	protected abstract void desing();
	protected abstract void action();

}
