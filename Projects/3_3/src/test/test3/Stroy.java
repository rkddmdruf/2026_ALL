package test.test3;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import main.Util;
import orms.*;

public class Stroy extends CFrame {
	
	
	JLabel left = lb("<", FONT(sp.font.deriveFont(25f).deriveFont(1)), FG(Color.white));
	JLabel right = lb(">", FONT(sp.font.deriveFont(25f).deriveFont(1)), FG(Color.white));
	JPanel p = new JPanel();
	
	JLabel indexLabel = lb("스토리", FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)));
	
	JLabel l1 = lb("");
	JLabel l2 = lb("", FG(Color.white));
	JLabel l3 = lb("", FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f)));
	
	JLabel l4 = new JLabel() {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = sp.anti(g);
			if(list.isEmpty()) return;
			
			g2.drawImage(new ImageIcon("datafiles/story/" + ss.get(sindex).s_file + ".jpg").getImage(), 0, 0, getWidth(), getHeight(), null);
		};
	};
	
	List<storyEntity> ss = new ArrayList<>();
	List<Integer> list = new ArrayList<>();
	
	int sindex = 0;
	
	Timer timer = new Timer(1000, e -> {
		if(sindex >= ss.size() - 1) sindex = 0; 
		else sindex ++;
		changeStory();
	});
	
	public Stroy(userEntity user) {
		list.addAll(sp.user.myFollowing());
		int u = user.u_no;
		while(list.get(0) != u) {
			list.add(list.get(0));
			list.remove(0);
		}
		System.out.println(list);
		ss.addAll(storyEntity.findBy(e -> e.u_no.equals(list.get(0))));
		
		indexLabel.setText("스토리 " + (sindex + 1) + " / " + ss.size());
		l1.setIcon(sp.circleLine2(sp.circleImage(list.get(0), 40), Color.white));
		l2.setText(userEntity.findById(list.get(0)).get().u_nick);
		l3.setText(ss.get(sindex).s_content);
		
		getContentPane().setBackground(Color.black);
		
		setFramed("스토리", 300, 450, () -> new A_Main());
		timer.start();
	}

	private void changeStory() {
		l3.setText(ss.get(sindex).s_content);
		indexLabel.setText("스토리 " + (sindex + 1) + " / " + ss.size());
		repaint();
	}
	
	private void userChange() {
		timer.stop();
		ss.clear();
		sindex = 0;
		
		ss.addAll(storyEntity.findBy(e -> e.u_no.equals(list.get(0))));
		indexLabel.setText("스토리 " + (sindex + 1) + " / " + ss.size());
		l1.setIcon(sp.circleLine2(sp.circleImage(list.get(0), 40), Color.white));
		l2.setText(userEntity.findById(list.get(0)).get().u_nick);
		l3.setText(ss.get(sindex).s_content);
		timer.start();
		revalidate();
		repaint();
	};
	protected void desing() {
		add(set(row(0, 
				fh(col(0, vg(), left, vg()).setBackColor(Color.black)),
				f(col(10, 
						row(10, l1, fw(col(3, 3, 0, fw(l2), fw(l3)).setBackColor(Color.black))).setBackColor(Color.black),
						f(l4),
						indexLabel
					).setBackColor(Color.black)
				),
				fh(col(0, vg(), right, vg()).setBackColor(Color.black))
			), BORDER(sp.em(10, 10, 10, 10)), BG(Color.black)));
	}

	protected void action() {
		MouseAdapter mac = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getSource() == right) {
					list.add(list.get(0));
					list.remove(0);
				}else {
					list.add(0, list.get(list.size() - 1));
					list.remove(list.size() - 1);
				}
				userChange();
			}
		};
		left.addMouseListener(mac);
		right.addMouseListener(mac);
	}
	
	public static void main(String[] args) {
		Util.start(new Stroy(userEntity.findById(3).get()));
	}
}