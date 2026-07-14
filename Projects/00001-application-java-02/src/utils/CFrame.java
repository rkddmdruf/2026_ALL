package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class CFrame extends JFrame{
	protected void setFrame(String title, int w, int h, Runnable run) {
		setTitle(title);
		setSize(w, h);
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
