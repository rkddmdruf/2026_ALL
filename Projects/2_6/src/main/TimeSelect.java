package main;

import javax.swing.*;

import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class TimeSelect extends CFrame{
	LocalDateTime max = LocalDateTime.MAX;
	LocalDateTime min = LocalDateTime.MIN;
	LocalDateTime now = LocalDateTime.now();
	LocalDateTime select = LocalDateTime.now();
	JSpinner year = new JSpinner(new SpinnerNumberModel(now.getYear(), min.toLocalDate().getYear(), max.toLocalDate().getYear(), 1));
	JSpinner moment = new JSpinner(new SpinnerNumberModel(now.getMonthValue(), min.toLocalDate().getMonthValue(), max.toLocalDate().getMonthValue(), 1));
	JSpinner day = new JSpinner(new SpinnerNumberModel(now.getDayOfMonth(), now.minusDays(now.getDayOfMonth() - 1).getDayOfMonth(), now.toLocalDate().lengthOfMonth(), 1));
	
	JSpinner hour = new JSpinner(new SpinnerNumberModel(now.getHour(), 6, 22, 1));
	JSpinner minute = new JSpinner(new SpinnerNumberModel(now.getMinute(), min.toLocalTime().getMinute(), max.toLocalTime().getMinute(), 1));
	List<JSpinner> jps = Arrays.asList(year, moment, day, hour, minute);
	CButton b = comp(CButton::new, TEXT("확인"), FG(Color.white), BG(sp.color), SIZE(200, 50));
	JLabel l1 = lb("d", FONT(sp.font.deriveFont(1).deriveFont(13f)));
	JLabel l2 = lb("※ 시간은 06:00 ~ 23:00 범위에서 선택 가능합니다.", FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY));
	
	scdule s;
	public TimeSelect(scdule s) {
		this.s = s;
		setl1();
		year.setEditor(new JSpinner.NumberEditor(year, "#"));
		jps.forEach(e -> {
			e.setBorder(sp.line(sp.color));
			e.setForeground(sp.color);
			((JSpinner.DefaultEditor)e.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
			((JSpinner.DefaultEditor)e.getEditor()).getTextField().setFocusable(false);
			((JSpinner.DefaultEditor)e.getEditor()).getTextField().setForeground(sp.color);
			e.setFont(sp.font.deriveFont(20f).deriveFont(1));
		});
		setFrame("날짜/시간 설정", 425, 450, () -> {});
	}

	@Override
	protected void desing() {
		JPanel topPanel = set(col(10, 10, 10
				, lb("출발 날짜 / 시간 설정", FG(sp.color), FONT(sp.font.deriveFont(1).deriveFont(15f)))
				, lb("조회할 날짜와 출발 시간을 선택하세요", FG(Color.gray), FONT(sp.font)))
				.setBackColor(Color.white), BORDER(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.lightGray)));
		
		JPanel mainPanel = colF(10, daySp(), timeSp()).setBackColor(Color.white);
		JPanel buttonPanel = col(10, l1, b, l2).setBackColor(Color.white);
		JPanel panel = set(col(10, topPanel, f(mainPanel), buttonPanel), BORDER(sp.em(10, 10, 10, 5))).setBackColor(Color.white);
		add(panel);
	}

	private void setl1() {
		l1.setText(select.format(DateTimeFormatter.ofPattern("yyyy-MM-dd / HH:mm")) + " 이후 열차 조회");
	}
	
	private JPanel daySp() {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(sp.color.getRed(), sp.color.getGreen(), sp.color.getBlue(), 20));
				g.fillRoundRect(0, 0, getWidth() - 1,  getHeight() - 1, 20, 20);
				g.setColor(Color.LIGHT_GRAY);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				super.paintComponent(g);
			}
		};
		panel.setOpaque(false);
		set(panel, BORDER(sp.em(5, 10, 10, 10)));
		panel.add(lb("날짜 선택", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(11f)), FG(sp.color)), BorderLayout.NORTH);
		
		List<JPanel> panels = Arrays.asList(
				col(5, lb("년", FG(sp.color)), f(year)), 
				col(5, lb("월", FG(sp.color)), f(moment)), 
				col(5, lb("일", FG(sp.color)), f(day)));
		panels.forEach(e -> {
			Arrays.asList(e.getComponents()).forEach(c -> ((JComponent) c).setOpaque(false));
			e.setOpaque(false);
		});
		JPanel mainPanel = new JPanel(new GridLayout(1, 3, 10, 10));
		panels.forEach(e -> mainPanel.add(e));
		mainPanel.setOpaque(false);
		panel.add(mainPanel);
		return panel;
	}
	
	private JPanel timeSp() {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(sp.color.getRed(), sp.color.getGreen(), sp.color.getBlue(), 20));
				g.fillRoundRect(0, 0, getWidth() - 1,  getHeight() - 1, 20, 20);
				g.setColor(Color.LIGHT_GRAY);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				super.paintComponent(g);
				g.setColor(sp.color);
				g.setFont(sp.font.deriveFont(50f));
				g.drawString(":", getWidth() / 2, getHeight() / 2 + 25);
			}
		};
		panel.setOpaque(false);
		set(panel, BORDER(sp.em(5, 10, 10, 10)));
		panel.add(lb("날짜 선택", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(11f)), FG(sp.color)), BorderLayout.NORTH);
		
		List<JPanel> panels = Arrays.asList(
				col(5, lb("시", FG(sp.color)), f(hour)),
				col(5, lb("분", FG(sp.color)), f(minute)));
		panels.forEach(e -> {
			Arrays.asList(e.getComponents()).forEach(c -> ((JComponent) c).setOpaque(false));
			e.setOpaque(false);
		});
		JPanel mainPanel = set(new JPanel(new GridLayout(1, 3, 40, 10)), BORDER(sp.em(0, 20, 0, 40)));
		panels.forEach(e -> mainPanel.add(e));
		mainPanel.setOpaque(false);
		panel.add(mainPanel);
		return panel;
	}
	@Override
	protected void action() {
		moment.addChangeListener(e -> {
			day.setModel(new SpinnerNumberModel((int) day.getValue(), 1, LocalDate.of((int) year.getValue(), (int) moment.getValue(), 1).lengthOfMonth(), 1));
			day.setBorder(sp.line(sp.color));
			day.setForeground(sp.color);
			((JSpinner.DefaultEditor)day.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
			((JSpinner.DefaultEditor)day.getEditor()).getTextField().setFocusable(false);
			((JSpinner.DefaultEditor)day.getEditor()).getTextField().setForeground(sp.color);
			day.setFont(sp.font.deriveFont(20f).deriveFont(1));
		});
		jps.forEach(j -> {
			j.addChangeListener(ac -> {
				try {
					select = LocalDateTime.of((int) year.getValue(), (int) moment.getValue(), (int) day.getValue(), (int) hour.getValue(), (int) minute.getValue());
					setl1();
				} catch (Exception e) {
					day.setValue(LocalDate.of((int) year.getValue(), (int) moment.getValue(), 1).lengthOfMonth());
					select = LocalDateTime.of((int) year.getValue(), (int) moment.getValue(), (int) day.getValue(), (int) hour.getValue(), (int) minute.getValue());
					setl1();
				}
			});
		});
		b.addActionListener(e -> {
			s.firstTime = select.toLocalTime();
			s.ldt = select;
			s.init();
			dispose();
		});
	}
}
