package utils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;


public class CTree extends JPanel{
	
	public Map<Integer, JLabel> titles = new LinkedHashMap<>();
	public Map<Integer, List<JLabel>> subTitle = new LinkedHashMap<>();
	
	public JLabel selectLabel;
	
	{
		setLayout(new BorderLayout());
		setBackground(Color.white);
		setBorder(sp.em(20, 20, 20, 20));
	}
	
	BoxPanel panel = col(10).setBackColor(Color.white);
	public CTree(){
		add(panel);
	}
	
	
//▼
	public void Values(String title, String...val){
		int h = titles.size();
		titles.put(h, lb("▶ " + title, FONT(sp.font.deriveFont(20f).deriveFont(1))));
		
		for(String s : val) {
			subTitle.computeIfAbsent(h, k -> new ArrayList<>()).add(fw(lb(s, FG(Color.lightGray))));
		}
		var a = subTitle.get(h).stream().map(e -> {
			var v = row(0,lb("           "), e).setBackColor(Color.white);
			v.setVisible(false);
			return v;
		}).toArray(JComponent[]::new);
		
		panel.addz(fw(row(0, fw(titles.get(h)))).setBackColor(Color.white));
		panel.addz(col(5, a).setBackColor(Color.white));
		settingAction(h);
	}


	private void settingAction(int h) {
		subTitle.get(h).forEach(l -> {
			l.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(selectLabel != null) selectLabel.setForeground(Color.LIGHT_GRAY);
					l.setForeground(sp.red);
					selectLabel = l;
					System.out.println(selectLabel.getText());
				}
			});
		});
		titles.get(h).addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(titles.get(h).getText().contains("▶")) {
					titles.get(h).setText(titles.get(h).getText().replace("▶", "▼"));
					subTitle.get(h).forEach(s -> s.getParent().setVisible(true));
				}else {
					titles.get(h).setText(titles.get(h).getText().replace("▼","▶"));
					subTitle.get(h).forEach(s -> s.getParent().setVisible(false));
				}
				revalidate();
				repaint();
			}
		});
	}
	
	public String selectCategory() {
		return selectLabel == null ? "" : selectLabel.getText();
	}

}
