package main;

import java.util.Arrays;

import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Util {

	public static void validateBlank(String message, JTextField...fields) {
		Arrays.stream(fields).forEach(field -> {
			if (field.getText().isBlank()) throw new RuntimeException(message);
		});
	}
	
}
