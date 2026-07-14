package utils;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class BoxUI {

    public static JLabel L(String text, int width) {
        var l = new JLabel(text);
        var pref = l.getPreferredSize();
        l.setPreferredSize(new Dimension(Math.max(width, pref.width), pref.height));
        return l;
    }

    public static JComponent VGAP(int h) {
        return (JComponent) Box.createVerticalStrut(h);
    }

    public static JComponent VGAP() {
        return (JComponent) Box.createVerticalGlue();
    }

    public static JComponent HGAP() {
        return (JComponent) Box.createHorizontalGlue();
    }

    public static <T extends JComponent> T fillWidth(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, c.getPreferredSize().height));
        return c;
    }

    public static <T extends JComponent> T fillHeight(T c) {
        c.setMaximumSize(new Dimension(c.getPreferredSize().width, Integer.MAX_VALUE));
        return c;
    }

    public static <T extends JComponent> T fill(T c) {
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return c;
    }

    public static int ROW = 0;
    public static int COL = 1;

    public static JPanel box(int alignment, int start, int inner, int end, JComponent... comps) {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, alignment));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(alignment == ROW ? Box.createHorizontalStrut(start) : Box.createVerticalStrut(start));
        for (int i = 0; i < comps.length; ++i) {
            var c = comps[i];
            c.setAlignmentX(Component.CENTER_ALIGNMENT);
            if (!(c instanceof JPanel) && !(c instanceof Box.Filler) && !c.isMaximumSizeSet()) c.setMaximumSize(c.getPreferredSize());
            panel.add(c);
            if (i < comps.length - 1) panel.add(alignment == ROW ? Box.createHorizontalStrut(inner) : Box.createVerticalStrut(inner));
        }
        panel.add(alignment == ROW ? Box.createHorizontalStrut(end) : Box.createVerticalStrut(end));
        if (alignment == ROW) panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, panel.getPreferredSize().height));
        return panel;
    }

}
