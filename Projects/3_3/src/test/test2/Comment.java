package test.test2;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;

import main.*;
import orms.*;


public class Comment extends CFrame{
	
	String[] imgs;
	int imgN = 0;
	JLabel img = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			g.drawImage(new ImageIcon("datafiles/posts/" + imgs[imgN] + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
		};
	};
	postEntity post;
	userEntity postUser;
	
	JLabel heartLabel = lb("", SIZE(30, 30));
	JLabel sendLabel = lb("", ICON(sp.getImage("icons/send", 30, 30)));
	JLabel likeCount = lb("0", FONT(sp.font.deriveFont(1)));
	
	JTextField t1 = tf("");
	JLabel upload = lb("게시", FG(sp.color), FONT(sp.font.deriveFont(1)), HOA(JLabel.CENTER));
	
	JScrollPane sc = comp(JScrollPane::new, BG(Color.white), BORDER(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.lightGray)));
	
	public Comment(int pno) {
		post = postEntity.findById(pno).get();
		postUser = userEntity.findById(post.u_no).get();
		imgs = post.p_files.split(",");
		setFrame("댓금", 800, 550);
		
		setHeart();
		setLikeLabel();
		
		Timer timer = new Timer(1000, e -> {
			imgN++;
			if(imgN >= imgs.length) imgN = 0;
			repaint();
		});
		timer.start();
	}

	
	private void setLikeLabel() {
		likeCount.setText("종아요 " + post.p_like + "개");
	}
	
	private void setHeart() {
		if(likesEntity.findBy(e -> e.p_no.equals(post.p_no) && e.u_no.equals(sp.user.u_no)).isEmpty()) heartLabel.setIcon(sp.getImage("icons/heart1", 30, 30));
		else heartLabel.setIcon(sp.getImage("icons/heart2", 30, 30));
	}
	
	@Override
	protected void desing() {
		JPanel panel = set(new JPanel(new GridLayout()), BG(Color.white));
		panel.add(img);
		panel.add(mainPanel());
		add(panel);
	}

	
	private JPanel mainPanel() {
		setScrollPanel();
		return col(0, fw(set(lb(postUser.u_nick, ICON(sp.circleImage(postUser.u_no, 40))), BG(Color.white), BORDER(sp.em(10, 20, 10, 0)))), f(sc), bottomPanel()).setBackColor(Color.white);
	}
	
	private void setScrollPanel() {
		BoxPanel c = set(col(10), BORDER(sp.em(10, 10, 10, 10)), BG(Color.white));
		c.addz(card(postUser.u_no, postUser.u_nick, post.p_content, post.p_date));
		replyEntity.findBy(e -> e.p_no.equals(post.p_no)).forEach(e -> {
			c.addz(card(e.u_no, userEntity.findById(e.u_no).get().u_nick, e.r_content, e.r_date));
		});;
		sc.setViewportView(c);
	}
	
	private JPanel bottomPanel() {
		JPanel iconPanel = row(30, 20, 20, heartLabel, sendLabel, hg()).setBackColor(Color.white);
		return set(col(20, 20, 10, iconPanel, likeCount, 
				fw(lb(post.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")), FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY))),
				fw(row(0, f(t1), set(upload, SIZE(80, 25))))
				), BG(Color.white), BORDER(sp.em(0, 10, 0, 10)));
	}
	
	private JPanel card(int uno, String nick, String str, LocalDateTime dt) {
		return row(10, lb("", ICON(sp.circleImage(uno, 45))), 
				col(10, 
					row(5, fw(lb(nick, FONT(sp.font.deriveFont(1)))), fw(lb(str, FONT(sp.font)))).setBackColor(Color.white), 
					fw(lb(dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")), FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY)))).setBackColor(Color.white)
				).setBackColor(Color.white);
	}
	@Override
	protected void action() {
		
	}

	public static void main(String[] args) {
		Util.start(new Comment(10));
	}
	
}
