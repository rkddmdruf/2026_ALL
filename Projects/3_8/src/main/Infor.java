package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.*;

import orms.ProjectEntity;
import orms.productEntity;
import orms.rateplanEntity;
import orms.starEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Infor extends CFrame{
	
	JComboBox<String> storege = comp(JComboBox<String>::new, NAME("용량"));
	JComboBox<String> company = comp(JComboBox<String>::new, NAME("통신사"));
	JComboBox<Integer> moment = comp(JComboBox<Integer>::new, NAME("할부"));
	
	JLabel planLabel = lb("요금제 선택 안됨", BORDER(sp.com(sp.line, sp.em(5, 0, 5, 0))), NAME("요금제"));
	JLabel planSelect = set(new JLabel("+") {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(sp.color);
			g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
			super.paintComponent(g);
		};
	}, SIZE(50, 0), FONT(sp.font.deriveFont(20f).deriveFont(1)), HOA(JLabel.CENTER));
	JLabel priceLabel = lb("", FONT(sp.font.deriveFont(14f).deriveFont(1)));
	JButton button = bt("결제하러가기", FONT(sp.font.deriveFont(14f).deriveFont(1)), FG(Color.white), BG(sp.color));
	JLabel img;
	
	
	
	productEntity product;
	ProjectEntity project;
	
	rateplanEntity ratep;
	
	public Infor(int pno) {
		product = productEntity.findById(pno).get();
		project = ProjectEntity.findById(pno).get();
		project.items.forEach(e -> {
			company.addItem(e.type);
		});
		project.capacities.forEach(e -> {
			storege.addItem(e.value + "GB");
		});
		project.installments.forEach(e -> {
			moment.addItem(e.month);
		});
		
		setPriceLabel();
		
		setFrame("상세 정보", 425, 325);
		
		// 반드시 이미지가 부모 패널에 추가된 다음 호출
		Timer timer = new Timer(1, e -> {
			JLabel lblImage = new JLabel();
			lblImage.setBounds(0, 0, img.getWidth()-1, img.getHeight()-1);
			
			// 이미지 크기를 JLabel 크기에 맞춤
			ImageIcon icon = new ImageIcon("datafiles/기종/1.jfif");
			lblImage.setIcon(new ImageIcon(icon.getImage().getScaledInstance(
					lblImage.getWidth(), lblImage.getHeight(), Image.SCALE_SMOOTH)));
			
			img.add(lblImage);
			addZoom(lblImage);
		});
		timer.setRepeats(false);
		timer.start();
	}

	@Override
	protected void desing() {
		img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(new ImageIcon("datafiles/기종/" + project.pno + ".jfif").getImage(), 0, 0, getWidth(), getHeight(), null);
				super.paintComponent(g);
			}
		};
		set(img,SIZE(0, 175), BORDER(sp.line));
		
		JPanel panel = set(new JPanel(new GridLayout(1, 2, 20, 20)), BG(Color.white), BORDER(sp.em(10, 15, 10, 20)));
		panel.add(col(10, fw(img), fw(priceLabel), vg()).setBackColor(Color.white));
		panel.add(setPanel());
		
		
		add(panel);
	}

	private JPanel setPanel() {
		List<starEntity> stars = starEntity.findBy(s -> s.pno.equals(product.pno));
		int scope = (int) Math.round(stars.stream().mapToDouble(s -> s.scope).average().getAsDouble());
		//☆★
		JPanel starPanel = row(5, IntStream.range(0, 5).mapToObj(e -> {
			return lb(e <= scope ? "★" : "☆", FONT(sp.font.deriveFont(14f)), FG(Color.orange));
		}).toArray(JComponent[]::new)).setBackColor(Color.white);
		
		
		return col(10, 
				fw(lb(product.pname, FONT(sp.font.deriveFont(16f).deriveFont(1)))),
				fw(row(50, lb("별점 : "), fw(starPanel))).setBackColor(Color.white),
				field(storege),
				field(company),
				field(moment),
				field(planLabel),
				f(button)
			).setBackColor(Color.white);
	}
	
	private JPanel field(JComponent c) {
		BoxPanel p = row(5, f(c)).setBackColor(Color.white);
		if(c.equals(planLabel)) p.addz(fh(planSelect));
		return col(1, fw(lb(c.getName())), p).setBackColor(Color.white);
		
	}
	@Override
	protected void action() {
		planSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				class test extends CFrame{
					List<rateplanEntity> list = rateplanEntity.findBy(r -> r.service.equals(company.getSelectedItem().toString()));
					public test() {
						setFrame("요금제 선택", 600, 250);
					}
					
					@Override
					protected void desing() {
						JScrollPane sc = new JScrollPane();
						JPanel mainPanel = set(new JPanel(new GridLayout(1, 0, 15, 15)), BG(Color.white), BORDER(sp.em(15, 15, 0, 15)));
						list.forEach(r -> mainPanel.add(card(r)));
						sc.setViewportView(mainPanel);
						SwingUtilities.invokeLater(() -> sc.getHorizontalScrollBar().setValue(0));
						add(sc);
					}

					private JPanel card(rateplanEntity r) {
						String s = r.effect.replace("\"", "").replace(",", "\n√");
						s = "√" + s;
						JTextArea ta = set(new JTextArea(s), FONT(sp.font.deriveFont(11f)));
						ta.setLineWrap(true);
						ta.setFocusable(false);
						ta.setCursor(Cursor.getDefaultCursor());
						JPanel p = col(0, 
									fw(lb(r.rname, FG(sp.color), FONT(sp.font.deriveFont(13f).deriveFont(1)))), 
									fw(lb(sp.df.format(r.price) + "원 / 월", FONT(sp.font.deriveFont(13f).deriveFont(1)))), 
									set(col(10, 0, 10, f(ta)), BG(Color.white), BORDER(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)))
								);
						set(p, BORDER(sp.com(sp.line(sp.color), sp.em(10, 10, 10, 10))), BG(Color.white));
						p.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								ratep = r;
								set(planLabel, HOA(JLabel.CENTER), TEXT(r.rname));
								dispose();
							}
						});
						return p;
					}
					
					@Override
					protected void action() {
						
					}
					
				}
				new test();
			}
		});
		ItemListener ac = e -> {
			if(e.getStateChange() != ItemEvent.SELECTED) return;
			
		};
		storege.addItemListener(ac);
		moment.addItemListener(ac);
		company.addItemListener(ac);
		
		
	}
	
	static void addZoom(JLabel image) {
	    BufferedImage shown = new BufferedImage(
	            image.getWidth(), image.getHeight(),
	            BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g = shown.createGraphics();
	    image.paint(g);
	    g.dispose();

	    JLabel zoom = new JLabel() {
	        public boolean contains(int x, int y) {
	            return false;
	        }
	    };

	    zoom.setSize(90, 90);
	    zoom.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	    zoom.setVisible(false);

	    Container parent = image.getParent();
	    parent.add(zoom);
	    parent.setComponentZOrder(zoom, 0);

	    MouseAdapter mouse = new MouseAdapter() {
	        public void mouseMoved(MouseEvent e) {
	            int x = Math.max(0,
	                    Math.min(image.getWidth() - 30, e.getX() - 15));
	            int y = Math.max(0,
	                    Math.min(image.getHeight() - 30, e.getY() - 15));

	            Image enlarged = shown.getSubimage(x, y, 30, 30)
	                    .getScaledInstance(90, 90, Image.SCALE_FAST);

	            zoom.setIcon(new ImageIcon(enlarged));
	            zoom.setLocation(
	                    image.getX() + e.getX() + 10,
	                    image.getY() + e.getY() + 10
	            );
	            zoom.setVisible(true);
	        }

	        public void mouseExited(MouseEvent e) {
	            zoom.setVisible(false);
	        }
	    };

	    image.addMouseMotionListener(mouse);
	    image.addMouseListener(mouse);
	}
	private void setPriceLabel() {
		project.capacities.get(storege.getSelectedIndex()).price = 10;
		System.out.println(project.find2(i -> i.type.equals(company.getSelectedObjects().toString())));
		priceLabel.setText( "원 / 월");
	}
	public static void main(String[] args) {
		Util.start(new Infor(1));
	}

}
