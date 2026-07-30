package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import orms.likesEntity;
import orms.postEntity;
import orms.replyEntity;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Comments extends CFrame{
	String[] imgNumbers;
	postEntity post;
	List<likesEntity> likeStatus = new ArrayList<>();
	
	JLabel likeLabel = lb("", FONT(sp.font.deriveFont(1).deriveFont(14f)));
	JTextField tf = comp(JTextField::new, SIZE(0, 30));
	JLabel insert = lb("게시", FG(sp.color));
	JLabel likeImg = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) { super.paintComponent(g); g.drawImage(new ImageIcon("datafiles/icons/" + heart +  ".png").getImage(), 0, 0, getWidth(), getHeight(), null); };
	};
	JLabel sendImg = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) { super.paintComponent(g); g.drawImage(new ImageIcon("datafiles/icons/send.png").getImage(), 0, 0, getWidth(), getHeight(), null); };
	};
	
	JLabel imgLabel = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			g.drawImage(new ImageIcon("datafiles/posts/" + imgNumbers[imgN] + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
		};
	};
	
	JPanel mainPanel = set(new JPanel(new GridLayout(0, 1, 10, 10)), BG(Color.white), BORDER(sp.em(5, 5, 5, 5)));
	
	int imgN = 0;
	Timer timer = new Timer(1000, e -> {
		imgN ++;
		if(imgN >= imgNumbers.length) imgN = 0;
		imgLabel.repaint();
	});
	String heart = "heart1";
	public Comments(int pno) {
		post = postEntity.findById(pno).get();
		imgNumbers = post.p_files.split(",");
		setComments();
		setHeart();
		setFramed("댓금", 750,  450, () -> timer.stop());
	}

	private void setHeart() {
		likeStatus.clear();
		likeStatus.addAll(likesEntity.findBy(e -> e.p_no.equals(post.p_no) && e.u_no.equals(sp.user.u_no)));
		heart = "heart" + (likeStatus.isEmpty() ? 1 : 2);
		likeLabel.setText("종아요" + post.p_like + "개");
		repaint();
	}
	@Override
	protected void desing() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 0,0));
		set(panel, BG(Color.white));
		
		panel.add(imgLabel);
		panel.add(setPanel());
		add(panel);
		timer.start();
	}

	private JPanel setPanel() {
		userEntity auther = userEntity.findById(post.u_no).get();
		JLabel profile = set(lb(auther.u_nick, ICON(sp.circleImage(auther.u_no, 35))), BORDER(sp.em(10, 20, 10, 10)));
		
		JPanel p2 = col(5, 
				fw(set(row(50, 50, 50, set(likeImg, SIZE(30, 40)), set(sendImg, SIZE(30, 40)), hg()).setBackColor(Color.white), BORDER(sp.em(0, 0, 0, 0)))),
				fw(likeLabel), 
				fw(lb(post.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")), FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)))),
				fw(row(0, fw(tf), set(insert, FONT(sp.font.deriveFont(13f)),SIZE(75, 30), HOA(JLabel.CENTER))).setBackColor(Color.white))
			).setBackColor(Color.white);
		
		JScrollPane sc = comp(JScrollPane::new, BG(Color.white), BORDER(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.LIGHT_GRAY)));
		sc.setViewportView(mainPanel);
		JPanel panel = col(0, fw(profile), f(sc), p2).setBackColor(Color.white);
		
		return panel;
	}
	
	private void setComments() {
		mainPanel.removeAll();
		List<replyEntity> list = replyEntity.findBy(e -> e.p_no.equals(post.p_no));
		if(true) {
			postEntity r = post;
			userEntity user = userEntity.findById(r.u_no).get();
			JPanel nameReply = row(3, lb(user.u_nick, FONT(sp.font.deriveFont(13f).deriveFont(1))), lb(r.p_content, FONT(sp.font))).setBackColor(Color.white);
			JLabel dateLabel = lb(r.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")), FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)));
			JPanel p = row(5, lb("", ICON(sp.circleImage(r.u_no, 40))), fw(col(2, nameReply, fw(dateLabel)).setBackColor(Color.white))).setBackColor(Color.white);
			mainPanel.add(p);
		}
		for(int i = 0; i < list.size(); i++) {
			replyEntity r = list.get(i);
			userEntity user = userEntity.findById(r.u_no).get();
			JPanel nameReply = row(3, lb(user.u_nick, FONT(sp.font.deriveFont(13f).deriveFont(1))), lb(r.r_content, FONT(sp.font))).setBackColor(Color.white);
			JLabel dateLabel = lb(r.r_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")), FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)));
			JPanel p = row(5, lb("", ICON(sp.circleImage(r.u_no, 40))), fw(col(2, nameReply, fw(dateLabel)).setBackColor(Color.white))).setBackColor(Color.white);
			mainPanel.add(p);
		}
		repaint();
	}
	
	@Override
	protected void action() {
		likeImg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(likeStatus.isEmpty()) {
					likesEntity like = new likesEntity();
					like.p_no = post.p_no; like.u_no = sp.user.u_no; like.l_date = LocalDateTime.now();
					post.p_like += 1;

					post.save();
					like.save();
				}else {
					likeStatus.get(0).delete();
					post.p_like -= 1;
					
					post.save();
				}
				setHeart();
			}
		});
		insert.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				comInsert();
			}
		});
		tf.addActionListener(e -> comInsert());
	}
	
	private void comInsert() {
		if(tf.getText().isBlank()) throw new RuntimeException("댓글을 입력하세요.");
		replyEntity r = new replyEntity();
		r.p_no = post.p_no; r.u_no = sp.user.u_no; r.r_content = tf.getText(); r.r_date = LocalDateTime.now();
		r.save();
		tf.setText("");
		setComments();
		sp.info("댓글이 작성되었습니다.");
	}
	public static void main(String[] args) {
		Util.start(new Comments(95));
	}
}
