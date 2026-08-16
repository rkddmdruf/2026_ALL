package test.test3;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import main.Util;
import orms.*;

public class Search extends CFrame {
	JLabel fontL = new JLabel("");
	JLabel left = lb("<", FONT(fontL.getFont().deriveFont(20f)), SIZE(40, 40), HOA(JLabel.CENTER));
	JTextField t1 = comp(JTextField::new, BG(left.getBackground()), SIZE(0, 40), BORDER(sp.com(sp.line, sp.em(5, 5, 5, 5))));
	BoxPanel p = set(col(10), BORDER(sp.em(10, 10, 10, 10)), BG(Color.white));
	Timer timer = new Timer(300, e -> {
		if(t1.getText().length() == 0) init();
		else {
			reload();
		}
	});
	public Search() {
		timer.setRepeats(false);
		p.addz(lb("검색어를 입력하세오", BORDER(sp.em(15, 15, 0, 0)), FG(Color.LIGHT_GRAY)));
		setFrames("검색", 400, 500, () -> new A_Main());
	}

	protected void desing() {
		JPanel p1 = set(row(0, left, f(t1)), BORDER(sp.com(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), sp.em(10, 10, 10, 10))), BG(Color.white));
		add(col(0, fw(p1), f(set(new JScrollPane(p), BG(Color.white), BORDER(null)))));
	}

	private void init() {
		p.removeAll();
		p.addz(lb("검색어를 입력하세오", BORDER(sp.em(15, 15, 0, 0)), FG(Color.LIGHT_GRAY)));
		revalidate();
		repaint();
	}
	
	private void reload() {
		p.removeAll();
		p.addz(fw(row(0, fw(lb("유저", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(15f))))).setBackColor(Color.white)));
		
		System.out.println(t1.getText());
		userEntity.findBy(e -> e.u_nick.toLowerCase().contains(t1.getText().toLowerCase())).forEach(e -> {
			p.addz(row(10, 
					lb("", ICON(sp.circleImage(e.u_no, 45))), 
					col(3, fw(lb(e.u_nick)), fw(lb(e.u_name, FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f))))).setBackColor(Color.white)
					).setBackColor(Color.white));
		});
		
		p.addz(fw(row(0, fw(lb("게시물", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(15f))))).setBackColor(Color.white)));
		postEntity.findBy(e -> userEntity.findById(e.u_no).get().u_nick.toLowerCase().contains(t1.getText().toLowerCase())).forEach(e -> {
			userEntity u = userEntity.findById(e.u_no).get();
			p.addz(row(10, 
					lb("", ICON(sp.getImage("posts/" + e.p_files.split(",")[0], 50, 50))), 
					col(3, fw(lb(u.u_nick)), fw(lb(e.p_content)), fw(lb("게시물 #" + e.p_no, FG(Color.LIGHT_GRAY), FONT(sp.font.deriveFont(11f))))).setBackColor(Color.white)
					).setBackColor(Color.white));
		});
		
		revalidate();
		repaint();
	}
	protected void action() {
		left.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new A_Main();
				dispose();
			}
		});
		t1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				timer.stop();
				timer.start();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Search());
	}
}