package test.upDowns;

import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
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

import main.Util;
import orms.*;

public class A_Login extends CFrame {
	//45분 인데 이건 더 외우거나 쳐봐야할듯
	Map<String, Shape> map;
	List<Color> colors = Arrays.asList(new Color(0xB3AC97), new Color(0x98D1A7), new Color(0xE5D1AF),
			new Color(0xCEDBD8), new Color(0xD6BADD), new Color(0xA0D6D6), new Color(0xE1C7CC), new Color(0xCEB1B0),
			new Color(0x97B5B4), new Color(0xDBA1A8), new Color(0xB3A4BD), new Color(0xC6CEDD), new Color(0xD2D4BB),
			new Color(0x9EB7B9), new Color(0xAECBE5), new Color(0xABC5DC), new Color(0xFFFDD0));

	List<String> strs = Arrays.asList("충청남도", "제주도", "경상남도", "경상북도", "전라북도", "전라남도", "충청북도", "강원도", "경기도", "울산광역시",
			"부산광역시", "대구광역시", "대전광역시", "인천광역시", "서울특별시", "광주광역시", "세종시");

	Map<String, Integer> locationMap = new LinkedHashMap<>() {{
			put("충청남도", 1);
			put("제주도", 2);
			put("경상남도", 3);
			put("경상북도", 4);
			put("전라북도", 5);
			put("전라남도", 9);
			put("충청북도", 6);
			put("강원도", 7);
			put("경기도", 8);
			put("울산광역시", 10);
			put("부산광역시", 11);
			put("대구광역시", 12);
			put("대전광역시", 13);
			put("인천광역시", 14);
			put("서울특별시", 15);
			put("광주광역시", 16);
			put("세종시", 17);
	}};

	int mouseClick = 0;
	int mouseMoved = 0;
	CardLayout card = new CardLayout();
	JPanel cp = new JPanel(card);
	
