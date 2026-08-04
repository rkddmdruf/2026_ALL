package utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CButton extends JButton{
	
	public int r = 20;
	{
		this.setOpaque(false);
		this.setBorderPainted(false);
		this.setContentAreaFilled(false);
		this.setFocusable(false);
	}
	
	public CButton(String s) { this.setText(s); }
	public CButton(String s, ImageIcon icon) { this.setText(s); this.setIcon(icon); }
	public CButton(ImageIcon icon) { this.setIcon(icon); }
	public CButton() {}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2.setColor(getBackground());
		g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, r, r);
		
		super.paintComponent(g);
	}
}
