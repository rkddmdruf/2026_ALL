package test.test1;

import main.Util;
import orms.likesEntity;
import orms.postEntity;
import orms.userEntity;

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
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D.Double;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import utils.*;

public class Main extends CFrame{
	JPanel menuPanel;
	Map<Integer, List<Integer>> myfollow = new HashMap<>();
	Map<Integer, List<Integer>> myfollowing = new HashMap<>();
	{
		setFollowMap();
	}
	
	List<Integer> userLabelsNumber= new ArrayList<>();
	List<JLabel> userLabels = new ArrayList<>();
	JScrollPane sc, mainSc = comp(JScrollPane::new, BG(Color.white), BORDER(sp.em(10, 10, 0, 10)));
	JPanel myStory = set(topCard(null), BORDER(sp.em(10, 0, 10, 0)));
	BoxPanel mainPanel = set(col(10), BG(Color.white), BORDER(sp.em(15, 15, 15, 15)));
	public Main() {
		setFrame("ITGRAM", 800, 700);
	}

	private void setFollowMap() {
		userEntity.findAll().forEach(us -> {
			List<Integer> list = Arrays.asList(us.u_follow.split(",")).stream().map(e -> Integer.parseInt(e)).collect(Collectors.toList()); 
			list.forEach(u -> myfollow.computeIfAbsent(u, k -> new ArrayList<>()).add(us.u_no));
			list.forEach(u -> myfollowing.computeIfAbsent(us.u_no, k -> new ArrayList<>()).add(u));
		});
	}

	@Override
	protected void desing() {
		setMainPanel(mainPanel);
		menuPanel = set(new JPanel(new GridLayout(5, 1, 20, 20)), BORDER(sp.em(120, 10, 120, 50)), BG(Color.white));
		String[] imgns = "home,search,send,heart1,plus".split(",");
		Arrays.stream(imgns).forEach(s -> menuPanel.add(lb("", ICON(sp.getImage("icons/" + s, 40, 40)))));
		
		JPanel followPanel = col(20, 10, 0, lb("회원님을 위한 추천", FONT(sp.font.deriveFont(14f).deriveFont(1)), BORDER(sp.em(0, 10, 0, 100)))).setBackColor(Color.white);
		
		JPanel p1 = new JPanel(new BorderLayout());
		p1.add(topPanel(), BorderLayout.NORTH);
		p1.add(menuPanel, BorderLayout.WEST);
		p1.add(mainSc);
		mainSc.setViewportView(mainPanel);
		
		JPanel p2 = col(10, bt("fsdf"), bt("fsdf"), bt("fsdf"));
		add(row(0, f(p1), fh(followPanel)));
	}
	
