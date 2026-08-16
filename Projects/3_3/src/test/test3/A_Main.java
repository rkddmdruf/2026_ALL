package test.test3;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import main.Util;
import orms.*;

public class A_Main extends CFrame {
	JLabel myl = new JLabel(new ImageIcon(new ImageIcon("datafiles/profile/1.jpg").getImage().getScaledInstance(60, 60, 4))) {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = sp.anti(g);
			g2.setColor(sp.color);
			g2.setStroke(new BasicStroke(2f));
			g2.drawOval(1, 1, 56, 56);
			
			int line = 7;
			int sx = getWidth() / 2 - 1, sy = getHeight() / 2 - 1;
			g2.drawLine(sx - line, sy, sx + line, sy);
			g2.drawLine(sx, sy - line, sx, sy + line);
		};
	};
	JScrollPane sc = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public A_Main() {
		setFrame("ITGRAM", 675, 600);
	}

	protected void desing() {
		String[] imgNames = "home,search,send,heart1,plus".split(",");
		
		List<Integer> list = new ArrayList<>();
		sp.user.myFollowing().forEach(e -> list.addAll(userEntity.findById(e).get().myFollowing()));
		Map<Integer, Integer> mapCount = new HashMap<>();
		list.removeIf(e -> sp.user.myFollowing().contains(e) || sp.user.u_no.equals(e));
		list.forEach(e -> mapCount.put(e, mapCount.getOrDefault(e, 0) + 1));
		List<Integer> mapList = new ArrayList<Integer>(mapCount.keySet());
		mapList.sort((a, b) -> {
			int n = Integer.compare(mapCount.get(b), mapCount.get(a));
			if(n != 0) return n;
			return userEntity.findById(b).get().myFollow().size() - userEntity.findById(a).get().myFollow().size();
		});
		
		JPanel p2 = set(col(10, 
					fw(lb("회원님을 위한 추천", FONT(sp.font.deriveFont(13f).deriveFont(1)))),
					fw(cardB(mapList.get(0))),
					fw(cardB(mapList.get(1))),
					fw(cardB(mapList.get(2))),
					fw(cardB(mapList.get(3))),
					fw(cardB(mapList.get(4)))
				), BG(Color.white), BORDER(sp.em(20, 0, 0, 0)));
		JPanel p = new JPanel(new BorderLayout());
		sc.setViewportView(topPanel());
		
		p.add(set(row(10, 
					lb("", ICON(sp.getImage("icons/profile", 35, 35))), 
					hg(35), 
					col(0, f(set(myl, SIZE(60, 60))), lb("내 스토리", FONT(sp.font.deriveFont(11f)))).setBackColor(Color.white),
					f(set(sc, BORDER(null), BG(Color.white)))
				), BG(Color.white), BORDER(sp.em(10, 10, 10, 0))), BorderLayout.NORTH);
		p.add(set(col(10,
					Arrays.stream(imgNames).map(e -> {
						JLabel l = new JLabel(sp.getImage("icons/" + e, 40, 40));
						l.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent ae) {
								if(e.equals("search")) {
									new Search();
									dispose();
								}
							}
						});
						return fh(col(0, l)).setBackColor(Color.white);
					}).toArray(JComponent[]::new)
				), BG(Color.white), BORDER(sp.em(70, 10, 70, 50))), BorderLayout.WEST);
		p.add(set(new JScrollPane(scPanel()), BG(Color.white), BORDER(null)));
		add(set(row(10, f(p), fh(p2)), BG(Color.white)));
	}

	private JPanel scPanel() {
		JPanel panel = set(new JPanel(new GridLayout(0, 1, 10, 10)), BG(Color.white), BORDER(sp.em(15, 15, 15, 15)));
		List<postEntity> list =  postEntity.findBy(e -> sp.user.myFollowing().contains(e.u_no));
		Collections.shuffle(list);
		for(int i = 0; i < list.size(); i++) {
			postEntity p = list.get(i);
			userEntity u = userEntity.findById(list.get(i).u_no).get();
			String[] ps = p.p_files.split(",");
			List<Rectangle> rect = Arrays.asList(new Rectangle(), new Rectangle());
			JLabel topLabel = lb(u.u_nick, ICON(new ImageIcon(sp.circleLine(sp.circleImage(u.u_no, 37), sp.lineColor))), BORDER(sp.em(0,10,5,5)));
			JLabel numberLabel = lb("1 / " + ps.length, FG(Color.white), BG(new Color(0,0,0,100)), HOA(JLabel.CENTER));
			numberLabel.setOpaque(true); numberLabel.setBounds(280, 10, 50, 25);
			var img = new JLabel() {
				public int s = 0;
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					rect.set(0, new Rectangle());
					rect.set(1, new Rectangle());
					g.drawImage(new ImageIcon("datafiles/posts/" + ps[s] + ".jpg").getImage(), 0, 0, getWidth(),getHeight(), null);
					g.setColor(Color.white);
					g.setFont(sp.font.deriveFont(25f).deriveFont(1));
					FontMetrics fm = g.getFontMetrics();
					if(s != ps.length - 1) {
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
			img.add(numberLabel);
			set(img, SIZE(0, 350));
			img.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					System.out.println("fsd");
					if(rect.get(1).contains(e.getPoint()) && img.s >= 1) img.s--;
					if(rect.get(0).contains(e.getPoint()) && img.s < ps.length) img.s++;
					numberLabel.setText((img.s + 1) + " / " + ps.length);
					img.repaint();
				};
			});
			JPanel imgPanel = set(row(20, 
					lb("", ICON(sp.getImage("icons/heart1", 30, 30))),
					lb("", ICON(sp.getImage("icons/comment", 30, 30))),
					lb("", ICON(sp.getImage("icons/send", 30, 30)))
					), BORDER(sp.em(5, 5, 5, 5)), BG(Color.white));
			panel.add(col(5, 
					fw(lb(u.u_nick, ICON(sp.circleLine2(sp.circleImage(u.u_no, 30), sp.lineColor)), BORDER(sp.em(0, 10, 0, 0)), HOA(JLabel.LEFT))),
					f(img),
					fw(imgPanel),
					fw(col(3,
							fw(lb("좋아요 " + likesEntity.findBy(ll -> ll.p_no.equals(p.p_no)).size() + "개", FONT(sp.font))),
							fw(lb(p.p_content, FONT(sp.font))),
							fw(lb(p.p_date.toLocalDate() + " " + p.p_date.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm:ss.S")), FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(10f))))
							).setBackColor(Color.white))
				).setBackColor(Color.white));
		}
		
		return panel;
	}

	private JPanel topPanel() {
		JPanel p = set(new JPanel(new GridLayout(1, 0, 10, 10)), BG(Color.white));
		sp.user.myFollowing().forEach(e -> {
			userEntity u = userEntity.findById(e).get();
			JLabel img = lb("", ICON(sp.circleLine2(sp.circleImage(e, 60), sp.lineColor)));
			MouseAdapter mac = new MouseAdapter() {
				int x = 0;
				@Override
				public void mousePressed(MouseEvent e) {
					x = e.getX();
				}
				@Override
				public void mouseDragged(MouseEvent e) {
					sc.getHorizontalScrollBar().setValue(sc.getHorizontalScrollBar().getValue() + x - e.getX());
					x = e.getX();
				}
				public void mouseClicked(MouseEvent e) {
					new Stroy(u);
					dispose();
				}
			};
			img.addMouseListener(mac);
			img.addMouseMotionListener(mac);
			p.add(col(0, f(img), lb(u.u_nick.length() > 8 ? u.u_nick.substring(0, 8) + "..." : u.u_nick, FONT(sp.font.deriveFont(11f)))).setBackColor(Color.white));
		});
		return p;
	}

	private JPanel cardB(Integer n) {
		userEntity user = userEntity.findById(n).get();
		JLabel l = lb("팔로우", FG(sp.color));
		return row(5, lb("", ICON(sp.circleImage(n, 35))), lb(user.u_nick.length() >= 12 ? user.u_nick.substring(0, 12) + "...": user.u_nick), hg(), l).setBackColor(Color.white);
	}

	protected void action() {
		myl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new StoryAdd();
				dispose();
			}
		});
		
		MouseAdapter mac = new MouseAdapter() {
			int x = 0;
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
		sc.addMouseListener(mac);
		sc.addMouseMotionListener(mac);
	}
	
	public static void main(String[] args) {
		Util.start(new A_Main());
	}
}