package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.Timer;

import orms.postEntity;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Comments extends CFrame{
	String[] imgNumbers;
	postEntity post;
	
	JLabel likeLabel = lb("", FONT(sp.font.deriveFont(1).deriveFont(14f)));
	JTextField tf = comp(JTextField::new, SIZE(0, 30));
	JLabel insert = lb("게시", FG(sp.color));
	
	JLabel imgLabel = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			g.drawImage(new ImageIcon("datafiles/posts/" + imgNumbers[imgN] + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
		};
	};
	int imgN = 0;
	Timer timer = new Timer(1000, e -> {
		imgN ++;
		if(imgN >= imgNumbers.length) imgN = 0;
		imgLabel.repaint();
	});
	public Comments(int pno) {
		post = postEntity.findById(pno).get();
		imgNumbers = post.p_files.split(",");
		likeLabel.setText("종아요" + post.p_like + "개");
		setFramed("댓금", 700,  450, () -> timer.stop());
	}

	@Override
	protected void desing() {
		JPanel panel = new JPanel(new GridLayout(1, 2, 5, 5));
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
				fw(likeLabel), 
				fw(lb(post.p_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.S")), FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)))),
				fw(row(0, fw(tf), set(insert, FONT(sp.font.deriveFont(13f)),SIZE(75, 30), HOA(JLabel.CENTER))).setBackColor(Color.white))
			);
		
		JPanel panel = col(0, fw(profile), p2).setBackColor(Color.white);
		
		return panel;
	}
	
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Util.start(new Comments(1));
	}
}
