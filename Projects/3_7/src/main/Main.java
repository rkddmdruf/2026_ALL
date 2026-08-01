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
import orms.productEntity;

public class Main extends CFrame{
	
	JButton search = set(new CButton(sp.getImage("search", 20, 30)), BG(Color.lightGray));
	JTextField tf = comp(JTextField::new, BORDER(sp.line));
	BoxPanel categoryPanel = set(col(20), BORDER(sp.com(sp.line, sp.em(10, 10, 10, 10))), BG(Color.white));
	
	JLabel login = lb(sp.user == null ? "로그인" : "로그아웃", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	JLabel roullet = lb("룰렛", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	JLabel shopping = lb("장바구니", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	JLabel myPage = lb("마이페이지", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	
	
	JPanel mainPanel = set(new JPanel(new GridLayout(0, 5, 15, 15)), BORDER(sp.em(10, 10, 10, 50)));
	
	List<productEntity> list = new ArrayList<>();
	
	Map<String, List<String>> category = new LinkedHashMap<>();
	List<String> key = new ArrayList<>();
	List<JLabel> categoryLabels = new ArrayList<>();
	List<List<JLabel>> detailLabels= new ArrayList<>();
	
	List<Integer> expasionList = new ArrayList<>();
	int selectDetil = -1, selectCategory = -1;
	
	Map<Integer, Boolean> mouseOvers = new HashMap<>();
	Map<Integer, JLabel> psLabels = new HashMap<>();
	Map<Integer, RoundRectangle2D.Double> rrs = new HashMap<>();
	
	public Main() {
		detailEntity.findAll().forEach(e -> category.computeIfAbsent(categoryEntity.findById(e.cno).get().cname, k -> new ArrayList<>()).add(e.dname));
		key.addAll(category.keySet());
		
		setFrame("메인", 1111, 700);
	}
	//▶▼
	@Override
	protected void desing() {
		setCategoryPanel();
		JPanel leftPanel = col(20, lb("", ICON(sp.getImage("logo", 150, 75))), row(3, f(tf), search).setBackColor(Color.white), f(categoryPanel)).setBackColor(Color.white);
		set(leftPanel, BORDER(sp.em(5, 5, 15, 5)), SIZE(200, 0));		
		
		JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, sp.user == null ? 100 : 25, 10));
		if(sp.user == null) menuPanel.add(login);
		else {
			menuPanel.add(roullet);
			menuPanel.add(lb("|", FG(Color.LIGHT_GRAY)), HOA(JLabel.CENTER));
			menuPanel.add(login);
			menuPanel.add(lb("|", FG(Color.LIGHT_GRAY)), HOA(JLabel.CENTER));
			menuPanel.add(shopping);
			menuPanel.add(lb("|", FG(Color.LIGHT_GRAY)), HOA(JLabel.CENTER));
			menuPanel.add(myPage);
		}
		
		JPanel topPanel = row(0, lb("Skillmall", FONT(sp.font.deriveFont(24f).deriveFont(1))), hg(), menuPanel);
		JPanel panel = col(20, fw(topPanel), f(new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)));
		set(panel, BORDER(sp.em(20, 20, 20, 20)));
		add(row(0, fh(leftPanel), f(panel)));
	}

	private void setMainPanel() {
		mainPanel.removeAll();
		list.clear();
		list.addAll((selectCategory == -1 || selectDetil == -1) ? 
				  productEntity.findAll() 
				: productEntity.findBy(e -> detailEntity.findById(e.dno).get().dname.equals(detailLabels.get(selectCategory).get(selectDetil).getText())));
		
		for(int i = 0; i < list.size(); i++) {
			int index = i;
			productEntity p = list.get(i);
			JLabel img = new JLabel() {
				protected void paintComponent(java.awt.Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
					g2.drawImage(sp.getImage("product/" + p.pno, 200, 200).getImage(), 0, 0, getWidth(), getHeight(), null);
					
					
					Point size = new Point((getWidth() / 4) * 3, 30);
					RoundRectangle2D.Double rr = new RoundRectangle2D.Double((getWidth() - size.x) / 2, (getHeight() - size.y) / 2, size.x, size.y, 15, 15);
					
					if(p.pcount == 0) {
						Rectangle r = rr.getBounds();
						g2.setColor(Color.lightGray);
						g2.draw(r);
						g2.setColor(new Color(Color.black.getRed(), Color.black.getGreen(), Color.black.getBlue(), 175));
						g2.fill(r);
						g2.setColor(Color.white);
						g2.setFont(sp.font.deriveFont(20f).deriveFont(1));
						FontMetrics fm = g2.getFontMetrics();
						g2.drawString("품절", (getWidth() - fm.stringWidth("품절")) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
						return;
					}
					if(!mouseOvers.get(index)) return;
					rrs.put(index, rr);
					g2.setColor(sp.red);
					g2.fill(rr);
					g2.setColor(Color.white);
					g2.setFont(sp.font.deriveFont(15f));
					FontMetrics fm = g2.getFontMetrics();
					g2.drawString("결재하기", (getWidth() - fm.stringWidth("결재하기")) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
				};
			};
			if(p.pcount != 0) {
				psLabels.put(index, img);
				mouseOvers.put(index, false);
			}
			
			img.setPreferredSize(new Dimension(0, 110));
			mainPanel.add(set(
					col(10, 
							f(img), 
							fw(lb(p.pname, FONT(sp.font.deriveFont(1)), HOA(JLabel.LEFT), SIZE(100, 20))),
							fw(row(10, 
									lb(sp.df.format(p.pprice) + "원", FG(sp.red)), 
									lb(p.pcount == 0 ? "품절" : "재고 " + p.pcount + "개", FG(p.pcount == 0 ? sp.red : Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)))
								).setBackColor(Color.white))
					).setBackColor(Color.white), BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(10, 10, 40, 10)))));
		}
		
		
	}
	private void setCategoryPanel() {
		categoryPanel.removeAll();
		for(int i = 0; i < key.size(); i++) {
			List<String> cate = category.get(key.get(i));
			JLabel label = lb("▶" + key.get(i), FONT(sp.font.deriveFont(1).deriveFont(20f)));
			categoryLabels.add(label);
			
			JLabel[] ls = cate.stream().map(e -> lb(e, FG(Color.LIGHT_GRAY))).toArray(JLabel[]::new);
			detailLabels.add(Arrays.asList(ls));
			
			categoryPanel.addz(col(20, fw(label), fw(col(5, ls).setBackColor(Color.white))).setBackColor(Color.white));
		}
		
		setDetailVisit();
	}
	
