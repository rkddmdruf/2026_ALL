package orms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class reviewEntity2 {
	public Integer pno;
	public Integer ono;
	public Integer rno;
	public String review;
	public Integer star;

	public static final Map<Integer, List<reviewEntity2>> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager
				.execute("SELECT product.pno, `order`.ono, rno, review.review, star FROM idelivery.product \r\n"
						+ "join `order` on `order`.pno = product.pno\r\n" + "join review on review.ono = `order`.ono;");
				var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new reviewEntity2();
				e.pno = rs.getInt("pno");
				e.rno = rs.getInt("rno");
				e.ono = rs.getInt("ono");
				e.review = rs.getString("review");
				e.star = rs.getInt("star");
				cache.computeIfAbsent(e.pno, k -> new ArrayList<>()).add(e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<reviewEntity2> findAll(int pno) {
		System.out.println(cache.get(pno).size());
		return new ArrayList<>(cache.get(pno));
	}

	@Override
	public String toString() {
		return "reviewEntity2 [pno=" + pno + ", ono=" + ono + ", rno=" + rno + ", review=" + review + ", star=" + star
				+ "]";
	}

}
