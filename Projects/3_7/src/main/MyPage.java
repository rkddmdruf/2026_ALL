package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import orms.categoryEntity;
import orms.couponEntity;
import orms.detailEntity;
import orms.ordersEntity;
import orms.productEntity;
import orms.rewardEntity;
import orms.userEntity;


public class MyPage extends CFrame{
	LocalDate now = LocalDate.now();
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
		list.sort((a, b) -> {
			int n = (int) (Math.abs(ChronoUnit.DAYS.between(a.odate, now)) - Math.abs(ChronoUnit.DAYS.between(b.odate, now)));
			if(n != 0) return n;
			return (a.ocount * a.oprice) - (b.ocount * b.oprice);
		});
		
		for(int i = 0; i < list.size(); i++) {
			panel.addz(card(list.get(i)));//ChronoUnit.DAYS.between(null, null)
		}
		return panel;
	}
	
	private JPanel card(ordersEntity order) {
		productEntity pro = productEntity.findById(order.pno).get();
		JLabel img = lb("", ICON(sp.getImage("product/" + order.pno, 125, 125)));
		
		JPanel infor = colF(5, 
				lb(order.odate.toString(), FONT(sp.font)), 
				vg(),
				lb(pro.pname, FONT(sp.font.deriveFont(20f).deriveFont(1))), 
				lb("수량 " + order.ocount + "개", FONT(sp.font)),
				lb(sp.df.format(order.oprice * order.ocount) + "원", FONT(sp.font.deriveFont(1).deriveFont(20f)))
			).setBackColor(Color.white);
		
		String str = order.odate.equals(now) ? "배송준비중" : (order.odate.isAfter(now.minusDays(1)) ? "배송중" : "배송완료");
		JLabel statusLabel = lb(str, FG(Color.white), BG(str.contains("배송준비") ? sp.orange : str.contains("배송중") ? Color.yellow : Color.green), FONT(sp.font.deriveFont(14f)), SIZE(100, 25), HOA(JLabel.CENTER));
		statusLabel.setOpaque(true);
		CButton button = comp(CButton::new, TEXT("상세보기"), FONT(sp.font), SIZE(135, 35));
		button.addActionListener(e -> {
			new ProductInfor(order.pno);
			dispose();
		});
		button.r = 10;
		
		JPanel p = new BoxPanel(R, 0, 10, 0, img, infor, hg(), col(0, row(0, hg(), statusLabel).setBackColor(Color.white), vg(), row(0, hg(), button).setBackColor(Color.white)).setBackColor(Color.white)) {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.white);
				g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				super.paintComponent(g);
			}
		};
		set(p,BORDER(sp.em(10, 10, 10, 10)));
		p.setOpaque(false);
		return p;
	}
	
	@Override
	protected void action() {
		b1.addActionListener(e -> {
			class test extends CFrame{
				JScrollPane sc = set(new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BORDER(null));
				public test() {
					setFrame("쿠폰함", 400, 400);
				}
				
				@Override
				protected void desing() {
					sc.setViewportView(col(10, couponEntity.findBy(e -> e.uno.equals(sp.user.uno)).stream().map(e -> fw(card(e))).toArray(JComponent[]::new)));
					add(set(col(10, fw(lb("사용가능한 쿠폰", FONT(sp.font.deriveFont(20f).deriveFont(1)))), f(sc)), BORDER(sp.em(20, 20, 20, 20))));
				}

				private JPanel card(couponEntity c) {
					JLabel l = lb("COUPON", BG(Color.yellow), FONT(sp.font.deriveFont(15f).deriveFont(1)), SIZE(100, 40), HOA(JLabel.CENTER)); 
					l.setOpaque(true);
					JPanel p = new BoxPanel(R, 0, 15, 0, l, 
							colF(0, 
									lb(rewardEntity.findById(c.reno).get().resale * 100 + "% 할인", FONT(sp.font.deriveFont(23f).deriveFont(1)), FG(sp.orange)),
									lb("사용 가능한 쿠폰", FONT(sp.font.deriveFont(13f))),
									lb("발급일 " + c.cpdate, FONT(sp.font), FG(Color.LIGHT_GRAY))
								).setBackColor(Color.white)
						) {
							protected void paintComponent(Graphics g) {
								g.setColor(Color.white);
								g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
								super.paintComponent(g);
							}
						};
					p.setOpaque(false);
					return set(p, BORDER(sp.em(15, 15, 15, 15)));
				};
				
				@Override
				protected void action() {
					sc.addMouseWheelListener(e -> {
						JScrollBar sb = sc.getVerticalScrollBar();
						sb.setValue(sb.getValue() + e.getWheelRotation() * 12);
					});
				}
				
			}
			new test();
		});
	}

	
	public static void main(String[] args) {
		Util.start(new MyPage());
	}
}