	private void setDetailVisit() {
		IntStream.range(0, key.size()).forEach(i -> {
			categoryLabels.get(i).setText("▶" + key.get(i));
			detailLabels.get(i).get(0).getParent().setVisible(false);
		});
		
		expasionList.forEach(e -> {
			JLabel l = detailLabels.get(e).get(selectDetil);
			l.getParent().setVisible(true);
			l.setForeground(sp.red);
			categoryLabels.get(e).setText("▼" + key.get(e));
		}); // 선택된거 펼치기
		System.out.println("selectCategory : " + selectCategory + ", " + "selectDetil : " + selectDetil );
		setMainPanel();
		revalidate();
		repaint();
	}
	
	@Override
	protected void action() {
		categoryLabels.forEach(l -> {
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					if(expasionList.contains(categoryLabels.indexOf(l))) {
						expasionList.removeIf(ex -> ex.equals(categoryLabels.indexOf(l)));
						selectCategory = -1;
						selectDetil = -1;
					}
					else {
						expasionList.add(categoryLabels.indexOf(l));
						selectCategory = categoryLabels.indexOf(l);
						selectDetil = 0;
					}
					setDetailVisit();
				}
			});
		});
		detailLabels.forEach(ls -> {
			ls.forEach(l -> {
				l.addMouseListener(new MouseAdapter() {
					@Override
					 public void mouseClicked(MouseEvent e) {
						detailLabels.forEach(ll -> ll.forEach(lll -> lll.setForeground(Color.LIGHT_GRAY)));
						if(detailLabels.indexOf(ls) == selectCategory) {
							selectDetil = ls.indexOf(l);
							setDetailVisit();
						}
					}
				});
			});
		});
		
		
		for(int i : psLabels.keySet()) {
			int index = i;
			psLabels.get(index).addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) { change(true); };
				public void mouseExited(MouseEvent e) { change(false); };
				
				public void mouseClicked(MouseEvent e) {
					if(sp.user == null) {
						sp.err("로그인을 해주세요.");
						new Login();
						dispose();
					}
					if(rrs.get(index).contains(e.getPoint())) {
						System.out.println("결재 : " + list.get(index).pname);
					}
				};
				
				private void change(boolean b) {
					mouseOvers.put(index, b);
					((JComponent) psLabels.get(index).getParent()).setBorder(sp.com(sp.line(b ? sp.red : Color.LIGHT_GRAY), sp.em(10, 10, 40, 10)));
					revalidate();
					repaint();
				}
			});
		}
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
