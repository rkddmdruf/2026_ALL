package main;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import orms.postEntity;
import orms.userEntity;

import static utils.Properties.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.List;

import static utils.BoxPanel.*;
import utils.*;

public class Search extends CFrame{
	JLabel left = lb("<", FONT(sp.font.deriveFont(22f)), SIZE(35, 35), HOA(JLabel.CENTER), VEA(JLabel.CENTER));
	JTextField tf = comp(JTextField::new, BG(new JPanel().getBackground()),BORDER(sp.com(sp.line(Color.LIGHT_GRAY), sp.em(5, 5, 5, 5))));
	
	BoxPanel mainPanel = set(col(10).setBackColor(Color.white), BORDER(sp.em(10, 10, 10, 10)));
	JScrollPane sc = comp(JScrollPane::new, BG(Color.white), BORDER(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)));
	
	Timer timer = new Timer(300, e -> {
		System.out.println("text : " + tf.getText() + ", " + tf.getText().isBlank());
		if(tf.getText().isBlank()) {
			mainPanel.removeAll();
			mainPanel.add(lb("검색어를 입력하세요", FG(Color.LIGHT_GRAY)));
		}else reloading();
		
		revalidate();
		repaint();
	});
	
	public Search() {
		timer.setRepeats(false);
		setFrame("검색", 400, 550);
	}

	@Override
	protected void desing() {
		timer.start();
		JPanel topPanel = set(row(10, left, f(tf)).setBackColor(Color.white), BORDER(sp.em(10, 10, 10, 10)));
		col(10).setBackColor(Color.white);
		sc.setViewportView(mainPanel);
		add(col(0, fw(topPanel), f(sc)));
	}

	private void reloading() {
		mainPanel.removeAll();
		List<userEntity> users = userEntity.findBy(u -> u.u_nick.toLowerCase().contains(tf.getText().toLowerCase()));
		List<postEntity> posts = postEntity.findBy(p -> users.stream().anyMatch(u -> u.u_no.equals(p.u_no)));
		
		mainPanel.addz(row(0, hg(180), lb("유저", FONT(sp.font.deriveFont(14f)))).setBackColor(Color.white));
		for(int i = 0; i < users.size(); i++) {
			userEntity user = users.get(i);
			JLabel img = lb("", ICON(sp.circleImage(user.u_no, 45)));
			JPanel p = col(5, fw(lb(user.u_nick, FONT(sp.font))), fw(lb(user.u_name, FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f))))).setBackColor(Color.white);
			mainPanel.addz(row(5, fh(img), p).setBackColor(Color.white));
		}
		mainPanel.addz(row(0, hg(175), lb("게시물", FONT(sp.font.deriveFont(14f)))).setBackColor(Color.white));
		for(int i = 0; i < posts.size(); i++) {
			postEntity post = posts.get(i);
			userEntity user = userEntity.findById(post.u_no).get();
			JLabel img = lb("", ICON(sp.getImage("posts/" + post.p_files.split(",")[0], 50, 50)));
			JPanel p = col(5, fw(lb(user.u_nick, FONT(sp.font))), fw(lb(post.p_content, FONT(sp.font))), fw(lb("게시물 #" + post.p_files.split(",")[0], FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)))))
					.setBackColor(Color.white);
			JPanel panel = row(5, fh(img), p).setBackColor(Color.white);
			panel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					new Comments(post.p_no);
					dispose();
				};
			});
			mainPanel.addz(panel);
			
		}
	}
	
	@Override
	protected void action() {
		tf.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) { timer.restart(); } });
	}

	public static void main(String[] args) {
		Util.start(new Search());
	}
}
