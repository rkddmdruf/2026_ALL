package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import orms.categoryEntity;
import orms.jobEntity;
import orms.locationEntity;
import orms.storeEntity;
import utils.BoxPanel;
import utils.CFrame;
import utils.getter;

public class JobSelect extends CFrame{

	int s_no;
	public JobSelect(int s_no) {
		this.s_no = s_no;
		borderPanel.setLayout(new BorderLayout(20, 20));
		borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		setFrame("공고 선택", 525, 450, new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new JobLocation().setVisible(true);
			}
		});
	}
	
	@Override
	protected void design() {
		borderPanel.add(new BoxPanel(BoxPanel.C, 0, 20, 0, BoxPanel.fillWidth(northPanel()), BoxPanel.fill(setMainPanel())));
	}
	
	@Override
	protected void action() {
	}
	
	private JPanel northPanel() {
		JLabel label1 = new JLabel("공고를 선택하세요");
		label1.setFont(getter.font.deriveFont(22f).deriveFont(1));
		
		JLabel label2 = new JLabel("선택한 조건에 맞는 공고 목록입니다.");
		label2.setForeground(Color.LIGHT_GRAY);
		label2.setFont(getter.font);
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, BoxPanel.fillWidth(label1), BoxPanel.fillWidth(label2));
		return panel;
	}
	
	private JScrollPane setMainPanel() {
		List<jobEntity> list = jobEntity.findBy(e -> e.s_no == s_no);//e -> e.s_no == s_no
		
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, list.stream().map(c -> {
			JPanel p = BoxPanel.fillWidth(jobPanel(c));
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					new JobInfor(c.j_no).setVisible(true);
					dispose();
				}
			});
			return p;
		}).toArray(JComponent[]::new));
		
		JScrollPane sc = new JScrollPane(panel);
		sc.setBorder(null);
		return sc;
	}
	
	private JPanel jobPanel(jobEntity j) {
		JLabel wLabel = new JLabel(categoryEntity.findById(storeEntity.findById(s_no).get().c_no).get().c_name, JLabel.CENTER);
		wLabel.setForeground(getter.color);
		wLabel.setOpaque(true);
		wLabel.setBackground(new Color(getter.color.getRed(), getter.color.getGreen(), getter.color.getBlue(), 15));
		wLabel.setFont(getter.font.deriveFont(1).deriveFont(13f));
		wLabel.setPreferredSize(new Dimension(80, 50));
		
		JLabel eLabel = new JLabel("시급 " + j.j_salary + "원");
		eLabel.setForeground(getter.color);
		eLabel.setFont(getter.font.deriveFont(13f).deriveFont(1));
		
		JLabel jtitleLabel = new JLabel(j.j_title);
		jtitleLabel.setFont(getter.font.deriveFont(1).deriveFont(15f));
		
		JLabel subLabel = new JLabel(locationEntity.findById(storeEntity.findById(s_no).get().l_no).get().l_name + " · " + j.j_start + " ~ " + j.j_end);
		subLabel.setForeground(Color.LIGHT_GRAY);
		
		JPanel cPanel = new BoxPanel(BoxPanel.C, 0, 5, 0, BoxPanel.fill(jtitleLabel), BoxPanel.fill(subLabel)).setBackColor(Color.white);
		cPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		JPanel p = new BoxPanel(BoxPanel.R, 0, 10, 0, wLabel,cPanel, BoxPanel.HGAP(), eLabel);
		p.setBackground(Color.white);
		p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		return p;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JobSelect(1).setVisible(true));
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
