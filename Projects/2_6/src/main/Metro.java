package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import orms.stationEntity;

import static uitls.BoxPanel.*;
import static uitls.Properties.*;
import uitls.*;

public class Metro extends CFrame{
	JLabel label;
	JLabel bLabel = new JLabel("역을 우클릭하여 출발역을 선택하세요", JLabel.CENTER) {
		public int arc = 15;

	    @Override
	    protected void paintComponent(Graphics g) {
	        Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);

	       	g2.setColor(Color.black); // 기본색
	        g2.fillRoundRect(0, -20, getWidth(), getHeight() + 20, arc, arc);
	        super.paintComponent(g); // 👉 글씨 자동
	    }

	    @Override
	    protected void paintBorder(Graphics g) {
	    	g.setColor(Color.black);
	        g.drawRoundRect(0, -20, getWidth()-1, getHeight() + 20 - 2, arc, arc);
	    }
	};
	
	List<stationEntity> stations = stationEntity.findAll();
	Image img = new ImageIcon("datafiles/metro.png").getImage();
	Map<Integer, Ellipse2D> map = new LinkedHashMap<>();
	Map<Integer, List<Integer>> node = new LinkedHashMap<>();
	JPopupMenu menu = new JPopupMenu();

	JButton startB = bt("출발", HOA(JButton.LEFT));
	JButton endB = bt("도착", HOA(JButton.LEFT));

	final double imgX = img.getWidth(null);
	final double imgY = img.getHeight(null);
	double maxX = stations.stream().sorted((a, b) -> Integer.compare(b.x, a.x)).findFirst().get().x;
	double maxY = stations.stream().sorted((a, b) -> Integer.compare(b.y, a.y)).findFirst().get().y;
	double minX = stations.stream().sorted((a, b) -> Integer.compare(a.x, b.x)).findFirst().get().x;
	double minY = stations.stream().sorted((a, b) -> Integer.compare(a.y, b.y)).findFirst().get().y;
	
	int start = -1, end = -1, selectN = -1;
	public Metro() {
		System.out.println(bfs(34, 38));
		bLabel.setBorder(getter.em(2, 0, 2, 0));
		bLabel.setFont(getter.font.deriveFont(13f));
		bLabel.setOpaque(false);
		bLabel.setForeground(Color.white);
		setFrame("경로 검색", 600, 930, () -> {});
	}

	@Override
	protected void desing() {
		startB.setBorderPainted(false);
		startB.setMargin(new Insets(0, 0, 0, 10));
		
		endB.setBorderPainted(false);
		endB.setMargin(new Insets(0, 0, 0, 10));
		
		menu.add(startB);
		menu.add(endB);
		
		label = new JLabel() {
		    int r = 12;
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        Graphics2D g2 = (Graphics2D) g;
		        g2.drawImage(new ImageIcon("datafiles/metro.png").getImage(), 0, 0, getWidth(), getHeight(), null);
		        
		        g2.setColor(Color.RED);
		        
		        double scaleX = getWidth() / imgX;
		        double scaleY = getHeight() / imgY;
		        stations.forEach(e -> {
		        	double x = e.x * scaleX;
		        	double y = e.y * scaleY;
		            Ellipse2D.Double oval = new Ellipse2D.Double(x - r, y - r, r * 2, r * 2);
		            map.put(e.sno, oval);
		        });
		    }
		};
		add(col(0, f(label), fw(bLabel)));
	}

	@Override
	protected void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON3) {
					start = -1;
					end = -1;
					repaint();
				}
				map.keySet().forEach(c -> {
					if(map.get(c).contains(e.getPoint())) {
						System.out.println(c);
						selectN = c;
						menu.setVisible(true);
						menu.show(label, e.getX(), e.getY());
					};
				});
			}
		});
		startB.addActionListener(e -> {
			start = selectN;
			selectN = -1;
			menu.setVisible(false);
			if(end != -1) {
				System.out.println(bfs(start, end));
			}
			repaint();
		});
		endB.addActionListener(e -> {
			end = selectN;
			menu.setVisible(false);
			selectN = -1;
			if(start != -1) {
				System.out.println(bfs(start, end));
			}
			repaint();
		});
	}
	
	public List<Integer> bfs(int start, int end){
		Queue<Integer> q = new LinkedList<>();
		Map<Integer, Integer> map = new LinkedHashMap<Integer, Integer>();
		List<Integer> visit = new ArrayList<>();
		
		List<Integer> list = new ArrayList<>();
		Integer cur = end;
		while(cur != null) {
			list.add(cur);
			cur = map.get(cur);
		}
		return list;
	}
	public static void main(String[] args) {
		Util.start(new Metro());
	}
}
