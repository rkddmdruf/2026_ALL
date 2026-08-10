package utils;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import static utils.Properties.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
public class BoxPanel extends JPanel{
	
	public static BoxPanel col(int inner, JComponent...cs) {return  new BoxPanel(C, 0, inner, 0, cs); };
	public static BoxPanel row(int inner, JComponent...cs) {return  new BoxPanel(R, 0, inner, 0, cs); };
	public static BoxPanel col(int s, int inner, int e, JComponent...cs) {return  new BoxPanel(C, s, inner, e, cs); };
	public static BoxPanel row(int s, int inner, int e, JComponent...cs) {return  new BoxPanel(R, s, inner, e, cs); };
	public BoxPanel setBackColor(Color color) {
		this.setBackground(color);
		return this;
	}
	
	public static JComponent hg() {
		return (JComponent) Box.createHorizontalGlue();
	}
	public static JComponent hg(int n) {
		return (JComponent) Box.createHorizontalStrut(n);
	}
	public static JComponent vg() {
		return (JComponent) Box.createVerticalGlue();
	}
	public static JComponent vg(int n) {
		return (JComponent) Box.createVerticalStrut(n);
	}
	
	int aligen, inner;
	
	public static int R = BoxLayout.X_AXIS;
	public static int C = BoxLayout.Y_AXIS;
	
	public static <T extends JComponent> T fh(T c) {
		c.setMaximumSize(new Dimension(c.getPreferredSize().width, Integer.MAX_VALUE));
		return c;
	}
	public static <T extends JComponent> T fw(T c) {
		c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
		return c;
	}
	public static <T extends JComponent> T f(T c) {
		c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		return c;
	}
	
	public BoxPanel(int aligen, int start, int inner, int end, JComponent... comps) {
		this.aligen = aligen;
		this.inner = inner;
		setLayout(new BoxLayout(this, aligen));
		setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(aligen == R ? hg(start) : vg(start));
		for(int i = 0; i < comps.length; i++) {
			var c = comps[i];
			c.setAlignmentX(Component.CENTER_ALIGNMENT);
			if (!(c instanceof JPanel) && !(c instanceof Box.Filler) && !c.isMaximumSizeSet()) c.setMaximumSize(c.getPreferredSize());
			this.add(c);
			if (i < comps.length - 1) this.add(aligen == R ? Box.createHorizontalStrut(inner) : Box.createVerticalStrut(inner));
		}
		this.add(aligen == R ? hg(end) : vg(end));
		if (aligen == R) this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getPreferredSize().height));
	}
	
}
