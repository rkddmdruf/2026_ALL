package main;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import orms.applicationEntity;
import orms.favoriteEntity;
import orms.jobEntity;
import orms.locationEntity;
import orms.storeEntity;
import orms.userEntity;
import utils.BoxPanel;
import utils.CFrame;
import utils.getter;

public class MyPage extends CFrame{
	
	JButton button = new JButton("회원정보 수정") {{
		setBackground(getter.color);
		setForeground(Color.white);
	}};
	JPanel scrollPanel1, scrollPanel2;
	JLabel userImage = new JLabel();
	JTextArea infor = new JTextArea() {{
		setLineWrap(true);
	}};
	
	JTextField name = new JTextField();
	JTextField id = new JTextField();
	JTextField pw = new JTextField();
	JTextField uDate = new JTextField();
	JTextField phoneNumber = new JTextField();
	JTextField email = new JTextField();
	JComboBox<String> location = new JComboBox<String>() {{
		locationEntity.findAll().forEach(e -> addItem(e.l_name));
	}};
	List<JTextField> tfs = Arrays.asList(name, id, pw, uDate,phoneNumber, email);
	JPanel mainPanel = new JPanel(new BorderLayout(20, 20)) {{
		setBackground(Color.white);
	}};
	JPanel locationPanel = new JPanel(new BorderLayout()) {{
		setBackground(Color.white);
	}};
	JScrollPane sc;
	userEntity user = getter.user.copyUser();
	boolean changeUserInfor = false, chageImage = false, chageLocation = false;
	public MyPage() {
		//오늘 바꾼거 getter.ms대신 그냥 전처럼 infor, err로 바꿈,
		//EntityGenerator수정 save에서 value 마지막이 id 두개였던거 EntityGenerator파일 들어가면 있음
		//MyPage해야 하는거 -> 유저 정보 바꾸는 로직 짜기
		borderPanel.setLayout(new BorderLayout(10, 10));
		borderPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
		borderPanel.setBackground(Color.white);
		
		initAndReset();
		setFrame("마이페이지", 775, 400, null);
		SwingUtilities.invokeLater(() -> sc.getVerticalScrollBar().setValue(0));
	}

	private void initAndReset() {
		userEntity user = getter.user;
		userImage.setIcon(new ImageIcon(getter.user.u_image.getScaledInstance(140, 150, 4)));
		infor.setCursor(Cursor.getDefaultCursor());
		infor.setBorder(null);
		infor.setFocusable(false);
    	
		tfs.forEach(e -> {
			e.setFont(getter.font.deriveFont(1).deriveFont(13f));
			e.setBorder(null);
			e.setFocusable(false);
			e.setCursor(Cursor.getDefaultCursor());
		});
		name.setFont(getter.font.deriveFont(1).deriveFont(25f));
		
		infor.setText(user.u_resume);
		name.setText(user.u_name);
		id.setText(user.u_id);
		pw.setText(user.u_pw);
		uDate.setText(user.u_birth.toString());
		phoneNumber.setText(user.u_phone);
		email.setText(user.u_email);
		location.setSelectedIndex(user.l_no - 1);
	}
	
	@Override
	protected void design() {
		userImage.setVerticalAlignment(JLabel.TOP);
		
		JLabel label = new JLabel("회원정보");
		label.setFont(getter.font.deriveFont(1).deriveFont(26f));
		
		JPanel name_exceptionPanel = new BoxPanel(BoxPanel.C, 0, 20, 0, userPanel(), myLetter(),
					scrollPanel1 = jobPanel("내가 지원한 알바", applicationEntity.findBy(e -> e.u_no == getter.user.u_no)), 
					scrollPanel2 = jobPanel("관심 알바 목록", favoriteEntity.findBy(e -> e.u_no == getter.user.u_no))
				).setBackColor(Color.white);
		name_exceptionPanel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		
		sc = new JScrollPane(new BoxPanel(BoxPanel.R, 0, 20, 0, new BoxPanel(BoxPanel.C, 0, 15, 0, BoxPanel.fillWidth(name), name_exceptionPanel).setBackColor(Color.white)).setBackColor(Color.white));
		sc.setBackground(Color.white);
		sc.setBorder(null);
		
		JPanel mainPanel = new BoxPanel(BoxPanel.R, 0, 20, 0, BoxPanel.fillHeight(userImage), BoxPanel.fill(sc)).setBackColor(Color.white);
		JPanel panel = new BoxPanel(BoxPanel.C, 0, 10, 0, new BoxPanel(BoxPanel.R, 0, 0, 0, label, BoxPanel.HGAP(), BoxPanel.fillHeight(button)).setBackColor(Color.white), mainPanel).setBackColor(Color.white);
		borderPanel.add(panel);
	}

