package utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoxPanel extends JPanel{
	
	public static final BoxPanel col(int inner, JComponent...cs) { return new BoxPanel(BoxPanel.C, 0, inner, 0, cs); }
	public static final BoxPanel row(int inner, JComponent...cs) { return new BoxPanel(BoxPanel.R, 0, inner, 0, cs); }
	public static final BoxPanel col(int s, int inner, int e, JComponent...cs) { return new BoxPanel(BoxPanel.C, s, inner, e, cs); }
	public static final BoxPanel row(int s, int inner, int e, JComponent...cs) { return new BoxPanel(BoxPanel.R, s, inner, e, cs); }
	public static final BoxPanel colF(int inner, JComponent...cs) { for(var c : cs) f(c); return col(inner, cs); }
	public static final BoxPanel rowF(int inner, JComponent...cs) { for(var c : cs) f(c); return row(inner, cs); }
	
	public static int R = BoxLayout.X_AXIS; // x 0, x 1, x 2 옆으로 이동
	public static int C = BoxLayout.Y_AXIS;
	
	int alignment;
	int inner;

	public BoxPanel setBackColor(Color color) {
		this.setBackground(color);
		return this;
	}
    public static JLabel L(String text, int width) {
        var l = new JLabel(text);
        var pref = l.getPreferredSize();
        l.setPreferredSize(new Dimension(Math.max(width, pref.width), pref.height));
        return l;
    }

    public static JComponent vg(int h) {
        return (JComponent) Box.createVerticalStrut(h);
    }

    public static JComponent vg() {
        return (JComponent) Box.createVerticalGlue();
    }

    public static JComponent hg() {
        return (JComponent) Box.createHorizontalGlue();
    }
    
    public static JComponent hg(int h) {
    	return (JComponent) Box.createHorizontalStrut(h);
    }

    public static <T extends JComponent> T fw(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return c;
    }

    public static <T extends JComponent> T fh(T c) {
        c.setMaximumSize(new Dimension(c.getPreferredSize().width, Integer.MAX_VALUE));
        return c;
    }

    public static <T extends JComponent> T f(T c) {
    	if (c == null) return null; // 🔥
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return c;
    }

    public BoxPanel(int alignment, int start, int inner, int end, JComponent... comps) {
        this.alignment = alignment;
        this.inner = inner;
        this.setLayout(new BoxLayout(this, alignment));
        this.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(alignment == R ? Box.createHorizontalStrut(start) : Box.createVerticalStrut(start));
        for (int i = 0; i < comps.length; ++i) {
            var c = comps[i];
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!(c instanceof JPanel) && !(c instanceof Box.Filler) && !c.isMaximumSizeSet()) c.setMaximumSize(c.getPreferredSize());
            this.add(c);
            if (i < comps.length - 1) this.add(alignment == R ? Box.createHorizontalStrut(inner) : Box.createVerticalStrut(inner));
        }
        this.add(alignment == R ? Box.createHorizontalStrut(end) : Box.createVerticalStrut(end));
        if (alignment == R) this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));
    }
    
    public BoxPanel addz(JComponent c) {
    	this.add(c);
    	if(inner != 0)
    		this.add(alignment == R ? Box.createHorizontalStrut(inner) : Box.createVerticalStrut(inner));
    	return this;
    }
}
