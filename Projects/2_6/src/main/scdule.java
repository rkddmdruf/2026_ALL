package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import orms.*;
import uitls.*;
import static uitls.BoxPanel.*;
import static uitls.Properties.*;

public class scdule extends CFrame{
	LocalTime firstTime = LocalTime.of(6, 0);
	final LocalTime endTime = LocalTime.of(23, 0);
	LocalDateTime ldt = LocalDateTime.now();
	List<stationEntity> stations = stationEntity.findAll();
	List<Integer> totalPix = Arrays.asList(0);
	List<Integer> bfs = new ArrayList<>();
	stationEntity start, end;
	public JLabel tLabel = lb(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd/hh:mm")), FONT(getter.font));
	
	public scdule(int start, int end) {
		this.start = stationEntity.findById(start).get();
		this.end = stationEntity.findById(end).get();
		bfs = Util.bfs(start, end, totalPix);
		setFrame("노선 스케줄", 400, 550, () -> {});
	}

	@Override
	protected void desing() {
		setTime();
		JLabel logo = new JLabel(new ImageIcon(Util.logo.getScaledInstance(110, 40, 4)));
		JLabel seLabel = lb(start.name + " → " + end.name, FONT(getter.font.deriveFont(16f).deriveFont(1)));
		CButton bt = comp(CButton::new , TEXT("시간 변경"), SIZE(110, 25), FG(Color.white), BG(getter.color));
		bt.arc = 25;
		JLabel timeLine = lb("<html>서구청<br>입니다</html>", FONT(getter.font), HOA(JLabel.LEFT), BORDER(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)));
		JPanel topPanel = set(col(10, 0, 0, row(0, logo, hg(60),  seLabel).setBackColor(Color.white), row(5, bt, tLabel).setBackColor(Color.white), fw(timeLine)).setBackColor(Color.white), BORDER(getter.em(5, 5, 0, 5)));
		JScrollPane sc = set(new JScrollPane(setPanel(), JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BG(Color.white), BORDER(getter.em(5, 5, 5, 5)));
		add(col(0, fw(topPanel), f(sc)).setBackColor(Color.white));
	}
	
	private JPanel setPanel() {
		System.out.println(totalPix.get(0) * 0.05 /40*60);
		JPanel mainPanel = new JPanel(new GridLayout(0, 1, 10, 10));
		mainPanel.setBorder(getter.em(5, 5, 5, 5));
		mainPanel.setBackground(Color.white);
		int totalTime = (int) Math.ceil((totalPix.get(0) * 0.05 /40*60));
		for(int i = 0; i < 10; i++) {
			
			JPanel panel = new JPanel(new BorderLayout()) {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(getter.color);
					g.fillRoundRect(0, 0, 6, getHeight(), 25, 20);
					g.setColor(Color.lightGray);
					g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				}
			};
			set(panel, BORDER(getter.em(5, 45, 5, 45)), BG(Color.white), SIZE(0, 100));
			
			JPanel p = new JPanel(new GridLayout(3, 3, 0, 0));
			p.add(lb(start.name, FONT(getter.font.deriveFont(16f))));
			p.add(lb("[I2->I7]", HOA(JLabel.CENTER)));
			p.add(lb(end.name, FONT(getter.font.deriveFont(16f))));
			p.add(lb(firstTime.format(DateTimeFormatter.ofPattern("hh:mm")),FONT(getter.font.deriveFont(17f).deriveFont(1)), FG(getter.color)));
			p.add(lb("→", FG(getter.color), HOA(JLabel.CENTER)));
			p.add(lb(firstTime.plusMinutes(totalTime).format(DateTimeFormatter.ofPattern("hh:mm")),FONT(getter.font.deriveFont(17f).deriveFont(1)), FG(getter.color)));
			JLabel img = new JLabel(getter.getImage("icon/trains", 50, 25));
			img.setHorizontalAlignment(JLabel.LEFT);
			p.add(img);
			p.add(lb(totalTime + "분 소요", FONT(getter.font.deriveFont(11f)), HOA(JLabel.CENTER)));
			set(p, BG(Color.white));
			panel.add(p);
			mainPanel.add(panel);
			firstTime = firstTime.plusMinutes(15);
		}
		return mainPanel;
	}
	
	private void setTime() {
		while(!firstTime.isAfter(ldt.toLocalTime())) firstTime = firstTime.plusMinutes(15);
	}
	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		Util.start(new scdule(43, 9));
	}
}
