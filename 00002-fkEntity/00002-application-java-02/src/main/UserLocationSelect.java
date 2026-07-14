package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import orms.locationEntity;
import utils.CFrame;
import utils.Image;
import utils.getter;

public class UserLocationSelect extends CFrame{

	JLabel map = new JLabel() {
		protected void paintComponent(java.awt.Graphics g) {
			super.paintComponent(g);
			int size = 12;
			g.drawImage(Image.REGION.getImage(locationName, 680, 420).getImage(), 0, 0, 680, 420, null);
			if(getter.user.l_no == locationEntity.findBy(c -> c.l_name.equals(locationName)).get(0).l_no) {
				g.setColor(Color.red);
				g.fillOval(getter.user.x - (size / 2), getter.user.y - (size / 2), size, size);
			}
		};
	};
	
	String locationName;
	public UserLocationSelect(String locationName) {
		this.locationName = locationName;
		setFrame("위치 변경", 680, 420, new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				
			}
		});
	}
	
	@Override
	protected void design() {
		borderPanel.add(map);
	}

	@Override
	protected void action() {
		map.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(getter.inputMs("이 위치로 변경하시겠습니까?")) {
					getter.infor("위치가 변경되었습니다.");
					getter.user.x = e.getX();
					getter.user.y = e.getY();
					getter.user.l_no = locationEntity.findBy(c -> c.l_name.equals(locationName)).get(0).l_no;
					getter.user.save();
					map.revalidate();
					map.repaint(); 
				}
			}
		});
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new UserLocationSelect("서울").setVisible(true));
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
	
	public static void handle(Throwable t) {
		t.printStackTrace();
		getter.err(t.getMessage());
	}
}
