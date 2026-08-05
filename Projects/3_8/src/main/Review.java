package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import orms.*;

public class Review extends CFrame{
	List<starEntity> list = new ArrayList<>();
	JScrollPane sc = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	
	JLabel imgLabel = lb("", SIZE(100, 100));
	JPopupMenu popup = comp(JPopupMenu::new, SIZE(100, 100));
	int pno;
	public Review(int pno) {
		this.pno = pno;
		popup.setBackground(Color.white);
		popup.add(imgLabel);
		popup.setBorderPainted(false);
		imgLabel.setIcon(sp.getImage("review/" + pno, 100, 100));
		list.addAll(starEntity.findBy(e -> e.pno.equals(pno)));
		
		setFramed("리뷰", 350, 450, () -> new Infor(pno));
		SwingUtilities.invokeLater(() -> sc.getVerticalScrollBar().setValue(0));
	}

	@Override
	protected void desing() {
		JPanel top = col(10, fw(lb("기종: " + productEntity.findById(pno).get().pname, FONT(sp.font.deriveFont(20F).deriveFont(1)), FG(sp.color))),
		lb("평점 " + new DecimalFormat("0.00").format(list.stream().mapToDouble(e -> e.scope).average().getAsDouble()) + " / 5", FONT(sp.font.deriveFont(15f)))).setBackColor(Color.white);
		
		set(sc, BORDER(null), BG(Color.white));
		sc.setViewportView(set(col(10, list.stream().map(e -> {
			return fw(card(e));
		}).toArray(JComponent[]::new)), BORDER(sp.em(5, 5, 0, 5)), BG(Color.white)));
		
		add(set(col(20, top, f(sc)), BORDER(sp.em(10, 10, 10, 10)), BG(Color.white)));
	}

	private JPanel card(starEntity star) {
		String title = (star.title.length() >= 14 ? star.title.substring(0, 14) + "..." : star.title).replace("\"", "");
		JTextArea ta = new JTextArea((star.detail.length() >= 55 ? star.detail.substring(0, 55) + "..." : star.detail).replace("\"", ""));
		ta.setOpaque(false);
		ta.setLineWrap(true);
		ta.setCursor(Cursor.getDefaultCursor());
		
		JLabel label = lb(title.replace("\"", ""), FG(sp.color), FONT(sp.font.deriveFont(13f).deriveFont(1)));
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
				popup.show(label, e.getX(), e.getY());
			}
		};
		label.addMouseListener(mac);
		label.addMouseMotionListener(mac);
		JPanel p = set(
				col(0, 20, 25,
						row(0, label, hg(), fh(set(new StarView((int) (star.scope * 10)), SIZE(100, 0)))).setBackColor(new Color(245, 250, 245)),
						f(ta)
					)
				, BG(new Color(245, 250, 245)), BORDER(sp.com(sp.line, sp.em(10, 10, 10, 10))));
		return p;
	}
	@Override
	protected void action() {
		sc.addMouseWheelListener(e -> {
			JScrollBar sb = sc.getVerticalScrollBar();
			sb.setValue(sb.getValue() + e.getWheelRotation() * 20);
		});
	}

	public static void main(String[] args) {
		Util.start(new Review(1));
	}
}
