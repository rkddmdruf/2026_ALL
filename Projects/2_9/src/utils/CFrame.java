package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class CFrame extends JFrame{
	
	protected void setFrame(String title, int w, int h, Runnable run) {
		setTitle(title);
		setSize(w + 16, h + 39);
		setIconImage(new ImageIcon("datafiles/logo/logo.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.white);
		setLocationRelativeTo(null);
		desing();
		action();
	}
	
	protected void setFrame(String title, int w, int h) {
		setTitle(title);
		setSize(w + 16, h + 39);
		setIconImage(new ImageIcon("datafiles/logo/logo.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.white);
		setLocationRelativeTo(null);
		desing();
		action();
	}
	
	protected void setFramed(String title, int w, int h, Runnable run) {
		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				run.run();
				System.out.println("sdf");
			}
		});
		setFrame(title, w, h);
	}
	
	protected void setFrameg(String title, int w, int h, Runnable run) {
		setFrame(title, w, h);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
			}
		});
	}
	
	protected abstract void desing();
	protected abstract void action();
}
