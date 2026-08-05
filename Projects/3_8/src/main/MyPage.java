package main;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import orms.ordersEntity;
import orms.productEntity;
import orms.rateplanEntity;
import orms.userEntity;

public class MyPage extends CFrame{

	String[] columns = {"이미지", "기종", "통신사", "총가격", "개통일"};
	int[] widths = {103, 100, 99, 100, 101};

	DefaultTableModel model = new DefaultTableModel(columns, 0) {
		@Override
		public boolean isCellEditable(int r, int c) { return false; }
		@Override
		public Class<?> getColumnClass(int c) { return c == 0 ? ImageIcon.class : Object.class; }
	};
	JTable table = new JTable(model);

	JTextField name = tf("이름", SIZE(260, 26), FG(Color.LIGHT_GRAY));
	JTextField id = tf("아이디", SIZE(260, 26));
	JTextField year = tf("년", SIZE(90, 26));
	JTextField month = tf("월", SIZE(70, 26));
	JTextField day = tf("일", SIZE(70, 26));

	JButton edit = bt("정보 수정", SIZE(116, 30), BG(sp.color), FG(Color.white));
	JButton write = bt("리뷰작성", SIZE(116, 30), BG(sp.color), FG(Color.white));

	userEntity user = sp.user;
	List<ordersEntity> list = ordersEntity.findBy(e -> e.uno.equals(sp.user.uno));

	boolean move = false;

	public MyPage() {
		list.sort((a, b) -> a.opening_date.compareTo(b.opening_date));

		name.setEditable(false);
		name.setText(user.name);
		id.setText(user.id);
		year.setText(String.valueOf(user.birth.getYear()));
		month.setText(String.format("%02d", user.birth.getMonthValue()));
		day.setText(String.format("%02d", user.birth.getDayOfMonth()));
		setFramed("마이페이지", 550, 400, () -> {
			if(!move) new Main();
		});
	}

	@Override
	protected void desing() {
		table.setRowHeight(100);
		table.setShowGrid(true);
		table.setGridColor(Color.LIGHT_GRAY);
		table.setFont(sp.font);
		table.getTableHeader().setReorderingAllowed(false);
		table.getTableHeader().setResizingAllowed(false);
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
			if(i != 0) table.getColumnModel().getColumn(i).setCellRenderer(center);
		}
		
		reloadTable();

		add(set(col(15,
				lb("My Page", FONT(sp.font.deriveFont(24f).deriveFont(1))),
				field("이름", name),
				field("아이디", id),
				field("생년월일", row(0, 10, 0, year, month, day, hg()).setBackColor(Color.white)),
				f(row(0, hg(18), f(set(new JScrollPane(table), BORDER(sp.line(sp.color)))), hg(18)).setBackColor(Color.white)),
				fw(row(0, hg(), edit, hg(), write, hg()).setBackColor(Color.white))
				).setBackColor(Color.white), BORDER(sp.em(15, 12, 12, 12))));
	}

	private BoxPanel field(String n, JComponent c) {
		return row(10, hg(35), set(L(n, 70), HOA(JLabel.RIGHT)), c, hg()).setBackColor(Color.white);
	}

	private void reloadTable() {
		model.setRowCount(0);
		for(ordersEntity o : list) {
			model.addRow(new Object[] {
					sp.getImage(String.valueOf(o.pno), 103, 100),
					productEntity.findById(o.pno).map(p -> p.pname).orElse(""),
					rateplanEntity.findById(o.rno).map(r -> r.service).orElse(""),
					sp.df.format(o.price) + "원",
					o.opening_date
			});
		}
	}
	
	@Override
	protected void action() {
		edit.addActionListener(e -> {
			if(id.getText().isBlank() || year.getText().isBlank() || month.getText().isBlank() || day.getText().isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
			LocalDate birth;
			try {
				birth = LocalDate.of(Integer.parseInt(year.getText().trim()),
						Integer.parseInt(month.getText().trim()),
						Integer.parseInt(day.getText().trim()));
			}catch(Exception e2) {
				throw new RuntimeException("생년월일을 확인해주세요.");
			}
			String i = id.getText().trim();
			if(i.equals(user.id) && birth.equals(user.birth)) {
				throw new RuntimeException("수정할 내용이 없습니다.");
			}
			user.id = i;
			user.birth = birth;
			user.save();
			sp.infor("수정되었습니다.");
		});

		write.addActionListener(e -> {
			int r = table.getSelectedRow();
			if(r < 0) {
				throw new RuntimeException("리뷰를 작성할 제품을 선택해주세요.");
			}
			move = true;
			sp.pno = list.get(r).pno;
			new ReviewInsert();
			dispose();
		});
	}

	public static void main(String[] args) {
		sp.user = userEntity.findById(1).get();
		Util.start(new MyPage());
	}
}
