package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.*;

import orms.*;

public class Main extends CFrame{
	JScrollPane sc;
	JScrollPane mainSc = set(new JScrollPane(), BG(Color.white), BORDER(sp.em(0, 0, 0, 10)));
	List<JComponent> topComps = new ArrayList<>();
	List<Integer> aList = new ArrayList<>();
	JPanel myStroy = topCard(0, "  내 스토리  ");
	
	public Main() {
		setFrame("홈", 725, 600);
	}

	@Override
	protected void desing() {
		String[] strs = "home,search,send,heart1,plus".split(",");
		JPanel menuPanel = col(100, 30, 100, Arrays.stream(strs).map(s -> {
			JLabel label = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
					Image img = new ImageIcon("datafiles/icons/" + s + ".png").getImage();
					if(s.equals("plus")) img = Util.bImage();
					g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
				}
			};
			return f(label);
		}).toArray(JComponent[]::new));
		set(menuPanel, SIZE(100, 0), BG(Color.white), BORDER(sp.em(0, 10, 0, 50)));
		
		List<Integer> list = new ArrayList<>();
		Util.myFollowing().get(sp.user.u_no).forEach(e -> {
			list.addAll(Util.myFollowing().get(e));
		});
		Map<Integer, Integer> countMap = new HashMap<>();
		list.removeIf(e -> Util.myFollowing().get(sp.user.u_no).contains(e) || sp.user.u_no.equals(e));
		list.forEach(n -> countMap.put(n,  countMap.getOrDefault(n, 0) + 1));
		List<Integer> mapList = new ArrayList<>(countMap.keySet());
		mapList.sort((a , b) -> {
			int comp = Integer.compare(countMap.get(b), countMap.get(a));
			if(comp != 0) return comp;
			return Util.myFollow().get(b).size() - Util.myFollow().get(a).size();
		});
		
		
		JPanel g = set(new JPanel(new GridLayout(0, 1, 10, 10)), BG(Color.white), BORDER(sp.em(15, 25, 0, 10)));
		setMainPanel(g);
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(topPanel(), BorderLayout.NORTH);
		p1.add(menuPanel, BorderLayout.WEST);
		p1.add(mainSc);
		mainSc.setViewportView(g);
		JPanel p2 = col(15, fw(lb("회원님을 위한 추천", FONT(sp.font.deriveFont(1).deriveFont(13f)))),
				col(15, IntStream.range(0, 5).mapToObj(e -> {
					return rightCard(userEntity.findById(mapList.get(e)).get());
				}).toArray(JComponent[]::new)).setBackColor(Color.white)
				).setBackColor(Color.white);
		
		JPanel panel = row(0, f(p1), fh(p2)).setBackColor(Color.white);
		set(p2, BORDER(sp.em(20, 10, 10, 10)));
		add(panel);
	}
	
	private JPanel topPanel() {
		sc = set(new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BORDER(null), BG(Color.white));
		JLabel profileIcon = lb("", ICON(sp.getImage("icons/profile", 40, 40)));
		List<String> us = Arrays.asList(sp.user.u_follow.split(","));
		
		sc.setViewportView(row(20, 10, 0, us.stream().map(e -> {
			userEntity user = userEntity.findById(Integer.parseInt(e)).get();
			return topCard(user.u_no, user.u_nick);
		}).toArray(JComponent[]::new)).setBackColor(Color.white));
		
		return set(
				row(0, fh(profileIcon), hg(50), myStroy, f(sc)),
				BORDER(sp.em(10, 10, 10, 0)), BG(Color.white));
	}
	
	private JPanel topCard(int uno, String s) {
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int imgS = 20;
				int line = 2;
				Graphics2D g2 =(Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setStroke(new BasicStroke(line));
				
				if(uno == 0) {
					g2.drawImage(plusToColor(), (getWidth() - imgS) / 2, (getHeight() - imgS) / 2, 20, 20, null);
					g2.setColor(sp.color);
					g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
					return;
				}
				g2.drawImage(sp.circleLine(sp.circleImage(uno, getWidth()), sp.lineColor), 0, 0, getWidth(), getHeight(), null);
			}
		};
		String str = s.length() > 8 ? s.substring(0, 8) + "..." : s;
		if(uno != 0) {
			topComps.add(img);
			aList.add(uno);
		}else str = s;
		MouseAdapter scma = new MouseAdapter() {
			int x = 0;
			@Override
			public void mousePressed(MouseEvent e) { x = e.getX(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				sc.getHorizontalScrollBar().setValue(sc.getHorizontalScrollBar().getValue() - e.getX() + x );
				x = e.getX();
			}
		};
		img.addMouseListener(scma);
		img.addMouseMotionListener(scma);
		img.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(uno);
			}
		});
		JPanel panel = col(5, set(img, SIZE(60, 60)), lb(str, HOA(JLabel.CENTER), FONT(sp.font.deriveFont(11f)))).setBackColor(Color.white);
		return panel;
	}
	
	private void setMainPanel(JPanel panel) {
		List<Integer> us = Util.myFollowing().get(sp.user.u_no);
		List<postEntity> list =  postEntity.findBy(e -> us.contains(e.u_no));
		Collections.shuffle(list);
		String[] icons = "heart,comment,send".split(",");
		for(int i = 0; i < list.size(); i++) {
			int index = i;
			postEntity post = list.get(index);
			userEntity user = userEntity.findById(post.u_no).get();
			int likes = likesEntity.findBy(e -> e.p_no.equals(post.p_no)).size();
			String[] posts = post.p_files.split(",");
			
			
			List<Rectangle> rect = Arrays.asList(new Rectangle(), new Rectangle());
			JLabel topLabel = lb(user.u_nick, ICON(new ImageIcon(sp.circleLine(sp.circleImage(user.u_no, 37), sp.lineColor))), BORDER(sp.em(0,10,5,5)));
			JLabel numberLabel = lb("1 / " + posts.length, FG(Color.white), BG(new Color(0,0,0,100)), HOA(JLabel.CENTER));
			numberLabel.setOpaque(true); numberLabel.setBounds(280, 10, 50, 25);
			var imgs = new JLabel() {
				public int s = 0;
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					rect.set(0, new Rectangle());
					rect.set(1, new Rectangle());
					g.drawImage(new ImageIcon("datafiles/posts/" + posts[s] + ".jpg").getImage(), 0, 0, getWidth(),getHeight(), null);
					g.setColor(Color.white);
					g.setFont(sp.font.deriveFont(25f).deriveFont(1));
					FontMetrics fm = g.getFontMetrics();
					if(s != posts.length - 1) {
						int sx = getWidth() - 20 - fm.stringWidth(">");
						int sy = getHeight() / 2;
						g.drawString(">", sx, sy);
						rect.set(0, new Rectangle(sx, sy - fm.getHeight(), fm.stringWidth(">"), fm.getHeight()));
					}
					if(s != 0) {
						g.drawString("<", 20, getHeight() / 2);
						rect.set(1, new Rectangle(20, getHeight() / 2 - fm.getHeight(), fm.stringWidth("<"), fm.getHeight()));
					}
				};
			};
			imgs.add(numberLabel);
			imgs.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					for(int i = 0; i < rect.size(); i++) {
						if(!rect.get(i).contains(e.getPoint())) continue;
						imgs.s = imgs.s + (i == 0 ? 1 : -1);
						numberLabel.setText((imgs.s + 1) + " / " + posts.length);
						imgs.repaint();
					}
				};
			});
			
			JComponent[] iconLabels = IntStream.range(0, 3).mapToObj(e -> {
				String s = icons[e] + (e == 0 ? (likesEntity.findBy(l -> l.u_no.equals(sp.user.u_no) && l.p_no.equals(post.p_no)).isEmpty() ? "2" : "1") : "");
				JLabel l = set(new JLabel(sp.getImage("datafiles/icons/" + s + ".png", 40, 40)), SIZE(40, 40));
				return fh(l);
				
			}).toArray(JComponent[]::new);
			iconLabels[0].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(likesEntity.findBy(l -> l.u_no.equals(sp.user.u_no) && l.p_no.equals(post.p_no)).isEmpty())
						System.out.println("추가");
					else
						System.out.println("삭제");
				};
			});
			JPanel iconsp = row(10, 10, 10, iconLabels).setBackColor(Color.white);
			iconsp.add(hg());
			
			JPanel infor = col(5, 
					fw(lb("좋아요 " + likes + "개", FONT(sp.font.deriveFont(1)))),
					fw(lb(post.p_content, FONT(sp.font))),
					fw(lb(post.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")), FONT(sp.font.deriveFont(11f)), FG(Color.gray)))
					).setBackColor(Color.white);
			
			JPanel p = set(new JPanel(new BorderLayout()), BG(Color.white));
			p.add(set(imgs, SIZE(0, 400)));
			p.add(topLabel, BorderLayout.NORTH);
			p.add(col(0, iconsp, infor).setBackColor(Color.white), BorderLayout.SOUTH);
			panel.add(p);
		}
	}

	
	private JPanel rightCard(userEntity user) {
		JLabel img = lb("", ICON(sp.circleImage(user.u_no, 40)), SIZE(40, 40));
		JLabel text = lb(user.u_nick.length() > 12 ? user.u_nick.substring(0, 12) + "..." : user.u_nick, FONT(sp.font.deriveFont(1)));
		JLabel follow = lb("팔로우", FG(sp.color), FONT(sp.font));
		return row(5, img, fw(text), hg(20), follow).setBackColor(Color.white);
	}
	
	private Image plusToColor() {
		Image img = new ImageIcon("datafiles/icons/plus.png").getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bf.getHeight(); y++) {
				for(int x = 0; x < bf.getWidth(); x++) {
					if((bf.getRGB(x, y) & 0xFFFFFF) < 0x222222) {
						bf.setRGB(x, y, sp.color.getRGB());
					}else {
						bf.setRGB(x, y, 0);
					}
				}
			}
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	@Override
	protected void action() {
		MouseAdapter scma = new MouseAdapter() {
			int x = 0;
			@Override
			public void mousePressed(MouseEvent e) { x = e.getX(); }
			@Override
			public void mouseDragged(MouseEvent e) {
				sc.getHorizontalScrollBar().setValue(sc.getHorizontalScrollBar().getValue() - e.getX() + x );
				x = e.getX();
			}
		};
		sc.addMouseListener(scma);
		sc.addMouseMotionListener(scma);
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
