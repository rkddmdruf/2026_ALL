package main;

import javax.swing.*;

import orms.*;
import utils.*;
import static utils.BoxPanel.*;
import static utils.Properties.*;

import java.awt.Color;
import java.awt.Dimension;

public class ProductInfor extends CFrame{
	
	int pno;
	productEntity product;
	JButton back = bt("돌아가기", BG(Color.white));
	JButton buying = bt("구매하기", BG(Color.white));
	
	public ProductInfor(int pno) {
		this.pno = pno;
		product = productEntity.findById(pno).get();
		setFrame("상품정보", 700, 525, () -> {});
	}

	@Override
	protected void desing() {
		JLabel image = new JLabel(new ImageIcon(product.img.getScaledInstance(180, 140, 4)));
		image.setPreferredSize(new Dimension(180, 180));
		JPanel inforPanel = colF(10, 
				comp(JLabel::new, TEXT(product.pname), FONT(getter.font.deriveFont(22f).deriveFont(1))), 
				comp(JLabel::new, TEXT(product.description)),
				comp(JLabel::new, TEXT("판매지 : " + Util.areaStr(product.sno))),
				comp(JLabel::new, TEXT("분류 : " + categoryEntity.findById(product.cno).get().cname)),
				comp(JLabel::new, TEXT("가격 : " + getter.df.format(product.price) + "원"), FONT(getter.font.deriveFont(18f).deriveFont(1))),
				comp(JLabel::new, TEXT("평점 : " + Math.round(reviewEntity2.findAll(pno).stream().mapToInt(e -> e.star).average().getAsDouble() * 10d) / 10d))
			).setBackColor(Color.white);
		JPanel topPanel = set(row(15,image, inforPanel), BORDER(getter.eLine(Color.LIGHT_GRAY, 15,15,15,15)), BG(Color.white));
		JPanel buttonPanel = rowF(10, back, buying);
		add(set(col(10, topPanel, fill(reviewPanel()), fillWidth(buttonPanel)), BORDER(getter.em(10, 10, 10, 10))));
	}

	@Override
	protected void action() {
		back.addActionListener(e -> System.out.println(getter.qution("1 이상의 숫자를 입력해주세요.")));
	}
	
	private JPanel reviewPanel() {
		BoxPanel testPanel = col(10).setBackColor(Color.white);
		for(reviewEntity2 r : reviewEntity2.findAll(pno)) {
			userEntity user = userEntity.findById(orderEntity.findById(r.ono).get().uno).get();
			JLabel userLogo = set(new JLabel(getter.getImage("logo/user.png", 40, 40)), VEA(JLabel.BOTTOM));
			JPanel mainPanel = row(10, 10, 5, fillHeight(userLogo),
					fill(
							col(0, 20, 0, 
									fill(row(0, fillWidth(lb(user.uname, HOA(JLabel.LEFT))), fillWidth(lb("평점 : " + r.star, HOA(JLabel.RIGHT)))).setBackColor(Color.white)),
									fillWidth(lb(r.review))
								).setBackColor(Color.white)
						)
					
				).setBackColor(Color.white);
			JPanel borderPanel = set(col(5, 0, 20, mainPanel), BORDER(Util.rrb(Color.LIGHT_GRAY)), BG(Color.white));
			testPanel.addz(fill(borderPanel));
		}
		return set(
				col(10, fillWidth(lb("리뷰", FONT(getter.font.deriveFont(14f).deriveFont(1)))), fill(new JScrollPane(testPanel)))
				, BORDER(getter.eLine(Color.LIGHT_GRAY, 10, 10, 10, 10)), BG(Color.white));
	}
	public static void main(String[] args) {
		UIManager.put("Label.font", getter.font.deriveFont(1).deriveFont(13f));
		UIManager.put("Button.font", getter.font.deriveFont(1).deriveFont(13f));
		UIManager.put("ToggleButton.foreground", Color.black);
		UIManager.put("ToggleButton.background", Color.white);
		UIManager.put("ToggleButton.font", getter.font.deriveFont(1).deriveFont(13f));
		Util.start(new ProductInfor(2));
	}
}