	JTextField t1 = new JTextField() {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Util.setA(Color.white, 170));
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			g2.setColor(colors.get(mouseClick));
			g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			super.paintComponent(g);
		};
	};
	JTextField t2 = new JPasswordField() {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(Util.setA(Color.white, 170));
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			g2.setColor(colors.get(mouseClick));
			g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			super.paintComponent(g);
		};
	};
	
	JButton b1 = set(new JButton("로그인") {
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
			super.paintComponent(g);
		};
	}, FG(Color.white), BG(new Color(68, 68, 65)));
	JLabel l1 = lb("", FONT(sp.font.deriveFont(1).deriveFont(16f)), HOA(JLabel.CENTER));
	private JLabel p1;
	
	public A_Login() {
		((JPasswordField) t2).setEchoChar('●');
		b1.setOpaque(false);
		b1.setBorder(null);
		t1.setOpaque(false);
		t1.setBorder(sp.em(5, 5, 5, 5));
		t2.setOpaque(false);
		t2.setBorder(sp.em(5, 5, 5, 5));
		try {
			map = loadMap();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		setFrame("로그인", 797 + 81 + 30, 964 - 2 + 30);
	}

	static Map<String, Shape> loadMap() throws Exception{
		Map<String, Shape> map = new LinkedHashMap<String, Shape>();
		Pattern token = Pattern.compile("[MLZ]|-?\\d+(?:\\.\\d+)?");
		
		for(String line : Files.readAllLines(Path.of("datafiles/backimg/map.svg"))) {
			if(!line.contains("<path")) 
				continue;
			
			String id = line.split("id=\"")[1].split("\"")[0];
			String d = line.split("d=\"")[1].split("\"")[0];
			
			Matcher m = token.matcher(d);
			Path2D.Double path = new Path2D.Double();
			while(m.find()) {
				String cmd = m.group();
				
				if(cmd.equals("Z")) {
					path.closePath();
					continue;
				}
				
				m.find();
				double x = Double.parseDouble(m.group());
				
				m.find();
				double y = Double.parseDouble(m.group());
				
				if(cmd.equals("M")) {
					path.moveTo(x, y);
				}else {
					path.lineTo(x, y);
				}
			}
			map.put(id, path);
		}
		
		return map;
	}
	
	protected void desing() {
		p1 = new JLabel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				int n = 0;
				
				g2.setFont(sp.font.deriveFont(1).deriveFont(14f));
				FontMetrics fm = g2.getFontMetrics();
				for (Shape s : map.values()) {
					g2.setColor(colors.get(n));
					if (mouseMoved == n) {
						Color c = colors.get(n);
						g2.setColor(new Color(Math.min(255, (int) (c.getRed() * 1.2)), Math.min(255, (int) (c.getGreen() * 1.2)),
								Math.min(255,(int) (c.getBlue() * 1.2))));
					}
					g2.fill(s);
					g2.setColor(Color.black);
					g2.draw(s);

					Rectangle2D r = s.getBounds2D();
					g2.drawString(strs.get(n), (float) r.getCenterX() - fm.stringWidth(strs.get(n++)) / 2,
							(float) r.getCenterY());
				}
			};
		};
		
		JPanel p2 = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				Shape s = new ArrayList<>(map.values()).get(mouseClick);
				AffineTransform al = g2.getTransform();

				Rectangle2D b = s.getBounds();
				double scale = Math.min(getWidth() / b.getWidth(), getHeight() / b.getHeight()) * 0.8;
				double tx = (getWidth() - b.getWidth() * scale) / 2 - b.getX() * scale;
				double ty = (getHeight() - b.getHeight() * scale) / 2 - b.getY() * scale;
				g2.translate(tx, ty);
				g2.scale(scale, scale);

				g2.setColor(colors.get(mouseClick));
				g2.fill(s);
				g2.setColor(Color.black);
				g2.draw(s);
				g2.setTransform(al);

				g2.setFont(sp.font.deriveFont(20f).deriveFont(1));
				FontMetrics fm = g2.getFontMetrics();
				g2.drawString(strs.get(mouseClick), (getWidth() - fm.stringWidth(strs.get(mouseClick))) / 2,
						getHeight() / 2 - fm.getHeight());
			}
		};
		p2.setBorder(sp.em(300,300,300,300));
		p2.setOpaque(false);
		p2.add(loginPanel());
		JLabel l1 = lb("← 이전");
		JPanel p = col(0, fw(row(0, l1).setBackColor(sp.imgBackColor)), f(set(p2, BG(sp.imgBackColor))))
				.setBackColor(sp.imgBackColor);
		l1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				card.show(cp, "P1");
				repaint();
			}
		});
		
		cp.setBackground(sp.imgBackColor);
		cp.setBorder(sp.em(20, 10, 0, 0));
		cp.add(set(p1, BG(sp.imgBackColor)), "P1");
		cp.add(p, "P2");
		card.show(cp, "P1");
		add(cp);
	}

	private JPanel loginPanel() {
		JPanel p = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				GradientPaint gd = new GradientPaint(0, 0, Util.setA(colors.get(mouseClick), 240), 0,
						getHeight() - 1, Util.setA(Color.white, 100));
				g2.setPaint(gd);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

				g2.setColor(Color.black);
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

			}
		};
		p.setOpaque(false);
		set(p, BORDER(sp.em(25, 25, 35, 25)));

		JPanel pt1 = col(2, fw(lb("아이디")), f(t1));
		pt1.setOpaque(false);
		JPanel pt2 = col(2, fw(lb("비밀번호")), f(t2));
		pt2.setOpaque(false);

		JPanel p0 = col(20, fw(l1), f(pt1), f(pt2), f(b1), lb("지도에서 지역을 선택한 후 로그인하세요"));
		p0.setOpaque(false);
		p.add(p0);
		return p;
	}
	
	protected void action() {
		MouseAdapter p1m = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				List<String> keys = new ArrayList<>(map.keySet());
				for (String k : keys) {
					if (map.get(k).contains(e.getPoint())) {
						mouseMoved = keys.indexOf(k);
						repaint();
						return;
					}
					;
				}
				mouseMoved = -1;
				repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				List<String> keys = new ArrayList<>(map.keySet());
				for (String k : keys) {
					if (map.get(k).contains(e.getPoint())) {
						mouseClick = keys.indexOf(k);
						l1.setText(strs.get(mouseClick));
						t1.setText("");
						t2.setText("");
						card.show(cp, "P2");
						repaint();
						return;
					}
					;
				}
			}
		};
		
		p1.addMouseListener(p1m);
		p1.addMouseMotionListener(p1m);
		
		b1.addActionListener(e -> {
			String s1 = t1.getText();
			String s2 = t2.getText();
			if(s1.isBlank() || s2.isBlank()) sp.tException("빈칸이 존재합니다.");
			
			sp.user = userEntity.findFirst(t -> t.id.equals(s1) && t.pw.equals(s2)).orElse(null);
			sp.doctor = doctorEntity.findFirst(t -> t.id.equals(s1) && t.pw.equals(s2)).orElse(null);
			
			if(sp.user == null && sp.doctor == null) sp.tException("존재하지 않는 아이디입니다.");
			
			int lno = locationMap.get(strs.get(mouseClick));
			if(sp.user !=  null) {
				if(sp.user.lno != lno) sp.tException("지역을 확인하세요.");
				sp.inf(sp.user.name + "님 환영합니다.");
				new Main();
				dispose();
			}
			if(sp.doctor !=  null) {
				if(sp.doctor.lno != lno) sp.tException("지역을 확인하세요.");
				sp.inf(sp.doctor.dname + "선생님 환영합니다.");
			}
			
		});
	}
	
	public static void main(String[] args) {
		Util.start(new A_Login());
	}
}