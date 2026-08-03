package test.test2;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.geom.AffineTransform;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import main.Util;
import orms.postEntity;
import orms.userEntity;


public class Main extends CFrame{
	JScrollPane tsc = set(new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BG(Color.white), BORDER(null));
	
	Map<Integer, JLabel> storyMap = new HashMap<>();
	
	List<JLabel> menuLabel = Arrays.asList(lb("home"), lb("search"), lb("send"), lb("heart1"), lb("plus"));
	JLabel myStory = new JLabel() {
		@Override
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setStroke(new BasicStroke(2f));
			g2.setColor(sp.color);
			g2.drawOval(1, 1, getWidth() - 3, getHeight() - 3);
			
			AffineTransform old = g2.getTransform();
			g2.translate(getWidth() / 2,  getHeight() / 2);
			for(int i = 0; i < 4; i++) {
				g2.rotate(Math.toRadians(90));
				g2.drawLine(0, 0, 10, 0);
			}
			g2.rotate(0);
			g2.setTransform(old);
		};
	};
	
	public Main() {
		set(myStory, SIZE(70, 70));
		setFrames("ITGRAM", 775, 600, () -> {
			sp.user = null;
			new Login();
		});
	}

	@Override
	protected void desing() {
		JPanel menuPanel = set(new JPanel(new GridLayout(5, 1, 20, 20)), BG(Color.white), BORDER(sp.em(80, 10, 80, 50)));
		menuLabel.forEach(e -> {
			e.setIcon(sp.getImage("icons/" + e.getText(), 40, 40)); e.setText("");
			menuPanel.add(e);
		});
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.white);
		panel.add(createTopPanel(), BorderLayout.NORTH);
		panel.add(menuPanel, BorderLayout.WEST);
		panel.add(set(new JScrollPane(mainPanel()), BORDER(null), BG(Color.white)));
		
		JPanel rightPanel = col(30, 10, 0, 
					lb("회원님을 위한 추천", FONT(sp.font.deriveFont(14f).deriveFont(1)), BORDER(sp.em(0, 0, 0, 100))), 
					col(10, setRightPanel())
				).setBackColor(Color.white);
		
		add(row(0, f(panel), fh(rightPanel)).setBackColor(Color.white));
	}

	private JPanel mainPanel() {
		BoxPanel panel = set(col(10), BORDER(sp.em(20, 30, 10, 30))).setBackColor(Color.white);
		List<Integer> users = Util.myFollowing().get(sp.user.u_no);
		List<postEntity> posts = postEntity.findBy(e -> users.contains(e.u_no));
		Collections.shuffle(posts);
		for(int i = 0; i < posts.size(); i++) {
			postEntity post = posts.get(i);
			userEntity user = userEntity.findById(post.u_no).get();
			
			String[] s = post.p_files.split(",");
			JLabel topLabel = lb(user.u_nick, ICON(new ImageIcon(sp.circleLine(sp.circleImage(user.u_no, 30), sp.lineColor))), FONT(sp.font.deriveFont(1)), BORDER(sp.em(0, 10, 0, 0)));
			
			List<Rectangle> rect = Arrays.asList(new Rectangle(), new Rectangle());
			var img = new JLabel() {
				int number = 0;
				protected void paintComponent(java.awt.Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					
					g2.drawImage(new ImageIcon("datafiles/posts/" + s[number] + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
					g2.setFont(sp.font.deriveFont(1));
					FontMetrics fm = g2.getFontMetrics();
					String str = (number + 1) + " / " + s.length;
					g2.setColor(new Color(0, 0, 0, 150));
					g2.fillRect(getWidth() - fm.stringWidth(str) - 30, fm.getHeight(), fm.stringWidth(str) + 15, 20);
					g2.setColor(Color.white);
					g2.drawString(str, getWidth() - fm.stringWidth(str) - 25, fm.getHeight() * 2 - 3);
					
					g2.setFont(sp.font.deriveFont(25f).deriveFont(1));
					if(number != s.length - 1) {
						int sx = getWidth() - 20 - fm.stringWidth(">");
						int sy = getHeight() / 2;
						g2.drawString(">", sx, sy);
						rect.set(0, new Rectangle(sx, sy - fm.getHeight(), fm.stringWidth(">"), fm.getHeight()));
					}
					if(number != 0) {
						g2.drawString("<", 20, getHeight() / 2);
						rect.set(1, new Rectangle(20, getHeight() / 2 - fm.getHeight(), fm.stringWidth("<"), fm.getHeight()));
					}
				};
			};
			
			img.addMouseListener(new MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if(rect.get(0).contains(e.getPoint()) && img.number < s.length - 1) img.number++;
					else if(rect.get(1).contains(e.getPoint()) && img.number > 0) img.number--;
					repaint();
				};
			});
			set(img, SIZE(0, 325));
			
			JLabel heartL = lb("", ICON(sp.getImage("icons/heart1", 30, 30)));
			JLabel commentL = lb("", ICON(sp.getImage("icons/comment", 30, 30)));
			JLabel sendL = lb("", ICON(sp.getImage("icons/send", 30, 30)));
			
			JLabel likeLabel = lb("종아요 " + post.p_like + "개");
			
			panel.addz(col(10, 
					fw(topLabel),
					f(img), 
					fw(set(row(20, fh(heartL), fh(commentL), fh(sendL)), BG(Color.white))),
					col(2, fw(likeLabel), fw(lb(post.p_content, FONT(sp.font))), fw(lb(post.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S"))
							, FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f))))).setBackColor(Color.white)
					).setBackColor(Color.white));
		}
		return panel;
	}
	
	private JPanel createTopPanel() {
		tsc.setViewportView(followPanel());
		return row(10, 10, 0, 
				lb("", ICON(sp.getImage("icons/profile", 40, 40))), 
				hg(40), 
				fh(col(10, 5, 0, myStory, lb("내 스토리", FONT(sp.font)))).setBackColor(Color.white), 
				f(tsc)).setBackColor(Color.white);
	}
	
	private JPanel followPanel() {
		List<JPanel> list = new ArrayList<>();
		Util.myFollowing().get(sp.user.u_no).forEach(e -> {
			userEntity u = userEntity.findById(e).get();
			list.add(
					col(5, 
						lb("", ICON(new ImageIcon(sp.circleLine(sp.circleImage(e, 70), sp.lineColor)))), 
						lb(u.u_nick.length() > 8 ? u.u_nick.substring(0, 8) + "..." : u.u_nick, FONT(sp.font))
					).setBackColor(Color.white)
				);
		});
		return set(row(10, list.toArray(JComponent[]::new)), BORDER(sp.em(10, 10, 10, 10))).setBackColor(Color.white);
	}

	private JPanel setRightPanel() {
		List<Integer> list = new ArrayList<>();
		Util.myFollowing().get(sp.user.u_no).forEach(u -> {
			list.addAll(Util.myFollowing().get(u));
		});
		Map<Integer, Integer> countMap = new HashMap<>();
		list.removeIf(e -> Util.myFollowing().get(sp.user.u_no).contains(e) || sp.user.u_no.equals(e));
		list.forEach(e -> countMap.put(e, countMap.getOrDefault(e, 0) + 1));
		List<Integer> mapList = new ArrayList<>(countMap.keySet());
		mapList.sort((a, b) -> {
			int n = Integer.compare(countMap.get(b), countMap.get(a));
			if(n != 0) return n;
			return Util.myFollow().get(b).size() - Util.myFollow().get(a).size();
 		});
		
		return col(10, mapList.stream().limit(5).map(e -> {
			userEntity user = userEntity.findById(e).get();
			return row(0, 10, 10, 
					lb("", ICON(sp.circleImage(e, 35))), 
					lb((user.u_nick.length() > 12 ? user.u_nick.substring(0, 12) + "..." : user.u_nick), FONT(sp.font.deriveFont(1))), hg(),
					lb("팔로우", FG(sp.color), FONT(sp.font))
					).setBackColor(Color.white);
		}).toArray(JComponent[]::new)).setBackColor(Color.white);
	}
	
	@Override
	protected void action() {
		
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
