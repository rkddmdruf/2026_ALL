package test.upDowns;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import main.Util;
import orms.*;

public class testMain extends CFrame {
	List<JLabel> list = new ArrayList<>();
	List<Color> colors = new ArrayList<>();
	List<categoryEntity> cl = categoryEntity.findAll();
	
	JLabel l = lb("Mypage");
	
	public testMain() {
		cl.forEach(e -> colors.add(new Color((int) (Math.random() * 0x1000000))));
		setFrame("메인", 600, 350);
	}

	protected void desing() {
		JPanel p = set(new JPanel(new GridLayout(2, 3, 10, 15)), BG(Color.white));
		cl.forEach(e -> {
			JLabel l = lb(e.cname);
			l.setIcon(cImage("icon/" + e.cno, 50, 50));
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setVerticalTextPosition(JLabel.BOTTOM);
			l.setHorizontalTextPosition(JLabel.CENTER);
			l.setOpaque(true);
			l.setBackground(colors.get(e.cno - 1));
			list.add(l);
			p.add(l);
		});
		
		add(set(col(10, 
					fw(row(0, hg(), l).setBackColor(Color.white)),
					lb("진료 과목", FONT(sp.font.deriveFont(1).deriveFont(20f)), FG(sp.color)),
					f(p)
				), BORDER(sp.em(10, 20, 10, 10)), BG(Color.white)));
		
	}

	private ImageIcon cImage(String string, int i, int j) {
		Image img = new ImageIcon("datafiles/" + string + ".png").getImage();
		BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = bf.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.drawImage(img, 0, 0, null);
		g2.dispose();
		
		for(int y = 0; y < bf.getHeight(); y++) {
			for(int x = 0; x < bf.getWidth(); x++) {
				int rr = bf.getRGB(x, y);
				int r = (rr >> 16) & 0xFF;
				int g = (rr >> 8) & 0xFF;
				int b = rr & 0xFF;
				if(r > 240 && g > 240 && b > 240) {
					bf.setRGB(x, y, 0);
				}
			}
		}
		return new ImageIcon(bf.getScaledInstance(i, j, 4));
	}

	protected void action() {
		list.forEach(l -> {
			l.addMouseListener(new MouseAdapter() {
				int index = list.indexOf(l);
				public void mouseClicked(MouseEvent e) {
					new JLabel(cl.get(index).cname);
					dispose();
				}
				
			    public void mouseEntered(MouseEvent e) {
			    	l.setBackground(Color.white);
			    	l.setBorder(sp.line(sp.color));
			    	repaint();
			    }

			    public void mouseExited(MouseEvent e) {
			    	l.setBackground(colors.get(index));
			    	l.setBorder(null);
			    	repaint();
			    }
			});
		});
		
	}
	
	public static void main(String[] args) {
		Util.start(new testMain());
	}
}