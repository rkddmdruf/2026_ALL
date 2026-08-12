package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class CFrame extends JFrame{

	public void setFrame(String s, int w, int h) {
		setTitle(s);
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("datafiles/icon/1.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		desing();
		action();
		setVisible(true);
	}
	
	protected void setFramed(String s, int w, int h, Runnable r) {
		setFrame(s, w, h);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				r.run();
			}
		});
	}
	
	protected void setFrames(String s, int w, int h, Runnable r) {
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
