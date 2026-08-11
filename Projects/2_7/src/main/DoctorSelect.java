package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import orms.*;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class DoctorSelect extends CFrame{
	
	categoryEntity category;
	List<doctorEntity> ds;
	List<JPanel> panels = new ArrayList<>();
	Border cardem = sp.em(10, 20, 10, 20);
	String[] dayNames = "일,월,화,수,목,금,토".split(",");
	public DoctorSelect(int cno) {
		category = categoryEntity.findById(cno).get();
		ds = doctorEntity.findBy(e -> e.cno == cno && sp.user.lno == e.lno);
		setFrame("의사선택", 575, 300, () -> {});
	}
	
	@Override
	public void desing() {
		JLabel label = lb("전문의를 선택해주세요", FG(Color.white), HOA(JLabel.CENTER), BG(sp.color), FONT(sp.font.deriveFont(20f).deriveFont(1)), BORDER(sp.em(10, 10, 10, 10)));
		label.setOpaque(true);
		JPanel panel = set(new JPanel(new GridLayout(0, 2, 15, 10)), BG(Color.white), BORDER(sp.em(10, 30, 10, 5)));
		setPanel(panel);
		JScrollPane sc = set(new JScrollPane(panel), BG(Color.white));
		add(col(0, fw(label), f(sc)).setBackColor(Color.white));
	}
	
	private void setPanel(JPanel panel) {
		for(int i = 0; i < ds.size(); i++) {
			doctorEntity d = ds.get(i);
			JLabel name = lb(d.dname, FONT(sp.font.deriveFont(14f).deriveFont(1)), HOA(JLabel.CENTER));
			JLabel img = new JLabel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.drawImage(new ImageIcon("datafiles/doctor/" + d.dno + ".png").getImage(), 0, 0, getWidth(), getHeight(), null);
				}
			};
			img.setLayout(new BorderLayout());
			img.add(new JPanel(new GridLayout(3, 1, 10, 10)){
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(new Color(0, 0, 255, 100));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			{
				setOpaque(false);
				setBorder(sp.em(10, 20, 10, 20));
				add(lb("진료횟수" + ordersEntity.findBy(e -> e.dno == d.dno).size() + "번", FG(Color.white), HOA(JLabel.CENTER), FONT(sp.font.deriveFont(1))));
				add(lb("휴일 : " + dayNames[d.day_off -1 ] + "요일", FG(Color.white), HOA(JLabel.CENTER), FONT(sp.font.deriveFont(1))));
				add(lb("더블 클릭으로 선택하세요", FG(Color.white), HOA(JLabel.CENTER), FONT(sp.font.deriveFont(11f))));
				setVisible(false);
			}});
			img.setPreferredSize(new Dimension(0, 150));
			JPanel p = set(new JPanel(new BorderLayout()), BORDER(BorderFactory.createCompoundBorder(sp.line(Color.LIGHT_GRAY), cardem)), BG(Color.white));
			p.add(img);
			p.add(name, BorderLayout.SOUTH);
			panel.add(p);
			panels.add(p);
		}
	}

	@Override
	public void action() {
		for(int i = 0; i < panels.size(); i++){
			JPanel p = panels.get(i);
			p.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					((JLabel) p.getComponent(0)).getComponent(0).setVisible(true);
					((JLabel) p.getComponent(1)).setForeground(sp.color);
					p.setBorder(BorderFactory.createCompoundBorder(sp.line(sp.color), cardem));
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					((JLabel) p.getComponent(0)).getComponent(0).setVisible(false);
					((JLabel) p.getComponent(1)).setForeground(Color.black);
					p.setBorder(BorderFactory.createCompoundBorder(sp.line(Color.LIGHT_GRAY), cardem));
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount() == 2) {
						new JLabel();
						dispose();
					}
				}
			});
		}
	}

	public static void main(String[] args) {
		Util.start(new DoctorSelect(1));
	}
}
