package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.geom.RoundRectangle2D.Double;
import java.awt.image.BufferedImage;

import javax.swing.*;

import org.w3c.dom.html.HTMLAreaElement;

import main.Util;
import orms.*;

public class sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj extends CFrame {
	JLabel l1 = lb("룰렛", FG(Color.LIGHT_GRAY));
	JLabel l2 = lb(sp.user == null ? "로그인" : "로그아웃", FG(Color.LIGHT_GRAY));
	JLabel l3 = lb("장바구니", FG(Color.LIGHT_GRAY));
	JLabel l4 = lb("마이페이지", FG(Color.LIGHT_GRAY));
	
	CTree t;
	JTextField t1 = comp(JTextField::new,SIZE(0, 35));
	JPanel p1 = set(new JPanel(new GridLayout(0, 5, 20, 18)), BORDER(sp.em(15, 15, 15, 50)));
	BoxPanel topP = row(10);
	public String category = "";
	boolean slClick = false;
	
	JLabel sl = new JLabel() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = sp.anti(g);
			g2.setColor(Color.black);
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
			int gap = 10;
			g2.drawImage(searchImage(), gap, gap, getWidth() - gap * 2, getHeight() - gap * 2, null);
		}

		private Image searchImage() {
			Image img = new ImageIcon("datafiles/search.png").getImage();
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, img.getWidth(null), 0, 0, img.getHeight(null), 0, 0, img.getWidth(null), img.getHeight(null), null);
			g2.dispose();
			
			for(int y = 0; y < bf.getHeight(); y++) {
				for(int x = 0; x < bf.getWidth(); x++) {
					int rr = bf.getRGB(x, y);
					int r = (rr >> 16) & 0xFF;
					int g = (rr >> 8) & 0xFF;
					int b = rr & 0xFF;
					if(rr != 0 && r < 100 && g < 100 && b < 100) {
						bf.setRGB(x, y, Color.white.getRGB());
					}
				}
			}
			
			return bf;
			
		};
	};
	public sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj() {
		t = new CTree(this);
		set(sl, SIZE(40, 30));
		categoryEntity.findAll().forEach(e -> {
			t.Values(e.cname, detailEntity.findBy(c -> c.cno.equals(e.cno)).stream().map(c -> c.dname).toArray(String[]::new));
		});
		setFrame("메인", 1200	, 650);
	}

	protected void desing() {
		JLabel logo = set(new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(sp.getImage("logo"), 0, 0, getWidth(), getHeight(), null);
			}
		}, SIZE(200, 110));
		
		setTop();
		
		BoxPanel p1 = set(col(20, 
				fw(logo),
				fw(row(4, fw(t1), fh(sl)).setBackColor(Color.white)),
				f(set(new JScrollPane(set(t, BORDER(sp.em(10, 10, 10, 10)))), BG(Color.white)))
			), BORDER(sp.em(10, 10, 20, 10)), BG(Color.white));
		
		BoxPanel p2 = set(col(20, 20, 30,
				fw(row(0, lb("Skillmall", FONT(sp.font.deriveFont(24f).deriveFont(1))), fw(topP))),
				f(new JScrollPane(this.p1))
				), BORDER(sp.em(0, 0, 0, 30)));
		
		reload();
		add(row(30, fh(p1), f(p2)));
	}
	
	private void setTop() {
		topP.removeAll();
		topP.addz(hg());
		if(sp.user == null) {
			l2.setText("로그인");
			topP.addz(l2);
		}else {
			l2.setText("로그아웃");
			topP.addz(l1);
			topP.addz(lb("|", FG(Color.LIGHT_GRAY)));
			topP.addz(l2);
			topP.addz(lb("|", FG(Color.LIGHT_GRAY)));
			topP.addz(l3);
			topP.addz(lb("|", FG(Color.LIGHT_GRAY)));
			topP.addz(l4);
		}
		revalidate();
		repaint();
	}

	public void reload() {
		p1.removeAll();
		productEntity.findAll().stream()
		.filter(e -> !slClick || (slClick && e.pname.contains(t1.getText())))
		.filter(e -> detailEntity.findById(e.dno).get().dname.contains(category))
		.forEach(e -> {
			var img = new JLabel() {
				boolean b = false;
				public Double rd;
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponents(g);
					Graphics2D g2 = sp.anti(g);
					
					int w = getWidth(), h = getHeight();
					int rx = w / 2, ry = h / 3;
					g2.drawImage(sp.getImage("product/" + e.pno), 0, 0, getWidth(), getHeight(), null);
					if(e.pcount == 0) {
						System.out.println(e.pname);
						Rectangle r = new Rectangle((w - rx) / 2, (h - ry) / 2, rx, ry);
						g2.setColor(sp.setA(Color.black, 150));
						g2.fill(r);
						g2.setColor(sp.setA(Color.gray, 200));
						g2.draw(r);
						
						g2.setFont(sp.font.deriveFont(20f).deriveFont(1));
						String s = "품절";
						FontMetrics fm = g2.getFontMetrics();
						g2.setColor(Color.white);
						g2.drawString(s, (w - fm.stringWidth(s)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
						return;
					}
					
					if(b) {
						Rectangle r = new Rectangle((w - rx) / 2, (h - ry) / 2, rx, ry);
						rd = new RoundRectangle2D.Double(r.x, r.y, r.width, r.height, 20, 20);
						g2.setColor(sp.red);
						g2.fill(rd);
						
						g2.setFont(sp.font.deriveFont(20f).deriveFont(1));
						String s = "결제";
						FontMetrics fm = g2.getFontMetrics();
						g2.setColor(Color.white);
						g2.drawString(s, (w - fm.stringWidth(s)) / 2, (h - fm.getHeight()) / 2 + fm.getAscent());
					}
				}
			};
			set(img, SIZE(0, 125));
			
			img.addMouseListener(new MouseAdapter() {
			    public void mouseEntered(MouseEvent ae) {
			    	if(e.pcount == 0) return;
			    	((JComponent)img.getParent()).setBorder(sp.com(sp.line(Color.red),  sp.em(0, 7, 0, 7)));
			    	img.b = true;
			    }

			    public void mouseExited(MouseEvent ae) {
			    	if(e.pcount == 0) return;
			    	((JComponent)img.getParent()).setBorder(sp.com(sp.line(Color.lightGray),  sp.em(0, 7, 0, 7)));
			    	img.b = false;
			    }
			    public void mouseClicked(MouseEvent ae) {
			    	if(! img.b) return;
			    	if(!img.rd.contains(ae.getPoint())) return;
			    	if(sp.user == null) {
			    		sp.err("로그인을 해주세요");
			    		new Login(e.pno);
			    	}else new Infor(e.pno);
			    	dispose();
			    }
			});
			
			BoxPanel p = set(col(10, 10, 25,
					fw(img),
					fw(lb(e.pname, FONT(sp.font.deriveFont(14f).deriveFont(1)), SIZE(0, 30))),
					fw(row(10, lb(sp.df.format(e.pprice) + "원", FG(sp.red)), lb("재고 " + e.pcount + "개", FG(Color.LIGHT_GRAY))).setBackColor(Color.white))
					), BG(Color.white), BORDER(sp.com(sp.line(Color.lightGray),  sp.em(0, 7, 0, 7))));
			p1.add(p);
		});
		slClick = false;
		revalidate();
		repaint();
	}

	protected void action() {
		l2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(sp.user == null) {
					new Login(-1);
					dispose();
				}else {
					sp.user = null;
					sp.infor("로그아웃되었습니다.");
					setTop();
				}
			}
		});
		l1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new game();
				dispose();
			}
		});
		sl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				slClick = true;
				if(t1.getText().isBlank()) {
					sp.err("빈칸이 존재합니다.");
					reload();
					return;
				}
				reload();
				if(p1.getComponentCount() == 0) {
					sp.err("검색 결과가 없습니다.");
					t1.setText("");
					reload();
					return;
				}
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new sdflkjsdflsdjfldskjflksdjflsdkfjsdlkfj());
	}
}