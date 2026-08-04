package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import javax.swing.*;

import utils.CFrame;
public class Snippet extends CFrame{
	
	private void payment(LocalDate endDate) {
	
	    // 아직 약정기간이 남은 경우
	    if (LocalDate.now().isBefore(endDate)) {
	        JOptionPane.showMessageDialog(this,
	                "현재약정일이 종료되고 난 후 다시 구매해주세요.",
	                "경고", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	
	    // 영문 대문자 6자리 랜덤 코드
	    String pass = "";
	    for (int i = 0; i < 6; i++)
	        pass += (char) ('A' + (int) (Math.random() * 26));
	
	    // 윈도우 시스템 알림
	    try {
	        SystemTray tray = SystemTray.getSystemTray();
	
	        TrayIcon icon = new TrayIcon(
	                new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB));
	
	        tray.add(icon);
	        icon.displayMessage(
	                "비밀번호 안내",
	                "pass: " + pass,
	                TrayIcon.MessageType.NONE);
	
	        // 일정 시간 뒤 트레이 아이콘 제거
	        Timer timer = new Timer(8000, e -> tray.remove(icon));
	        timer.setRepeats(false);
	        timer.start();
	
	    } catch (Exception e) {
	        // 시스템 알림을 지원하지 않는 환경의 예외 처리
	        JOptionPane.showMessageDialog(this,
	                "pass: " + pass,
	                "비밀번호 안내",
	                JOptionPane.INFORMATION_MESSAGE);
	    }
	
	    // 코드 입력용 모달창
	    JTextField input = new JTextField();
	
	    int result = JOptionPane.showOptionDialog(
	            this,
	            new Object[] {
	                    "시스템 알림창의 PASS 코드를 입력하세요.",
	                    input
	            },
	            "결제 인증",
	            JOptionPane.OK_CANCEL_OPTION,
	            JOptionPane.PLAIN_MESSAGE,
	            null,
	            new String[] { "결제", "취소" },
	            "결제");
	
	    if (result != 0)
	        return;
	
	    if (!input.getText().trim().equalsIgnoreCase(pass)) {
	        JOptionPane.showMessageDialog(this,
	                "인증코드가 올바르지 않습니다.",
	                "경고", JOptionPane.WARNING_MESSAGE);
	        return;
	    }
	
	    JOptionPane.showMessageDialog(this,
	            "결제가 완료되었습니다.",
	            "정보", JOptionPane.INFORMATION_MESSAGE);
	
	    // 이 위치에서 DB 결제정보 저장
	}

	@Override
	protected void desing() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void action() {
		// TODO Auto-generated method stub
		
	}
}

