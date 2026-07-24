package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import static utils.BoxPanel.*;
import static utils.Properties.*;
import utils.*;

public class Login extends CFrame{
	Map<String, Shape> map;
	List<Color> colors = Arrays.asList( new Color(0xB3AC97), new Color(0x98D1A7), new Color(0xE5D1AF), new Color(0xD6BADD), new Color(0xA0D6D6),
			new Color(0xE1C7CC), new Color(0xCEB1B0), new Color(0x97B5B4), new Color(0xCEDBD8), new Color(0xDBA1A8), new Color(0xB3A4BD), new Color(0xC6CEDD),
			new Color(0xD2D4BB), new Color(0x9EB7B9), new Color(0xAECBE5), new Color(0xABC5DC), new Color(0xFFFDD0)
		);
	List<String> strs = Arrays.asList("충청남도", "제주도", "경상남도", "경상북도", "전라북도", "전라남도", "충청북도",
			"강원도", "경기도", "울산광역시", "부산광역시", "대구광역시", "대전광역시", "인천광역시", "서울특별시", "광주광역시", "세종시");
	JLabel label1;
	JPanel panel2;
	
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	
	String selectLocation = "";
	int cardN = 1;
	public Login() {
		setFrame("test", 797 + 81 + 30, 964 - 2 + 30, () -> {});
	}
	
	static Map<String, Shape> loadMap(String file) throws Exception {
	    Map<String, Shape> map = new LinkedHashMap<>();
	    Pattern token = Pattern.compile("[MLZ]|-?\\d+(?:\\.\\d+)?");
	
	    for (String line : Files.readAllLines(Path.of(file))) {
	
	        if (!line.contains("<path")) continue;
	
	        String id = line.split("id=\"")[1].split("\"")[0];
	        String d  = line.split("d=\"")[1].split("\"")[0];
	
	        Matcher m = token.matcher(d);
	        Path2D.Double p = new Path2D.Double();
	
	        while (m.find()) {
	            String cmd = m.group();
	
	            if (cmd.equals("Z")) {
	                p.closePath();
	                continue;
	            }
	            
	            m.find();
	            double x = Double.parseDouble(m.group());
	
	            m.find();
	            double y = Double.parseDouble(m.group());
	
	            if (cmd.equals("M")) {
	                p.moveTo(x, y);
	            } else {
	                p.lineTo(x, y);
	            }
	        }
	
	        map.put(id, p);
	    }
	
	    return map;
	}

	private Point2D.Double getRegionCenter(Shape regionShape) {
	    Rectangle2D bounds = regionShape.getBounds2D();
	
	    double centerX = bounds.getCenterX();
	    double centerY = bounds.getCenterY();
	
	    return new Point2D.Double(centerX, centerY);
	}
	
	@Override
	public void desing() {
		try { map = loadMap("datafiles/backimg/map.svg");
		} catch (Exception e) { e.printStackTrace(); }
		label1 = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				int n = 0;
				for(Shape s : map.values()) {
					Point2D.Double pd = getRegionCenter(s);
					g2.setColor(colors.get(n));
					g2.fill(s);
					g2.setColor(Color.black);
					g2.draw(s);
					n++;
				}
				n = 0;
				g2.setFont(getter.font.deriveFont(13f).deriveFont(1));
				FontMetrics fm = g2.getFontMetrics();
				for(Shape s : map.values()) {
					Point2D.Double pd = getRegionCenter(s);
					g2.drawString(strs.get(n), (float) pd.x - fm.stringWidth(strs.get(n)) / 2, (float) pd.y);
					n++;
				}
			}
		};
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(getter.em(30, 0, 0, 30));
		panel.setBackground(new Color(245, 245, 240));
		panel.add(label1);
		
		setPanel2();
		cardP.add(panel, "P1");
		cardP.add(panel2, "P2");
		card.show(cardP, "P1");
		add(cardP);
	}

	private void setPanel2() {
		JLabel label = lb("←이전", SIZE(100, 20));
		label.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { card.show(cardP, "P1"); }});
		
		JPanel paintPanel = set(new JPanel(null) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				int index = strs.indexOf(selectLocation);
				Shape s = new ArrayList<>(map.values()).get(index);
				Rectangle2D b = s.getBounds();
				double scale = Math.min(getWidth() / b.getWidth(), getHeight() / b.getHeight()) * 0.8;
				double tx = (getWidth() - b.getWidth() * scale) / 2 - b.getX() * scale;
				double ty = (getHeight() - b.getHeight() * scale) / 2 - b.getY() * scale;
				
				g2.translate(tx, ty);
				g2.scale(scale, scale);
				
				g2.setColor(colors.get(index));
				g2.fill(s);
				g2.setColor(Color.black);
				g2.draw(s);
				
			}
		}, BG(new Color(245, 245, 240)));
		

		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.fillRect(200 ,200, getWidth(), getHeight());
			}
		};
		panel.setBounds(200, 200, 300, 300);
		panel.setOpaque(false);
		paintPanel.add(panel);
		SwingUtilities.invokeLater(() -> repaint());
		panel2 = col(10, 0, 0, label, paintPanel).setBackColor(new Color(245, 245, 240));
	}

	@Override
	public void action() {
		label1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int n = 0;
				System.out.println(e.getPoint());
				for(Shape s : map.values()) {
					if(s.contains(e.getPoint())) {
						selectLocation = strs.get(n);
						card.show(cardP, "P2");
						repaint();
					}	
					n++;
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
	}
	
	public static void main(String[] args) {
		new Login().setVisible(true);
	}
}

