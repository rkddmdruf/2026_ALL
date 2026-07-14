package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import orms.applicationEntity;
import orms.favoriteEntity;
import orms.jobEntity;
import orms.locationEntity;
import orms.storeEntity;
import utils.BoxPanel;
import utils.CFrame;
import utils.Html;
import utils.getter;

public class JobInfor extends CFrame{

	JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {{
		setBackground(Color.white);
	}};
	
	JScrollPane sc = new JScrollPane(mainPanel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER) {{
		setBorder(null);
		setBackground(Color.white);
	}};
	
	JButton apply = new JButton("지원하기") {{
		setBackground(getter.color);
		setForeground(Color.white);
		setFont(getter.font.deriveFont(13f));
		setPreferredSize(new Dimension(getPreferredSize().width + (110 - getPreferredSize().width), getPreferredSize().height));
	}};
	JButton heart = new JButton("♡") {{
		setBackground(Color.white);
		setFont(getter.font.deriveFont(16f));
		setForeground(getter.color);
	}};
	int j_no;
	favoriteEntity favorite;
	applicationEntity application;
	jobEntity job;
	storeEntity store;
	locationEntity location;
	public JobInfor(int j_no) {
		this.j_no = j_no;
		job = jobEntity.findById(j_no).get();
		store = storeEntity.findById(job.s_no).get();
		location = locationEntity.findById(store.l_no).get();
		
		if(getter.user != null) {
			favorite = favoriteEntity.findFirst(c -> c.u_no == getter.user.u_no && c.j_no == j_no).orElse(null);
			if(favorite != null)
				heart.setText("♥");
		}
		application = applicationEntity.findFirst(c -> c.u_no == getter.user.u_no && c.j_no == j_no).orElse(null);
		
		if(application != null)
			apply.setEnabled(false);
		borderPanel.setLayout(new BorderLayout(10, 10));
		borderPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
		borderPanel.setBackground(Color.white);
		setFrame("알바 정보", 525, 475, new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				super.windowClosed(e);
			}
		});
	}
	@Override
	protected void design() {
		borderPanel.add(sc);
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 15, 0, mainNorthPanel(), mainCenterPanel(), mainSouthPanel()).setBackColor(Color.white);
		mainPanel.add(panel);
		setSouthPanel();
	}

	
	@Override
	protected void action() {
		heart.addActionListener(e -> setHeart());
		apply.addActionListener(e -> {
			new applicationEntity(null, getter.user.u_no, j_no, LocalDate.now(), 0).save();
			getter.infor("지원이 완료되었습니다.");
			apply.setEnabled(false);
		});
	}
	
	private void setSouthPanel() {
		apply.setPreferredSize(new Dimension(apply.getPreferredSize().width, heart.getPreferredSize().height));
		JPanel panel = new BoxPanel(BoxPanel.R, 0, 5, 0, BoxPanel.HGAP(), heart, apply).setBackColor(Color.white);
		borderPanel.add(panel, BorderLayout.SOUTH);
	}
	
	private void setHeart() {
		if(favorite != null) {
			favorite.delete();
			favorite = null;
			heart.setText("♡");
		}
		else {
			favorite = new favoriteEntity(null, getter.user.u_no, j_no);
			favorite.save();
			heart.setText("♥");
		}
	}
	
	private JPanel mainNorthPanel() {
		JLabel label = new JLabel("상시모집");
		label.setForeground(getter.color);
		
		JLabel northLabel = new JLabel(job.j_title);
		northLabel.setFont(getter.font.deriveFont(22f).deriveFont(1));
		
		JLabel southLabel = new JLabel(store.s_name + " · " + location.l_name);
		
		JPanel panel = new BoxPanel(BoxPanel.R, 20, 0, 20, new BoxPanel(BoxPanel.C, 0, 20, 0, BoxPanel.fill(northLabel), BoxPanel.fill(southLabel)).setBackColor(Color.white), BoxPanel.HGAP(), label);
		panel.setBackground(Color.white);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(20, 0, 20, 0)));
		return panel;
	}
	
	private JPanel mainCenterPanel() {
		String[] strTitle = "급여,근무기간,근무시간,모집인원,가게 이름,지역,공고번호,상태".split(",");
		List<String> test = Arrays.asList(job.j_salary.toString(), job.j_regdate.toString(), job.j_start.toString(), job.j_people.toString(), store.s_name, location.l_name, job.j_no.toString(), job.stateText());
		List<JLabel> labels = new ArrayList<>();
		
		for(int i = 0; i < 8; i++) {
			int n = i;
			String s = test.get(n);
			
			JLabel l = new JLabel(s);
			if(n == 0) {
				l.setFont(getter.font.deriveFont(1));
				l.setText(new Html().forgeColor("orange").add("시급 ").endForgeColor().add(l.getText() + "원").getString());
			}
			if(n == 2) {
				l.setText(l.getText() + "~" + job.j_end);
			}
			if(n == 3) {
				l.setText(l.getText() + "명");
			}
			labels.add(l);
		}
		
		JLabel label = new JLabel("근무조건");
		label.setFont(getter.font.deriveFont(19f).deriveFont(1));
		
		JComponent[] wCom = labels.subList(0, labels.size() / 2).stream().map(c -> new BoxPanel(BoxPanel.R, 0, 0, 0,
			new JLabel(strTitle[labels.indexOf(c)]) {{
				setFont(getter.font);
				setPreferredSize(new Dimension(70, getFontMetrics(getFont()).getHeight()));
				setForeground(Color.LIGHT_GRAY);
			}}, 
			BoxPanel.fill(c)).setBackColor(Color.white))
		.toArray(JComponent[]::new);
		JComponent[] eCom = labels.subList(labels.size() / 2, labels.size()).stream().map(c -> new BoxPanel(BoxPanel.R, 0, 0, 0,
				new JLabel(strTitle[labels.indexOf(c)]) {{
					setFont(getter.font);
					setPreferredSize(new Dimension(70, getFontMetrics(getFont()).getHeight()));
					setForeground(Color.LIGHT_GRAY);
				}}, 
				BoxPanel.fill(c)).setBackColor(Color.white))
			.toArray(JComponent[]::new);
		JPanel wPanel = new BoxPanel(BoxPanel.C, 0, 10, 0, wCom).setBackColor(Color.white);
		JPanel ePanel = new BoxPanel(BoxPanel.C, 0, 10, 0, eCom).setBackColor(Color.white);
		
		JPanel mPanel = new BoxPanel(BoxPanel.R, 0, 0, 0, BoxPanel.fill(wPanel), BoxPanel.fill(ePanel)).setBackColor(Color.white);
		mPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		JPanel panel2 = new BoxPanel(BoxPanel.C, 0, 5, 0, BoxPanel.fill(label), mPanel).setBackColor(Color.white);
		return panel2;
	}
	
	private JPanel mainSouthPanel() {
		JLabel label = new JLabel("상세내용");
		label.setFont(getter.font.deriveFont(17f).deriveFont(1));
		
		JTextArea infor = new JTextArea(job.j_content);
		infor.setBorder(null);
		infor.setFocusable(false);
		infor.setCursor(Cursor.getDefaultCursor());
		infor.setLineWrap(true);
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, BoxPanel.fillWidth(label), BoxPanel.fill(infor)).setBackColor(Color.white);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		return panel;
	}
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JobInfor(1).setVisible(true));
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
