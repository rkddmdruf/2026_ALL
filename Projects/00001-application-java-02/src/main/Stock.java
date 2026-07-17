package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import orms.stockEntity;

import static utils.Properties.*;

import utils.BoxPanel;
import utils.getter;

public class Stock extends JPanel{
	JTextField tf = comp(JTextField::new, SIZE(125, 25));
	JButton search = Util.button("검색");
	String[] koreaMarket = "전체,KOSPI,KOSDAQ".split(","), usaMarket = "전체,NASDAQ".split(",");
	JComboBox<String> market = new JComboBox<>("전체, KOSPI, KOSDAQ, NASDAQ".split(", "));
	JComboBox<String> category = new JComboBox<String>("전체/IT/자동차/금융/에너지/화학/바이오/철강/방산/2차전지".split("/"));
	JComboBox<String> order = new JComboBox<String>("이름순/가격높은순/가격낮은순/등락률순".split("/"));
	
	JPanel korea;
	JPanel usa;
	JPanel koreaGridPanel = set(new JPanel(new GridLayout(0, 4, 5, 5)), BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(2, 2, 30, 2))));
	JPanel usaGridPanel = set(new JPanel(new GridLayout(5, 4, 5, 5)), BORDER(getter.com(getter.line(Color.LIGHT_GRAY), getter.em(2, 2, 30, 2))));
	JPanel koreaTopPanel = set(new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0)), BG(getter.backColor));
	JPanel usaTopPanel = set(new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0)), BG(getter.backColor));
	
	public Stock() {
		setLayout(new BorderLayout());
		
		JTabbedPane tab = new JTabbedPane();
		tab.setFont(getter.font.deriveFont(1).deriveFont(13f));
		tab.setTabPlacement(JTabbedPane.TOP);
		tab.setBorder(null);
		tab.setBackground(getter.backColor);
		
		tab.addTab("국내", setKoreaPanel());
		tab.addTab("해외", setUSAPanel());
		tab.addChangeListener(e -> initTopPanel_Combo(tab));
		initTopPanel_Combo(tab);
		
		add(tab);
	}

	private void initTopPanel_Combo(JTabbedPane tab) {
		JPanel panel = tab.getSelectedComponent() == korea ? koreaTopPanel : usaTopPanel;
		panel.removeAll();
		market.removeAllItems();
		
		for(String s : tab.getSelectedIndex() == 0 ? koreaMarket : usaMarket)
			market.addItem(s);
		
		panel.add(new JLabel("검색:"));
		panel.add(tf);
		panel.add(search);
		panel.add(new JLabel("시장:"));
		panel.add(market);
		panel.add(new JLabel("섹터:"));
		panel.add(category);
		panel.add(new JLabel("정렬:"));
		panel.add(order);
		
		setGridPanel(tab.getSelectedIndex() == 0 ? koreaGridPanel : usaGridPanel);
		
		revalidate();
		repaint();
	}
	
	private JPanel setKoreaPanel() {
		korea = new BoxPanel(BoxPanel.C, 0, 3, 0, koreaTopPanel, BoxPanel.fill(koreaGridPanel));
		return korea;
	}
	
	private JPanel setUSAPanel() {
		usa = new BoxPanel(BoxPanel.C, 0, 3, 0, usaTopPanel, BoxPanel.fill(usaGridPanel));
		return usa;
	}
	
	private void setGridPanel(JPanel panel) {//test
		List<Predicate<stockEntity>> predicates = new ArrayList<>();
		IntStream.range(1, market.getItemCount()).forEach( e -> predicates.add(p -> p.s_market.equals(market.getItemAt(e))) );
		List<stockEntity> list = stockEntity.findBy(e -> predicates.stream().anyMatch(c -> c.test(e)));// comboBox사용해서 구별
		
		panel.removeAll();
		for(int i = 0; i < list.size(); i++)
			panel.add(new JPanel() {{setBackground(Color.white); setBorder(getter.line(Color.LIGHT_GRAY));}});
		for(int i = 0; i < 20 - list.size(); i++) {
			panel.add(new JLabel(""));
		}
	}
}