	private void reset(Border border, ImageIcon imageIcon, Point size, boolean bool, String buttonText, Cursor inputComponentCursor) {
		tfs.forEach(e -> {
			e.setBorder(border);
			e.setCursor(inputComponentCursor);
			e.setFocusable(!bool);
		});
		userImage.setIcon(imageIcon);
		setSize(new Dimension(getWidth() + size.x, getHeight() + size.y));
		button.setText(buttonText);
		scrollPanel1.setVisible(bool);
		scrollPanel2.setVisible(bool);
		infor.setBorder(border);
		infor.setCursor(inputComponentCursor);
		infor.setFocusable(!bool);
		locationPanel.add(bool ? new JLabel(location.getSelectedItem().toString()) : location);
	}
	
	@Override
	protected void action() {
		button.addActionListener(e -> {
			changeUserInfor = !changeUserInfor;
			locationPanel.remove(1);
			if(changeUserInfor) {
				reset(BorderFactory.createLineBorder(Color.LIGHT_GRAY), new ImageIcon(getter.user.u_image.getScaledInstance(120, 150, 4)),
						new Point(-20, -30), !changeUserInfor, "변경사함 저장", Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
			}else {
				String name  = this.name.getText();
				String id    = this.id.getText();
				String pw    = this.pw.getText();
				String birth = this.uDate.getText();
				String phone = this.phoneNumber.getText();
				String email = this.email.getText();
				int location = this.location.getSelectedIndex() + 1;
				String infor = this.infor.getText();
				
				/*if(name.equals(user.u_name) && id.equals(user.u_id) && pw.equals(user.u_pw) && 
						birth.equals(user.u_birth.toString()) && phone.equals(user.u_phone) && email.equals(user.u_email) && 
						location == user.l_no.intValue() && infor.equals(user.u_resume) && user.u_image == getter.user.u_image && !chageLocation) 
				{
					throw new RuntimeException("변경 사항이 없습니다.");
				}*/
				
				initAndReset();
				reset(null, new ImageIcon(getter.user.u_image.getScaledInstance(140, 150, 4)),
						new Point(20, 30), !changeUserInfor, "회원정보 수정", Cursor.getDefaultCursor());
			}
			SwingUtilities.invokeLater(() -> sc.getVerticalScrollBar().setValue(0));
			revalidate();
			repaint();
		});
		
		location.addActionListener(e -> {
			new UserLocationSelect(location.getSelectedItem().toString()).setVisible(true);
		});
		
		userImage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if(button.getText().contains("수정")) return;
				JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("이미지", "jpg");
				chooser.setFileFilter(filter);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.showOpenDialog(null);
		        try {
					userImage.setIcon(new ImageIcon(ImageIO.read(chooser.getSelectedFile()).getScaledInstance(userImage.getIcon().getIconWidth(), userImage.getIcon().getIconHeight(), 4)));
				} catch (Exception e2) {
					System.out.println(e2.getMessage());
				}
			};
		});
	}

	
    private JPanel userPanel() {
		String[] strs = "아이디,비밀번호,생년월일,연락처,이메일,지역".split(",");
		return new BoxPanel(BoxPanel.C, 0, 10, 0, IntStream.range(0, strs.length).mapToObj(c -> {
			JLabel label = new JLabel(strs[c]);
			label.setForeground(Color.LIGHT_GRAY);
			label.setFont(getter.font.deriveFont(13f));
			label.setPreferredSize(new Dimension(125, 30));
			try {// try 쓴이유 tfs.get(c + 1)에서 indexOutException을 이용
				JPanel p = new BoxPanel(BoxPanel.R, 0, 0, 0, label, BoxPanel.HGAP(), BoxPanel.fill(tfs.get(c + 1))).setBackColor(Color.white);// 여기서 +1은 name은 따로 들어가서임
				return p;
			} catch (Exception e) {
				locationPanel.add(label, BorderLayout.WEST);
				locationPanel.add(new JLabel(location.getSelectedItem().toString()) {{
					setFont(getter.font.deriveFont(1));
				}});
				JPanel p = new BoxPanel(BoxPanel.R, 0, 0, 0, BoxPanel.fill(locationPanel));
				return p;
			}
			
		}).toArray(JComponent[]::new)).setBackColor(Color.white);
	}

    private JPanel myLetter() {
    	JLabel l = new JLabel("자소서");
    	l.setFont(getter.font.deriveFont(23f).deriveFont(1));
    	return new BoxPanel(BoxPanel.C, 0, 15, 0, BoxPanel.fillWidth(l), BoxPanel.fill(infor)).setBackColor(Color.white);
    }
    
    private JPanel jobPanel(String s, Object obj) {
    	JLabel label = new JLabel(s);
    	label.setFont(getter.font.deriveFont(1).deriveFont(25f));
    	return new BoxPanel(BoxPanel.C, 0, 10, 0, BoxPanel.fillWidth(label), BoxPanel.fill(new JScrollPane(getCustomScrollPane(obj)))).setBackColor(Color.white);
    }
    
    private JScrollPane getCustomScrollPane(Object obj) {
    	List<JComponent> list = new ArrayList<>();
    	((List<Object>) obj).forEach(e -> {
    		jobEntity job = null;
    		if(e instanceof applicationEntity) {
    			job = jobEntity.findById(((applicationEntity) e).j_no).get();
    		}else if(e instanceof favoriteEntity) {
    			job = jobEntity.findById(((favoriteEntity) e).j_no).get();
    		}else {
    			throw new RuntimeException("파라미터로 온 값이 잘못되었거나, null입니다.");
    		}
    		storeEntity store = storeEntity.findById(job.s_no).get();
    		locationEntity location = locationEntity.findById(store.l_no).get();
    		
    		JLabel titleLabel = new JLabel(job.j_title);
    		titleLabel.setFont(getter.font.deriveFont(1).deriveFont(11f));
    		
    		JLabel subTitleLabel = new JLabel(store.s_name + " · " + location.l_name);
    		subTitleLabel.setFont(getter.font.deriveFont(1).deriveFont(11f));
    		subTitleLabel.setForeground(Color.LIGHT_GRAY);
    		
    		JLabel stateLabel = new JLabel("");
    		stateLabel.setForeground(Color.gray);
    		stateLabel.setVerticalAlignment(JLabel.CENTER);
    		if(e instanceof applicationEntity)
    			stateLabel.setText(((applicationEntity) e).stateText());
    		
    		JButton but = new JButton("보기");
    		but.setPreferredSize(new Dimension(but.getPreferredSize().width, 30));
    		but.setBackground(Color.white);
    		but.setFont(getter.font);
    		
    		JPanel wPanel = new BoxPanel(BoxPanel.C, 0, 3, 0, BoxPanel.fillWidth(titleLabel), BoxPanel.fillWidth(subTitleLabel)).setBackColor(Color.white);
    		JPanel ePanel = new BoxPanel(BoxPanel.R, 0, 5, 0,BoxPanel.fill(BoxPanel.HGAP()), stateLabel, but).setBackColor(Color.white);
    		
    		JPanel panel = new BoxPanel(BoxPanel.R, 0, 10, 0, wPanel, BoxPanel.HGAP(),ePanel).setBackColor(Color.white);
    		list.add(panel);
    	});
    	
    	JPanel p = new BoxPanel(BoxPanel.C, 0, 25, 0, list.stream().toArray(JComponent[]::new)).setBackColor(Color.white);
    	p.setBorder(BorderFactory.createEmptyBorder(7, 14, 7, 28));
    	
    	JScrollPane sc = new JScrollPane(p);
    	sc.setPreferredSize(new Dimension(0, (45 * 3) + 20));
    	sc.setBorder(null); sc.setBackground(Color.white);
    	return sc;
    }
    
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MyPage().setVisible(true));
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
            @Override
            protected void dispatchEvent(AWTEvent event) {
                try {
                    super.dispatchEvent(event);
                } catch (Exception e) {
                    handle(e);
                }
            }
        });
    }

    private static void handle(Throwable throwable) {
        throwable.printStackTrace();
        JOptionPane.showMessageDialog(null, throwable.getMessage()); 
    }
}
