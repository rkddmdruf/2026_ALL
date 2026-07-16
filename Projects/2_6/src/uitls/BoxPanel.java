package uitls;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class BoxPanel extends JPanel{
	public static BoxPanel col(int s, int n, int e, JComponent...comps) { return new BoxPanel(C, s, n, e, comps); }
	public static BoxPanel col(int n, JComponent...comps) { return col(0, n, 0, comps); }
	public static BoxPanel row(int s, int n, int e, JComponent...comps) { return new BoxPanel(R, s, n, e, comps); }
	public static BoxPanel row(int n, JComponent...comps) { return row(0, n, 0, comps); }
	public static BoxPanel colF(int n, JComponent...comps) { for(var c : comps) f(c); return col(0, n, 0, comps); };
	public static BoxPanel rowF(int n, JComponent...comps) { for(var c : comps) f(c); return row(0, n, 0, comps); };
	
	public static int R = BoxLayout.X_AXIS, C = BoxLayout.Y_AXIS;
	
	public BoxPanel setBackColor(Color color) {
		this.setBackground(color);
		return this;
	}
	
	public BoxPanel opf() {
		this.setOpaque(false);
		return this;
	}
	public static <T extends JComponent> T fw(T t) {
		t.setMaximumSize(new Dimension(Integer.MAX_VALUE, t.getPreferredSize().width));
		return t;
	}
	public static <T extends JComponent> T fh(T t) {
		t.setMaximumSize(new Dimension(t.getPreferredSize().height, Integer.MAX_VALUE));
		return t;
	}
	public static <T extends JComponent> T f(T t) {
		t.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		return t;
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
    public static JComponent hg(int v) {
        return (JComponent) Box.createHorizontalStrut(v);
    }
    
    public BoxPanel(int alignment, int start, int inner, int end, JComponent... comps) {
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

}
