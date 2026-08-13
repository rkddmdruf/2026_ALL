package test.test1;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

import orms.*;

public class A_Main extends CFrame {
	JLabel l1 = lb(sp.user == null ? "로그인" : "로그아웃");
	JLabel l2 = lb("mypage");
	
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("전체");
	CTree tree = new CTree(root);
	JPanel p = set(new JPanel(new GridLayout(0, 4, 10, 10)), BG(Color.white), BORDER(sp.em(20, 10, 10, 20)));
	
	public A_Main() {
		DefaultMutableTreeNode r1 = new DefaultMutableTreeNode("종류");
		categoryEntity.findAll().forEach(e -> r1.add(new DefaultMutableTreeNode(e.cname)));
		DefaultMutableTreeNode r2 = new DefaultMutableTreeNode("용량");
		Arrays.asList("256,512,1024,128".split(",")).forEach(e -> r2.add(new DefaultMutableTreeNode(e)));
		DefaultMutableTreeNode r3 = new DefaultMutableTreeNode("통신사");
		Arrays.asList("LGU+,KT,SKT".split(",")).forEach(e -> r3.add(new DefaultMutableTreeNode(e)));
		
		root.add(r1);
		root.add(r2);
		root.add(r3);
		
		for(int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);
		
		setFrame("메인", 750, 500);
	}

	protected void desing() {
		JPanel p1 = set(col(5, 10, 20
				, fw(row(0, 10, 20, hg(),(sp.user != null ? l2 : new JLabel()),  l1)).setBackColor(Color.white)
				, lb("main", FONT(sp.font.deriveFont(1).deriveFont(20f)))), BG(Color.white));
		JPanel panel = set(new JPanel(new BorderLayout()), BG(Color.white));
		panel.add(p, BorderLayout.NORTH);
		
		reload();
		
		add(set(col(0, fw(p1), 
				f(row(0, fh(set(new JScrollPane(tree), BG(Color.white), SIZE(140, 0))), f(new JScrollPane(panel))).setBackColor(Color.white))
				), BG(Color.white)));
	}

	private void reload() {
		p.removeAll();
		productEntity.findAll().forEach(p -> {
			ProjectEntity project = ProjectEntity.findById(p.pno).get();
			Image image = new ImageIcon("datafiles/기종/" + p.pno + ".jfif").getImage();
			JLabel img = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponents(g);
					g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
				}
			};
			set(img, BORDER(sp.line(Color.black)), SIZE(0, 100));
			JPanel pa = new JPanel(new GridBagLayout());
			
			JTextArea t = new JTextArea("기종 : " + p.pname + "\n평균 가격 : " + sp.df.format(project.items.stream().mapToInt(e -> e.price).average().getAsDouble()) + "원");
			t.setFont(sp.font.deriveFont(11f));
			t.setEditable(false);
			t.setOpaque(false);
			t.setLineWrap(true);
			
			GridBagConstraints gbc = new GridBagConstraints();
			
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			gbc.anchor = GridBagConstraints.WEST;
			
			pa.add(t, gbc);
			
			set(pa, SIZE(0, 70));
			
			this.p.add(set(col(0, f(img), fw(pa)), BORDER(sp.com(sp.line(Color.black), sp.em(3, 3, 3, 3)))));
			
			imageAction(img, p);
		});
	}

	private void imageAction(JLabel l, productEntity p) {
		l.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() != 2) return;
				if(sp.user == null) {
					sp.err("로그인을 하고 선택해주세요");
					new Login();
					dispose();
					return;
				};
				new Infor(p.pno);
				dispose();
			}
		});
	}
	
	protected void action() {
		l1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(sp.user == null) {
					new Login();
					dispose();
				}else {
					l1.setText("로그인");
					sp.user = null;
					sp.infor("로그아웃되었습니다.");
				}
			}
		});
	}
	public static void main(String[] args) {
		Util.start(new A_Main());
	}
}