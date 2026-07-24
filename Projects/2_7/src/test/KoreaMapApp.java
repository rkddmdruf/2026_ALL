package test;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.io.InputStream;

/**
 * 실행 진입점.
 *
 * 사용법:
 *   java KoreaMapApp              (같은 폴더의 map.svg 를 읽음)
 *   java KoreaMapApp 다른경로.svg
 */
public class KoreaMapApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) { }

            SvgMapParser map;
            try {
                map = load("src/test/map.svg");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "map.svg 를 읽지 못했습니다.\n" + e,
                        "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            KoreaMapPanel panel = new KoreaMapPanel(map.getViewBox(), map.getRegions());
            panel.setPreferredSize(new Dimension(760, 900));

            JFrame f = new JFrame("대한민국 지도");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setLayout(new BorderLayout());
            f.add(buildToolBar(panel), BorderLayout.NORTH);
            f.add(panel, BorderLayout.CENTER);
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }

    private static JPanel buildToolBar(KoreaMapPanel panel) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xE3E3E0)));

        JButton shuffle = new JButton("색 다시 채우기");
        shuffle.addActionListener(e -> panel.randomizeColors());

        JCheckBox pastel = new JCheckBox("파스텔", panel.isPastel());
        pastel.setBackground(Color.WHITE);
        pastel.addActionListener(e -> panel.setPastel(pastel.isSelected()));

        JCheckBox labels = new JCheckBox("이름 표시", panel.isShowLabels());
        labels.setBackground(Color.WHITE);
        labels.addActionListener(e -> panel.setShowLabels(labels.isSelected()));

        JComboBox<KoreaMapPanel.LabelMode> mode =
                new JComboBox<>(KoreaMapPanel.LabelMode.values());
        mode.setSelectedItem(panel.getLabelMode());
        mode.addActionListener(e ->
                panel.setLabelMode((KoreaMapPanel.LabelMode) mode.getSelectedItem()));

        JCheckBox guide = new JCheckBox("계산 영역 보기", panel.isShowLabelBox());
        guide.setBackground(Color.WHITE);
        guide.addActionListener(e -> panel.setShowLabelBox(guide.isSelected()));

        bar.add(shuffle);
        bar.add(Box.createHorizontalStrut(4));
        bar.add(pastel);
        bar.add(labels);
        bar.add(Box.createHorizontalStrut(8));
        bar.add(new JLabel("글씨 자리:"));
        bar.add(mode);
        bar.add(guide);
        return bar;
    }

    /** 파일 경로 -> 없으면 클래스패스 리소스(/map.svg) 순으로 찾는다. */
    private static SvgMapParser load(String path) throws Exception {
        File f = new File(path);
        if (f.isFile()) return SvgMapParser.parse(f);

        InputStream in = KoreaMapApp.class.getResourceAsStream("/" + new File(path).getName());
        if (in != null) {
            try (InputStream stream = in) {
                return SvgMapParser.parse(stream);
            }
        }
        throw new java.io.FileNotFoundException(f.getAbsolutePath());
    }
}
