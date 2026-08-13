package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import orms.ProjectEntity;
import orms.categoryEntity;
import orms.productEntity;

public class Admin extends CFrame{

	String[] columns = {"번호", "제품이미지", "제품명", "제조회사", "용량", "통신사", "약정"};
	int[] widths = {135, 127, 118, 125, 123, 122, 125};

	DefaultTableModel model = new DefaultTableModel(columns, 0) {
		@Override
		public boolean isCellEditable(int r, int c) { return false; }
		@Override
		public Class<?> getColumnClass(int c) { return c == 1 ? ImageIcon.class : Object.class; }
	};
	JTable table = new JTable(model);

	JLabel chart = lb("차트→", FONT(sp.font.deriveFont(11f)));

	JTextField pname = tf("제품명", SIZE(110, 22), BORDER(sp.line(sp.color)));
	JComboBox<String> maker = comp(JComboBox::new, NAME("제조회사"), SIZE(110, 24));
	JComboBox<String> cap = comp(JComboBox::new, NAME("용량"), SIZE(180, 24));
	JComboBox<String> carrier = comp(JComboBox::new, NAME("통신사"), SIZE(110, 24));
	JComboBox<String> month = comp(JComboBox::new, NAME("약정"), SIZE(160, 24));

	JButton image = comp(JButton::new, TEXT("이미지등록"), FG(Color.white), BG(new Color(150, 150, 150)), SIZE(105, 26));
	JButton save = comp(JButton::new, TEXT("추가"), FG(Color.white), BG(sp.color), SIZE(65, 26));

	JTextField c128 = tf("128GB 추가금", SIZE(85, 22), BORDER(sp.line(sp.color)));
	JTextField c256 = tf("256GB 추가금", SIZE(85, 22), BORDER(sp.line(sp.color)));
	JTextField c512 = tf("512GB 추가금", SIZE(85, 22), BORDER(sp.line(sp.color)));
	JTextField c1024 = tf("1024GB 추가금", SIZE(85, 22), BORDER(sp.line(sp.color)));
	JTextField skt = tf("SKT 가격", SIZE(95, 22), BORDER(sp.line(sp.color)));
	JTextField kt = tf("KT 가격", SIZE(95, 22), BORDER(sp.line(sp.color)));
	JTextField lgu = tf("LGU+ 가격", SIZE(95, 22), BORDER(sp.line(sp.color)));

	Map<String, JTextField> capPrice = Map.of("128", c128, "256", c256, "512", c512, "1024", c1024);
	Map<String, JTextField> carPrice = Map.of("SKT", skt, "KT", kt, "LG U+", lgu);

	productEntity selected;
	File imageFile;

	public Admin() {
		subsets("128,256,512,1024", "GB").forEach(cap::addItem);
		subsets("SKT,KT,LG U+", "").forEach(carrier::addItem);
		subsets("12,24,36,48,60,72", "").forEach(month::addItem);
		reloadMaker();
		setFrame("관리자", 890, 420);
	}

