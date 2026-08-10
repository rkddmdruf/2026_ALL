package utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.border.Border;

import utils.Properties.Property;

public class Properties {
	
	public static class Property{
		String name;
		Class<?> clazz;
		Object value;
		
		public Property(String s, Class<?> c, Object v) {
			name = s; clazz = c; value = v;
		}
		
		public void apply(Object target) {
			try {
				target.getClass().getMethod(name, clazz).invoke(target, value);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			}
		}
	}
	
	public static <T> T comp(Supplier<T> obj, Property...ps){
		return set(obj.get(), ps);
	}
	
	public static <T> T set(T c, Property...ps) {
		Arrays.stream(ps).forEach(p -> p.apply(c));
		return c;
	}
	
	
	public static Property BORDER(Border b) {
		return new Property("setBorder", Border.class, b);
	}
	public static Property FONT(Font font) {
		return new Property("setFont", Font.class, font);
	}
	
	public static Property BG(Color color) {
		return new Property("setBackground", Color.class, color);
	}
	
	public static Property FG(Color c) {
		return new Property("setForeground", Color.class, c);
	}
	
	public static Property HOA(int i) {
		return new Property("setHorizontalAlignment", Integer.class, i);
	}
	
	public static Property VEA(int i) {
		return new Property("setVerticalAlignment", Integer.class, i);
	}
	
	public static Property NAME(String s) {
		return new Property("setName", String.class, s);
	}
	
	public static Property TEXT(String s) {
		return new Property("setText", String.class, s);
	}
	
	public static Property SIZE(int w, int h) {
		return new Property("setPreferredSize", Dimension.class, new Dimension(w, h));
	}
	
	public static JLabel lb(String s, Property...ps) {
		return set(new JLabel(s), ps);
	}
	
	public static JButton bt(String s, Property...ps) {
		return set(new JButton(s), ps);
	}
	
	public static JComboBox<String> cb(String[] s, Property...ps){
		return set(new JComboBox<>(s), ps);
	}
}
