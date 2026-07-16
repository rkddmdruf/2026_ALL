package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
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
	
	
	List<Ellipse2D> ovals = new ArrayList<>();
	
	final double imgX = img.getWidth(null);
	final double imgY = img.getHeight(null);
	double maxX = stations.stream().sorted((a, b) -> Integer.compare(b.x, a.x)).findFirst().get().x;
	double maxY = stations.stream().sorted((a, b) -> Integer.compare(b.y, a.y)).findFirst().get().y;
	double minX = stations.stream().sorted((a, b) -> Integer.compare(a.x, b.x)).findFirst().get().x;
	double minY = stations.stream().sorted((a, b) -> Integer.compare(a.y, b.y)).findFirst().get().y;
	
	public Metro() {
		bLabel.setBorder(getter.em(2, 0, 2, 0));
		bLabel.setFont(getter.font.deriveFont(13f));
		bLabel.setOpaque(false);
		bLabel.setForeground(Color.white);
		setFrame("경로 검색", 600, 930, () -> {});
	}

	@Override
	protected void desing() {
		label = new JLabel() {
		    int r = 12;
		    @Override
		    protected void paintComponent(Graphics g) {
		    	ovals.clear();
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
		            ovals.add(oval);
		            g2.fill(oval);
		        });
		    }
		};
		add(col(0, f(label), fw(bLabel)));
	}

	@Override
	protected void action() {
		new Thread(() -> {
			try {
				while(true) {
					System.out.println(ovals.size());
					Thread.sleep(100);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}).start();
	}
	
	public static void main(String[] args) {
		Util.start(new Metro());
	}
}
