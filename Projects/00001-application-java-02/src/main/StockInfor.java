package main;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;


import orms.accountEntity;
import orms.stockEntity;
import orms.stock_tickEntity;
import utils.BoxPanel;
import utils.CFrame;
import utils.getter;

import static utils.Properties.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;

public class StockInfor extends CFrame{

	stockEntity stock;
	JLabel stockPrice = comp(JLabel::new, TEXT("0원"), FG(getter.color));
	JLabel plusMinus = comp(JLabel::new, TEXT("-0 (-0.64%)"), FG(getter.color));
	
	JCheckBox MA5 = comp(JCheckBox::new, TEXT("MA5"), FG(Color.yellow));
	JCheckBox MA20 = comp(JCheckBox::new, TEXT("MA20"), FG(Color.magenta));
	JCheckBox MA60 = comp(JCheckBox::new, TEXT("MA60"), FG(Color.cyan));
	JComboBox<String> time = new JComboBox<String>("1분/5분/15분/1시간/4시간".split("/"));
	
	
	JLabel stockPrice2;
	JButton buy = comp(JButton::new, TEXT("매수"), BG(Color.red), FG(Color.white)), sell = comp(JButton::new, TEXT("매도"), FG(Color.black), BG(Color.LIGHT_GRAY));
	JComboBox<String> accounts = set(new JComboBox<>(accountEntity.findBy(e -> e.u_no == getter.user.u_no).stream().map(e -> e.a_name).toArray(String[]::new)), NAME("계좌"));
	JTextField priceSelect = comp(JTextField::new, NAME("지정가"));
	JSpinner numberSelect = set(new JSpinner(new SpinnerNumberModel(1, 1, null, 1)), NAME("수량"));
	JRadioButton rb1 = comp(JRadioButton::new, TEXT("시장가"), BG(getter.backColor)), rb2 = comp(JRadioButton::new, TEXT("지정가"), BG(getter.backColor));
	JButton pb1 = Util.button("10%"), pb2 = Util.button("25%"), pb3 = Util.button("50%"), pb4 = Util.button("MAX");
	JLabel priceLabel1 = comp(JLabel::new, TEXT("0원"), NAME("체결 예상가:")), priceLabel2 = comp(JLabel::new, TEXT("0원"), NAME("총액:"));
	JLabel commission = comp(JLabel::new, TEXT("0원"), NAME("수수료 (0.25%):")), totalPrice = comp(JLabel::new, TEXT("0원"), NAME("합계:")), numberYes = comp(JLabel::new, TEXT("0 주"), NAME("주문 가능 수량:"));
	JButton buyOrder = comp(JButton::new, TEXT("매수 주문"), FG(Color.white), BG(Color.red));
	
	public StockInfor(int s_no) {
		ButtonGroup bg = new ButtonGroup();
		bg.add(rb1);
		bg.add(rb2);
		rb1.setSelected(true);
		priceSelect.setEnabled(false);
		stock = stockEntity.findById(s_no).get();
		stockPrice2 = comp(JLabel::new, TEXT("현재가: " + stockPrice.getText() + " [" + (stock.s_market.equals("NASDAQ") ? "USD" : "KRW") + "]"));
		setFrame(stock.s_name + " (" + stock.s_code + ")" + " 종목 상세", 1000, 700, () -> {});
		
	}
	
	@Override
	protected void desing() {
		JLabel stockLabel = new JLabel(stock.s_name + " " + stock.s_code + " | " + stock.s_market + " ");
		JPanel topPanel = new BoxPanel(BoxPanel.R, 5, 5, 5, stockLabel, stockPrice, comp(JLabel::new, TEXT(""), SIZE(100, 1)), plusMinus);
		topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, BoxPanel.fillWidth(topPanel), BoxPanel.fill(mainPanel()));
		add(BoxPanel.fill(panel));
	}

	private JPanel mainPanel() {
		JPanel chartPanel = new BoxPanel(BoxPanel.C, 0, 1, 0, new BoxPanel(BoxPanel.R, 5, 3, 0, new JLabel("주기"), time, new JLabel("이동평균"), MA5, MA20, MA60), 
				comp(JPanel::new, BG(Color.black))
				);
		JLabel nomalLabel = set(new JLabel(stock.s_name + " (" + stock.s_code + ")"), SIZE(350, getFontMetrics(getter.font).getHeight()));
		
		JPanel orderInfor = new BoxPanel(BoxPanel.C, 0, 0, 0, new JScrollPane(Util.getTable("유형,주문,수량,가격,상태,시각".split(","))));
		orderInfor.setBorder(getter.titleBorder("주문 내역"));
		orderInfor.setPreferredSize(new Dimension(350, 150));
		JPanel orderPanel = new BoxPanel(BoxPanel.C, 10, 5, 10, 
				BoxPanel.fill(nomalLabel),
				BoxPanel.fill(stockPrice2),
				BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 2, 0, BoxPanel.fillWidth(buy), BoxPanel.fillWidth(sell))),
				BoxPanel.fill(panel1(accounts)),
				BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 5, 0, new JLabel("주문 유형"), BoxPanel.HGAP(), rb1, rb2)),
				BoxPanel.fill(panel1(priceSelect)),
						BoxPanel.fill(panel1(numberSelect)),
				BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 3, 0, BoxPanel.fillWidth(pb1),BoxPanel.fillWidth(pb2),BoxPanel.fillWidth(pb3),BoxPanel.fillWidth(pb4))),
				BoxPanel.fill(panel1(priceLabel1)),
						BoxPanel.fill(panel1(priceLabel2)),
								BoxPanel.fill(panel1(commission)),
				BoxPanel.fill(panel1(totalPrice)),
						BoxPanel.fill(panel1(numberYes)),
				BoxPanel.fillWidth(buyOrder),
				BoxPanel.fillWidth(orderInfor)
				);
		orderPanel.setBorder(getter.em(0, 10, 0, 10));
		JPanel panel = new BoxPanel(BoxPanel.R, 10, 10, 10, BoxPanel.fill(chartPanel), BoxPanel.fillHeight(orderPanel));
		return panel;
	}
	
	private JPanel panel1(JComponent c) {
		if(c instanceof JLabel) ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
		JLabel label = comp(JLabel::new, TEXT(c.getName()), SIZE(100, c.getPreferredSize().height));
		return new BoxPanel(BoxPanel.R, 0, 0, 0, label, BoxPanel.fillWidth(c));
	}
	
	@Override
	protected void action() {
		ActionListener ac = e -> priceSelect.setEnabled(e.getSource() == rb2);
		rb1.addActionListener(ac);
		rb2.addActionListener(ac);
	}

	public static void main(String[] args) {
		UIManager.put("ToggleButton.select", getter.color);
		UIManager.put("ToggleButton.foreground", Color.white);
		UIManager.put("ToggleButton.background", getter.lightDarkColor);
		UIManager.put("ToggleButton.font", getter.font);
		
		UIManager.put("Label.font", getter.font);
		UIManager.put("Button.font", getter.font);
		Util.start(new StockInfor(1));
	}
}
