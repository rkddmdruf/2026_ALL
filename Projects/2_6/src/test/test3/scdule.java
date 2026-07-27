package test.test3;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.Util;
import orms.*;
import uitls.*;
import static uitls.BoxPanel.*;
import static uitls.Properties.*;

public class scdule extends CFrame{
	public LocalTime firstTime = LocalTime.of(6, 0);
	private final LocalTime endTime = LocalTime.of(23, 0);
	public LocalDateTime ldt = LocalDateTime.now();
	List<stationEntity> stations = stationEntity.findAll();
	List<Integer> totalPix = Arrays.asList(0);
	List<Integer> bfs = new ArrayList<>();
	stationEntity start, end;
	public JLabel tLabel = lb(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd/hh:mm")), FONT(getter.font));
	
	JPanel mainPanel = set(new JPanel(new GridLayout(0, 1, 10, 10)), BG(Color.white), BORDER(getter.em(5, 5, 5, 5)));
	JScrollPane sc = set(new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BG(Color.white), BORDER(getter.em(5, 5, 5, 5)));
	CButton bt = comp(CButton::new , TEXT("시간 변경"), SIZE(110, 25), FG(Color.white), BG(getter.color));
	
	public scdule(int start, int end) {
		this.start = stationEntity.findById(start).get();
		this.end = stationEntity.findById(end).get();
		bfs = Util.bfs(start, end, totalPix);
		bt.arc = 25;
		setFrame("노선 스케줄", 400, 550, () -> {new metro("","").setVisible(true);});
	}

	@Override
	protected void desing() {
		setTime();
		init();
		JLabel logo = new JLabel(new ImageIcon(Util.logo.getScaledInstance(110, 40, 4)));
		JLabel seLabel = lb(start.name + " → " + end.name, FONT(getter.font.deriveFont(16f).deriveFont(1)));
		JTextArea ta = comp(JTextArea::new, TEXT(setTLStr()), FONT(getter.font.deriveFont(11f)), BORDER(getter.com(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), getter.em(0, 0, 0, 30))));
		ta.setLineWrap(true);
		JPanel topPanel = set(col(10, 0, 0, row(0, logo, hg(60),  seLabel).setBackColor(Color.white), row(5, bt, tLabel).setBackColor(Color.white), fw(ta)).setBackColor(Color.white), BORDER(getter.em(5, 5, 0, 5)));
		add(col(0, fw(topPanel), f(sc)).setBackColor(Color.white));
	}
	
	public void init() {
		setPanel();
		set(tLabel, TEXT(ldt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm"))));
		revalidate();
		repaint();
	}
	private void setPanel() {
		mainPanel.removeAll();
		String str = Arrays.asList(setTLStr().split(" > ")).stream()
				.filter(e -> e.contains("["))
				.map(e -> String.join("", e.split("환승")))
				.collect(Collectors.joining("→"));
		int totalTime = (int) Math.ceil((totalPix.get(0) * 0.05 /40*60));
		LocalTime time = ldt.toLocalTime();
		while(!time.isAfter(endTime)) {
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
			
			JLabel label = lb(str, HOA(JLabel.CENTER), BG(new Color(255, 140, 0)), FG(Color.white), FONT(getter.font.deriveFont(11f)));
			label.setOpaque(true);
			JPanel p = new JPanel(new GridLayout(3, 3, 0, 0));
			p.add(lb(start.name, FONT(getter.font.deriveFont(16f))));
			p.add(new JPanel() {{
				setBackground(Color.white);
				add(label);
			}});
			p.add(lb(end.name, FONT(getter.font.deriveFont(16f))));
			p.add(lb(time.format(DateTimeFormatter.ofPattern("HH:mm")),FONT(getter.font.deriveFont(17f).deriveFont(1)), FG(getter.color)));
			p.add(lb("→", FG(getter.color), HOA(JLabel.CENTER)));
			p.add(lb(time.plusMinutes(totalTime).format(DateTimeFormatter.ofPattern("HH:mm")),FONT(getter.font.deriveFont(17f).deriveFont(1)), FG(getter.color)));
			JLabel img = new JLabel(getter.getImage("icon/trains", 50, 25));
			img.setHorizontalAlignment(JLabel.LEFT);
			p.add(img);
			p.add(lb(totalTime + "분 소요", FONT(getter.font.deriveFont(11f)), HOA(JLabel.CENTER)));
			set(p, BG(Color.white));
			panel.add(p);
			mainPanel.add(panel);
			time = time.plusMinutes(15);
		}
	}
	
	private String setTLStr() {
		String[] str = "석남,부평구청,인천시청".split(",");
		String s = "";
		List<String> stationNames = new ArrayList<>();
		Map<String, List<Integer>> stations = stationEntity.findBy(e -> Arrays.asList(str).contains(e.name)).stream().collect(Collectors.groupingBy(e -> e.name, Collectors.mapping(e -> e.sno, Collectors.toList())));
		IntStream.range(0, bfs.size()).forEach(i -> {
			int size = stationNames.size();
			for(String key : stations.keySet()) {
				List<Integer> list = stations.get(key);
				if(bfs.get(i) == list.get(0) && bfs.get(i + 1) == list.get(1)) {
					stationNames.add(stationEntity.findById(bfs.get(i)).get().name);
					stationNames.add("[" + stationEntity.findById(list.get(0)).get().line.substring(0, 2) + "→" + stationEntity.findById(list.get(1)).get().line.substring(0, 2) + "환승]");
				}else if(bfs.get(i) == list.get(1) && bfs.get(i + 1) == list.get(0)) {
					stationNames.add(stationEntity.findById(bfs.get(i)).get().name);
					stationNames.add("[" + stationEntity.findById(list.get(1)).get().line.substring(0, 2) + "→" + stationEntity.findById(list.get(0)).get().line.substring(0, 2) + "환승]");
				}
			}
			if(size == stationNames.size()) {
				stationNames.add(stationEntity.findById(bfs.get(i)).get().name);
			}
		});
		return stationNames.stream().collect(Collectors.joining(" > "));
	}
	
	private void setTime() {
		while(!firstTime.isAfter(ldt.toLocalTime())) firstTime = firstTime.plusMinutes(15);
	}
	@Override
	protected void action() {
		bt.addActionListener(e -> {
			new TimeSelect(this).setVisible(true);
			init();
		});
	}

	public static void main(String[] args) {
		Util.start(new scdule(43, 9));//46, 12
	}
}
