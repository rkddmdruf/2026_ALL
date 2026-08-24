package test.test6;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import orms.*;

public class Home extends JPanel{
	JPanel p1 = set(new JPanel(new BorderLayout()), BORDER(sp.eLine(Color.LIGHT_GRAY, 10,10,10,10)), BG(Color.white));
	public Home(JFrame f) {
		setLayout(new BorderLayout());
		
		JPanel p3_1 = getPanel("logo/maze.png", "미로", "포인트 적립");
		JPanel p3_2 = getPanel("logo/roulette.png", "경품", "룰렛 뽑기");
		
		p3_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Game().setVisible(true);
				f.dispose();
			}
		});
		JPanel p = new JPanel(new GridLayout(2, 1, 10, 10));
		JPanel p1 = set(col(5, fw(lb("광고", FONT(sp.font.deriveFont(14f))))), BORDER(sp.eLine(Color.LIGHT_GRAY, 5, 5, 5, 5)), BG(Color.white));
		JPanel p2 = getPanel("logo/cash.png", "충전소", "포인트 충전하기");
		JPanel p3 = set(col(5, fw(lb("바로가기", FONT(sp.font.deriveFont(14f)))), 
				f(row(10, f(p3_1),f(p3_2)).setBackColor(Color.white))
				), BORDER(sp.eLine(Color.LIGHT_GRAY, 5, 5, 5, 5)), BG(Color.white));
		JPanel p_p = new JPanel(new GridLayout(1, 3, 10, 10));
		p_p.add(p1);p_p.add(p2);p_p.add(p3);
		p.add(p_p);
		p.add(set(new JPanel(), BORDER(sp.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)), BG(Color.white)));
		add(p);
		
	}
	
	private JPanel getPanel(String img, String title, String subTitle) {
		JLabel l = lb("", FONT(sp.font.deriveFont(14f)), ICON(sp.getImage(img, 50, 50)));
		l.setHorizontalAlignment(JLabel.CENTER);
		return set(col(5, 
				f(l), 
				lb(title, FONT(sp.font.deriveFont(13f))), lb(subTitle, FG(Color.LIGHT_GRAY))
			), BORDER(sp.eLine(Color.LIGHT_GRAY, 5, 5, 5, 5)), BG(Color.white));
	}
}