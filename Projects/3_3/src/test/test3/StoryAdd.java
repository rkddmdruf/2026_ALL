package test.test3;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.time.LocalDateTime;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.Util;
import orms.*;

public class StoryAdd extends CFrame {
	JTextArea ta = set(new JTextArea(), BORDER(sp.line), BG(Color.black.brighter()),SIZE(0, 50));
	JButton b1 = bt("스토리 공유", FG(Color.white), BG(sp.color), BORDER(sp.em(5, 5, 5, 5)));
	JLabel l1 = new JLabel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = sp.anti(g);
			if(file == null)
				g2.drawImage(new ImageIcon(getClass().getResource("/test.png")).getImage(), 0, 0, getWidth(), getHeight(), null);
			else g2.drawImage(new ImageIcon(file.getPath()).getImage(), 0, 0, getWidth(), getHeight(), null);
		}
	};
	File file;
	public StoryAdd() {
		setFramed("스토리 추가", 300, 450, () -> new A_Main());
	}

	protected void desing() {
		add(set(col(10,
				lb("새 스토리", FONT(sp.font.deriveFont(15f).deriveFont(1)), FG(Color.white)),
				f(l1),
				fw(ta),
				fw(b1)
				), BG(Color.black), BORDER(sp.em(10, 20, 10, 10))));
	}

	protected void action() {
		l1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fs = new JFileChooser();
				fs.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg"));
				if(fs.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fs.getSelectedFile();
					l1.repaint();
				}
			}
		});
		b1.addActionListener(e -> {
			if(ta.getText().isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			sp.info("스토리가 공유되었습니다.");
			storyEntity s = new storyEntity();
			s.s_date = LocalDateTime.now();
			s.s_content = ta.getText();
			s.u_no = sp.user.u_no;
			s.s_file = "1";
			s.s_view = 0;
			s.save();
		});
	}
	
	public static void main(String[] args) {
		Util.start(new StoryAdd());
	}
}