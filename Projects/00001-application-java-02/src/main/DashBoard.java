package main;

import static utils.Properties.BG;
import static utils.Properties.BORDER;
import static utils.Properties.FG;
import static utils.Properties.SIZE;
import static utils.Properties.TEXT;
import static utils.Properties.comp;
import static utils.Properties.set;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import utils.BoxPanel;
import utils.getter;

public class DashBoard extends JPanel{
	
	public DashBoard() {
		setLayout(new BorderLayout());
		JPanel topPanel = new BoxPanel(BoxPanel.R, 5, 2, 5, BoxPanel.fill(topPanel("KOSPI")), BoxPanel.fill(topPanel("KOSDAQ")), BoxPanel.fill(topPanel("NASDAQ"))).setBackColor(getter.darkColor);
		
		/*
		 * JPanel mainPanel = new BoxPanel(BoxPanel.C, 10, 10, 10 , BoxPanel.fill(new
		 * BoxPanel(BoxPanel.R, 0, 10, 0, lineChartPanel("KOSPI 추이"),
		 * lineChartPanel("KOSDAQ 추이"))) , new BoxPanel(BoxPanel.C, 0, 0, 0,
		 * lineChartPanel("NASDAQ 추이")) , BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 10,
		 * 0, BoxPanel.fill(stickChartPanel("등락률 TOP 5")),
		 * BoxPanel.fill(stickChartPanel("거래량 TOP 5")))) );
		 */
		
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.insets = new Insets(5, 0, 5, 0);

		JPanel row1 = new BoxPanel(BoxPanel.R, 0, 10, 0,
		        lineChartPanel("KOSPI 추이"), lineChartPanel("KOSDAQ 추이"));
		gbc.gridy = 0;
		gbc.weighty = 0.7;
		mainPanel.add(row1, gbc);

		JPanel row2 = lineChartPanel("NASDAQ 추이");
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		mainPanel.add(row2, gbc);

		JPanel row3 = new BoxPanel(BoxPanel.R, 0, 10, 0,
		        stickChartPanel("등락률 TOP 5"), stickChartPanel("거래량 TOP 5"));
		gbc.gridy = 2;
		gbc.weighty = 2.0;
		mainPanel.add(row3, gbc);
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fillWidth(topPanel), BoxPanel.fill(mainPanel));
		
		add(panel);
	}
	
	private JPanel stickChartPanel(String s) {
		JPanel p = new BoxPanel(BoxPanel.R, 0, 0, 0, BoxPanel.fill(new JLabel()));
		p.setBorder(getter.titleBorder(s));
		p.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		return p;
	}
	private JPanel lineChartPanel(String s) {
		JLabel chart = comp(JLabel::new, BG(getter.backColor), TEXT("sdfds"));
		chart.setOpaque(true);
		JLabel title = comp(JLabel::new, TEXT(s), BG(Color.white));
		title.setOpaque(true);
		JPanel label5 = new BoxPanel( BoxPanel.C, 5, 5, 0, BoxPanel.fillHeight(comp(JLabel::new, SIZE(50, 0), TEXT("2200"))) ).setBackColor(Color.white);
		JPanel chartPanel = new BoxPanel(BoxPanel.R, 0, 1, 0, BoxPanel.fill(chart), label5);
		
		JPanel p = set(new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fillWidth(title),
				BoxPanel.fill(chartPanel)
			), BORDER(getter.line(Color.LIGHT_GRAY)));
		return p;
	}
	
	private JPanel topPanel(String s) {
		return set(new BoxPanel(BoxPanel.C, 5, 0, 5, comp(JLabel::new, TEXT(s), FG(Color.white)), comp(JLabel::new, TEXT("-"), FG(Color.white))), BG(getter.lightDarkColor), BORDER(getter.line(Color.gray)));
	}

}
