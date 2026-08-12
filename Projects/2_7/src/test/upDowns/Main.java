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

public class Main extends CFrame {
	//좀더 하고 에러 안났으면 10분 내외로 가능할듯
	JLabel l1 = lb("Mypage", FONT(sp.font.deriveFont(1)));
	List<JLabel> list = new ArrayList<>();
	List<Color> colors = new ArrayList<>();
	
	private Color rc() {
		return new Color((int) (Math.random() * 0x1000000));
	}
	public Main() {
		for(int i = 0; i < 6; i++) colors.add(rc());
		setFrames("메인", 600, 350, () -> {
			new A_Login();
			sp.user = null;
		});
	}

	protected void desing() {
		JPanel p = new JPanel(new GridLayout(2, 3, 15, 22));
		
		categoryEntity.findAll().forEach(e -> {
			JLabel l = lb(e.cname, FONT(sp.font.deriveFont(1)));
			l.setIcon(cImage("icon/" + e.cno, 50, 50));
			l.setHorizontalAlignment(JLabel.CENTER);
			l.setHorizontalTextPosition(JLabel.CENTER);
			l.setVerticalTextPosition(JLabel.BOTTOM);
			
			l.setBackground(colors.get(e.cno - 1));
			l.setOpaque(true);
			
			list.add(l);
			p.add(l);
		});
		
		add(set(col(10,
					fw(row(0, hg(), l1)).setBackColor(Color.white),
					lb("진료 선택", FONT(sp.font.deriveFont(20f).deriveFont(1)), FG(sp.color)),
					f(p)
				), BG(Color.white), BORDER(sp.em(10, 25, 10, 20))));
	}

	private ImageIcon cImage(String s, int w, int h) {
		Image img = new ImageIcon("datafiles/" + s + ".png").getImage();
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
		return new ImageIcon(bf.getScaledInstance(w, h, 4));
	}
	protected void action() {
		list.forEach(l -> {
			l.addMouseListener(new MouseAdapter() {
			    public void mouseEntered(MouseEvent e) {
			    	l.setBackground(Color.white);
			    	l.setBorder(sp.line(sp.color));
			    }
			    public void mouseExited(MouseEvent e) {
			    	l.setBorder(null);
			    	l.setBackground(colors.get(list.indexOf(l)));
			    }
			    public void mouseClicked(MouseEvent e) {
			    	new DrSelect(list.indexOf(l) + 1);
			    	dispose();
			    }
			});
		});
		l1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MyPage(true);
				dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Main());
	}
}