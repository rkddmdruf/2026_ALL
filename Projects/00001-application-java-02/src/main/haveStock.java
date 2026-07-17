package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import utils.BoxPanel;
import static utils.Properties.*;
import utils.getter;

public class haveStock extends JPanel{
	
	JLabel krwProperty = 	comp(JLabel::new, TEXT("1000000원"), 	NAME("KRW 잔고"));
	JLabel usdProperty = 	comp(JLabel::new, TEXT("1000000$"), 	NAME("USD 잔고"));
	JLabel stockProperty = 	comp(JLabel::new, TEXT("10000000원"), 	NAME("보유 평가 (KRW 환산)"));
	JLabel allProperty = 	comp(JLabel::new, TEXT("10000000원"), 	NAME("총 자산 (KIRW 환산)"));
	
	JTable table1 = Util.getTable("종목,현재가,등락률".split(","));
	JTable table2 = Util.getTable("종목,수량,평단가,현재가,손익,손익률".split(","));
	
	public haveStock() {
		setLayout(new BorderLayout());
		
		JPanel myProperty = new BoxPanel(BoxPanel.R, 0, 10, 0, setMyAllProperty(), BoxPanel.fill(setMyPropertyChart()));
		JPanel tp1 = new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fill(new JScrollPane(table1)));
		tp1.setBorder(getter.titleBorder("관심 종목"));
		JPanel tp2 = new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fill(new JScrollPane(table2)));
		tp2.setBorder(getter.titleBorder("보유 종목"));
		
		JPanel tablePanel = new BoxPanel(BoxPanel.R, 0, 10, 0, BoxPanel.fill(tp1), BoxPanel.fill(tp2));
		table1.setPreferredSize(new Dimension(table2.getPreferredSize().width, table2.getPreferredSize().height));
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, myProperty, BoxPanel.fill(tablePanel));
		panel.setBorder(getter.em(10, 10, 10, 10));
		
		add(panel);
	}
	
	private JPanel setMyPropertyChart() {
		
		JPanel rightPanel = new BoxPanel(BoxPanel.C, 5, 5, 0, set(new JLabel("0"), SIZE(50, getFontMetrics(getFont()).getHeight())), new JLabel("0"),new JLabel("0"),new JLabel("0"),new JLabel("0")).setBackColor(Color.white);
		JPanel panel = new BoxPanel(BoxPanel.R, 0, 2, 5, BoxPanel.fill(new JLabel() {{setBackground(getter.backColor); setOpaque(true);}}), rightPanel);
		panel.setBorder(getter.titleBorder("자산 추이"));
		return panel;
	}
	private JPanel setMyAllProperty() {
		JPanel panel = new BoxPanel(BoxPanel.C, 10, 5, 10, propertyPanel(krwProperty), propertyPanel(usdProperty), propertyPanel(stockProperty), propertyPanel(allProperty));
		panel.setBorder(getter.titleBorder("내 자산 요약"));
		panel.setMaximumSize(new Dimension(panel.getPreferredSize().width, Integer.MAX_VALUE));
		return panel;
	}
	
	private JPanel propertyPanel(JLabel label) {
		label.setPreferredSize(new Dimension(100,getFontMetrics(label.getFont()).getHeight()));
		label.setHorizontalAlignment(JLabel.RIGHT);
		return new BoxPanel(BoxPanel.R, 10, 0, 10, comp(JLabel::new, TEXT(label.getName()), SIZE(130, getFontMetrics(label.getFont()).getHeight())), label);
	}
}
