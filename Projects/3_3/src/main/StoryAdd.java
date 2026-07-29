package main;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import orms.storyEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class StoryAdd extends CFrame{
	File photoFile;

	JLabel preview = new JLabel() {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if(photoFile != null) {
				g2.drawImage(new ImageIcon(photoFile.getPath()).getImage(), 0, 0, getWidth(), getHeight(), null);
				return;
			}
			g2.setColor(new Color(38,38,38));
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			
			g2.setColor(Color.white);
			g2.setFont(sp.font.deriveFont(40f));
			FontMetrics fm = g2.getFontMetrics();
			g2.drawString("+", (getWidth() - fm.stringWidth("+")) / 2, getHeight() / 2 - 15);

			g2.setFont(sp.font.deriveFont(1).deriveFont(13f));
			fm = g2.getFontMetrics();
			g2.drawString("사진 추가", (getWidth() - fm.stringWidth("사진 추가")) / 2, getHeight() / 2 + 25);

			g2.setColor(Color.LIGHT_GRAY);
			g2.setFont(sp.font.deriveFont(11f));
			fm = g2.getFontMetrics();
			g2.drawString("클릭해서 컴퓨터에서 선택", (getWidth() - fm.stringWidth("클릭해서 컴퓨터에서 선택")) / 2, getHeight() / 2 + 45);
		}
	};

	JTextArea content = comp(JTextArea::new, SIZE(0, 40), BG(new Color(38, 38, 38)), FG(Color.white));
	JButton share = bt("스토리 공유", FONT(sp.font.deriveFont(1).deriveFont(14f)), BG(sp.color), FG(Color.white), SIZE(0, 45));

	public StoryAdd() {
		setFrames("스토리 추가", 350, 550, () -> new Main());
	}

	@Override
	protected void desing() {
		set(preview, BG(new Color(28, 28, 28)));
		preview.setOpaque(false);
		preview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		content.setBorder(sp.em(5,5,5,5));
		content.setLineWrap(true);
		content.setCaretColor(Color.white);
		content.setPreferredSize(new Dimension(0, 50));
		JLabel title = lb("새 스토리", FONT(sp.font.deriveFont(1).deriveFont(16f)), FG(Color.white), HOA(JLabel.CENTER));

		add(set(col(15, fw(title), f(preview), fw(content), fw(share)).setBackColor(Color.black), BORDER(sp.em(20, 20, 20, 20)), BG(Color.black)));
	}

	@Override
	protected void action() {
		preview.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("이미지 파일", "jpg", "jpeg", "png", "gif"));
				if(fc.showOpenDialog(StoryAdd.this) == JFileChooser.APPROVE_OPTION) {
					photoFile = fc.getSelectedFile();
					preview.repaint();
				}
			}
		});

		share.addActionListener(e -> {
			if(photoFile == null) throw new RuntimeException("사진을 추가해주세요.");

			storyEntity s = new storyEntity();
			s.u_no = sp.user.u_no;
			s.s_content = content.getText();
			s.s_date = LocalDateTime.now();
			s.s_view = 0;
			s.s_file = "";
			s.save();

			try {
				Files.copy(photoFile.toPath(), Paths.get("datafiles/story/" + s.s_no + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			s.s_file = String.valueOf(s.s_no);
			s.save();

			sp.info("스토리가 공유되었습니다.");

			photoFile = null;
			content.setText("");
			preview.repaint();
		});
	}

	public static void main(String[] args) {
		Util.start(new StoryAdd());
	}
}
