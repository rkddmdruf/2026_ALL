package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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

public class Reply extends CFrame{
	postEntity post;
	String[] files;
	int imgIndex = 0;

	JLabel img = new JLabel() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(new ImageIcon("datafiles/posts/" + files[imgIndex] + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
		};
	};

	JPanel userArea = set(new JPanel(), SIZE(0, 50), BG(Color.white));
	JPanel divider = set(new JPanel(), SIZE(0, 1), BG(Color.LIGHT_GRAY));

	JScrollPane replySc = set(new JScrollPane(), BORDER(null), BG(Color.white));

	JLabel heart = set(new JLabel(), SIZE(28, 28));
	JLabel send = set(new JLabel(sp.getImage("datafiles/icons/send.png", 28, 28)), SIZE(28, 28));

	JLabel likeLabel = lb("", FONT(sp.font.deriveFont(1)));
	JLabel captionLabel = lb("", FONT(sp.font));
	JLabel dateLabel = lb("", FONT(sp.font.deriveFont(10f)), FG(Color.gray));

	JTextField input = comp(JTextField::new, SIZE(0, 35));
	JButton postBt = bt("게시", FONT(sp.font.deriveFont(1)), FG(sp.color));

	Timer timer = new Timer(1000, e -> nextImage());

	public Reply(int pno) {
		post = postEntity.findById(pno).orElse(null);
		setFrame("댓글", 700, 500);
	}

	@Override
	protected void desing() {
		if(post == null) return;
		files = post.p_files.split(",");

		refreshLike();
		refreshInfo();
		replySc.setViewportView(replyList());

		JPanel iconRow = row(10, heart, send).setBackColor(Color.white);
		JPanel inputRow = row(10, f(input), postBt).setBackColor(Color.white);

		JPanel rightBottom = col(8, f(replySc), fw(iconRow), fw(likeLabel), fw(captionLabel), fw(dateLabel), fw(inputRow)).setBackColor(Color.white);
		JPanel right = set(col(0, fw(userArea), fw(divider), f(rightBottom)).setBackColor(Color.white), SIZE(300, 0));

		JPanel panel = new JPanel(new GridLayout(1, 2, 0, 0));
		panel.add(img);
		panel.add(right);
		set(panel, BG(Color.white));
		add(panel);
		timer.start();
	}

	private JPanel replyList() {
		JComponent[] rows = replyEntity.findBy(r -> r.p_no.equals(post.p_no)).stream().map(this::replyRow).toArray(JComponent[]::new);
		return col(12, rows).setBackColor(Color.white);
	}

	private JPanel replyRow(replyEntity r) {
		userEntity user = userEntity.findById(r.u_no).get();
		JLabel icon = lb("", ICON(sp.circleImage(user.u_no, 32)), SIZE(32, 32));
		JLabel nick = lb(user.u_nick, FONT(sp.font.deriveFont(1)));
		JLabel body = lb(r.r_content, FONT(sp.font));
		JLabel date = lb(r.r_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), FONT(sp.font.deriveFont(10f)), FG(Color.gray));
		return row(8, icon, fw(col(2, fw(row(6, nick, body)), fw(date)))).setBackColor(Color.white);
	}

	private void nextImage() {
		imgIndex++;
		if(imgIndex >= files.length) imgIndex = 0;
		img.repaint();
	}

	private void refreshLike() {
		boolean liked = !likesEntity.findBy(l -> l.u_no.equals(sp.user.u_no) && l.p_no.equals(post.p_no)).isEmpty();
		heart.setIcon(sp.getImage("datafiles/icons/" + (liked ? "heart1" : "heart2") + ".png", 28, 28));
	}

	private void refreshInfo() {
		int likeCount = likesEntity.findBy(l -> l.p_no.equals(post.p_no)).size();
		likeLabel.setText("좋아요 " + likeCount + "개");
		captionLabel.setText(post.p_content);
		dateLabel.setText(post.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")));
	}

	@Override
	protected void action() {
		if(post == null) return;

		ActionListener postAction = e -> {
			if(input.getText().isBlank()) throw new RuntimeException("댓글을 입력하세요.");

			replyEntity r = new replyEntity();
			r.p_no = post.p_no;
			r.u_no = sp.user.u_no;
			r.r_content = input.getText();
			r.r_date = LocalDateTime.now();
			r.save();

			sp.info("댓글이 작성되었습니다.");
			input.setText("");
			replySc.setViewportView(replyList());
		};
		postBt.addActionListener(postAction);
		input.addActionListener(postAction);
	}

	public static void main(String[] args) {
		Util.start(new Reply(1));
	}
}
