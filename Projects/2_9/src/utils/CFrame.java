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
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				run.run();
			}
		});
		desing();
		action();
	}
	
	protected abstract void desing();
	protected abstract void action();
}
