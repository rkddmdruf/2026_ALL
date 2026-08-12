package test.test2;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import main.*;
import orms.categoryEntity;
import orms.doctorEntity;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Main extends CFrame{
	
	JPanel p1 = set(new JPanel(new GridLayout(2, 3, 10, 20)), BG(Color.white), BORDER(sp.em(5, 15, 15, 15)));
	List<Color> colors = Arrays.asList(rc(),rc(),rc(),rc(),rc(),rc());
	List<JLabel> list = new ArrayList<>();
	
	private Color rc() {
		return new Color((int) (Math.random() * 0x1000000));
	}
	
	public Main() {
		setFrame("메인", 600, 400);
	}
	
	
	@Override
	public void desing() {
		categoryEntity.findAll().forEach(c -> {
			JLabel l = lb(c.cname, BG(colors.get(c.cno - 1)));
			l.setOpaque(true);
			l.setIcon(cImage("icon/" + c.cno, 55, 55));
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setVerticalTextPosition(SwingConstants.BOTTOM);
			l.setHorizontalTextPosition(SwingConstants.CENTER); 
			p1.add(l);
			list.add(l);
		});
		
		add(set(col(10, 
				fw(lb("Mypage", HOA(JLabel.RIGHT), FONT(sp.font.deriveFont(1)))),
				lb("진료 선택", FG(sp.color), FONT(sp.font.deriveFont(1).deriveFont(20f))),
				f(p1)
			).setBackColor(Color.white), BORDER(sp.em(10, 10, 10, 10))));
	}

	public ImageIcon cImage(String path, int w, int h) {
		Image img = new ImageIcon("datafiles/" + path + ".png").getImage();
		
		BufferedImage bf = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = bf.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g2.drawImage(img, 0, 0, null);
		g2.dispose();
		
		for(int y = 0; y < bf.getHeight(); y++) {
			for(int x = 0; x < bf.getWidth(); x++) {
				int rgb = bf.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				if(r > 240 && g > 240 && b > 240) {
					bf.setRGB(x, y, 0);
				}
			}
		}
		return new ImageIcon(bf.getScaledInstance(w, h, 4));
	}
	
	@Override
	public void action() {
		list.forEach(l -> {
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					l.setBackground(colors.get(list.indexOf(l)));
					l.setBorder(null);
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					l.setBackground(Color.white);
					l.setBorder(sp.line(sp.color));
				}
			});
		});
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
