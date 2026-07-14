package main;

import javax.swing.*;

import static utils.Properties.*;

import java.awt.Color;

import utils.*;

public class Main extends CFrame{

	JButton startButton = comp(JButton::new, BG(new Color(40, 140, 80)), FG(Color.white), TEXT("▶"));
	JLabel tickLabel = comp(JLabel::new, TEXT("틱: 0"), FG(Color.white));
	JComboBox<String> speed = set(new JComboBox<>("1x,2x,5x,10x".split(",")));
	public Main() {
		setFrame(getter.text, 950, 600, () -> {});
	}
	
	@Override
	protected void desing() {
		JLabel K_TRADE = comp(JLabel::new, TEXT(getter.text), FG(Color.white), FONT(getter.font.deriveFont(16f).deriveFont(1)));
		
		JPanel topPanel = new BoxPanel(BoxPanel.R, 0, 10, 0, K_TRADE, startButton, comp(JLabel::new, FG(Color.white), TEXT("속도:")), speed, tickLabel).setBackColor(getter.darkColor);
		topPanel.setBorder(getter.em(10, 15, 10, 15));
		add(new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fillWidth(topPanel)));
	}

	@Override
	protected void action() {
		
	}

	public static void main(String[] args) {
		UIManager.put("Label.font", getter.font);
		Util.start(new Main());
	}
}
