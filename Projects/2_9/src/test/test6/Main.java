package test.test6;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;

import javax.swing.*;

import main.Util;
import orms.*;

public class Main extends CFrame {
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	JToggleButton b1 = set(new JToggleButton("홈"), ICON(sp.getImage("logo/home.png", 25, 25)), BG(Color.white));
	JToggleButton b2 = set(new JToggleButton("상품"), ICON(sp.getImage("logo/product.png", 25, 25)), BG(Color.white));
	JToggleButton b3 = set(new JToggleButton("설정"), ICON(sp.getImage("logo/setting.png", 25, 25)), BG(Color.white));
	public Main() {
		b1.setVerticalTextPosition(JLabel.BOTTOM);
		b1.setHorizontalTextPosition(JLabel.CENTER);
		b2.setVerticalTextPosition(JLabel.BOTTOM);
		b2.setHorizontalTextPosition(JLabel.CENTER);
		b3.setVerticalTextPosition(JLabel.BOTTOM);
		b3.setHorizontalTextPosition(JLabel.CENTER);
		b1.setSelected(true);
		setFrameg("홈", 850, 550, () -> new test6_Login().setVisible(true));
	}

	protected void desing() {
		ButtonGroup bg = new ButtonGroup();
		bg.add(b1);
		bg.add(b2);
		bg.add(b3);
		
		JPanel bp = set(new JPanel(new GridLayout()), BG(Color.white));
		bp.add(b1);bp.add(b2);bp.add(b3);
		
		JPanel topPanel = set(new JPanel(new BorderLayout()), BG(Color.white), BORDER(sp.em(10, 10, 10, 10)));
		topPanel.add(lb("iDelivery", FONT(sp.font.deriveFont(20f).deriveFont(1)), ICON(sp.getImage("logo/logo.png", 45, 45))), BorderLayout.WEST);
		topPanel.add(row(20, lb("충전소", FONT(sp.font.deriveFont(13f)), ICON(sp.getImage("logo/cash.png", 45, 45))),
				row(0, lb("", ICON(sp.getImage("logo/user.png", 45, 45))), 
						fh(col(2, 
								fw(lb(sp.user.uname, HOA(JLabel.RIGHT))),
								fw(lb(sp.df.format(sp.user.point) + "P", HOA(JLabel.RIGHT))),
								fw(lb("룰렛 " + sp.user.chance + "회", HOA(JLabel.RIGHT)))
								).setBackColor(Color.white)
						)
					).setBackColor(Color.white)).setBackColor(Color.white), BorderLayout.EAST);
		cardP.add(new Home(this), "P1");
		cardP.add(new Search(this), "P2");
		add(col(0, fw(topPanel), f(set(col(0, f(cardP)), BORDER(sp.em(10, 10, 10, 10)))), fw(bp)).setBackColor(Color.white));
	}

	protected void action() {
		b1.addActionListener(e -> {
			setTitle("홈");
			card.show(cardP, "P1");
		});
		b2.addActionListener(e -> {
			setTitle("검색");
			card.show(cardP, "P2");
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Main());
	}
}
