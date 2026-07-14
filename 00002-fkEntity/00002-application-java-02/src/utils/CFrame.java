package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

public abstract class CFrame extends JFrame{
	static {UIManager.put("Label.font", getter.font);}
	protected JPanel borderPanel = new JPanel(new BorderLayout());
	private void setFrame(String string, int w, int h) {
		setForeground(getBackground());
		setTitle(string);
		add(borderPanel);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(new Dimension(w + 16, h + 39));
		setLocationRelativeTo(null);
        addComponentListener(new ComponentAdapter() {
        	@Override
        	public void componentResized(ComponentEvent e) {
        		System.out.println("x : " + getWidth() + ", y : " + getHeight());
        	}
		});
		design();
		action();
	}
	
	public void setFrame(String string, int w, int h, WindowAdapter e) {
		if(e != null)
			addWindowListener(e);
		setFrame(string, w, h);
	}

	protected abstract void design();
	protected abstract void action();
}
