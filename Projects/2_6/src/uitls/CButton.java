package uitls;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class CButton extends JButton{
	{
		setContentAreaFilled(false);
		setOpaque(false);
		setBorderPainted(false);
	}
	
	public CButton(String s){ this.setText(s); }
	public CButton(String s, ImageIcon icon){ this.setText(s); this.setIcon(icon); }
	public CButton(){ }
	
	public int arc = 30;

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isArmed())  g2.setColor(getter.color.brighter()); // 클릭 시
        else g2.setColor(getter.color); // 기본색
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
        super.paintComponent(g); // 👉 글씨 자동
    }

    @Override
    protected void paintBorder(Graphics g) {
    	g.setColor(getter.color);
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
    }

}
