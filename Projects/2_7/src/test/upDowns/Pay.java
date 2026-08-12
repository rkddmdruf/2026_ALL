package test.upDowns;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

import javax.swing.*;

import main.Util;
import orms.*;

public class Pay extends CFrame {
	JTextField t1 = comp(JTextField::new, FONT(sp.font.deriveFont(1).deriveFont(13f)), HOA(JTextField.CENTER));
	JTextField t2 = comp(JPasswordField::new, FONT(sp.font.deriveFont(1).deriveFont(13f)), HOA(JTextField.CENTER));
	JTextField t3 = comp(JPasswordField::new, FONT(sp.font.deriveFont(1).deriveFont(13f)), HOA(JTextField.CENTER));
	JTextField t4 = comp(JTextField::new, FONT(sp.font.deriveFont(1).deriveFont(13f)), HOA(JTextField.CENTER));
	
	boolean check = false;
	JLabel b1 = set(new JLabel("결제") {
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			g2.setColor(Util.setA(check ? sp.color : Color.LIGHT_GRAY, 100));
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			super.paintComponent(g);
		};
	}, FG(Color.lightGray), HOA(JLabel.CENTER));
	JLabel b2 = set(new JLabel("취소") {
		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Util.setA(sp.color, 100));
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			super.paintComponent(g);
		};
	}, FG(Color.white), HOA(JLabel.CENTER));
	ordersEntity order;
	boolean toMain;
	public Pay(int ono, boolean toMain) {
		this.toMain = toMain;
		((JPasswordField) t2).setEchoChar('●');
		((JPasswordField) t3).setEchoChar('●');
		order = ordersEntity.findById(ono).get();
		setFrames("결제하기", 350, 350, () -> new MyPage(toMain));
	}

	protected void desing() {
		JLabel l = lb("결제", BG(sp.color.darker()), FG(Color.white), HOA(JLabel.CENTER), VEA(JLabel.CENTER), BORDER(sp.em(10, 10, 10, 10)), FONT(sp.font.deriveFont(20f).deriveFont(1)));
		l.setOpaque(true);
		
		
		add(
				col(0, 
					fw(l), 
					f(set(col(0, 10, 5, 
							f(inforPanel()),
							fw(tfPanel()), 
							fw(buttonPanel())
							), BORDER(sp.em(10, 10, 10, 10)))
					)
				)
			);
	}

	
	private JComponent inforPanel() {
		JPanel p = set(new BoxPanel(C, 0, 10, 0,
					fw(row(10, lb("날짜",FONT(sp.font)), hg(), lb(LocalDate.now().toString()))).setBackColor(Color.white),
					fw(row(10, lb("의사",FONT(sp.font)), hg(), lb(doctorEntity.findById(order.dno).get().dname))).setBackColor(Color.white),
					fw(row(10, lb("시간",FONT(sp.font)), hg(), lb(order.ordertime))).setBackColor(Color.white),
					fw(row(10, lb("진료",FONT(sp.font)), hg(), lb(categoryEntity.findById(order.cno).get().cname))).setBackColor(Color.white),
					fw(row(10, lb("금액",FONT(sp.font)), hg(), lb(sp.df.format(categoryEntity.findById(order.cno).get().price) + "원", FG(sp.color), FONT(sp.font.deriveFont(13f).deriveFont(1))))).setBackColor(Color.white)
				) 
		{	
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.white);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g2.setColor(Color.black);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				super.paintComponent(g);
			}
		}, BORDER(sp.em(15,15,15,15)));
		p.setOpaque(false);
		return p;
	}

	private JComponent tfPanel() {
		JPanel p = new JPanel(new GridLayout(1, 4, 10, 10)) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				g2.setColor(Color.white);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g2.setColor(Color.black);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				super.paintComponent(g);
			}
		};
		p.setBorder(sp.em(15,15,15,15));
		p.setOpaque(false);
		
		p.add(t1);
		p.add(t2);
		p.add(t3);
		p.add(t4);
		return p;
	}

	private JComponent buttonPanel() {
		set(b1,SIZE(200, 40));
		set(b2,SIZE(200, 40));
		return row(15, 15, 15, f(b1), f(b2));
	}

	protected void action() {
		KeyAdapter ke = new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				JTextField t = ((JTextField) e.getSource());
				if(!Character.isDigit(e.getKeyChar()) || (e.getKeyCode() != KeyEvent.VK_BACK_SPACE && t.getText().length() >= 4))
					e.consume();
			}
			@Override
			public void keyReleased(KeyEvent e) {
				JTextField t = ((JTextField) e.getSource());
				if(t.getText().length() >= 4 && t != t4) {
					t.transferFocus();
				}
				check = t1.getText().length() == 4 && t2.getText().length() == 4 && t3.getText().length() == 4 && t4.getText().length() == 4;
				repaint();
			}
		};
		t1.addKeyListener(ke);
		t2.addKeyListener(ke);
		t3.addKeyListener(ke);
		t4.addKeyListener(ke);
		
		b1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!sp.user.card.equals(t1.getText() + "-" + t2.getText() + "-" +  t3.getText() + "-" +  t4.getText())) sp.tException("카드번호를 확인해주세요");
				
				sp.inf("결제가 완료되었습니다.");
				order.paydate = LocalDate.now();
				order.save();
				new Main();
				dispose();
			}
		});
		b2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new MyPage(toMain);
				dispose();
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new Pay(1, false));
	}
}