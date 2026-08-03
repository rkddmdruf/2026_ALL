package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import orms.categoryEntity;
import orms.detailEntity;
import orms.ordersEntity;
import orms.productEntity;
import orms.userEntity;


public class MyPage extends CFrame{
	
	userEntity user = sp.user;
	CButton b1 = set(new CButton("쿠폰함"), FG(Color.black), BG(Color.yellow), FONT(sp.font.deriveFont(15f).deriveFont(1)), SIZE(175, 0));
	CButton b2 = set(new CButton("찜한 목록 보러가기"), FG(Color.white), BG(sp.red), FONT(sp.font.deriveFont(15f)), SIZE(175, 0));
	List<ordersEntity> list = ordersEntity.findBy(e -> e.uno.equals(sp.user.uno));
	public MyPage() {
		setFrame("마이페이지", 900, 650);
	}

	@Override
	protected void desing() {
		JPanel infoPanel = row(10, 
				fw(col(10, 
						fw(lb(user.uname + "님의 정보", FONT(sp.font.deriveFont(25f).deriveFont(1)))),
						fw(lb("이름 : " + user.uname, FONT(sp.font.deriveFont(1).deriveFont(20f)))),
						fw(lb("연락처 : " + user.unumber, FONT(sp.font.deriveFont(13f))))
					).setBackColor(Color.white)),
				hg(),
				col(10, 10, 10, 
						lb("보유 포인트 " + sp.df.format(user.uprice) + "P", FONT(sp.font.deriveFont(25f).deriveFont(1))),
						lb("총 주문수 " + ordersEntity.findBy(e -> e.uno.equals(user.uno)).size() + "개", FONT(sp.font.deriveFont(25f).deriveFont(1)))
					).setBackColor(Color.white),
				col(10, fh(b1), fh(b2)).setBackColor(Color.white)
				).setBackColor(Color.white);
		set(infoPanel, BORDER(sp.em(10, 20, 10, 20)));
		add(set(col(10, fw(infoPanel), f(set(new JScrollPane(setMainPanel()), BORDER(null)))), BORDER(sp.em(10,20,5,20))));
	}

	private JPanel setMainPanel() {
		BoxPanel panel = set(col(10), BORDER(sp.em(10, 10, 10, 10)));
		LocalDate now = LocalDate.now();
		list.sort((a, b) -> {
			long n = (int) (ChronoUnit.DAYS.between(a.odate, now) - ChronoUnit.DAYS.between(b.odate, now));
			return n;
		});
		for(int i = 0; i < list.size(); i++) {
			panel.addz(card(list.get(i)));//ChronoUnit.DAYS.between(null, null)
		}
		return panel;
	}
	
	private JPanel card(ordersEntity order) {
		JPanel p = new JPanel();
		return p;
	}
	
	@Override
	protected void action() {
		
	}

	
	public static void main(String[] args) {
		Util.start(new MyPage());
	}
}
