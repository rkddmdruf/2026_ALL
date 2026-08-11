package test.test1;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import main.Util;
import orms.doctorEntity;
import orms.userEntity;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

public class Login extends CFrame {
	Map<String, Shape> map;
	List<Color> color = Arrays.asList(new Color(0xB3AC97), new Color(0x98D1A7), new Color(0xE5D1AF),
			new Color(0xD6BADD), new Color(0xA0D6D6), new Color(0xE1C7CC), new Color(0xCEB1B0), new Color(0x97B5B4),
			new Color(0xCEDBD8), new Color(0xDBA1A8), new Color(0xB3A4BD), new Color(0xC6CEDD), new Color(0xD2D4BB),
			new Color(0x9EB7B9), new Color(0xAECBE5), new Color(0xABC5DC), new Color(0xFFFDD0));

	List<String> strs = Arrays.asList("충청남도 ", "제주도", "경상남도", "경상북도", "전라북도","전라남도", "충청북도",
			"강원도", "경기도", "울산광역시", "부산광역시", "대구광역시", "대전광역시", "인천광역시", "서울특별시", "광주광역시", "광주광역시", "세종시");
	
	CardLayout card = new CardLayout();
	JPanel cardP = new JPanel(card);
	
	JLabel label;
	
	String selectLocation;
	
