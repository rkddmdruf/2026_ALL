package demo;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import orms.*;

public class Main extends CFrame {
	
	JPanel p = set(new JPanel(new GridLayout(0, 4, 10, 10)), BG(Color.white), BORDER(sp.em(20, 10, 10, 20)));
	JLabel l1 = lb("로그인");
	DefaultMutableTreeNode root = new DefaultMutableTreeNode("전체");
	CTree tree = new CTree(root);
	
	public Main() {
		DefaultMutableTreeNode r1 = new DefaultMutableTreeNode("종류");
		categoryEntity.findAll().forEach(e -> {
			r1.add(new DefaultMutableTreeNode(e.cname));
		});
		DefaultMutableTreeNode r2 = new DefaultMutableTreeNode("용량");
		Arrays.asList("256,512,1024,128".split(",")).forEach(e -> {
			r2.add(new DefaultMutableTreeNode(e));
		});
		DefaultMutableTreeNode r3 = new DefaultMutableTreeNode("통신사");
		Arrays.asList("LGU+,KT,SKT".split(",")).forEach(e -> {
			r3.add(new DefaultMutableTreeNode(e));
		});
		
		SwingUtilities.invokeLater(() -> {for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);});
		
		root.add(r1);
		root.add(r2);
		root.add(r3);
		setFrame("메인", 725, 500);
	}

	protected void desing() {
		JPanel p1 = set(col(5,10, 20, fw(row(0, hg(), l1).setBackColor(Color.white)), lb("main", FONT(sp.font.deriveFont(20f).deriveFont(1)))).setBackColor(Color.white), BORDER(sp.em(0, 10, 0, 10)));
		JPanel p2 = col(0, fh(set(new JScrollPane(tree), BG(Color.white), BORDER(null), SIZE(125, 0)))).setBackColor(Color.white);
		reload();
		add(col(0,
					fw(p1),
					f(row(10, fh(p2), f(new JScrollPane(p))).setBackColor(Color.white))
				).setBackColor(Color.white));
	}

	private void reload() {
		p.removeAll();
		productEntity.findAll().forEach(e -> {
			ProjectEntity project = ProjectEntity.findById(e.pno).get();
			Image image = new ImageIcon("datafiles/기종/" + e.pno + ".jfif").getImage();
			JLabel img = new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
					super.paintComponents(g); 
					g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
				}
			};
			set(img, BORDER(sp.line(Color.black)), SIZE(0, 100));
			img.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						System.out.println("두번클릭");
					}
				}
			});
			JPanel panel = new JPanel(new GridBagLayout());
			JTextArea ta = set(new JTextArea("기종 : " + e.pname + "\n평균 가격 : " + sp.df.format(project.items.stream().mapToInt(p -> p.price).average().getAsDouble()) + "원")
					, BG(img.getBackground()), FONT(img.getFont().deriveFont(11f)));
			ta.setEditable(false);
			ta.setOpaque(false);
			ta.setLineWrap(true);
			ta.setMargin(new Insets(0, 0, 0, 0));
			ta.setBorder(null);
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.anchor = GridBagConstraints.WEST;
			gbc.fill =  GridBagConstraints.HORIZONTAL;
			gbc.weightx = 1;
			panel.add(ta, gbc);
			set(panel, SIZE(0, 50));
			p.add(set(col(0, 15, 15, f(img), 
					fw(panel)
					), BORDER(sp.com(sp.line(Color.black), sp.em(3, 3, 3, 3)))));
		});
	}

	protected void action() {
	}
	
	public static void main(String[] args) {
		Util.start(new Main());
	}
}