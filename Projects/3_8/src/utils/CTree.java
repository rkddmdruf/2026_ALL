package utils;

import javax.swing.*;
import javax.swing.tree.*;

import utils.sp;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CTree extends JTree {

    Set<Object> checked = new HashSet<>();
    Set<Object> parents = new HashSet<>();
    public CTree(DefaultMutableTreeNode root) {
        JTree tree = this;
        tree.setModel(new DefaultTreeModel(root));
        tree.setCellRenderer(new CircleCheckRenderer());
        tree.setToggleClickCount(1);
        
        tree.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) return;
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (!node.isLeaf()) {
                    if (parents.contains(node)) {
                        checked.remove(node);
                        Collections.list(node.preorderEnumeration()).forEach(checked::remove);
                    } else {
                    	parents.add(node);
                    }
                } else {
                    if (checked.contains(node))
                        checked.remove(node);
                    else
                        checked.add(node);
                }
                System.out.println(checked);
                tree.repaint();
                
            }
        });
    }

    // 동그란 체크 아이콘을 직접 그리는 렌더러
    class CircleCheckRenderer extends DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            if (leaf) {
                // 최하위 노드 -> 체크박스 표시
                boolean isChecked = checked.contains(value);
                setIcon(new CircleIcon(isChecked));
            }
            return this;
        }
    }

    // 동그라미 아이콘 (체크 시 초록색 + 체크마크)
    class CircleIcon implements Icon {
        boolean checked;
        CircleIcon(boolean checked) { this.checked = checked; }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 16;
            Ellipse2D circle = new Ellipse2D.Double(x, y + 2, size, size);
            
            if (checked) {
                g2.setColor(new Color(76, 175, 80)); // 초록색
                g2.fill(circle);
                g2.setColor(Color.WHITE);
                g2.setFont(sp.font.deriveFont(12f).deriveFont(1));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("√", (size - fm.stringWidth("√")) / 2, (getIconHeight() - fm.getHeight()) / 2 + fm.getAscent());
            } else {
                g2.setColor(Color.LIGHT_GRAY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(circle);
            }
            
            g2.dispose();
        }

        public int getIconWidth() { return 20; }
        public int getIconHeight() { return 20; }
    }
}