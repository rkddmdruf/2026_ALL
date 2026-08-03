package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import orms.categoryEntity;
import orms.detailEntity;
import orms.productEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class ProductAddFrame extends CFrame{

	JLabel imageBox = set(new JLabel("이미지 선택", JLabel.CENTER), FONT(sp.font.deriveFont(15f)));

	JComboBox<String> c1 = set(new JComboBox<>(), NAME("카테고리"), FONT(sp.font));
	JComboBox<String> c2 = set(new JComboBox<>(), NAME("상세분류"), FONT(sp.font));
	
	JTextField t1 = tf("상품명", FONT(sp.font));
	JTextField t2 = tf("제조사", FONT(sp.font));
	JTextField t3 = tf("가격", FONT(sp.font));
	JTextField t4 = tf("재고", FONT(sp.font));
	JTextArea ta1 = comp(JTextArea::new, BORDER(sp.line), NAME("설명"));

	CButton addB = set(new CButton("상품 추가"), BG(sp.blue), FG(Color.white), SIZE(100, 40), FONT(sp.font));
	CButton cancelB = set(new CButton("취소"), BG(Color.LIGHT_GRAY), FG(Color.white), SIZE(100, 40), FONT(sp.font));
	
	JTextField tt1 = tf("카테고리");
	JTextField tt2 = tf("상세분류");
	JPanel cp1, cp2;

	File file;
	productEntity product;
	public ProductAddFrame(int pno) {
		product = productEntity.findById(pno).orElse(null);
		if(product != null) {
			imageBox.setIcon(sp.getImage("product/" + product.pno, 150, 150));
			imageBox.setText("");
			t1.setText(product.pname);
			t2.setText(product.pcompany);
			t3.setText(product.pprice.toString());
			t4.setText(product.pcount.toString());
			ta1.setText(product.pcontent);
		}
		setFrame("상품 추가", 400, 625);
	}

	@Override
	protected void desing() {
		categoryEntity.findAll().forEach(c -> c1.addItem(c.cname));
		c1.addItem("직접입력");
		setSubCategory();
		imageBox.setOpaque(true);
		imageBox.setBorder(sp.line);
		imageBox.setPreferredSize(new Dimension(150, 150));
		ta1.setLineWrap(true);

		JScrollPane contentScroll = set(new JScrollPane(ta1), SIZE(0, 80), BORDER(null), NAME("설명"));

		JPanel form = col(15,
				fw(field(t1)),
				cp1 = fw(field(c1)),
				cp2 = fw(field(c2)),
				fw(field(t2)),
				fw(field(t3)),
				fw(field(t4)),
				fw(field(contentScroll))
				).setBackColor(Color.white);

		JPanel buttonPanel = row(15,hg(), fh(addB), fh(cancelB),hg());

		JPanel card = set(new BoxPanel(BoxPanel.C, 0, 20, 0,
				fw(lb("상품 추가", HOA(JLabel.CENTER), FONT(sp.font.deriveFont(1).deriveFont(26f)))),
				fw(row(75, 0, 75, f(imageBox))).setBackColor(Color.white),
				f(form)
				) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = Util.ANTI(g);
				g2.setColor(Color.white);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
				super.paintComponent(g);
			}
		}, BORDER(sp.em(20, 25, 20, 25)));
		card.setOpaque(false);
		
		
		add(set(col(5, f(card), fw(buttonPanel)), BORDER(sp.em(10, 10, 2, 10))));
	}

	private JPanel field(JComponent input) {
		if(input instanceof JTextField) input.setPreferredSize(new Dimension(0, 30));
		return row(10, fh(lb(input.getName(), SIZE(65, 25), FONT(sp.font), VEA(JLabel.TOP))), fw(input)).setBackColor(Color.white);
	}

	@Override
	protected void action() {
		imageBox.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileNameExtensionFilter("All Files", "png", "jpg", "gif"));
				if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					file = fc.getSelectedFile();
					imageBox.setIcon(new ImageIcon(new ImageIcon(file.getPath()).getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH)));
					imageBox.setText("");
				}
			}
		});
		t1.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) { if(t1.getText().length() >= 50) e.consume(); } });
		t2.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) { if(t2.getText().length() >= 50) e.consume(); } });
		ta1.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) { if(ta1.getText().length() >= 50) e.consume(); } });
		t3.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) { if(!Character.isDigit(e.getKeyChar())) e.consume(); } });
		t4.addKeyListener(new KeyAdapter() { @Override public void keyTyped(KeyEvent e) { if(!Character.isDigit(e.getKeyChar())) e.consume(); } });
		
		c1.addActionListener(ac -> {
			setSubCategory();
		});
		
		c2.addActionListener(ac -> {
			revalidate();
			repaint();
		});
		
		cancelB.addActionListener(e -> {
			dispose();
		});
		addB.addActionListener(e -> {
			if(file == null || !file.exists()) {
				throw new RuntimeException("이미지를 넣어주세요.");
			}
			if(t1.getText().isBlank() || t2.getText().isBlank() || t3.getText().isBlank() || t4.getText().isBlank() || ta1.getText().isBlank()
					|| (tt1.isDisplayable() && tt1.getText().isBlank()) || (tt2.isDisplayable() && tt2.getText().isBlank())) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			
			productEntity pro = new productEntity();
			pro.dno = 1;
			pro.pname = t1.getText();
			pro.pcontent = ta1.getText();
			pro.pcompany = t2.getText();
			pro.pprice = Integer.parseInt(t3.getText());
			pro.pcount = Integer.parseInt(t4.getText());
			pro.save();
			sp.infor(product == null ? "상품이 추가되었습니다." : "수정이 완료되었습니다.");
			String ext = file.getName().substring(file.getName().lastIndexOf('.'));  
			try {
				if(product != null) {
					File dest = new File("datafiles/product/" + product.pno + ".png");
				    Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}else {
					File dest = new File("datafiles/product", pro.pno + ext);
					Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	private void setSubCategory() {
		c2.removeAllItems();
		List<categoryEntity> cates = categoryEntity.findBy(c -> c.cname.equals(c1.getSelectedItem().toString()));
		if(!cates.isEmpty()) detailEntity.findBy(d -> d.cno.equals(cates.get(0).cno)).forEach(d -> c2.addItem(d.dname)); 
		c2.addItem("직접입력");
	}
	public static void main(String[] args) {
		Util.start(new ProductAddFrame(1));
	}
}
