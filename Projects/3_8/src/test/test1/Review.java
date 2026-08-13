package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import javax.swing.*;

import main.StarView;
import orms.*;

public class Review extends CFrame {
	productEntity p;
	JLabel imgLabel = lb("", SIZE(100, 100));
	JPopupMenu popup = comp(JPopupMenu::new, SIZE(100, 100));
	
	
	JScrollPane sc = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	public Review(int pno) {
		popup.setBackground(Color.white);
		popup.add(imgLabel);
		popup.setBorderPainted(false);
		imgLabel.setIcon(sp.getImage("review/" + pno, 100, 100));
		p = productEntity.findById(pno).get();
		setFramed("리뷰", 375, 475, () -> new Infor(pno));
	}

	protected void desing() {
		sc.setViewportView(setting());
		BoxPanel bp = col(10, 
						fw(lb("기종: " + p.pname, FONT(sp.font.deriveFont(21f).deriveFont(1)), FG(sp.color))), 
						lb("평점 " + new DecimalFormat("0.00").format(p.star()) + "/ 5", FONT(sp.font.deriveFont(14f)), FG(Color.LIGHT_GRAY))
					).setBackColor(Color.white);
		set(sc, BORDER(null), BG(Color.white));
		add(set(col(20, fw(bp), f(sc)), BORDER(sp.em(10, 15, 10, 15)), BG(Color.white)));
	}

	private JPanel setting() {
		JPanel panel = set(new JPanel(new BorderLayout()), BG(Color.white));
		
		JPanel p1 = set(new JPanel(new GridLayout(0, 1, 15, 15)), BG(Color.white));
		
		starEntity.findBy(e -> e.pno.equals(p.pno)).forEach(e -> {
			String s = e.detail.replace("\"", "");
			JTextArea ta = new JTextArea(s.length() > 55 ? s.substring(0, 55) + "..." : s);
			ta.setOpaque(false);
			ta.setEditable(false);
			ta.setLineWrap(true);
			
			JLabel title = lb((e.title.length() > 14 ? e.title.substring(0, 14) + "..." : e.title), FONT(sp.font.deriveFont(13f).deriveFont(1)), FG(sp.color));
			BoxPanel pp = set(col(10, 20, 30,
					fw(row(0, 
							title, 
							hg(), 
							set(new StarView(e.scope.intValue() * 10), SIZE(100, 20)))).setBackColor(Util.setA(sp.color, 0))
					, f(ta)
					), BG(Util.setA(sp.color, 10)), BORDER(sp.com(sp.line, sp.em(0, 10, 0, 10))));
			p1.add(pp);
			
			MouseAdapter mac = new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					popup.setVisible(true);
				}
				@Override
				public void mouseExited(MouseEvent e) {
					popup.setVisible(false);
				}
				
				@Override
				public void mouseMoved(MouseEvent e) {
					if(!imgLabel.isVisible()) return;
					popup.show(title, e.getX(), e.getY());
					repaint();
				}
			};
			title.addMouseMotionListener(mac);
			title.addMouseListener(mac);
		});
		panel.add(p1, BorderLayout.NORTH);
		return panel;
	}

	protected void action() {
		sc.addMouseWheelListener(e -> {
			sc.getVerticalScrollBar().setValue(sc.getVerticalScrollBar().getValue() + e.getWheelRotation() * 10);
			revalidate();
			repaint();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Review(1));
	}
}