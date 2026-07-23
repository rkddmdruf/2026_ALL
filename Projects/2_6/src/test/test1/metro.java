package test.test1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import main.Util;
import orms.*;
import uitls.*;
import static uitls.BoxPanel.*;
import static uitls.Properties.*;

public class metro extends CFrame{
	Image img = new ImageIcon("datafiles/metro.png").getImage();
	final double imgX = img.getWidth(null);
	final double imgY = img.getHeight(null);
	List<stationEntity> stations = stationEntity.findAll();
	List<Integer> bfs = new ArrayList<>();
	List<Integer> totalpx = Arrays.asList(0);
	Map<Integer, Ellipse2D.Double> ovals = new HashMap<>();
	JLabel bLabel = lb("역을 우클릭하여 출발역을 선택하세요.", HOA(JLabel.CENTER), BG(Color.black), FG(Color.white), FONT(getter.font.deriveFont(14f)), BORDER(getter.em(5, 5, 5, 5)));
	JLabel label;
	JPopupMenu menu = new JPopupMenu();
	JButton startB = bt("출발", HOA(JButton.LEFT));
	JButton endB = bt("도착", HOA(JButton.LEFT));
	int nowStation = -1;
	double step = 0;
	
	public metro() {
		startB.setBorderPainted(false);
		startB.setMargin(new Insets(0, 0, 0, 10));
		
		endB.setBorderPainted(false);
		endB.setMargin(new Insets(0, 0, 0, 10));
		
		menu.add(startB);
		menu.add(endB);
		//bfs = bfs(34, 1, totalpx);
		nowStation = 0;
		setFrame("경로 찾기", 600, 900, () -> {});
	}
	
	public List<Integer> bfs(int start, int end, List<Integer> totalPix){
		Map<Integer, Integer> parents = new HashMap<>();
		Map<Integer, Integer> dist = new HashMap<>(); 
		Map<Integer, List<int[]>> map = new LinkedHashMap<>();
		List<Integer> visit = new ArrayList<>();
		for(int i : new int[]{1, 2, 7}) {
			int line1 = i * 1000, line2 = (i + 1) * 1000;
			List<stationEntity> list = stationEntity.findAll().stream().filter(e -> {
				int n = Integer.parseInt(e.line.substring(1));
				return line1 < n && line2 > n;
			}).collect(Collectors.toList());
			for(int s = 0; s < list.size(); s++) {
				stationEntity nowStation = list.get(s);
				List<Integer> ns = new ArrayList<>();
				if(s - 1 >= 0) ns.add(s - 1);
				if(s + 1 < list.size()) ns.add(s + 1);
				for(Integer n : ns) {
					stationEntity station = list.get(n);
					map.computeIfAbsent(nowStation.sno, k -> new ArrayList<>())
					.add(new int[] {station.sno, (int) Math.sqrt(Math.pow(nowStation.x - station.x, 2) + Math.pow(nowStation.y - station.y, 2))});
				}
			}
		}
		for(String s : "석남,부평구청,인천시청".split(",")) {
			List<stationEntity> s1 = stationEntity.findBy(e -> e.name.equals(s));
			map.get(s1.get(0).sno).add(new int[] {s1.get(1).sno, (int) Math.sqrt(Math.pow(s1.get(0).x - s1.get(1).x, 2) + Math.pow(s1.get(0).y - s1.get(1).y, 2))});
			map.get(s1.get(1).sno).add(new int[] {s1.get(0).sno, (int) Math.sqrt(Math.pow(s1.get(0).x - s1.get(1).x, 2) + Math.pow(s1.get(0).y - s1.get(1).y, 2))});
		}
		PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
		dist.put(start, 0);
		pq.add(new int[] {start, 0});
		
		while(!pq.isEmpty()) {
			int[] cur = pq.poll();
			int p = cur[0], d = cur[1];
			if(d > dist.getOrDefault(p, Integer.MAX_VALUE)) continue;
			if(p == end) break;
			for (int[] edge : map.getOrDefault(p, new ArrayList<>())) {
				
	            int next = edge[0], weight = edge[1];
	            int newDist = d + weight;
	            if (newDist < dist.getOrDefault(next, Integer.MAX_VALUE)) {
	                dist.put(next, newDist);
	                parents.put(next, p);
	                pq.add(new int[]{next, newDist});
	            }
	        }
		}
		totalPix.set(0, dist.get(end));
		LinkedList<Integer> path = new LinkedList<>();
	    Integer cur = end;
	    while (cur != null) {
	        path.addFirst(cur);
	        cur = (cur == start) ? null : parents.get(cur);
	    }
	    return path;
	}

