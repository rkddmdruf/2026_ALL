package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import orms.categoryEntity;
import orms.productEntity;

public class Main extends CFrame{
	JLabel loginLabel = lb(sp.user == null ? "로그인" : "로그아웃");
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("전체");
	JTree tree = new CTree(root);
	JPanel mainPanel = set(new JPanel(new GridLayout(0, 4, 10, 10)), BORDER(sp.em(20, 10, 10, 40)), BG(Color.white));
	
	JScrollPane sc;
	
	Set<Object> checked = new HashSet<>();
	List<DefaultMutableTreeNode> tList = new ArrayList<>();
	public Main() {
		for(String s : "종류,용량,통신사".split(",")) {
			DefaultMutableTreeNode t = new DefaultMutableTreeNode(s);
			root.add(t);
			if(s.contains("종류")) categoryEntity.findAll().forEach(e -> {
				DefaultMutableTreeNode ct = new DefaultMutableTreeNode(e.cname);
				t.add(ct); tList.add(ct);
			});
			else List.of((s.contains("용량") ? "256,512,1024,128" : "LGU+,KT,SKT").split(",")).forEach(e -> {
				DefaultMutableTreeNode ct = new DefaultMutableTreeNode(e);
				t.add(ct); tList.add(ct);
			});
		}
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if(checked.contains(node)) checked.remove(node);
				else checked.add(node);
				reloadMainPanel();
			}
		});
		setFrame("메인", 700, 550);
	}
	
	@Override
	protected void desing() {
		JPanel loginPanel = set(new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0)), BG(Color.white));
		loginPanel.add(loginLabel);
		setMyPageLabel(loginPanel);
		
		JPanel panel = set(new JPanel(new BorderLayout()), BG(Color.white));
		panel.add(mainPanel, BorderLayout.NORTH);
		
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(set(sc = new JScrollPane(tree), BG(Color.white), BORDER(null), SIZE(125, 0)), BorderLayout.WEST);
		leftPanel.add(f(new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER)));
		
		reloadMainPanel();
		add(col(10, 
				fw(loginPanel), 
				lb("main", FONT(sp.font.deriveFont(20f).deriveFont(1))), 
				f(leftPanel)
				).setBackColor(Color.white));
	}
	
	private void reloadMainPanel() {
		mainPanel.removeAll();
		
		List<productEntity> list = productEntity.findAll();
		list.sort((a, b) -> a.cno - b.cno);
		
		for(int i = 0; i < list.size(); i++) {
			productEntity product = list.get(i);
			System.out.println(product.pno);
			mainPanel.add(card(product));
		}
		revalidate();
		repaint();
		SwingUtilities.invokeLater(() -> sc.getVerticalScrollBar().setValue(0));
	}
	
	private JPanel card(productEntity p) {
		JLabel img = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("datafiles/기종/" + p.pno + ".jfif").getImage(), 5, 5, getWidth() - 10, getHeight() - 10, null);
			}
		};
		set(img, BORDER(sp.com(sp.em(5, 5, 0, 5), sp.line(Color.black))), SIZE(0, 90));
		JTextArea ta = new JTextArea("기종 : " + p.pname + "\n평균 가격 : 1,222,222원");
		ta.setLineWrap(true);
		ta.setOpaque(false);
		ta.setFont(sp.font.deriveFont(10f));
		return set(col(0, 15, 15, f(img), f(ta)), BORDER(sp.line(Color.black)));
	}

	private void setMyPageLabel(JPanel p) {
		if(sp.user == null) return;
		JLabel l = lb("mypage");
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("new MyPage();");
			}
		});
		p.add(l);
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
