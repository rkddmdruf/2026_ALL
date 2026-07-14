package main;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;

public class test extends JFrame {
	LocalTime time = LocalTime.now();
    public test() {
    	new Thread(() -> {
    		while(true) {
    			int s = (time = LocalTime.now()).getSecond();
    			String[] str = String.valueOf(s).split("");
    			Arrays.asList(str).forEach(System.out::print);
    			System.out.println();
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	}).start();;
    }

    public static void main(String[] args) {
    	new test();
    }
}