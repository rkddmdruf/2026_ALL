package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import orms.storeEntity;
import orms.userEntity;
import utils.BoxPanel;
import utils.BoxUI;
import utils.CFrame;
import utils.getter;

public class Login extends CFrame{

	JButton userLogin = new JButton("개인회원") {{
		setFont(getter.font.deriveFont(0).deriveFont(12f));
		setBackground(Color.white);
	}};
	JButton storeLogin = new JButton("기업회원") {{
		setFont(getter.font.deriveFont(0).deriveFont(12f));
		setBackground(Color.white);
	}};
	
	JButton login = new JButton("로그인") {{
		setBackground(getter.orangeColor);
		setForeground(Color.white);
	}};
	
	JToggleButton tButton1 = new JToggleButton("버튼1"); 
	JToggleButton tButton2 = new JToggleButton("버튼2"); 
	
	JTextField id = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(id.getText().isBlank()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("아이디", 2, (getHeight() - getFontMetrics(getFont()).getHeight()) / 2 + getFontMetrics(getFont()).getAscent());
			}
		};
	};
	JTextField pw = new JTextField() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			if(pw.getText().isBlank()) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawString("비밀번호", 2, (getHeight() - getFontMetrics(getFont()).getHeight()) / 2 + getFontMetrics(getFont()).getAscent());
			}
		};
	};

	public Login() {

		borderPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		borderPanel.setBackground(Color.white);
		setFrame("로그인", 350, 175, new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
//				new Main().setVisible(true);
			}
		});
	}
	
	@Override
	protected void design() {

		ButtonGroup group = new ButtonGroup();
		group.add(tButton1);
		group.add(tButton2);
		
		tButton1.setSelected(true);
		
		BoxPanel northPanel = new BoxPanel(BoxPanel.R, 0, 0, 0, BoxPanel.fill(tButton1), BoxPanel.fill(tButton2));
		BoxPanel tfPanel = new BoxPanel(BoxPanel.C, 0, 10, 0, BoxPanel.fill(id), BoxPanel.fill(pw));
		borderPanel.add( BoxPanel.fill(
				new BoxPanel(BoxPanel.C, 0, 20, 40, northPanel, 
						BoxPanel.fill(new BoxPanel(BoxPanel.R, 0, 10, 0, tfPanel, BoxPanel.fillHeight(login))))
				) );
	}

	@Override
	protected void action() {
		login.addActionListener(e -> login());
	}
	
	public void login() {
		Util.validateBlank("빈칸이 있습니다.", id, pw);
		getter.account = tButton1.isSelected() ? 
				userEntity.findFirst(c -> c.u_id.equals(id.getText()) && c.u_pw.equals(pw.getText())).orElseThrow(() -> new RuntimeException("일치하는 회원이 없습니다.")):
				storeEntity.findFirst(c -> c.s_id.equals(id.getText()) && c.s_pw.equals(pw.getText())).orElseThrow(() -> new RuntimeException("일치하는 매장이 없습니다."));
		
		getter.infor(getter.account.getName());
		
		dispose();
	}
	
    public static void main(String[] args) {
		UIManager.put("ToggleButton.background", Color.gray);
		UIManager.put("ToggleButton.select", Color.white);
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
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
