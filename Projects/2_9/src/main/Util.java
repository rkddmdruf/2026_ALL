package main;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import orms.areaEntity;
import orms.categoryEntity;
import orms.chanceitemEntity;
import orms.linelistEntity;
import orms.orderEntity;
import orms.productEntity;
import orms.reviewEntity;
import orms.sub_areaEntity;
import orms.userEntity;

import static utils.Properties.*;
import utils.getter;

public class Util {
	private static boolean[][] visit = new boolean[800][800];
	public static void textIsBlank(JTextField...fields) {
		for(JTextField tf : fields) {
			if(tf.getText().isBlank()) {
				throw new RuntimeException("빈칸이 있습니다.");
			}
		}
	}
	
	public static String areaStr(int sno) {
		sub_areaEntity s = sub_areaEntity.findById(sno).get();
		return areaEntity.findById(s.ano).get().aname + s.sname;
	}
	
	
	public static void start(JFrame f) {
		
		SwingUtilities.invokeLater(() -> f.setVisible(true));
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
		getter.err(throwable.getMessage());
	}
	
	public static Border rrb(Color color) {
		return new LineBorder(color, 1, true) {
			    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			        if ((this.thickness > 0) && (g instanceof Graphics2D)) {
			            Graphics2D g2d = (Graphics2D) g;
			            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			            Color oldColor = g2d.getColor();
			            g2d.setColor(this.lineColor);

			            Shape outer;
			            Shape inner;

			            int offs = this.thickness;
			            int size = offs + offs;
			            if (this.roundedCorners) {
			                float arc = .2f * offs;
			                outer = new RoundRectangle2D.Float(x, y, width, height, 10, 10);
			                inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, 10, 10);
			            }
			            else {
			                outer = new Rectangle2D.Float(x, y, width, height);
			                inner = new Rectangle2D.Float(x + offs, y + offs, width - size, height - size);
			            }
			            Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
			            path.append(outer, false);
			            path.append(inner, false);
			            g2d.fill(path);
			            g2d.setColor(oldColor);
			        }
			    }
			};
	}
}
