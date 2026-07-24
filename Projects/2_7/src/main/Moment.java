package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Moment extends CFrame{
	LocalDate now = LocalDate.now();
	LocalDate date = LocalDate.now();
	JPanel daysPanel = set(new JPanel(new GridLayout(6, 7)), BG(Color.white));
	
	JLabel dateLabel 	= lb("" , HOA(JLabel.CENTER), VEA(JLabel.CENTER), FONT(getter.font.deriveFont(14f).deriveFont(1)));
	JLabel left 		= lb("<", HOA(JLabel.CENTER), VEA(JLabel.CENTER));
	JLabel right 		= lb(">", HOA(JLabel.CENTER), VEA(JLabel.CENTER));
	
	List<JLabel> labels = new ArrayList<>();
	
	public Moment() {
		setDate(0);
		setFrame("달력", 350, 350, () -> {});
	}
	@Override
	public void desing() {
		JPanel panel = col(0, setP1(), setP2(), f(daysPanel));
		JButton button = bt("선택", BG(getter.color), SIZE(0, 40), FG(Color.white), FONT(getter.font.deriveFont(13f)));
		add(col(10, 0, 0, f(panel), fw(set(row(0, f(button)), BORDER(getter.em(15, 30, 15, 30))))).setBackColor(Color.white));
	}

	@Override
	public void action() {
		left.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setDate(-1);
			}
		});
		right.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setDate(1);
			}
		});
	}
	
	private JPanel setP1() {
		JPanel p = new JPanel(new GridLayout(1, 3));
		p.setBorder(BorderFactory.createCompoundBorder(getter.line, getter.em(10, 0, 10, 0)));
		p.setBackground(Color.white);
		p.add(left);
		p.add(dateLabel);
		p.add(right);
		return p;
	}
	
	private JPanel setP2() {
		JPanel p = new JPanel(new GridLayout(1, 7));
		p.setBorder(BorderFactory.createCompoundBorder(getter.line, getter.em(10, 0, 10, 0)));
		p.setBackground(Color.white);
		for(String s : "일,월,화,수,목,금,토".split(",")) {
			p.add(lb(s, FG( s.equals("일") ? Color.red : s.equals("토") ? Color.blue : Color.black ), HOA(JLabel.CENTER), VEA(JLabel.CENTER)));
		}
		return p;
	}
	
	private void setDaysPanel() {
		daysPanel.removeAll();
		LocalDate date = LocalDate.of(this.date.getYear(), this.date.getMonthValue(), this.date.getDayOfMonth());
		date = date.minusDays(date.getDayOfMonth() - 1);
		int n = date.getDayOfWeek().getValue() % 7;
		for(int i = 0; i < n; i++) {
			JLabel l = lb("",BORDER(getter.line(Color.LIGHT_GRAY)));
			daysPanel.add(l);
		}
		for(int i = 1; i <= date.lengthOfMonth(); i++) {
			JLabel l = lb(i + "",BORDER(getter.line(Color.LIGHT_GRAY)), HOA(JLabel.CENTER), VEA(JLabel.CENTER), BG(Color.white));
			l.setOpaque(true);
			daysPanel.add(l);
			if(now.isAfter(date)) l.setEnabled(false);
			else labels.add(l);
			if(now.equals(date)) l.setBackground(Color.orange);
			date = date.plusDays(1);
		}
		int size = 42 - daysPanel.getComponentCount();
		for(int i = 0; i < size; i++) {
			JLabel l = lb("",BORDER(getter.line(Color.LIGHT_GRAY)));
			daysPanel.add(l);
		}
		repaint();
	}
	
	private void setDate(int m) {
		date = date.plusMonths(m);
		dateLabel.setText(date.format(DateTimeFormatter.ofPattern("yyyy년 M월")));
		setDaysPanel();
	}
	
	public static void main(String[] args) {
		Util.start(new Moment());
	}

}
