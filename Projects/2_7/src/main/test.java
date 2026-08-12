package main;

import java.time.LocalDate;

public class test {

	public static void main(String[] args) {
		System.out.println(!LocalDate.now().isAfter(LocalDate.of(2026, 8, 12)));
	}
}