	@Override
	protected void desing() {
		table.setRowHeight(60);
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setFont(sp.font);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
		table.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 26));
		table.getTableHeader().setDefaultRenderer((t, v, s, f, r, c) -> {
			JLabel l = lb(String.valueOf(v), HOA(SwingConstants.CENTER), FG(Color.white), BG(sp.color),
					FONT(sp.font.deriveFont(1)), BORDER(sp.line(Color.white)));
			l.setOpaque(true);
			return l;
		});

		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(SwingConstants.CENTER);
		for(int i = 0; i < columns.length; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
			if(i != 1) table.getColumnModel().getColumn(i).setCellRenderer(center);
		}

		reloadTable();

		JPanel top = set(new JPanel(new java.awt.BorderLayout()), BG(Color.white));
		top.add(set(chart, HOA(SwingConstants.RIGHT)), java.awt.BorderLayout.EAST);

		add(set(col(8,
				lb("관리자 메인", FG(sp.color), FONT(sp.font.deriveFont(20f).deriveFont(1))),
				fw(top),
				f(set(new JScrollPane(table), BORDER(sp.line(sp.color)))),
				fw(form())
				).setBackColor(Color.white), BORDER(sp.em(10, 12, 10, 12))));
	}

	private JPanel form() {
		return col(6,
				row(10,
						field(pname), field(maker), field(cap), field(carrier), field(month),
						hg(), image, save
						).setBackColor(Color.white),
				lb("― 가격 설정 ―", FG(sp.color), FONT(sp.font.deriveFont(1))),
				row(12,
						field(c128), field(c256), field(c512), field(c1024),
						hg(20), field(skt), field(kt), field(lgu), hg()
						).setBackColor(Color.white)
				).setBackColor(Color.white);
	}

	private JPanel field(JTextField t) { return field(t.getName(), t); }
	private JPanel field(JComboBox<String> c) { return field(c.getName(), c); }

	private JPanel field(String name, javax.swing.JComponent c) {
		return col(3, fw(lb(name, FONT(sp.font.deriveFont(1).deriveFont(11f)))), c).setBackColor(Color.white);
	}

	// 12,24 / 12,36 ... 처럼 조합 가능한 모든 경우를 순서대로 만든다
	private List<String> subsets(String csv, String suffix) {
		String[] arr = csv.split(",");
		List<String> out = new ArrayList<>();
		for(int size = 1; size <= arr.length; size++) comb(arr, suffix, size, 0, new ArrayList<>(), out);
		return out;
	}

	private void comb(String[] arr, String suffix, int size, int start, List<String> cur, List<String> out) {
		if(cur.size() == size) { out.add(String.join(",", cur)); return; }
		for(int i = start; i < arr.length; i++) {
			cur.add(arr[i] + suffix);
			comb(arr, suffix, size, i + 1, cur, out);
			cur.remove(cur.size() - 1);
		}
	}

	private void reloadMaker() {
		maker.removeAllItems();
		categoryEntity.findAll().stream().sorted((a, b) -> a.cno - b.cno).forEach(c -> maker.addItem(c.cname));
		maker.addItem("+");
	}

	private void reloadTable() {
		model.setRowCount(0);
		List<productEntity> list = productEntity.findAll();
		list.sort((a, b) -> a.pno - b.pno);
		for(productEntity p : list) {
			ProjectEntity pe = ProjectEntity.findById(p.pno).orElse(null);
			model.addRow(new Object[] {
					p.pno,
					sp.getImage(String.valueOf(p.pno), 35, 50),
					p.pname,
					categoryEntity.findById(p.cno).map(c -> c.cname).orElse(""),
					pe == null ? "" : pe.capacities.stream().map(c -> c.value + "GB").collect(Collectors.joining(",")),
					pe == null ? "" : pe.items.stream().map(c -> c.type).collect(Collectors.joining(",")),
					pe == null ? "" : pe.installments.stream().map(c -> String.valueOf(c.month)).collect(Collectors.joining(","))
			});
		}
	}

	@Override
	protected void action() {
		chart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("new Chart();");
			}
		});

		maker.addActionListener(e -> {
			if(!"+".equals(maker.getSelectedItem())) return;
			String name = JOptionPane.showInputDialog(this, "추가할 제조회사 명을 입력하세요:", "Input", JOptionPane.QUESTION_MESSAGE);
			if(name == null || name.isBlank()) { maker.setSelectedIndex(0); return; }
			categoryEntity c = new categoryEntity();
			c.cname = name.trim();
			c.save();
			reloadMaker();
			maker.setSelectedItem(c.cname);
		});

		table.getSelectionModel().addListSelectionListener(e -> {
			if(e.getValueIsAdjusting() || table.getSelectedRow() < 0) return;
			select(productEntity.findById((Integer) model.getValueAt(table.getSelectedRow(), 0)).orElse(null));
		});

		image.addActionListener(e -> {
			JFileChooser fc = new JFileChooser();
			fc.setFileFilter(new FileNameExtensionFilter("jfif 파일", "jfif"));
			if(fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
			imageFile = fc.getSelectedFile();
		});

		save.addActionListener(e -> {
			if(pname.getText().isBlank()) {
				throw new RuntimeException("제품명을 입력해주세요.");
			}
			if("+".equals(maker.getSelectedItem())) {
				throw new RuntimeException("제조회사를 선택해주세요.");
			}
			if(selected == null && imageFile == null) {
				throw new RuntimeException("제품 이미지를 등록해주세요.");
			}

			productEntity p = selected == null ? new productEntity() : selected;
			p.pname = pname.getText().trim();
			p.cno = categoryEntity.findFirst(c -> c.cname.equals(maker.getSelectedItem())).map(c -> c.cno).orElse(null);

			ProjectEntity pe = new ProjectEntity();
			pe.capacities = new ArrayList<>();
			for(String v : String.valueOf(cap.getSelectedItem()).split(",")) {
				ProjectEntity.Capacity c = new ProjectEntity.Capacity();
				c.value = v.replace("GB", "");
				c.price = money(capPrice.get(c.value));
				pe.capacities.add(c);
			}
			pe.items = new ArrayList<>();
			for(String v : String.valueOf(carrier.getSelectedItem()).split(",")) {
				ProjectEntity.CarrierItem c = new ProjectEntity.CarrierItem();
				c.type = v;
				c.price = money(carPrice.get(c.type));
				pe.items.add(c);
			}
			pe.installments = new ArrayList<>();
			for(String v : String.valueOf(month.getSelectedItem()).split(",")) {
				ProjectEntity.Installment c = new ProjectEntity.Installment();
				c.month = Integer.parseInt(v);
				pe.installments.add(c);
			}

			p.save();
			pe.pno = p.pno;
			pe.save();
			copyImage(p.pno);

			sp.infor(selected == null ? "등록되었습니다." : "수정되었습니다.");
			reloadTable();
			select(null);
		});

		select(null);
	}

	private void select(productEntity p) {
		selected = p;
		imageFile = null;
		image.setText(p == null ? "이미지등록" : "이미지수정");
		save.setText(p == null ? "추가" : "수정");

		if(p == null) {
			table.clearSelection();
			pname.setText("");
			maker.setSelectedIndex(0);
			cap.setSelectedIndex(0);
			carrier.setSelectedIndex(0);
			month.setSelectedIndex(0);
			capPrice.values().forEach(t -> t.setText("0"));
			carPrice.values().forEach(t -> t.setText("0"));
			return;
		}

		pname.setText(p.pname);
		categoryEntity.findById(p.cno).ifPresent(c -> maker.setSelectedItem(c.cname));

		ProjectEntity pe = ProjectEntity.findById(p.pno).orElse(null);
		if(pe == null) return;
		cap.setSelectedItem(pe.capacities.stream().map(c -> c.value + "GB").collect(Collectors.joining(",")));
		carrier.setSelectedItem(pe.items.stream().map(c -> c.type).collect(Collectors.joining(",")));
		month.setSelectedItem(pe.installments.stream().map(c -> String.valueOf(c.month)).collect(Collectors.joining(",")));
		pe.capacities.forEach(c -> capPrice.get(c.value).setText(String.valueOf(c.price)));
		pe.items.forEach(c -> carPrice.get(c.type).setText(String.valueOf(c.price)));
	}

	private int money(JTextField t) {
		String s = t.getText().replace(",", "").trim();
		if(s.isBlank()) {
			throw new RuntimeException(t.getName() + "을(를) 입력해주세요.");
		}
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new RuntimeException(t.getName() + "은(는) 숫자만 입력해주세요.");
		}
	}

	private void copyImage(int pno) {
		if(imageFile == null) return;
		try {
			Files.copy(imageFile.toPath(), Path.of("datafiles/기종/" + pno + ".jfif"), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Util.start(new Admin());
	}
}