	JLabel locationLabel = lb("", FONT(sp.font.deriveFont(19f).deriveFont(1)), HOA(JLabel.CENTER));
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
	}, FG(Color.white), BG(new Color(85, 80, 65)), FONT(sp.font.deriveFont(14f)));
	JLabel text = lb("지도에서 지역을 선택한 후 로그인하세요.");
	int hover = -1;
	public Login() {
		login.setOpaque(false);
		login.setBorderPainted(false);
		login.setContentAreaFilled(false);
		pw.setEchoChar('●');
		try {
			map = loadMap("datafiles/backimg/map.svg");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setFrame("로그인", 797 + 81 + 30, 964 - 2 + 30, () -> {
		});
	}

	@Override
	public void desing() {
		label = new JLabel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
				List<String> key = new ArrayList<>(map.keySet());
				key.forEach(e -> {
					Color c = color.get(key.indexOf(e));
					if(hover == key.indexOf(e)) {
						c = new Color(Math.min(255, (int) (c.getRed() * 1.2)), Math.min(255, (int) (c.getGreen() * 1.2)), Math.min(255, (int) (c.getBlue() * 1.2)));
						System.out.println(hover);
					}
					g2.setColor(c);
					g2.fill(map.get(e));
					g2.setColor(Color.black);
					g2.draw(map.get(e));
				});
				
				key.forEach(e -> {
					Point2D.Double p = getRegionCenter(map.get(e));
					g2.setFont(sp.font.deriveFont(1).deriveFont(14f));
					FontMetrics fm = g2.getFontMetrics();
					g2.setColor(Color.black);
					g2.drawString(strs.get(key.indexOf(e)), (int) p.x - fm.stringWidth(strs.get(key.indexOf(e))) / 2, (int) p.y);
				});
			}
		};
		JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(sp.em(30, 0, 0, 30));
		p1.setBackground(new Color(245,245,240));
		p1.add(label);
		
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
				
				g2.setColor(color.get(index));
				g2.fill(s);
				g2.setColor(Color.black);
				g2.draw(s);
				System.out.println(scale);
				g2.scale(1, 1);
				g2.setTransform(old);
				g2.setFont(sp.font.deriveFont(20f).deriveFont(1));
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString(selectLocation, (getWidth() - fm.stringWidth(selectLocation)) / 2, getHeight() / 2 -fm .getHeight());
			}
		};
		
		paintPanel.setBorder(sp.em(300, 300,300, 300));

		paintPanel.add(setPanel());
		JLabel beforeLabel = lb("←이전", SIZE(100, 30), BORDER(sp.em(10, 40, 10, 0)));
		beforeLabel.addMouseListener(new MouseAdapter() { @Override public void mouseClicked(MouseEvent e) { card.show(cardP, "P1"); }});
		
		JPanel panel = col(0, fw(beforeLabel), f(paintPanel)).setBackColor(new Color(245, 245, 240));
		
		cardP.add(p1, "P1");
		cardP.add(panel, "P2");
		add(cardP);
	}

	private JPanel setPanel() {
		JPanel panel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
			    Graphics2D g2 = (Graphics2D) g;
		        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		                            RenderingHints.VALUE_ANTIALIAS_ON);

		        int arc = 40;
		        GradientPaint gp = new GradientPaint(0, 0, Util.setA(color.get(strs.indexOf(selectLocation)), 150), 0, getHeight(), Util.setA(Color.white, 150));
		        g2.setPaint(gp);
		        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        g2.setPaint(gp);
		        g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
		        
		        g2.setColor(Color.LIGHT_GRAY);
		        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
			}
		};
		panel.setOpaque(false);
		panel.setBorder(sp.em(25, 25, 50 ,25));
		
		
		JPanel mainPanel = col(15, fw(locationLabel), setTF(id), setTF(pw), f(login), text);
		mainPanel.setOpaque(false);
		panel.add(mainPanel);
		return panel;
	}
	
	private JPanel setTF(JTextField t) {
		JPanel p = col(2, fw(lb(t.getName(), FONT(sp.font.deriveFont(13f)))), f(t));
		p.setOpaque(false);
		return p;
	}
	@Override
	public void action() {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				List<String> key = new ArrayList<>(map.keySet());
				key.forEach(k -> {
					if(map.get(k).contains(e.getPoint())) {
						selectLocation = strs.get(key.indexOf(k));
						locationLabel.setText(selectLocation);
						text.setForeground(color.get(strs.indexOf(selectLocation)));
						card.show(cardP, "P2");
						repaint();
					}
				});
			}
		});
		label.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				List<String> key = new ArrayList<>(map.keySet());
				key.forEach(k -> {
					if(map.get(k).contains(e.getPoint())) {
						hover = key.indexOf(k);
						SwingUtilities.invokeLater(() -> label.repaint());
					}
				});
			}
		});
		
		login.addActionListener(e -> {
			String id = this.id.getText();
			String pw = String.valueOf(this.pw.getPassword());
			if(id.isBlank() || pw.isBlank())
				throw new RuntimeException("빈칸이 있습니다.");
			List<userEntity> users = userEntity.findBy(u -> u.id.equals(id) && u.pw.equals(pw));
			List<doctorEntity> ds = doctorEntity.findBy(d -> d.id.equals(id) && d.pw.equals(pw));
			if(users.isEmpty() && ds.isEmpty()) throw new RuntimeException("존재하지 않는 아이디잆니다."); 
			if(!users.isEmpty() || !ds.isEmpty()) {
				if(users.get(0).lno != strs.indexOf(selectLocation) || ds.get(0).lno != strs.indexOf(selectLocation)) {
					throw new RuntimeException("지역을 확인하세요.");
				}
			}
		});
	}

	private Point2D.Double getRegionCenter(Shape regionShape) {
	    Rectangle2D bounds = regionShape.getBounds2D();
	
	    double centerX = bounds.getCenterX();
	    double centerY = bounds.getCenterY();
	
	    return new Point2D.Double(centerX, centerY);
	}
	
	public static void main(String[] args) {
		Util.start(new Login());
	}
	static Map<String, Shape> loadMap(String file) throws Exception {
		Map<String, Shape> map = new LinkedHashMap<>();
		Pattern token = Pattern.compile("[MLZ]|-?\\d+(?:\\.\\d+)?");

		for (String line : Files.readAllLines(Path.of(file))) {

			if (!line.contains("<path"))
				continue;

			String id = line.split("id=\"")[1].split("\"")[0];
			String d = line.split("d=\"")[1].split("\"")[0];

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
}
