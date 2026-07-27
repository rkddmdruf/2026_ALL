package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import orms.categoryEntity;

public class Main extends CFrame{
	
	List<categoryEntity> categorys = categoryEntity.findAll();
	List<Color> colors = Arrays.asList(rColor(), rColor(), rColor(), rColor(), rColor(), rColor());
	List<Image> imgs = categorys.stream().map(e -> setImageAlhpa(e.cno)).collect(Collectors.toList());
	List<JPanel> panels = new ArrayList<>();
	Border cardBorder = getter.em(20, 45, 20, 45);
	
	public Main() {
		setFrame("메인", 600, 350, () -> {});
	}

	private Color rColor() {
		return new Color((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256));
	}
	@Override
	public void desing() {
		JLabel my =  lb("Mypage", FONT(getter.font.deriveFont(1)));
		JPanel panel = set(new JPanel(new GridLayout(2, 3, 10, 15)), BG(Color.white));
		setGridPanel(panel);
		JPanel mainPanel = col(15,
				fw(row(10, hg() ,my).setBackColor(Color.white)),
				lb("진료 선택", FONT(getter.font.deriveFont(20f).deriveFont(1)), FG(getter.color)),
				f(panel)
			);
		mainPanel.setBorder(getter.em(15, 40, 15, 40));
		mainPanel.setBackground(Color.white);
		add(mainPanel);
	}

	private void setGridPanel(JPanel panel) {
		for(int i = 0; i < 6; i++) {
			int index = i;
			JPanel p = new JPanel(new BorderLayout());
			p.setBackground(colors.get(index));
			p.setBorder(cardBorder);
			p.add(new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					Graphics2D g2 = (Graphics2D) g;
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.drawImage(imgs.get(index), 0, 0, getWidth(), getHeight(), null);
				}
			});
			p.add(lb(categorys.get(index).cname, FONT(getter.font.deriveFont(1).deriveFont(13f)), HOA(JLabel.CENTER)), BorderLayout.SOUTH);
			panel.add(p);
			panels.add(p);
		}
	}

	private Image setImageAlhpa(int n) {
		Image img = new ImageIcon("datafiles/icon/" + n + ".png").getImage();
		try {
			BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D g2 = bf.createGraphics();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(img, 0, 0, null);
			g2.dispose();
			for(int y = 0; y < bf.getHeight(); y++)
				for(int x = 0; x < bf.getWidth(); x++){
					int rgb = bf.getRGB(x, y);
					int r = (rgb >> 16) & 0xFF;
					int g = (rgb >> 8) & 0xFF;
					int b = rgb & 0xFF;
					// "진짜 흰색에 가까운 것만 제거"
					if (r > 240 && g > 240 && b > 240) {
					    bf.setRGB(x, y, 0x00000000);
					}
				}
			return bf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return img;
	}
	@Override
	public void action() {
		for(int i = 0; i < panels.size(); i++) {
			int index = i;
			JPanel p = panels.get(index);
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					p.setBackground(Color.white);
					p.setBorder(BorderFactory.createCompoundBorder(getter.line(getter.color), cardBorder));
				}
				@Override
				public void mouseExited(MouseEvent e) {
					p.setBackground(colors.get(index));
					p.setBorder(cardBorder);
				}
				@Override
				public void mouseClicked(MouseEvent e) {
					new DoctorSelect(index + 1).setVisible(true);
					dispose();
				}
			});
		}
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