	private void setMainPanel(BoxPanel panel) {
		List<Integer> us = Util.myFollowing().get(sp.user.u_no);
		List<postEntity> list =  postEntity.findBy(e -> us.contains(e.u_no));
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
			
			JLabel[] iconLabels = IntStream.range(0, 3).mapToObj(e -> {
				String s = icons[e] + (e == 0 ? (likesEntity.findBy(l -> l.u_no.equals(sp.user.u_no) && l.p_no.equals(post.p_no)).isEmpty() ? "2" : "1") : "");
				JLabel l = set(new JLabel(sp.getImage("icons/" + s, 40, 40)), SIZE(40, 40));
				return fh(l);
				
			}).toArray(JLabel[]::new);
			iconLabels[0].addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(likesEntity.findBy(l -> l.u_no.equals(sp.user.u_no) && l.p_no.equals(post.p_no)).isEmpty()) {
						likesEntity l = new likesEntity();
						l.p_no = post.p_no;
						l.u_no = sp.user.u_no;
						l.l_date = LocalDateTime.now();
						post.p_like += 1;
						
						l.save();
						post.save();
						iconLabels[0].setIcon(sp.getImage("icons/heart2", 40, 40));
					}
					else {
						likesEntity.findBy(l -> l.u_no.equals(sp.user.u_no) && l.p_no.equals(post.p_no)).get(0).delete();
						post.p_like -= 1;
						post.save();
						iconLabels[0].setIcon(sp.getImage("icons/heart1", 40, 40));
					}
					repaint();
					revalidate();
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
			panel.addz(p);
		}
	}

	private JPanel topPanel() {
		sc = set(new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BORDER(null), BG(Color.white));
		JLabel profileLabel = lb("", ICON(sp.getImage("icons/profile", 40, 40)));
		List<String> us = Arrays.asList(sp.user.u_follow.split(","));
		
		sc.setViewportView(set(row(20, us.stream().map(u -> {
			return topCard(userEntity.findById(Integer.parseInt(u)).get());
		}).toArray(JComponent[]::new)), BORDER(sp.em(10, 10, 10, 10))).setBackColor(Color.white));
		
		JPanel p = row(10, 10, 0, fh(profileLabel), hg(40), myStory, f(sc)).setBackColor(Color.white);
		return p;
	}
	
	private JPanel topCard(userEntity u) {
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				if(u == null) {
					g2.setColor(sp.color);
					g2.setStroke(new BasicStroke(2f));
					g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
					g2.drawImage(plusImage(), getWidth() / 2 - 10, getHeight() / 2 - 10, 20, 20, null);
				}
				
				System.out.println(getWidth() + ", " + getHeight());
				if(u != null) g.drawImage(circleLine(circleImage(u.u_no, 70), sp.lineColor), 0, 0, getWidth(), getHeight(), null);
			}
		};
		if(u != null) {
			userLabels.add(img);
			userLabelsNumber.add(u.u_no);
		}
		JLabel text = lb(u == null ? "내 스토리" : (u.u_nick.length() > 8 ? u.u_nick.substring(0, 8) + "..." : u.u_nick), HOA(JLabel.CENTER), FONT(sp.font.deriveFont(11f)));
		return col(5, f(set(img, SIZE(70, 70))), fw(text)).setBackColor(Color.white);
	}
	@Override
	protected void action() {
		MouseAdapter ma = new MouseAdapter() {
			int x;
			@Override
			public void mousePressed(MouseEvent e) {
				x = e.getX();
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				sc.getHorizontalScrollBar().setValue(sc.getHorizontalScrollBar().getValue() + x - e.getX());
				x = e.getX();
			}
		};
		
		for(int i = 0; i < userLabels.size(); i++) {
			int index = i;
			userLabels.get(i).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.println(userLabelsNumber.get(index));
					dispose();
				}
			});
		}
		sc.addMouseListener(ma);
		sc.addMouseMotionListener(ma);
		
		menuPanel.getComponent(1).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Search();
				dispose();
			}
		});
	}

	public static Image plusImage() {
		Image img= new ImageIcon("datafiles/icons/plus.png").getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bf.getHeight(); y++) {
				for(int x = 0; x < bf.getWidth(); x++)
				{
					if((bf.getRGB(x, y) & 0xFFFFFF) < 0x222222) bf.setRGB(x, y, sp.color.getRGB());
					else bf.setRGB(x, y, 0);
				}
			}
			return bf;
		} catch (Exception e) {
			return img;
		}
	}
	public static ImageIcon circleImage(int uno, int size) {
		Image img = new ImageIcon("datafiles/profile/" + uno + ".jpg").getImage();
		if(img.getWidth(null) == 0) return null;
		try {
			BufferedImage bf = new BufferedImage(size, size, BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.setClip(new Ellipse2D.Double(0, 0, size, size));
			g2.drawImage(img, 0, 0, size, size, null);
			g2.dispose();
			return new ImageIcon(bf);
		} catch (Exception e) {
			return new ImageIcon(img);
		}
	}
	
	public static Image circleLine(ImageIcon icon, Color color) {
		Image img = icon.getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(img, 4, 4, bf.getWidth() - 8, bf.getHeight() - 8, null);
			g2.setStroke(new BasicStroke(2f));
			g2.setColor(color);
			g2.drawOval(1, 1, bf.getWidth() - 3, bf.getHeight() - 3);
			g2.dispose();
			return bf;
		} catch (Exception e) {
			return img;
		}
	}
	public static void main(String[] args) {
		Util.start(new Main());
	}
}
