package test.upDowns;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import main.Util;
import orms.*;

public class MyPage extends CFrame {
	String[] colNs = "예약일,결제일,의사,시간,진료항목,금액,비고".split(",");
	DefaultTableModel tModel = new DefaultTableModel(colNs, 0) {
		public boolean isCellEditable(int row, int column) { return false; };
	};
	JTable table = new JTable(tModel);
	JScrollPane sc = new JScrollPane(table);
	
	List<ordersEntity> ol = new ArrayList<ordersEntity>();
	boolean toMain;
	public MyPage(boolean toMain) {
		this.toMain = toMain;
		table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				return new JLabel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						if(isSelected) {
							g.setColor(table.getSelectionBackground());
							g.fillRect(0, 0, getWidth(), getHeight());
						}
						g.drawImage(new ImageIcon("datafiles/icon/" + value + ".png").getImage(), 15, 10, getWidth() - 30, getHeight() - 20, null);
					}
				};
			}
		});
		for(int i = 0; i < table.getColumnCount(); i++) {
			if( i != 4) {
				table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {{setHorizontalAlignment(SwingConstants.CENTER); }});
			}
		}
		
		JTableHeader th = table.getTableHeader();
		th.setBackground(sp.color);
		th.setForeground(Color.white);
		th.setPreferredSize(new Dimension(0, 30));
		th.setReorderingAllowed(false);
		th.setResizingAllowed(false);
		
		table.setRowHeight(70);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setShowVerticalLines(false);
		setFrames("마이페이지", 700, 550, () -> {
			new Main();
		});
	}

	protected void desing() {
		setting();
		JPanel top = set(row(10,
					f(lb("     마이페이지", HOA(JLabel.CENTER), VEA(JLabel.CENTER), FG(sp.color), FONT(sp.font.deriveFont(24f)))),
					fh(lb("<html>" + sp.user.name + "님<br>환영합니다.", SIZE(100, 0), VEA(JLabel.BOTTOM), FONT(sp.font)))
				).setBackColor(Color.white), SIZE(0,75), BORDER(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, sp.color), sp.em(3, 3, 3, 3))));
		add(set(col(0
					, fw(top)
					, f(set(col(0, f(sc)).setBackColor(Color.white), BORDER(sp.em(5, 10, 10, 10)))))
				)
			);
	}

	
	private void setting() {
		ordersEntity.findBy(e -> e.uno.equals(sp.user.uno)).stream().sorted((a, b) -> {
			int n = b.orderdate.compareTo(a.orderdate);
			if(n != 0) return n;
			return a.ordertime.compareTo(b.ordertime);
		}).forEach(e -> {
			Vector<Object> v = new Vector<>();
			v.add(e.orderdate);
			v.add(e.paydate == null ? "-" : e.paydate);
			v.add(doctorEntity.findById(e.dno).get().dname);
			v.add(e.ordertime);
			v.add(categoryEntity.findById(e.cno).get().cname);
			v.add(e.paydate != null ? sp.df.format(categoryEntity.findById(e.cno).get().price) + "원" : "");
			String s = "";
			if(LocalDate.now().isBefore(e.orderdate)) s = "변경";
			else if(LocalDate.now().equals(e.orderdate)) s = "-";
			if(!reportEntity.findBy(r -> r.ono.equals(e.ono)).isEmpty()) s = "진료완료";
			if(e.paydate != null) s = "결재완료";
			v.add(s);
			tModel.addRow(v);
			ol.add(e);
		});
	}

	protected void action() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int r = table.getSelectedRow();
				int c = table.getSelectedColumn();
				if(c != table.getColumnCount() - 1) return;
				String s = table.getValueAt(r, c).toString();
				System.out.println(s);
				if(s.equals("진료완료")) {
					new Pay(ol.get(r).ono, toMain);
					dispose();
				}
				if(s.equals("변경")) {
					
				}
			}
		});
	}
	
	public static void main(String[] args) {
		Util.start(new MyPage(true));
	}
}