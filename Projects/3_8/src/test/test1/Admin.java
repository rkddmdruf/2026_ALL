package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;

import orms.*;

public class Admin extends CFrame {
	public Admin() {
		setFramed("관리자", 800, 500, () -> new Login());
	}

	protected void desing() {
	}

	protected void action() {
	}
	
	public static void main(String[] args) {
		Util.start(new Admin());
	}
}