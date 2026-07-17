package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import orms.accountEntity;
import utils.BoxPanel;
import static utils.Properties.*;
import utils.getter;

public class Account extends JPanel{
	String[] accountsString = accountEntity.findBy(e -> e.u_no == getter.user.u_no).stream().map(c -> c.a_name).toArray(String[]::new);
	JComboBox<String> accountCategory = set(new JComboBox<String>("주식거래,CMA,외화".split(",")), NAME("계좌 유형"));
	JTextField accountName = comp(JTextField::new, NAME("계좌명"));
	JButton newAccountButton = Util.button("계좌 개설");
	
	JComboBox<String> userAccount = set(new JComboBox<String>(accountsString), NAME("계좌"));
	JTextField payMentTf = comp(JTextField::new, NAME("중전 금액"));
	JButton payMent = Util.button("충전");
	
	JComboBox<String> outAccount = set(new JComboBox<>(accountsString), NAME("출금 계좌"));
	JLabel outAccountProperty = comp(JLabel::new, TEXT("0원"), NAME("출금 잔고"));
	JComboBox<String> inAccount = set(new JComboBox<>(accountsString), NAME("입금 계좌"));
	JLabel inAccountProperty = comp(JLabel::new, TEXT("0원"), NAME("입금 잔고"));
	JLabel KRW_USD = comp(JLabel::new, TEXT("1 USD = 1500.0000 KRW"), NAME("환율"));
	JTextField K_U_ChangeTF = comp(JTextField::new, NAME("환전 금액"));
	JLabel commissionLabel = comp(JLabel::new, NAME("수수료 (1%)"), TEXT("-"));
	JLabel resultLabel = comp(JLabel::new, NAME("수령 금액"), TEXT("-"));
	JButton chageButton = Util.button("환전 실행");
	
	JComboBox<String> outAccountTransfer = set(new JComboBox<>(accountsString), NAME("출금"));
	JComboBox<String> inAccountTransfer = set(new JComboBox<>(accountsString), NAME("입금"));
	JTextField accountTransferTF = comp(JTextField::new, NAME("금액"));
	JButton accountTransferButton = Util.button("이체");
	
	public Account() {
		setLayout(new BorderLayout());
		
		JPanel panel = new BoxPanel(BoxPanel.R, 10, 10, 10, BoxPanel.fill(leftPanel()), BoxPanel.fill(rightPanel()));
		add(panel);
	}
	
	private JPanel leftPanel() {
		JPanel p1 = new BoxPanel(BoxPanel.C, 5, 2, 5, panel1(accountCategory), panel1(accountName), panel1(newAccountButton));
		p1.setBorder(getter.titleBorder("계좌 개설"));
		JPanel p2 = new BoxPanel(BoxPanel.C, 5, 2, 5, panel1(userAccount), panel1(payMentTf), panel1(payMent));
		p2.setBorder(getter.titleBorder("입금/충전"));
		JPanel p3 = new BoxPanel(BoxPanel.C, 5, 2, 5, panel1(outAccount), panel1(outAccountProperty), panel1(inAccount),panel1(inAccountProperty),panel1(KRW_USD),panel1(K_U_ChangeTF), panel1(commissionLabel),
				panel1(resultLabel), panel1(chageButton));
		p3.setBorder(getter.titleBorder("환전"));
		JPanel p4 = new BoxPanel(BoxPanel.C, 5, 2, 5, panel1(outAccountTransfer), panel1(inAccountTransfer), panel1(accountTransferTF), panel1(accountTransferButton));
		p4.setBorder(getter.titleBorder("계좌 이체"));
		JPanel panel = new BoxPanel(BoxPanel.C, 10, 10, 10, p1, p2, p3, p4);
		return panel;
	}
	
	private JPanel rightPanel() {
		JPanel p1 = new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fill(new JScrollPane(Util.getTable("출금 계좌,입금 계좌,금액,날짜".split(",")))));
		p1.setBorder(getter.titleBorder("이체 내역"));
		JPanel p2 = new BoxPanel(BoxPanel.C, 0, 0, 0, BoxPanel.fill(new JScrollPane(Util.getTable("환전,출금 계좌,입금 계좌,출금액,수령액,날짜".split(",")))));
		p2.setBorder(getter.titleBorder("환전 내역"));
		
		JPanel panel = new BoxPanel(BoxPanel.C, 10, 10, 10, BoxPanel.fill(p1), BoxPanel.fill(p2));
		Dimension d = p2.getPreferredSize();
		p1.setPreferredSize(new Dimension(d.width, d.height));
		return panel;
	}

	private JPanel NewAccount() {
		JPanel panel = new BoxPanel(BoxPanel.C, 5, 3, 5);
		panel.setBorder(getter.titleBorder("계좌 개설"));
		return panel;
	}
	
	private JPanel panel1(JComponent c) {
		if(c instanceof JLabel) ((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
		c.setPreferredSize(new Dimension(c.getPreferredSize().width, 25));
		JPanel panel = new BoxPanel( BoxPanel.R, 5, 0, 5, comp(JLabel::new, TEXT(c.getName() == null ? "" : c.getName()), SIZE(c.getName() == null ? 0 : 70, 25)), BoxPanel.fill(c));
		return panel;
		
	}
}
