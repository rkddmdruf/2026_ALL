package main;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import utils.BoxPanel;
import utils.getter;

import static utils.Properties.*;
public class Orders extends JPanel{
	
	JComboBox<String> status = new JComboBox<String>("전체, 체결, 미체결, 취소".split(", "));
	JComboBox<String> buySell = new JComboBox<String>("전체, 매수, 매도".split(", "));
	JComboBox<String> market = new JComboBox<String>("전체, 국내, 해외".split(", "));
	
	JTable table = Util.getTable("번호, 종목, 시장, 유형, 주문유형, 수량, 가격, 총액, 상태, 날짜, 취소".split(","));
	public Orders() {
		setLayout(new BorderLayout());
		
		JPanel topPanel = new BoxPanel(BoxPanel.R, 7, 7, 7, comp(JLabel::new, TEXT("상태:")), status, comp(JLabel::new, TEXT("유형:")), buySell, comp(JLabel::new, TEXT("시장:")), market);
		add(new BoxPanel(BoxPanel.C, 0, 5, 0,topPanel, BoxPanel.fill(new JScrollPane(table))));
	}

}
