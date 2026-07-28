package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

import orms.*;

public class Main extends CFrame{


	public Main() {
		setFrame("홈", 725, 600);
	}

	@Override
	protected void desing() {
		add(col(0, fw(topPanel()), f(new JPanel())).setBackColor(Color.white));
	}

	private JPanel topPanel() {
		JScrollPane sc = set(new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BORDER(null), BG(Color.white));
		sc.setViewportView(comp(JPanel::new, BG(Color.white)));
		JLabel profileIcon = lb("", ICON(sp.getImage("icons/profile", 40, 40)));
		List<String> us = Arrays.asList(sp.user.u_follow.split(","));
		for(int i = 0; i < us.size(); i++) {
			
		}
		JPanel panel = row(0, fh(profileIcon), hg(50), topCard("", "내 스토리"), f(sc));
		set(panel, BORDER(sp.em(10, 10, 10, 10)), BG(Color.white));
		return panel;
	}
	
	private JPanel topCard(String path, String s) {
		JLabel text = lb(s, HOA(JLabel.CENTER), FONT(sp.font.deriveFont(11f)));
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int imgS = 20;
				Graphics2D g2 =(Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.drawImage(plusToColor(), (getWidth() - imgS) / 2, (getHeight() - imgS) / 2, 20, 20, null);
				g2.setColor(sp.color);
				float line = 2;
				g2.setStroke(new BasicStroke(line));
				g2.drawOval((int)line, (int)line, getWidth() - (int)line * 2, getHeight() - (int)line * 2);
			}
		};
		JPanel panel = col(5, fh(set(img, SIZE(60, 60))), text).setBackColor(Color.white);
		return panel;
	}
	
	private Image plusToColor() {
		Image img = new ImageIcon("datafiles/icons/plus.png").getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			
			for(int y = 0; y < bf.getHeight(); y++) {
				for(int x = 0; x < bf.getWidth(); x++) {
					if((bf.getRGB(x, y) & 0xFFFFFF) < 0x222222) {
						bf.setRGB(x, y, sp.color.getRGB());
					}else {
						bf.setRGB(x, y, 0);
					}
				}
			}
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
