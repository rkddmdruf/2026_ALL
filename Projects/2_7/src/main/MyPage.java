package main;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;
import orms.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MyPage extends CFrame{
	List<ordersEntity> orders = ordersEntity.findBy(e -> e.uno == getter.user.uno);
	DefaultTableModel tModel = new DefaultTableModel("예약일,결제일,의사,시간,진료항목,금액,비고".split(","), 0) {
		public boolean isCellEditable(int row, int column) {return false;};
	};
	
	JTable table = new JTable(tModel);
	JScrollPane sc = new JScrollPane(table);
	
	public MyPage() {
		table.getColumnModel().getColumn(4).setCellRenderer(new TableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JLabel l = new JLabel() {
					@Override
					protected void paintComponent(Graphics g) {
						super.paintComponent(g);
						if(isSelected) {
							g.setColor(table.getSelectionBackground());
							g.fillRect(0, 0, getWidth(), getHeight());
						}
						g.drawImage(new ImageIcon("datafiles/icon/" + categoryEntity.findById((int) value).get().cname + ".png").getImage(), 15, 10, getWidth() - 30, getHeight() - 20, null);
					}
				};
				return l;
			}
		});
		
		DefaultTableCellRenderer center = new DefaultTableCellRenderer();
		center.setHorizontalAlignment(SwingConstants.CENTER);

		for (int i = 0; i < table.getColumnCount(); i++) {
			if(i != 4)
				table.getColumnModel().getColumn(i).setCellRenderer(center);
		}
		
		JTableHeader th = table.getTableHeader();
		th.setBackground(getter.color);
		th.setForeground(Color.white);
		th.setPreferredSize(new Dimension(0, 30));
		th.setReorderingAllowed(false);
		th.setResizingAllowed(false);
		
		table.setRowHeight(70);
		table.setShowGrid(false);
		table.setIntercellSpacing(new Dimension(0, 0));
		table.setShowVerticalLines(false);
		setFrame("마이페이지", 700, 500, () -> {});
	}

	@Override
	public void desing() {
		init();
		JPanel top = set(row(0, 
				f(lb("     마이페이지", HOA(JLabel.CENTER), VEA(JLabel.CENTER), FG(getter.color), FONT(getter.font.deriveFont(24f).deriveFont(1)))),
				fh(lb("<html>" + getter.user.name + "님<br>환영합니다.</html>", SIZE(100, 0), FONT(getter.font), VEA(JLabel.BOTTOM)))
				), SIZE(0, 70), BG(Color.white), BORDER(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, getter.color), getter.em(3, 3, 3, 3))));
		add(col(0, fw(top), f(set(col(0, f(sc)).setBackColor(Color.white), BORDER(getter.em(5, 10, 10, 10))))));
	}

	@Override
	public void action() {
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int r = table.getSelectedRow();
				int c = table.getSelectedColumn();
				if(c == table.getColumnCount() - 1 && table.getValueAt(r, c).toString().equals("진료 완료")) {
					
				}
			}
		});
	}

	private void init() {
		orders.sort((a, b) -> b.orderdate.compareTo(a.orderdate));
		orders.forEach(e -> {
			Vector<Object> v = new Vector<>();
			v.add(e.orderdate);
			v.add(e.paydate == null ? "-" : e.paydate);
			v.add(doctorEntity.findById(e.dno).get().dname);
			v.add(LocalTime.parse(e.ordertime, DateTimeFormatter.ofPattern("H:mm")).format(DateTimeFormatter.ofPattern("HH:mm")));
			v.add(e.cno);
			v.add(categoryEntity.findById(e.cno).get().price);
			v.add("-");
			if(LocalDate.now().isBefore(e.orderdate)) v.set(v.size() - 1, "변경");
			else {
				v.set(v.size() - 1, "-");
				if(!reportEntity.findBy(r -> r.ono.equals(e.ono)).isEmpty()) {
					if(v.get(1) instanceof String) {
						v.set(v.size() - 1, "진료 완료");
					}else {
						v.set(v.size() - 1, "결재 완료");
					}
				}
			}
			System.out.println(v);
			tModel.addRow(v);
		});
		revalidate();
		repaint();
	}
	public static void main(String[] args) {
		UIManager.put("Table.focusCellHighlightBorder", BorderFactory.createEmptyBorder());
		Util.start(new MyPage());
	}
}
