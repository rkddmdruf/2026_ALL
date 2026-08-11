package main;

import javax.swing.*;

import orms.categoryEntity;
import orms.doctorEntity;
import orms.ordersEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pay extends CFrame{
	
	String[] str1 = "날짜,의사,시간,진료,금액".split(",");
	ordersEntity o;
	
	JTextField t1 = comp(JTextField::new);
	JPasswordField t2 = comp(JPasswordField::new);
	JPasswordField t3 = comp(JPasswordField::new);
	JTextField t4 = comp(JTextField::new);
	List<JTextField> tfs = Arrays.asList(t1, t2, t3, t4);
	JButton payB = set(new JButton("결재") {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
			super.paintComponent(g);
		};
	}, FG(Color.gray), BG(Util.setA(Color.LIGHT_GRAY, 100)), BORDER(null));
	JButton close = set(new JButton("취소") {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
			super.paintComponent(g);
		};
	}, FG(Color.white), BG(Util.setA(sp.color, 100)), BORDER(null));
	
	public Pay(int ono) {
		close.setContentAreaFilled(false);
		close.setOpaque(false);
		payB.setContentAreaFilled(false);
		payB.setFocusPainted(false);
		payB.setOpaque(false);
		tfs.forEach(e -> e.setHorizontalAlignment(JTextField.CENTER));
		tfs.forEach(e -> e.setPreferredSize(new Dimension(100, 30)));
		t2.setEchoChar('●');
		t3.setEchoChar('●');
		o = ordersEntity.findById(ono).get();
		setFrame("결제하기", 400, 350, () -> {});
	}

	@Override
	public void desing() {
		JLabel tl = lb("결제", FG(Color.white	), BG(sp.color.darker()), FONT(sp.font.deriveFont(20f)), BORDER(sp.em(10, 10, 10, 10)));
		tl.setOpaque(true);
		
		JPanel panel = set(new JPanel(new BorderLayout()), BORDER(sp.em(10, 15, 10, 10)));
		
		JPanel p1 = new BoxPanel(C, 0, 10, 0, IntStream.range(0, 5).mapToObj(e -> setLabel(e)).toArray(JPanel[]::new)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.white);
				g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g.setColor(Color.black);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			}
		};
		JPanel p2 = new BoxPanel(R, 0, 10, 0, fw(t1), fw(t2), fw(t3), fw(t4)) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.white);
				g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g.setColor(Color.black);
				g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			}
		};
		JPanel p3 = row(15, 15, 15, f(payB), f(close));
		JPanel c = colF(10, p1, p2, p3);
		
		p1.setBorder(sp.em(10, 10, 10, 10));
		p2.setBorder(sp.em(10, 10, 10, 10));
		panel.add(c);
		add(col(0, fw(tl), f(panel)));
	}

	private JPanel setLabel(int n) {
		Object obj = "";
		switch (n) {
		case 0 : { obj = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); break; }
		case 1 : { obj = doctorEntity.findById(o.dno).get().dname; break; }
		case 2 : { obj = LocalTime.parse(o.ordertime, DateTimeFormatter.ofPattern("H:mm")).format(DateTimeFormatter.ofPattern("HH:mm")); break; }
		case 3 : { obj = categoryEntity.findById(o.cno).get().cname; break; }
		case 4 : { obj = new DecimalFormat("###,###").format(categoryEntity.findById(o.cno).get().price) + "원"; break; }
		}
		JLabel l = lb(obj.toString(), HOA(JLabel.RIGHT), FONT(sp.font.deriveFont(1)));
		if(n == 4) {
			set(l, FONT(sp.font.deriveFont(16f).deriveFont(1)), FG(sp.color));
		}
		JPanel p = rowF(0, lb(str1[n], HOA(JLabel.LEFT), FONT(sp.font)), l).setBackColor(Color.white);
		return p;
	}
	
	@Override
	public void action() {
		for(int i = 0; i < 4; i++) {
			JTextField t = tfs.get(i);
			t.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e) {
					if (!Character.isDigit(e.getKeyChar()) || t.getText().length() >= 4)
			            e.consume();
				}
				@Override
				public void keyReleased(KeyEvent e) {
					if(t.getText().length() == 4 && tfs.indexOf(t) + 1 < 4)
						tfs.get(tfs.indexOf(t) + 1).requestFocus();
				}
			});
		}
		tfs.get(tfs.size() - 1).addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				super.keyReleased(e);
				if(tfs.get(tfs.size() - 1).getText().length() < 4) set(payB, FG(Color.gray), BG(Util.setA(Color.LIGHT_GRAY, 100)));
				else set(payB, FG(Color.white), BG(Util.setA(sp.color, 100)));
			}
		});
		
		payB.addActionListener(e -> {
			if(!sp.user.card.equals(tfs.stream().map(t -> t.getText()).collect(Collectors.joining("-"))))
				throw new RuntimeException("카드번호를 확인해주세요.");
			o.paydate = LocalDate.now();
			sp.inf("결재가 완료되었습니다.");
			o.save();
			new Main().setVisible(true);;
			dispose();
		});
	}

	
	public static void main(String[] args) {
		Util.start(new Pay(2));
	}
}
