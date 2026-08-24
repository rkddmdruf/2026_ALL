package main;

import javax.swing.*;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CashStation extends CFrame{
	
	JButton back = bt("돌아가기", BORDER(sp.line), BG(Color.white));
	JButton payMent = bt("결제하기", BORDER(sp.line), BG(Color.white));
	
	JLabel pl = lb("원하는금액", FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY));
	List<JComponent> lineComps = new ArrayList<>();
	
	JPanel gridPanel = new JPanel(new GridLayout(2, 3, 10, 10));
	int select = -1;
	int myPrice = -1;
	public CashStation() {
		setFrame("충전소", 550, 450, () -> {});
	}

	@Override
	protected void desing() {
		JLabel logo = set(new JLabel("포인트 충전", sp.getImage("logo/cash.png", 70, 70), JLabel.CENTER), FONT(sp.font.deriveFont(1).deriveFont(22f)));
		logo.setVerticalTextPosition(JLabel.BOTTOM);
		logo.setHorizontalTextPosition(JLabel.CENTER);
		
		JPanel logoPanel = set(col(10, fw(lb("충전소")),
				f(logo)), BG(Color.white), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)));
		JPanel buttonPanel = rowF(10, back, payMent);
		int n = 1000;
		for(int i = 0; i < 5; i++) {
			n *= (i == 0 ? 1 : (i % 2 == 0 ? 2 : 5));
			JLabel l = lb(sp.df.format(n) + "원", HOA(JLabel.CENTER), VEA(JLabel.CENTER)
					, FONT(sp.font.deriveFont(15f).deriveFont(1)), BORDER(sp.line(Color.LIGHT_GRAY)), BG(Color.white));
			l.setOpaque(true);
			gridPanel.add(l);
		}
		gridPanel.add(inputNumber());
		add(set(col(10, fw(logoPanel), f(gridPanel), fw(buttonPanel)), BORDER(sp.em(15, 15, 15, 15))));
	}

	private JPanel inputNumber() {
		JPanel p = col(20, 15, 10, 
				lb("직접입력", FONT(sp.font.deriveFont(15f).deriveFont(1))), 
				pl
				).setBackColor(Color.white);
		p.setBorder(sp.line(Color.lightGray));
		return p;
	}
	@Override
	protected void action() {
		back.addActionListener(e -> {
			dispose();
		});
		Arrays.asList(gridPanel.getComponents()).forEach(e -> {
			JComponent com = ((JComponent) e);
			com.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e2) {
					Arrays.asList(gridPanel.getComponents()).forEach(cs -> ((JComponent) cs).setBorder(sp.line(Color.LIGHT_GRAY)));
					com.setBorder(sp.line(Color.blue));
				}
			});
		});
		payMent.addActionListener(e -> {
			// 결제 액션
		});
	}

	public static void main(String[] args) {
		UIManager.put("Label.font", sp.font.deriveFont(1).deriveFont(13f));
		UIManager.put("Button.font", sp.font.deriveFont(1).deriveFont(13f));
		UIManager.put("ToggleButton.select", sp.color);
		UIManager.put("ToggleButton.foreground", Color.black);
		UIManager.put("ToggleButton.background", Color.white);
		UIManager.put("ToggleButton.font", sp.font.deriveFont(1).deriveFont(13f));
		Util.start(new CashStation());
	}
}