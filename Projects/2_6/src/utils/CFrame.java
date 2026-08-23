package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class CFrame extends JFrame{
	
	protected void setFrame(String title, int w, int  h, Runnable r) {
		setTitle(title);
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("datafiles/logo.png").getImage());
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				r.run();
			}
		});
		desing();
		action();
	}

	protected abstract void desing();
	protected abstract void action();
}
