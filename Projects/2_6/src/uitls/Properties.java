package uitls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Arrays;
import java.util.function.Supplier;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public final class Properties {

	public static final class Property {

		private final String methodName;
		private final Class<?> paramType;
		private final Object value;

		public Property(String methodName, Class<?> paramType, Object value) {
			this.methodName = methodName;
			this.paramType = paramType;
			this.value = value;
		}

		public void apply(Object target) {
			try {
				target.getClass().getMethod(methodName, paramType).invoke(target, value);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static <T> T comp(Supplier<T> factory, Property... properties) {
		return set(factory.get(), properties);
	}

	public static <T> T set(T component, Property... properties) {
		Arrays.stream(properties).forEach(p -> p.apply(component));
		return component;
	}

	public static Property TEXT(String text) {
		return new Property("setText", String.class, text);
	}

	public static Property BG(Color color) {
		return new Property("setBackground", Color.class, color);
	}

	public static Property FG(Color color) {
		return new Property("setForeground", Color.class, color);
	}

	public static Property BORDER(Border border) {
		return new Property("setBorder", Border.class, border);
	}

	public static Property FONT(Font font) {
		return new Property("setFont", Font.class, font);
	}

	public static Property ICON(Icon icon) {
		return new Property("setIcon", Icon.class, icon);
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

	public static JLabel lb(String text, Property... ps) {
		return set(new JLabel(text), ps);
	}

	public static JButton bt(String text, Property... ps) {
		return set(new JButton(text), ps);
	}

	public static JTextField tf(String name, Property... ps) {
		return set(comp(JTextField::new, NAME(name)), ps);
	}

	public static JComboBox<String> cb(String items, Property... ps) {
		return set(new JComboBox<>(items.split(",")), ps);
	}
}
