package utils;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class CFrame extends JFrame{
	
	public void setFrame(String s, int w, int h) {
		setTitle(s);
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("datafiles/logo.png").getImage());
		setVisible(true);
		desing();
		action();
	}

	public void setFramed(String s, int w, int h, Runnable r) {
		setFrame(s, w, h);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			}
		});
	}
	
	public void setFrameg(String s, int w, int h, Runnable r) {
		setFrame(s, w, h);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				r.run();
			}
		});
	}
	
	protected abstract void desing();
	protected abstract void action();
}
