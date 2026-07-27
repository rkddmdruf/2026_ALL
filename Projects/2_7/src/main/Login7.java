package main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
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

public class Login7 extends CFrame{
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
	
	//login comps
	JLabel locationLabel = lb("", FONT(getter.font.deriveFont(19f).deriveFont(1)), HOA(JLabel.CENTER));
	JTextField id = comp(JTextField::new, NAME("아이디"));
	JPasswordField pw = comp(JPasswordField::new, NAME("비밀번호"));
	JButton login = set(new JButton("로그인") {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
	        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                            RenderingHints.VALUE_ANTIALIAS_ON);

	        if (getModel().isArmed())  g2.setColor(login.getBackground().brighter()); // 클릭 시
	        else g2.setColor(login.getBackground()); // 기본색
	        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
	        super.paintComponent(g); // 👉 글씨 자동
		};
	}, FG(Color.white), BG(new Color(85, 80, 65)), FONT(getter.font.deriveFont(14f)));
	
	public Login7() {
		pw.setEchoChar('●');
		selectLocation = "충청남도";
		setFrame("test", 797 + 81 + 30, 964 - 2 + 30, () -> {});
		repaint();
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
		
		JPanel paintPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(new Color(245, 245, 240));
				g2.fillRect(0, 0, getWidth(), getHeight());
				AffineTransform old = g2.getTransform();
				int index = strs.indexOf(selectLocation);
				Shape s = new ArrayList<>(map.values()).get(index);
				Rectangle2D b = s.getBounds();
				double scale = Math.min(getWidth() / b.getWidth(), getHeight() / b.getHeight()) * 0.8;
				double tx = (getWidth() - b.getWidth() * scale) / 2 - b.getX() * scale;
				double ty = (getHeight() - b.getHeight() * scale) / 2 - b.getY() * scale;
				System.out.println(getWidth());
				g2.translate(tx, ty);
				g2.scale(scale, scale);
				
				g2.setColor(colors.get(index));
				g2.fill(s);
				g2.setColor(Color.black);
				g2.draw(s);
				System.out.println(scale);
				g2.scale(1, 1);
				g2.setTransform(old);
				g2.setFont(getter.font.deriveFont(20f).deriveFont(1));
				g2.drawString(selectLocation
						, s.getBounds().x + s.getBounds().width / 2 - getFontMetrics(g2.getFont()).stringWidth(selectLocation) / 2
						, s.getBounds().y + getFontMetrics(g2.getFont()).getHeight() / 2 + s.getBounds().height / 2);
			}
		};
		
		paintPanel.setBorder(getter.em(300, 300,300, 300));

		paintPanel.add(setPanel2());
		
		JLabel beforeLabel = lb("←이전", SIZE(100, 30), BORDER(getter.em(10, 40, 10, 0)));
		beforeLabel.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { card.show(cardP, "P1"); }});
		
		JPanel panel = col(0, fw(beforeLabel), f(paintPanel)).setBackColor(new Color(245, 245, 240));
		
		cardP.add(setPanel1(), "P1");
		cardP.add(panel, "P2");
		card.show(cardP, "P1");
		add(cardP);
	}
	
	private Component setPanel2() {
		JPanel panel = new JPanel(new BorderLayout()) {
		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);

		        Graphics2D g2 = (Graphics2D) g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                            RenderingHints.VALUE_ANTIALIAS_ON);

		        int arc = 40;

		        // 👉 위(진함) → 아래(투명)
		        GradientPaint gp = new GradientPaint(
		            0, 0, setAlpha(colors.get(strs.indexOf(selectLocation)), 240),   // 위 (alpha 200)
		            0, getHeight(), setAlpha(colors.get(strs.indexOf(selectLocation)), 50) // 아래 (alpha 0)
		        );

		        g2.setPaint(gp);
		        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        g2.setPaint(gp);
		        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

		        // 테두리
		        g2.setColor(new Color(0, 0, 0, 80));
		        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
		    }
		    
		    private Color setAlpha(Color color, int r) {
		    	return new Color(color.getRed(), color.getGreen(), color.getBlue(), r);
		    }
		};
		panel.setOpaque(false);
		panel.setBorder(getter.em(25, 25, 50 ,25));
		
		
		JPanel mainPanel = col(15, fw(locationLabel), setTF(id), setTF(pw), f(login), lb("지도에서 지역을 선택한 후 로그인하세요."));
		mainPanel.setOpaque(false);
		panel.add(mainPanel);
		return panel;
	}

	private JPanel setTF(JTextField t) {
		JPanel p = col(1, fw(lb(t.getName(), FONT(getter.font.deriveFont(15f)))), f(t));
		p.setOpaque(false);
		return p;
	}
	private JPanel setPanel1() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(getter.em(30, 0, 0, 30));
		panel.setBackground(new Color(245, 245, 240));
		panel.add(label1);
		
		return panel;
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
						locationLabel.setText(selectLocation);
						card.show(cardP, "P2");
						revalidate();
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
		new Login7().setVisible(true);
	}
}

