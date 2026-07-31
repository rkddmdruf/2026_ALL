package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import orms.categoryEntity;
import orms.detailEntity;
import orms.productEntity;

public class Main extends CFrame{
	
	JButton search = set(new CButton(sp.getImage("search", 20, 20)), BG(Color.lightGray));
	JTextField tf = comp(JTextField::new, BORDER(sp.line));
	BoxPanel categoryPanel = set(col(20), BORDER(sp.com(sp.line, sp.em(10, 10, 10, 10))), BG(Color.white));
	
	JLabel login = lb(sp.user == null ? "로그인" : "로그아웃", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	JLabel roullet = lb("룰렛", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	JLabel shopping = lb("장바구니", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	JLabel myPage = lb("마이페이지", FG(Color.LIGHT_GRAY), HOA(JLabel.CENTER));
	
	
	JPanel mainPanel = new JPanel(new GridBagLayout());
	
	Map<String, List<String>> category = new LinkedHashMap<>();
	List<String> key = new ArrayList<>();
	List<JLabel> categoryLabels = new ArrayList<>();
	List<List<JLabel>> detailLabels= new ArrayList<>();
	
	List<Integer> expasionList = new ArrayList<>();
	int selectDetil = -1, selectCategory = -1;
	public Main() {
		detailEntity.findAll().forEach(e -> category.computeIfAbsent(categoryEntity.findById(e.cno).get().cname, k -> new ArrayList<>()).add(e.dname));
		key.addAll(category.keySet());
		
		setFrame("메인", 900, 550);
	}
	//▶▼
	@Override
	protected void desing() {
		setCategoryPanel();
		JPanel leftPanel = col(20, lb("", ICON(sp.getImage("logo", 150, 75))), row(3, f(tf), search).setBackColor(Color.white), f(categoryPanel)).setBackColor(Color.white);
		set(leftPanel, BORDER(sp.em(5, 5, 15, 5)));
		
		JPanel menuPanel = new JPanel(new GridLayout(1, 0, 10, 10));
		menuPanel.setPreferredSize(new Dimension(300, 25));
		if(login.getText().equals("로그인")) menuPanel.add(login);
		else {
			menuPanel.add(roullet);
			menuPanel.add(lb("|", FG(Color.LIGHT_GRAY)), HOA(JLabel.CENTER));
			menuPanel.add(login);
			menuPanel.add(lb("|", FG(Color.LIGHT_GRAY)), HOA(JLabel.CENTER));
			menuPanel.add(shopping);
			menuPanel.add(lb("|", FG(Color.LIGHT_GRAY)), HOA(JLabel.CENTER));
			menuPanel.add(myPage);
		}
		
		JPanel topPanel = row(0, lb("Skillmall", FONT(sp.font.deriveFont(24f).deriveFont(1))), hg(), menuPanel);
		JPanel panel = col(20, fw(topPanel), f(new JScrollPane(mainPanel)));
		set(panel, BORDER(sp.em(20, 20, 20, 20)));
		add(row(0, leftPanel, f(panel)));
	}

	private void setMainPanel() {
		mainPanel.removeAll();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		
		List<productEntity> list = (selectCategory == -1 || selectDetil == -1) ? 
				  productEntity.findAll() 
				: productEntity.findBy(e -> detailEntity.findById(e.dno).get().dname.equals(detailLabels.get(selectCategory).get(selectDetil).getText()));
		System.out.println(list.size());
		
		gbc.gridx = 0;
		
		gbc.gridy = 0;
		
		
	}
	private void setCategoryPanel() {
		categoryPanel.removeAll();
		for(int i = 0; i < key.size(); i++) {
			List<String> cate = category.get(key.get(i));
			JLabel label = lb("▶" + key.get(i), FONT(sp.font.deriveFont(1).deriveFont(20f)));
			categoryLabels.add(label);
			
			JLabel[] ls = cate.stream().map(e -> lb(e, FG(Color.LIGHT_GRAY))).toArray(JLabel[]::new);
			detailLabels.add(Arrays.asList(ls));
			
			categoryPanel.addz(col(20, fw(label), fw(col(5, ls).setBackColor(Color.white))).setBackColor(Color.white));
		}
		
		setDetailVisit();
	}
	
	private void setDetailVisit() {
		IntStream.range(0, key.size()).forEach(i -> {
			categoryLabels.get(i).setText("▶" + key.get(i));
			detailLabels.get(i).get(0).getParent().setVisible(false);
		});
		
		expasionList.forEach(e -> {
			detailLabels.get(e).get(0).getParent().setVisible(true);
			categoryLabels.get(e).setText("▼" + key.get(e));
		}); // 선택된거 펼치기
		System.out.println("selectCategory : " + selectCategory + ", " + "selectDetil : " + selectDetil );
		setMainPanel();
		revalidate();
		repaint();
	}
	
	@Override
	protected void action() {
		categoryLabels.forEach(l -> {
			l.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e){
					if(expasionList.contains(categoryLabels.indexOf(l))) {
						expasionList.removeIf(ex -> ex.equals(categoryLabels.indexOf(l)));
						selectCategory = -1;
						selectDetil = -1;
					}
					else {
						expasionList.add(categoryLabels.indexOf(l));
						selectCategory = categoryLabels.indexOf(l);
						selectDetil = 0;
					}
					setDetailVisit();
				}
			});
		});
	}

	public static void main(String[] args) {
		Util.start(new Main());
	}
}
