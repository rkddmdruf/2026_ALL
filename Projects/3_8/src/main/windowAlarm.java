package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;

import utils.sp;
import utils.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import orms.ProjectEntity;
import orms.categoryEntity;
import orms.productEntity;
import orms.rateplanEntity;

public class windowAlarm {

	public windowAlarm(String str) {
		showToast(str);
	}
	
	public static void main(String[] args) {
		new windowAlarm("test");
		System.out.println(System.getProperty("java.version"));
	}
	
	void showToast(String pass) {
	    int w = 360, h = 100;
	    JWindow toast = new JWindow();
	    toast.setSize(w, h);
	    toast.setShape(new RoundRectangle2D.Double(0, 0, w, h, 8, 8));

	    JPanel panel = new JPanel(null) {
	        protected void paintComponent(Graphics g) {
	        	super.paintComponent(g);
	            Graphics2D g2 = (Graphics2D) g;
	            g2.setRenderingHint(
	                    RenderingHints.KEY_ANTIALIASING,
	                    RenderingHints.VALUE_ANTIALIAS_ON);
	            g2.setColor(new Color(55, 165, 70));
	            g2.fillRect(0, 0, 5, getHeight());

	            double r2 = 40;
	            g2.fill(new Ellipse2D.Double(15, (getHeight() - r2) / 2,  r2, r2));
	            
	            g2.setColor(Color.WHITE);
	            String s = "P";
	            g2.setFont(sp.font.deriveFont(20f).deriveFont(1));
	            FontMetrics fm = g2.getFontMetrics();
	            g2.drawString("P", (int) (15 + (r2  - fm.stringWidth(s)) / 2), (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
	        }
	    };
	    panel.setBackground(Color.black);
	    panel.setPreferredSize(new Dimension(60, h));

	    JLabel close = lb("×");
	    close.setForeground(Color.LIGHT_GRAY);
	    close.setFont(sp.font.deriveFont(22f));
	    close.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { toast.dispose(); } });
	    
	    toast.setContentPane(row(10, 
		    		panel, 
		    		f(set(colF(10, 
		    				lb("비밀번호 안내",FONT(sp.font.deriveFont(11f)), FG(Color.LIGHT_GRAY)), 
		    				row(0, 
		    						lb("pass:", FG(Color.white)), 
		    						lb(pass, FG(Color.white), FONT(sp.font.deriveFont(16f).deriveFont(1)))
		    					).setBackColor(Color.black)
		    			), BG(Color.black), BORDER(sp.em(20, 0, 30, 0)))),
		    		set(col(0, close, vg()), BG(Color.black), BORDER(sp.em(5, 0, 0, 20)))
	    		).setBackColor(Color.black)
	    	);

	    // 작업표시줄을 제외한 화면 오른쪽 아래에 배치
	    Rectangle screen = GraphicsEnvironment
	            .getLocalGraphicsEnvironment()
	            .getMaximumWindowBounds();

	    toast.setLocation(
	            screen.x + screen.width - w - 10,
	            screen.y + screen.height - h - 10
	    );

	    toast.setVisible(true);

	    // 10초 후 자동 종료
	    Timer timer = new Timer(10000, e -> toast.dispose());
	    timer.setRepeats(false);
	    timer.start();
	}
}
