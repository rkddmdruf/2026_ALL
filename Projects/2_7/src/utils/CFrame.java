package utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class CFrame extends JFrame{

	public void setFrame(String s, int w, int h, Runnable r) {
		setTitle(s);
		setSize(w + 16, h + 39);
		setLocationRelativeTo(null);
		setIconImage(new ImageIcon("datafiles/icon/1.png").getImage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) { 
				r.run(); 
			}
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
				System.out.println("fsdf");
			}
		});
		desing();
		action();
	}
	
	public abstract void desing();
	public abstract void action();
}