	@Override
	protected void desing() {
		bLabel.setOpaque(true);
		
		label = new JLabel() {
			int r = 12;
			protected void paintComponent(java.awt.Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.drawImage(new ImageIcon("datafiles/metro.png").getImage(), 0, 0, getWidth(), getHeight(), null);
				
				double scaleX  = getWidth() / imgX;
				double scaleY  = getHeight() / imgY;
				
				stations.forEach(e -> {
					double x = e.x * scaleX;
					double y = e.y * scaleY;
					Ellipse2D.Double oval = new Ellipse2D.Double(x - r, y - r, r*2, r*2);
					ovals.put(e.sno, oval);
				});
				g2.setStroke(new BasicStroke(4f));
				
				for(int i = 0; i < bfs.size() - 1; i++) {
					stationEntity s1 = stations.get(bfs.get(i) - 1);
					stationEntity s2 = stations.get(bfs.get(i + 1) - 1);
					Line2D.Double l = new Line2D.Double(s1.x * scaleX, s1.y * scaleY, s2.x * scaleX, s2.y * scaleY);
					g2.setColor(Color.red);
					g2.draw(l);
					g2.setColor(Color.orange);
					g2.fill(new Ellipse2D.Double(s1.x * scaleX - 2, s1.y * scaleY - 2, 4, 4));
					g2.fill(new Ellipse2D.Double(s2.x * scaleX - 2, s2.y * scaleY - 2, 4, 4));
				}
				if(start != -1) sed(g2,"출", start);
				if(end != -1) sed(g2,"도",end);
				if(!bfs.isEmpty() && nowStation < bfs.size() - 1) {
					 stationEntity s1 = stationEntity.findById(bfs.get(nowStation)).get();
					 stationEntity s2 = stationEntity.findById(bfs.get(nowStation + 1)).get();
					 
					 double x = (s1.x + (s2.x - s1.x) * step) * scaleX;
					 double y = (s1.y + (s2.y - s1.y) * step) * scaleY;
					 
					 double angle = Math.atan2(s2.y - s1.y, s2.x - s1.x);
					 
					 Graphics2D g2d = (Graphics2D) g2.create();
					 g2d.translate(x, y);
					 g2d.rotate(angle + Math.PI / 2);
					 
					 int w = 8, h = 40;
					 g2d.drawImage(Util.train, -w / 2, -h / 2, w, h, null);
					 g2d.dispose();
				}
			}
			private void sed(Graphics2D g2, String string, int i) {
				Ellipse2D o = ovals.get(i);
				g2.setFont(getter.font.deriveFont(17f));
		    	FontMetrics fm = getFontMetrics(getFont());
	        	int textWidth = fm.stringWidth(string);
	        	int textHeight = fm.getAscent();
	        	
	        	Ellipse2D e = ovals.get(i);
	        	
	        	double centerX = e.getX() + r;
	        	double centerY = e.getY() + r;

	        	// 중앙 좌표 계산
	        	int x = (int) (centerX - textWidth / 2);
	        	int y = (int) (centerY + textHeight / 2);
	        	
				g2.setColor(Color.white);
				g2.fill(o);
				g2.setColor(string.equals("출") ? getter.color : Color.red);
				g2.setStroke(new BasicStroke(2f));
				g2.draw(o);
				g2.drawString(string, x - 2, y - 2);
			};
		};
		add(col(0, f(label), fw(bLabel)));
	}

	int selectN = -1, start = -1, end = -1;
	@Override
	protected void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton() != MouseEvent.BUTTON3) {
					return;
				}
				ovals.keySet().forEach(c -> {
					if(ovals.get(c).contains(e.getPoint())) {
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
				bfs = Util.bfs(start, end, totalpx);
				List<Integer> bfsCopy = List.copyOf(bfs);
				for(String s : "석남,부평구청,인천시청".split(",")) {
					List<Integer> list = stationEntity.findBy(sta -> sta.name.equals(s)).stream().map(sta -> sta.sno).collect(Collectors.toList());
					if(bfs.contains(list.get(0)) && bfs.contains(list.get(1))) {
						bfs.remove(bfs.indexOf(list.get(0)));
					}
				}
				double dist = (totalpx.get(0) * 0.05);
				bLabel.setText("출발: " + stationEntity.findById(start).get().name + " → 도착: " + stationEntity.findById(start).get().name + 
						" ( " + (bfs.size() - 1) + "구간 ) 약 " + dist + " km | 약 " + (int) Math.ceil(dist / 40*60)+ " 분");
				bfs = bfsCopy;
				new Thread(() -> {
					try {
						while(true) {
						    step += 0.02;
						    if(step >= 1) {
						        step = 0;
						        nowStation++;
						        if(nowStation >= bfs.size() - 1) {
						            break;
						        }
						    }
						    label.repaint();
						    Thread.sleep(16);
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
		Util.start(new metro());
	}
}
