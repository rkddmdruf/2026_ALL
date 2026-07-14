package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import orms.applicationEntity;
import orms.categoryEntity;
import orms.jobEntity;
import orms.locationEntity;
import orms.storeEntity;
import utils.BoxPanel;
import utils.BoxPanel;
import utils.CFrame;
import utils.Image;
import utils.getter;

public class Main extends CFrame{
	
	JTextField tf = new JTextField() {{
		setBorder(BorderFactory.createLineBorder(Color.lightGray));
	}};
	
	JLabel loginImage = new JLabel();
	JLabel loginStatus = new JLabel(getter.user == null ? "로그인이 필요합니다." : getter.user.u_name + "님") {{
		setFont(getter.font.deriveFont(1));
	}};
	JButton login = new JButton(getter.user == null ? "로그인" : "로그아웃") {{
		setBackground(getter.color);
		setForeground(Color.white);
	}};
	
	public Main() {
		borderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		setFrame("메인", 825, 550, null);
	}
	

	@Override
	protected void design() {
		JPanel northPanel = new BoxPanel(BoxPanel.C, 0, 15, 0, topBar(), menuCards());
		JPanel p = setCenterPanel();
		JPanel southPanel = setSouthPanel();
		borderPanel.add(BoxPanel.fill(new BoxPanel(BoxPanel.C, 0, 15, 0, northPanel, BoxPanel.fill(p), southPanel)));
	}

	private JPanel setCenterPanel() {
		JLabel label1 = new JLabel("인기 공고");
		label1.setFont(getter.font.deriveFont(19f));
		
		JLabel label2 = new JLabel("전체보기 >");
		label2.setFont(getter.font.deriveFont(16f));
		label2.setForeground(getter.color);
		
		JPanel nPanel = new BoxPanel(BoxPanel.R, 0, 0, 0, label1, BoxPanel.HGAP(), label2).setBackColor(Color.white);
		
		JPanel gridPanel = new BoxPanel(BoxPanel.C, 0, 5, 0, jobEntity.findAll().stream().sorted((j1, j2) -> {
			int cnt1 = j1.getapplicationEntity().size();
			int cnt2 = j2.getapplicationEntity().size();
			
			int comp = cnt2 - cnt1;
			if (comp == 0) return j2.j_salary - j1.j_salary;
			return comp;
		}).limit(4).map(j -> {
			
			storeEntity store = j.getstoreEntity();
			categoryEntity category = store.getcategoryEntity();
			locationEntity location = store.getlocationEntity();
			return BoxPanel.fill(jobCard(j, store, category, location));
		}).toArray(JComponent[]::new)).setBackColor(Color.white);
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 15, 0, nPanel, BoxPanel.fill(gridPanel)).setBackColor(Color.white);
		panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
		
