package main;

import java.awt.CardLayout;

import javax.swing.*;

import utils.*;
import static utils.CFrame.*;

public class Main extends CFrame{
	
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	public Main() {
		setFrame("메인", 800, 550);
		
	}

	@Override
	protected void desing() {
		JLabel l = new JLabel();
	}

	@Override
	protected void action() {
		
	}

}
