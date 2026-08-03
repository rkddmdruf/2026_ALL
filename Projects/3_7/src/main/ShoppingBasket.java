package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.*;

import orms.cartEntity;
import orms.couponEntity;
import orms.productEntity;
import orms.rewardEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class ShoppingBasket extends CFrame{
	
	JScrollPane sc = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	JLabel l1 = lb("0원", NAME("총상품 가격"), FONT(sp.font.deriveFont(1).deriveFont(16f)));
	JLabel l2 = lb("0원", NAME("쿠폰 할인"), FONT(sp.font.deriveFont(1).deriveFont(16f)));
	List<String> str = new ArrayList<>() {{
		add("쿠폰 선택");
		for(couponEntity s : couponEntity.findBy(c -> c.uno.equals(sp.user.uno))) {
			add("1번 쿠폰 - " + (int) (rewardEntity.findById(s.reno).get().resale * 100) + "%");
		}
	}};
	JComboBox<String> cb = cb(str.stream().collect(Collectors.joining(",")));
	CButton plusB = comp(CButton::new, TEXT("+"), FG(Color.white), BG(sp.blue));
	CButton buyB = comp(CButton::new, TEXT(""), FG(Color.white), BG(sp.blue),SIZE(0, 35));
	JLabel l3 = comp(JLabel::new, TEXT("0원"), FONT(sp.font.deriveFont(1).deriveFont(25f)), HOA(JLabel.RIGHT));
	
	int price = 0;
	Map<Integer, Integer> prices = new HashMap<>();
	public ShoppingBasket() {
		cb.setPreferredSize(new Dimension(0, 35));
		setFrame("장바구니", 850,  500);
	}

	@Override
	protected void desing() {
		sc.setViewportView(createMainPanel());
		JPanel r = row(10, f(sc), col(0, fh(getInforPanel()), vg()));
		JPanel panel =  col(10, fw(lb("장바구니", FONT(sp.font.deriveFont(1).deriveFont(26f)))), f(r));
		set(panel, BORDER(sp.em(15, 15, 20, 15)));
		add(panel);
	}

	private JPanel createMainPanel() {
		JPanel panel = set(new JPanel(new GridLayout(0, 1, 10, 10)), BORDER(sp.em(10, 10, 10, 30)));
		
		List<cartEntity> list = new ArrayList<>(
				cartEntity.findBy(c -> c.uno.equals(sp.user.uno)).stream()
				.collect(Collectors.toMap(
						c -> c.pno,
						c -> c,
						(a, b) -> { a.ctcount += b.ctcount; return a; },
						LinkedHashMap::new
						)).values()
				);
		list.sort((a, b) -> Integer.compare(b.ctno, a.ctno));
		
		
		for(int i = 0; i < list.size(); i++) {
			cartEntity cart = list.get(i);
			productEntity product = productEntity.findById(cart.pno).get();
			
			JCheckBox cb = comp(JCheckBox::new, BG(Color.white));
			cb.setSelected(true);
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(java.awt.Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/product/" + product.pno + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
				};
			};
			img.setPreferredSize(new Dimension(100, 100));
			
			JLabel mi = lb("-", BORDER(sp.line), SIZE(20, 20), HOA(JLabel.CENTER));
			JLabel countLabel = lb(String.valueOf(cart.ctcount), BORDER(sp.line), SIZE(25, 20), HOA(JLabel.CENTER));
			JLabel pl = lb("+", BORDER(sp.line), SIZE(20, 20), HOA(JLabel.CENTER));
			mi.setOpaque(true);countLabel.setOpaque(true);pl.setOpaque(true);
			
			JLabel price = lb(sp.df.format(product.pprice * cart.ctcount) + "원", FONT(sp.font.deriveFont(20f).deriveFont(1)));
			
			JPanel infor = col(5, 
					fw(lb(product.pname, FONT(sp.font.deriveFont(18f)))), 
					vg(),
					fw(price),
					fw(row(0, mi, countLabel, pl)).setBackColor(Color.white)
					).setBackColor(Color.white);
			JPanel p = set(row(20, cb, img, infor, hg(), lb("삭제")).setBackColor(Color.white), BORDER(sp.em(20, 15, 20, 20)));
			panel.add(p);
			
			prices.put(product.pno, product.pprice);
			
			MouseAdapter ma = new MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					int count = Integer.parseInt(countLabel.getText());
					if(e.getSource() == mi && count > 1) count--;
					if(e.getSource() == pl) count++;
					countLabel.setText(String.valueOf(count));
					price.setText(sp.df.format(product.pprice * count) + "원");
				};
			};
			
			mi.addMouseListener(ma);
			pl.addMouseListener(ma);
			
			cb.addActionListener(e -> {prices.put(product.pno, cb.isSelected() ? product.pprice : 0); setPriceLabels();});
		}
		return panel;
	}
	
	private JPanel getInforPanel() {
		JPanel couponPanel = col(5, fw(lb("쿠폰", FONT(sp.font.deriveFont(0).deriveFont(16f)))), row(7, f(cb), fh(plusB)).setBackColor(Color.white)).setBackColor(Color.white);
		JPanel buyPanel = col(10, fw(l3), fw(buyB)).setBackColor(Color.white);
		
		JPanel panel = set(
				col(20, 
					fw(lb("주문 예상 금액           ", FONT(sp.font.deriveFont(1).deriveFont(25f)))), 
					col(10, setLabel(l1), setLabel(l2)).setBackColor(Color.white),
					couponPanel,
					buyPanel
				)
				, BG(Color.white), BORDER(sp.em(10, 10, 5, 10)));
		return panel;
	}
	
	private JPanel setLabel(JLabel label) {
		return row(0, fw(lb(label.getName(), FONT(label.getFont().deriveFont(0)))), fw(set(label, HOA(JLabel.RIGHT)))).setBackColor(Color.white);
	}
	
	private void setPriceLabels() {
		this.price = prices.keySet().stream().mapToInt(p -> prices.get(p)).sum();
		l1.setText(sp.df.format(price) + "원");
		long count = prices.keySet().stream().filter(e -> prices.get(e) != 0).count();
		buyB.setText("총 " + count + "개 상품 구매하러가기");
	}
	
	@Override
	protected void action() {
		sc.addMouseWheelListener(e -> {
			JScrollBar sb = sc.getVerticalScrollBar();
			sb.setValue(sb.getValue() + e.getWheelRotation() * 20);
		});
	}

	public static void main(String[] args) {
		Util.start(new ShoppingBasket());
	}
}