		return panel;
	}
	
	private JPanel setSouthPanel() {
		List<JPanel> list = new ArrayList<>();
		locationEntity.findAll().forEach(e -> {
			JPanel p = new JPanel();
			p.setBackground(Color.white);
			p.setBorder(new LineBorder(Color.black) {
			    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				    	int w = width - 1, h = height - 1;
				    	int arc = 20;
			            Graphics2D g2d = (Graphics2D) g;
			            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			            g2d.setColor(p.getBackground());
			            g2d.fillRect(x, y, w, h);
			            g2d.setColor(Color.white);
			            g2d.fillRoundRect(x, y, w, h, arc, arc);
			            g2d.setColor(lineColor);
			            g2d.drawRoundRect(x, y, w, h, arc, arc);
			    }
			});
			JLabel nameLabel = new JLabel(e.l_name);
			p.add(nameLabel);
			list.add(p);
		});
		
		JLabel label = new JLabel("지역별 바로가기");
		label.setFont(getter.font.deriveFont(16f).deriveFont(1));
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 5, 0, BoxPanel.fillWidth(label), BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 5, 0, list.toArray(JPanel[]::new))));
		return panel;
		
	}
	
	@Override
	protected void action() {
		
	}
	
	private JPanel topBar() {
	    JLabel label = new JLabel("ITALBA");
		label.setForeground(getter.color);
		label.setFont(getter.font.deriveFont(28f).deriveFont(1));
		int y = getFontMetrics(label.getFont()).getHeight();
		
		if(getter.user == null) loginImage.setPreferredSize(new Dimension(100, 10));
		else loginImage.setIcon(new ImageIcon(getter.user.u_image.getScaledInstance(y-2, y-2, 4)));
		
		JPanel panel = new BoxPanel(BoxPanel.R, 0, 10, 0, label, BoxPanel.fill(tf), loginImage, loginStatus, login);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.setBackground(Color.white);
	    return panel;
	}
	
	private JPanel menuCards() {// BoxPanel에 fill을 사용해도 크기가 다름
		JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
		String[] title = "지역별 알바,고시급 알바,최근 올라온 알바,지원 통계".split(",");
		String[] subTitle = "내 주변 공고 찾기,높은 시급순 보기,최근 근무 공고 보기,업종별 지원 형황".split(",");
		for(int i = 0; i < 4; i++) 
			panel.add(card(title[i], subTitle[i], () -> {}));
		return panel;
	}
	
	private JPanel card(String title, String subTitle, Runnable r) {
		JLabel titleLabel = new JLabel(title), subTitleLabel = new JLabel(subTitle);
		titleLabel.setFont(getter.font.deriveFont(1).deriveFont(16f));
		subTitleLabel.setFont(getter.font);
		subTitleLabel.setForeground(Color.LIGHT_GRAY);
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 5, 0, BoxPanel.fill(titleLabel), BoxPanel.fill(subTitleLabel));
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		return panel;
	}
	
	private JPanel jobCard(jobEntity job, storeEntity store, categoryEntity category, locationEntity location) {
		Color co = new Color(getter.color.getRed(), getter.color.getGreen(), getter.color.getBlue(), 10);
		JLabel westLabel = new JLabel(category.c_name, JLabel.CENTER);
		westLabel.setVerticalAlignment(JLabel.CENTER);
		westLabel.setForeground(getter.color);
		westLabel.setFont(getter.font.deriveFont(1).deriveFont(14f));
		westLabel.setPreferredSize(new Dimension(100, 0));
		westLabel.setOpaque(true);
		westLabel.setBackground(co);
		
		JLabel titleLabel = new JLabel(job.j_title);
		titleLabel.setFont(getter.font.deriveFont(1).deriveFont(15f));
		
		JLabel timeLabel = new JLabel(location.l_name + " . " + job.j_start + " ~ " + job.j_end) {{
			setVerticalAlignment(JLabel.TOP);
		}};
		timeLabel.setForeground(Color.LIGHT_GRAY);
		timeLabel.setFont(getter.font.deriveFont(11f));
		
		
		JLabel priceLabel = new JLabel("시급 " + job.j_salary + "원");
		priceLabel.setForeground(getter.color);
		priceLabel.setFont(getter.font.deriveFont(1).deriveFont(15f));
		priceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
		priceLabel.setHorizontalAlignment(JLabel.CENTER);
		priceLabel.setVerticalAlignment(JLabel.CENTER);
		
		JPanel panel = new BoxPanel(BoxPanel.R, 0, 10, 0
				, BoxPanel.fillHeight(westLabel)
				, new BoxPanel(BoxPanel.C, 0, 5, 0, BoxPanel.fillWidth(titleLabel), BoxPanel.fillWidth(timeLabel)).setBackColor(Color.white)
				, BoxPanel.HGAP()
				, priceLabel)
				.setBackColor(Color.white);
		panel.setPreferredSize(new Dimension(0, 100));
		panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println(store.s_name);
			}
		});
		
		return panel;
	}
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
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
