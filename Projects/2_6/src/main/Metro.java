package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
	List<Integer> bfs = new ArrayList<>();
	Image img = new ImageIcon("datafiles/metro.png").getImage();
	Map<Integer, Ellipse2D> ovalMap = new LinkedHashMap<>();
	JPopupMenu menu = new JPopupMenu();

	JButton startB = bt("출발", HOA(JButton.LEFT));
	JButton endB = bt("도착", HOA(JButton.LEFT));

	final double imgX = img.getWidth(null);
	final double imgY = img.getHeight(null);
	
	int start = -1, end = -1, selectN = -1;
	double step = 0;
	int stationNumber = 0;
	List<Integer> totalPix = Arrays.asList(0);
	
	public Metro() {
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
		            ovalMap.put(e.sno, oval);
		        });
		        g2.setStroke(new BasicStroke(6f));
		        
		        for(int i = 0; i < bfs.size() - 1; i++) {
		        	stationEntity s = stationEntity.findById(bfs.get(i)).get();
		        	stationEntity e = stationEntity.findById(bfs.get(i + 1)).get();
		        	Line2D.Double line = new Line2D.Double(s.x * scaleX, s.y * scaleY, e.x * scaleX, e.y * scaleY);
		        	Ellipse2D.Double ellipse = new Ellipse2D.Double(s.x * scaleX - 3, s.y * scaleY - 3, 6, 6);
		        	g2.setColor(Color.red);
		        	g2.draw(line);
		        	g2.setColor(Color.orange);
		        	g2.fill(ellipse);
		        }
		        
		        if(start != -1) drawStartEnd(g2, "출", start);
		        if(end != -1) drawStartEnd(g2, "도", end);
		        
		        
		        
		        if(!bfs.isEmpty() && stationNumber < bfs.size() - 1) {
		            stationEntity s1 = stationEntity.findById(bfs.get(stationNumber)).get();
		            stationEntity s2 = stationEntity.findById(bfs.get(stationNumber + 1)).get();

		            double x = (s1.x + (s2.x - s1.x) * step) * scaleX;
		            double y = (s1.y + (s2.y - s1.y) * step) * scaleY;

		            // 🔥 방향 계산
		            double angle = Math.atan2(s2.y - s1.y, s2.x - s1.x);

		            Graphics2D g2d = (Graphics2D) g2.create();

		            g2d.translate(x, y);
		            g2d.rotate(angle + Math.PI / 2);

		            int w = 8;
		            int h = 40;
		            g2d.drawImage(Util.train, -w/2, -h/2, w, h, null);
		            g2d.dispose();
		        }
		    }
		    
		    private void drawStartEnd(Graphics2D g2, String str, int n) {
		    	g2.setStroke(new BasicStroke(2f));
		        g2.setFont(getter.font.deriveFont(17f));
		    	FontMetrics fm = getFontMetrics(getFont());
	        	int textWidth = fm.stringWidth(str);
	        	int textHeight = fm.getAscent();
	        	
	        	Ellipse2D e = ovalMap.get(n);
	        	
	        	double centerX = e.getX() + r;
	        	double centerY = e.getY() + r;

	        	// 중앙 좌표 계산
	        	int x = (int) (centerX - textWidth / 2);
	        	int y = (int) (centerY + textHeight / 2);
	        	
	        	g2.setColor(Color.white);
	        	g2.fill(e);
	        	g2.setColor(str.equals("출") ? getter.color : Color.red);
	        	g2.draw(e);
	        	g2.drawString(str, x - 2, y + 2);
		    }
		};
		add(col(0, f(label), fw(bLabel)));
	}

	private void init() {
		start = -1;
		end = -1;
		bfs = new ArrayList<>();
		stationNumber = 0;
		step = 0;
		totalPix.set(0, null);
		repaint();
	}
	@Override
	protected void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON3) {
					init();
					return;
				}
				ovalMap.keySet().forEach(c -> {
					if(ovalMap.get(c).contains(e.getPoint())) {
						selectN = c;
						menu.setVisible(true);
						menu.show(label, e.getX(), e.getY());
					};
				});
			}
		});
		ActionListener ac = e -> {
			if(e.getSource() == startB) {
				start = selectN;
				bLabel.setText("출발: " + stationEntity.findById(start).get().name + " | 역을 우클릭하여 도착역을 선택하세요");
			}
			else {
				end = selectN;
				bLabel.setText("도착: " + stationEntity.findById(end).get().name + " | 역을 우클릭하여 출발역을 선택하세요");
			}
			menu.setVisible(false);
			selectN = -1;
			if(start != -1 && end != -1) {
				bfs = Util.bfs(start, end, totalPix);
				List<Integer> bfsCopy = List.copyOf(bfs);
				for(String s : "석남,부평구청,인천시청".split(",")) {
					List<Integer> list = stationEntity.findBy(sta -> sta.name.equals(s)).stream().map(sta -> sta.sno).collect(Collectors.toList());
					if(bfs.contains(list.get(0)) && bfs.contains(list.get(1))) {
						bfs.remove(bfs.indexOf(list.get(0)));
					}
				}
				double dist = (totalPix.get(0) * 0.05);
				bLabel.setText("출발: " + stationEntity.findById(start).get().name + " → 도착: " + stationEntity.findById(start).get().name + 
						" ( " + (bfs.size() - 1) + "구간 ) 약 " + dist + " km | 약 " + (int) Math.ceil(dist / 40*60)+ " 분");
				bfs = bfsCopy;
				new Thread(() -> {
					try {
						while(true) {
						    step += 0.02; // 속도
						    if(step >= 1) {
						        step = 0;
						        stationNumber++;
						        if(stationNumber >= bfs.size() - 1) {
						            break;
						        }
						    }
						    label.repaint();
						    Thread.sleep(16); // 60fps 느낌
						}
						System.out.println("끝");
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}).start();
			}
			repaint();
		};
		startB.addActionListener(ac);
		endB.addActionListener(ac);
	}

	public static void main(String[] args) {
		Util.start(new Metro());
	}
}
