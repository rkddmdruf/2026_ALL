package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import orms.*;
import utils.*;

public class JobLocation extends CFrame{
	
	JComboBox<String> cityName = new JComboBox<String>() {{
		locationEntity.findAll().forEach(c -> addItem(c.l_name));
	}};
	JComboBox<String> categoryName = new JComboBox<String>() {{
		addItem("전체");
		categoryEntity.findAll().forEach(c -> addItem(c.c_name));
	}};
	JComboBox<String> order = new JComboBox<String>("전체,가까운 알바순,급여순".split(","));
	JButton myLocation = new JButton("내 위치 찾기") {{
		setBackground(getter.orangeColor);
		setForeground(Color.white);
	}};
	
	List<storeEntity> stores = storeEntity.findAll();
	List<Ellipse2D> ovals = new ArrayList<>();
	
	JLabel map = new JLabel() {
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			setMap(g);
		};
	};
	
	Point userOval = null;
	JPanel popup;
	JLabel[] ls = new JLabel[5];
	public JobLocation() {
		setFrame("알바 위치 검색", 680, 451, new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
			}
		});
	}
	@Override
	protected void design() {
		setStoreList();
		setPopup();
		map.setPreferredSize(new Dimension(680, 420));
		
		JPanel northPanel = BoxUI.box(BoxUI.ROW, 0, 3, 0, BoxUI.HGAP(), cityName, categoryName, order, myLocation);
		northPanel.setBackground(Color.white);
		northPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JPanel panel = BoxUI.box(BoxUI.COL, 0, 0, 0, northPanel, BoxUI.fill(map));
		panel.setBackground(Color.white);
		borderPanel.add(panel);
	}
	
	@Override
	protected void action() {
		myLocation.addActionListener(e -> {
			if(getter.user == null) throw new RuntimeException("로그인이 되어있지 않습니다.");
			cityName.setSelectedIndex(getter.user.l_no - 1);
			userOval = new Point(getter.user.x, getter.user.y);
		});
		ActionListener actions = e -> {
			userOval = null;
			revalPaint();
		};
		categoryName.addActionListener(actions);
		order.addActionListener(actions);
		cityName.addActionListener(actions);
		
		map.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				ovals.forEach(c -> {
					if(c.contains(e.getX(), e.getY())) {
						new JobSelect(stores.get(ovals.indexOf(c)).s_no).setVisible(true);;
						dispose();
					}
				});
				
			};
		});
		
		locationEntity location = locationEntity.findById(cityName.getSelectedIndex() + 1).orElse(null);
		map.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(java.awt.event.MouseEvent e) {
				for(Ellipse2D c : ovals) {
					boolean found = false;
					if(c.contains(e.getPoint())) {
							storeEntity store = stores.get(ovals.indexOf(c));
							jobEntity job = store.getjobEntity()
									.stream()
									.sorted( Comparator.comparing( o -> Math.abs( ChronoUnit.DAYS.between( LocalDate.now(), ((jobEntity) o).j_regdate ) ) ) )
									.findFirst().orElseThrow(() -> new RuntimeException("값 없음"));
							ls[0].setText(categoryEntity.findById(store.c_no).get().c_name);
							ls[1].setText(job.j_title);
							ls[2].setText("시급 " + job.j_salary + "원");
							ls[3].setText(location.l_name);
							ls[4].setText(job.j_start.format(DateTimeFormatter.ofPattern("a h:mm")) + " ~ " + job.j_end.format(DateTimeFormatter.ofPattern("a h:mm")));
			                popup.setLocation(e.getX() + 10, e.getY() + 15);
			                popup.setSize(popup.getPreferredSize());
			                popup.setVisible(true);
			                found = true;
			                break;
					}
					if (!found) popup.setVisible(false);
				}
				map.setToolTipText(null);
			};
		});
	}
	
	private void setPopup() {
		for(int i = 0; i < 5; i++) {
			ls[i] = new JLabel("d");// 초기에 아무글자도 안넣으면 height가 0인듯 하다
			if(i != 4) 
				ls[i].setFont(getter.font.deriveFont(1).deriveFont(11f));
		}
		ls[2].setForeground(getter.color);
		ls[1].setFont(ls[1].getFont().deriveFont(12f));
		ls[4].setFont(getter.font);
		
		popup = new BoxPanel(BoxPanel.C, 0, 5, 0, Arrays.asList(ls).stream().map(e -> BoxPanel.fillWidth(e)).toArray(JComponent[]::new));
		
		popup.setOpaque(true);
		popup.setBackground(Color.WHITE);
		popup.setVisible(false);
		popup.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(3, 6, 3, 6)));
		popup.setPreferredSize(new Dimension(225, popup.getPreferredSize().height));
		
		map.add(popup);
	}
	
	private void setMap(Graphics g) {
		ovals.clear();
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(new ImageIcon("datafiles/region/" + cityName.getSelectedItem() + ".png").getImage(), 0, 0, 680, 420, null);
		int size = 10;
		
		g2.setColor(Color.black);
		stores.forEach(e -> {
			ovals.add(new Ellipse2D.Double(e.s_x - (size / 2), e.s_y - (size / 2), size, size));
			g2.fill(ovals.get(ovals.size() - 1));
		});
		
		if(userOval != null) {
			int userSize = 12;
			
			g2.setColor(Color.black);
			g2.setFont(getter.font.deriveFont(1));
			g2.drawString( getter.user.u_name , userOval.x - getFontMetrics(g2.getFont()).stringWidth(getter.user.u_name) / 2, userOval.y + userSize + (userSize / 2) + 3 );
			g2.setColor(Color.red); 
			g2.fill(new Ellipse2D.Double(getter.user.x - userSize / 2, getter.user.y - userSize/ 2, userSize, userSize));
		}
	}
	
	private void revalPaint() {
		setStoreList();
		map.revalidate();
		map.repaint();
	}
	
	private void setStoreList() {
		List<Predicate<storeEntity>> predicates = new ArrayList<>();
		predicates.add(c -> c.l_no == cityName.getSelectedIndex() + 1);
		if(categoryName.getSelectedIndex() != 0)
			predicates.add(c -> c.c_no == categoryName.getSelectedIndex());
		
		stores.clear();
		stores.addAll(storeEntity.findBy(c -> predicates.stream().allMatch(p -> p.test(c))));
		
		if(order.getSelectedIndex() != 0 && stores.size() > 5) {
			if(order.getSelectedIndex() == 1) { // 유저가 있는 도시로 이동?
				stores.sort(Comparator.comparing(o -> Math.sqrt(Math.pow((o.s_x - getter.user.x), 2) + Math.pow((o.s_y - getter.user.y), 2))));
				stores.subList(5, stores.size()).clear();
			}
			if(order.getSelectedIndex() == 2) {
				List<jobEntity> jobList = 
						jobEntity.findAll().stream()
						.filter(job -> stores.stream().map(store -> store.s_no).distinct().collect(Collectors.toList()).contains(job.s_no))
						.sorted(Comparator.comparing(o -> ((jobEntity) o).j_salary).reversed()).limit(5)
						.collect(Collectors.toList());
				
				List<storeEntity> list = stores.stream().filter(store -> jobList.stream().map(job -> job.s_no).collect(Collectors.toList()).contains(store.s_no)).collect(Collectors.toList());
				stores.clear();
				stores.addAll(list);
			}
		}
	}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobLocation().setVisible(true));
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
        getter.err(throwable.getMessage());
    }
}
