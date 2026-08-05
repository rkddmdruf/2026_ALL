package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

import utils.sp;

class StarView extends JComponent {
    private int value;  // 0~50

    public StarView(int d) {
        setValue(d);
    }

    void setValue(int value) {
        this.value = Math.max(0, Math.min(50, value));
        repaint();
    }

    
    private static File reviewImage;
    private static void test() {
    	
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON
        );

        float n = 100;
        FontMetrics fm = g2.getFontMetrics();
        while(getHeight() < n) {
        	g2.setFont(sp.font.deriveFont(n));
        	n--;
        }
        
        fm = g2.getFontMetrics();
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        int width = fm.stringWidth("★★★★★");

        // 비어 있는 별
        g2.setColor(Color.GRAY);
        g2.drawString("☆☆☆☆☆", 0, y);

        // value만큼만 노란 별이 보이도록 자름
        g2.clipRect(
            0, 0,
            (int) (width * value / 50.0),
            getHeight()
        );
        
        g2.setColor(new Color(255, 190, 0));
        g2.drawString("★★★★★", 0, y);
        
        g2.dispose();
    }
    
    
    
    public static void main(String[] args) {
    	JLabel imageLabel = new JLabel();
    	
    	imageLabel.setTransferHandler(new TransferHandler() {
    	    public boolean canImport(TransferSupport s) {
    	        return s.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    	    }

    	    public boolean importData(TransferSupport s) {
    	        try {
    	            File f = ((List<File>) s.getTransferable()
    	                    .getTransferData(DataFlavor.javaFileListFlavor)).get(0);

    	            if (!f.getName().toLowerCase().endsWith(".jfif"))
    	                return false;

    	            reviewImage = f;
    	            imageLabel.setIcon(new ImageIcon(new ImageIcon(f.getPath()).getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), 4)));
    	            imageLabel.setText("");

    	            return true;

    	        } catch (Exception e) {
    	            return false;
    	        }
    	    }
    	});
    	
		JFrame f = new JFrame();
		f.setSize(500, 500);
		f.add(imageLabel);
		f.setVisible(true);
	}
    
    
}