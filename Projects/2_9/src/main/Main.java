package main;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;


public class Main extends CFrame{
	JToggleButton b1 = set(new JToggleButton("홈", getter.getImage("logo/home.png", 30, 30)));
	JToggleButton b2 = set(new JToggleButton("상품", getter.getImage("logo/product.png", 30, 30)));
	JToggleButton b3 = set(new JToggleButton("설정", getter.getImage("logo/setting.png", 30, 30)));
	List<JToggleButton> buttonList = Arrays.asList(b1, b2, b3);
	
	JLabel timer = new JLabel("10:10:10", JLabel.CENTER) {{
		setFont(getFont().deriveFont(30f));
	}};
	
	Home home = new Home();
	Product product = new Product();
	Option option = new Option();
	
	CardLayout card = new CardLayout();
	JPanel cardPanel = set(new JPanel(card), BORDER(getter.em(0, 10, 0, 10)));
	
	public Main() {
		ButtonGroup bg = new ButtonGroup();
		buttonList.forEach(e -> {
			bg.add(e);
			e.setVerticalTextPosition(JLabel.BOTTOM);
			e.setHorizontalTextPosition(JLabel.CENTER);
		});
		b1.setSelected(true);
		
		cardPanel.add(home, "P0");
		cardPanel.add(product, "P1");
		cardPanel.add(option, "P2");
		card.show(cardPanel, "P0");
		
		b1.addActionListener(e -> card.show(cardPanel, "P0"));
		b2.addActionListener(e -> card.show(cardPanel, "P1"));
		b3.addActionListener(e -> card.show(cardPanel, "P2"));
		
		setFrame("홈", 950, 650, () -> {});
	}

	@Override
	protected void desing() {
		JLabel logoLabel = set(new JLabel("iDelivery", getter.getImage("logo/logo.png", 30, 30), JLabel.LEFT), FONT(getter.font.deriveFont(20f)));
		JLabel cashLabel = new JLabel("충전소", getter.getImage("logo/cash.png", 40, 40), JLabel.RIGHT);
		JLabel userImage = new JLabel(getter.getImage("logo/user.png", 40, 40));

		JPanel userInforPanel = col(2, lb(getter.user.uname), lb(getter.df.format(getter.user.point) + "P") , lb("룰렛 " + getter.user.chance + "회", HOA(JLabel.RIGHT))).setBackColor(Color.white);
		JPanel topPanel = set(row(10, 0, 30, fill(logoLabel), fill(timer), row(40, fill(cashLabel), userImage).setBackColor(Color.white), userInforPanel), BORDER(getter.em(10, 0, 10, 0)), BG(Color.white));
		JPanel buttonPanel = rowF(1, b1, b2, b3).setBackColor(Color.white);
		
		add(new BoxPanel(C, 0, 10, 0, topPanel, fill(cardPanel), BoxPanel.fillWidth(buttonPanel)));
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		UIManager.put("Label.font", getter.font.deriveFont(1).deriveFont(13f));
		UIManager.put("ToggleButton.foreground", Color.black);
		UIManager.put("ToggleButton.background", Color.white);
		UIManager.put("ToggleButton.font", getter.font.deriveFont(1).deriveFont(13f));
		Util.start(new Main());
	}
}
