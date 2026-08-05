package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.TransferHandler;

import orms.*;

public class ReviewInsert extends CFrame{
	JLabel imgLabel = lb("", BORDER(sp.line(Color.black)), SIZE(125, 125));
	JTextField tf = comp(JTextField::new, SIZE(265, 28));

	StarView star = set(new StarView(0), SIZE(185, 36)); // 별 5개가 잘리지 않도록 폭 = 높이 * 5
	JLabel scopeLabel = lb("별점: 0", FONT(sp.font.deriveFont(1)));
	JSlider slider = set(new JSlider(0, 50, 0), BG(Color.white), SIZE(0, 20));

	JTextArea ta = comp(JTextArea::new, FONT(sp.font));
	JScrollPane sc = set(new JScrollPane(ta), BORDER(sp.line(Color.black)));

	JButton regist = bt("등록", SIZE(120, 26), BG(sp.color), FG(Color.white));

	DecimalFormat sdf = new DecimalFormat("0.#");
	File reviewImage;
	boolean move = false;

	int pno = sp.pno == null ? 1 : sp.pno;

	public ReviewInsert() {
		ta.setLineWrap(true);
		setFramed("리뷰작성", 450, 395, () -> {
			if(!move) new MyPage();
		});
	}

	@Override
	protected void desing() {
		add(set(col(10,
				fw(row(15,
						imgLabel,
						col(10,
								fw(tf),
								fw(row(10, star, scopeLabel, hg()).setBackColor(Color.white)),
								fw(slider)
							).setBackColor(Color.white)
					).setBackColor(Color.white)),
				f(row(10,
						fh(set(col(0, lb("내용"), vg()).setBackColor(Color.white), SIZE(45, 0))),
						f(sc)
					).setBackColor(Color.white)),
				fw(row(0, hg(), regist).setBackColor(Color.white))
				).setBackColor(Color.white), BORDER(sp.em(15, 15, 10, 15))));
	}

	@Override
	protected void action() {
		// jfif 파일만 드래그해서 등록할 수 있다
		imgLabel.setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(TransferSupport s) {
				return s.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
			}
			@SuppressWarnings("unchecked")
			@Override
			public boolean importData(TransferSupport s) {
				try {
					File f = ((List<File>) s.getTransferable().getTransferData(DataFlavor.javaFileListFlavor)).get(0);
					if(!f.getName().toLowerCase().endsWith(".jfif")) return false;
					reviewImage = f;
					imgLabel.setIcon(new ImageIcon(new ImageIcon(f.getPath()).getImage()
							.getScaledInstance(imgLabel.getWidth(), imgLabel.getHeight(), 4)));
					return true;
				}catch(Exception e) {
					return false;
				}
			}
		});

		slider.addChangeListener(e -> {
			star.setValue(slider.getValue());
			scopeLabel.setText("별점: " + sdf.format(slider.getValue() / 10.0));
		});

		// 제목에서 Enter 를 누르면 내용으로 이동한다
		tf.addActionListener(e -> ta.requestFocusInWindow());

		regist.addActionListener(e -> {
			if(tf.getText().isBlank()) {
				throw new RuntimeException("제목을 입력해주세요.");
			}
			if(ta.getText().isBlank()) {
				throw new RuntimeException("내용을 입력해주세요.");
			}
			starEntity s = new starEntity();
			s.title = tf.getText().trim();
			s.detail = ta.getText().trim();
			s.scope = slider.getValue() / 10.0;
			s.pno = pno;
			s.save();
			saveImage();

			move = true;
			if(JOptionPane.showConfirmDialog(this, "리뷰를 더 작성하시겠습니까?", "확인",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) new MyPage();
			else new Main();
			dispose();
		});
	}

	private void saveImage() {
		if(reviewImage == null) return;
		try {
			Files.copy(reviewImage.toPath(), Path.of("datafiles/review/" + pno + ".jfif"),
					StandardCopyOption.REPLACE_EXISTING);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		sp.user = userEntity.findById(1).get();
		sp.pno = 1;
		Util.start(new ReviewInsert());
	}
}
