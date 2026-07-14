package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import orms.categoryEntity;
import orms.jobEntity;
import orms.locationEntity;
import orms.storeEntity;
import utils.BoxPanel;
import utils.CFrame;
import utils.Html;
import utils.getter;

public class Serch extends CFrame{
	// 수정해야 하는거 업직종검색 이상, 출근 시간검색 이상
	JTextField tf = new JTextField();
	JComboBox<String> location = new JComboBox<>();
	JComboBox<String> category = new JComboBox<>();
	JComboBox<String> stratTime = new JComboBox<>();
	JComboBox<String> endTime = new JComboBox<>();
	JComboBox<String> order_by = new JComboBox<>();
	List<JComboBox<String>> combos = Arrays.asList(location, category, stratTime, endTime, order_by);
	
	JLabel serch_ResultCount_Label = new JLabel("총 0건") {{
		setFont(getter.font.deriveFont(1).deriveFont(18f));
	}};
	BoxPanel mainGridPanel = new BoxPanel(BoxPanel.C, 0, 0, 0, new JLabel("d")).setBackColor(Color.white);
	JScrollPane sc = new JScrollPane(mainGridPanel) {{
		setBorder(null);
		setBackground(Color.white);
	}};
	
	public Serch() {
		setComboBox();
		borderPanel.setLayout(new BorderLayout(15, 15));
		borderPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
		borderPanel.setBackground(Color.white);
		setFrame("검색", 675, 425, new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				new Main();
			}
		});
	}

	private void setComboBox() {
		combos.forEach(e -> {
			e.addItem("전체");
		});
		locationEntity.findAll().forEach(c -> {
			location.addItem(c.l_name);
		});
		categoryEntity.findAll().forEach(c -> {
			category.addItem(c.c_name);
		});
		IntStream.range(0, 24).forEach(c -> {
			String time = c + "시간";
			stratTime.addItem(time);
			endTime.addItem(time);
		});
		Arrays.asList("급여 높은 순, 급여 낮은 순, 최신순".split(", ")).forEach(c -> {
			order_by.addItem(c);
		});
	}
	
	@Override
	protected void design() {
		reloadMainPanel();
		
		JLabel label = new JLabel("채용정보 검색 결과");
		label.setFont(getter.font.deriveFont(1).deriveFont(20f));
		
		//여기에서 BoxUI를 쓰면 5개의 컴포넌트 크기가 다르다. -> 6/26일 수정 사항 크기가 제일 큰거로 모두 맞추면 크기는 어느정도 맞다.
		String[] str = "지역,업직종,출근시간,퇴근시간,정렬".split(",");
		
		JPanel combosPanel = new BoxPanel(BoxPanel.R, 0, 10, 0, IntStream.range(0, 5).mapToObj(c -> BoxPanel.fill(comBoBoxCardPanel(c, str[c]))).toArray(JComponent[]::new)).setBackColor(Color.white);
		combosPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.lightGray), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		
		JPanel mainNorthPanel = new BoxPanel(BoxPanel.R, 0, 15, 0, label, serch_ResultCount_Label).setBackColor(Color.white);
		JPanel mainPanel = new BoxPanel(BoxPanel.C, 0, 0, 0, mainNorthPanel, BoxPanel.fill(sc)).setBackColor(Color.white);
		
		borderPanel.add(new BoxPanel(BoxPanel.C, 0, 15, 0, BoxPanel.fillWidth(tf), combosPanel, mainPanel).setBackColor(Color.white));
	}

	@Override
	protected void action() {
		Runnable r = () -> reloadMainPanel();
		tf.addActionListener(e -> r.run());
		for(JComboBox<String> c : combos)
			c.addActionListener(e -> r.run());
	}

	private void reloadMainPanel() {// 조건 살피기
		mainGridPanel.removeAll();
		List<jobEntity> jobs = jobEntity.findAll();
		List<Predicate<jobEntity>> predicates = new ArrayList<>();
		String[] strs = "l_name,c_name,j_start,j_end".split(",");
		for(int i = 0; i < combos.size() - 1; i++) {
			JComboBox<String> combo = combos.get(i);
			if(combo.getSelectedIndex() != 0) {
				if(i == 0)
					predicates.add(e -> locationEntity.findById(storeEntity.findById(e.s_no).get().l_no).get().l_no.equals(combo.getSelectedIndex()));
				if(i == 1)
					predicates.add(e -> categoryEntity.findById(storeEntity.findById(e.s_no).get().c_no).get().c_no.equals(combo.getSelectedIndex()));
				if(i == 2)
					predicates.add(e -> e.j_start.isAfter(LocalTime.of(combo.getSelectedIndex(), 0, 0)));
				if(i == 3)
					predicates.add(e -> e.j_end.isBefore(LocalTime.of(combo.getSelectedIndex(), 0, 0)));
			}
		}
		if(!tf.getText().isBlank())
			predicates.add(e -> e.j_title.contains(tf.getText()));
		
		List<jobEntity> jscopy = List.copyOf(jobs).stream()
				.filter(
						c -> predicates.stream()
							.allMatch(p -> p.test(c))
					)
				.collect(Collectors.toList());
		
		int selectOrder = order_by.getSelectedIndex();
		serch_ResultCount_Label.setText("총 " + jscopy.size() + "건");
		if(selectOrder != 0) {
			Comparator<jobEntity> comparator = Comparator.comparing(o -> ((jobEntity) o).j_salary);
			if(selectOrder == 1)
				jscopy.sort(comparator.reversed());
			else if(selectOrder == 2)
				jscopy.sort(comparator);
			else
				jscopy.sort(Comparator.comparing(o -> ((jobEntity) o).j_regdate));
		}
		jscopy.stream().forEach(e -> {
			storeEntity store =  storeEntity.findById(e.s_no).get();
			locationEntity location = locationEntity.findById(store.l_no).get();
			mainGridPanel.add(jobInforPanel(e, store, location));
		});
		revalidate();
		repaint();
	}
	
	private JPanel jobInforPanel(jobEntity job, storeEntity store, locationEntity location) {
		JLabel westLabel = new JLabel(new Html().add(location.l_name).nextLine().add(store.s_name).getString());
		westLabel.setPreferredSize(new Dimension(87, westLabel.getPreferredSize().height));
		
		JLabel centerLabel = new JLabel(new Html().add(job.j_title).nextLine()
				.forgeColor("orange").add("시급 " + job.j_salary + "원").endForgeColor().add(" · " + job.j_start + " ~ " + job.j_end).nextLine()
				.forgeColor("gray").add("등록일 " + job.j_regdate).endForgeColor()
				.getString());
		centerLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		
		JButton lookButton = new JButton("보기");
		lookButton.setBackground(Color.white);
		
		JPanel p = BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 20, 0, westLabel, centerLabel, BoxPanel.HGAP(), BoxPanel.fillHeight(lookButton))).setBackColor(Color.white);
		p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
		return p;
	}
	
	private JPanel comBoBoxCardPanel(int n, String s) {
		JLabel label = new JLabel(s);
		label.setFont(getter.font.deriveFont(1).deriveFont(15f));
		
		BoxPanel boxPanel = new BoxPanel(BoxPanel.C, 0, 10, 0, BoxPanel.fill(label), BoxPanel.fill(combos.get(n)));
		combos.get(n).setPreferredSize(new Dimension(combos.get(2).getPreferredSize().width, combos.get(n).getPreferredSize().height));
		return boxPanel.setBackColor(Color.white);
	}
	
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Serch().setVisible(true));
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
            @Override
            protected void dispatchEvent(AWTEvent event) {
                try {
                    super.dispatchEvent(event);
                } catch (Exception e) {
                    handle(e);
                }
            }
        });
    }

    private static void handle(Throwable throwable) {
        throwable.printStackTrace();
        JOptionPane.showMessageDialog(null, throwable.getMessage()); 
    }
    
}
