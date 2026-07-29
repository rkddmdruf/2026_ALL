package main;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import orms.storyEntity;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Story extends CFrame{
	List<storyEntity> storys = new ArrayList<>();
	int storyNumber = 0;
	List<Integer> users = Util.myFollowing().get(sp.user.u_no);
	
	Timer timer = new Timer(1000, e -> {
		nextStory();
	});
	userEntity user;
	
	JLabel imgLabel = new JLabel("");
	JLabel name = lb("", FONT(sp.font), FG(Color.white));
	JLabel status = lb("", FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY));
	JLabel storyNumberLabel = lb("", FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY));
	
	JLabel left  = lb("<", FONT(sp.font.deriveFont(25f).deriveFont(1)), FG(Color.white));
	JLabel mainLabel = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			g.drawImage(new ImageIcon("datafiles/story/" + storys.get(storyNumber).s_file + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
		};
	};
	JLabel right = lb(">", FONT(sp.font.deriveFont(25f).deriveFont(1)), FG(Color.white));
	
	
	public Story(int uno) {
		user = userEntity.findById(uno).orElse(null);
		setFrame("스토리", 350, 550);
	}

	@Override
	protected void desing() {
		if(user != null) {
			initUser();
			JPanel topPanel = row(10, imgLabel, col(2,hg(), fw(name), fw(status)).setBackColor(Color.black)).setBackColor(Color.black);
			JPanel mainPanel = row(0, left, f(mainLabel), right).setBackColor(Color.black);
			add(set(col(15, fw(topPanel), f(mainPanel), storyNumberLabel).setBackColor(Color.black), BORDER(sp.em(20,20,20,20))));
		}
	}

	private void initUser() {
		storys.clear();
		storys.addAll(storyEntity.findBy(e -> e.u_no.equals(user.u_no)));
		
		
		status.setText(storys.get(storyNumber).s_content);
		imgLabel.setIcon(new ImageIcon(sp.circleLine(sp.circleImage(user.u_no, 50), Color.white)));
		name.setText(user.u_nick);
		storyNumberLabel.setText("스토리 1 / " + storys.size());
		timer.start();
		repaint();
	}
	
	private void nextStory() {
		storyNumber++;
		if(storys.size() <= storyNumber) storyNumber = 0;
		storyNumberLabel.setText("스토리 " + (storyNumber + 1) + " / " + storys.size());
		mainLabel.repaint();
	}
	
	@Override
	protected void action() {
		MouseAdapter lrma = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = users.indexOf(user.u_no);
				if((index <= 0 && e.getSource() == left) || (index >= users.size() -1 && e.getSource() == right)) return;// right에 -1은 index + 1이라서임
				
				timer.stop();
				user = userEntity.findById(users.get(index + (e.getSource() == left ? -1 : 1))).get();
				storyNumber = -1;
				nextStory();
				initUser();
			}
		};
		left.addMouseListener(lrma);
		right.addMouseListener(lrma);
	}

	public static void main(String[] args) {
		Util.start(new Story(3));
	}
}
