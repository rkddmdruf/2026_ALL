package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

import utils.Properties.Property;

public class Properties {

	public static class Property{
		String methodName;
		Object value;
		Class<?> c;
		
		public Property(String name, Object value, Class<?> c) {
			methodName = name;
			this.value = value;
			this.c = c;
		}
		public Property(String name, Class<?> c, Object value) {
			methodName = name;
			this.value = value;
			this.c = c;
		}
		
		public void apply(Object target) {
			try {
				target.getClass().getMethod(methodName, c).invoke(target, value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static <T extends JComponent> T comp(Supplier<T> c, Property...ps) {
		return set(c.get(), ps);
	}
	
	public static <T extends JComponent> T set(T c, Property...ps) {
		Arrays.asList(ps).forEach(p -> p.apply(c));
		return c;
	}
	
	public static Property FONT(Font o) {
		return new Property("setFont", o, Font.class);
	}
	public static Property TEXT(String o) {
		return new Property("setText", o, String.class);
	}
	
	public static Property BG(Color o) {
		return new Property("setBackground", o, Color.class);
	}
	
	public static Property FG(Color o) {
		return new Property("setForeground", o, Color.class);
	}
	public static Property BORDER(Border o) {
		return new Property("setBorder", o, Border.class);
	}
	public static Property SIZE(int width, int height) {
		return new Property("setPreferredSize", Dimension.class, new Dimension(width, height));
	}

	public static Property NAME(String name) {
		return new Property("setName", String.class, name);
	}

	public static Property HOA(int n) {
		return new Property("setHorizontalAlignment", int.class, n);
	}

	public static Property VEA(int n) {
		return new Property("setVerticalAlignment", int.class, n);
	}

	public static JLabel lb(String text, Property...val) {
		return set(new JLabel(text), val);
	}
	
	public static JButton bt(String text, Property...val) {
		return set(new JButton(text), val);
	}
}
