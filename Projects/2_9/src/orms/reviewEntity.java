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

public class reviewEntity {
	public Integer rno;
	public Integer ono;
	public String review;
	public Integer star;

	public static final Map<Integer, reviewEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from review"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new reviewEntity();
				e.rno = rs.getInt("rno");
				e.ono = rs.getInt("ono");
				e.review = rs.getString("review");
				e.star = rs.getInt("star");
				cache.put(e.rno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Optional<reviewEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<reviewEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<reviewEntity> findBy(Predicate<reviewEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<reviewEntity> findFirst(Predicate<reviewEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (rno == null) ? "insert into review (ono,review,star) values (?,?,?)"
				: "update review set ono = ?,review = ?,star = ? where rno = ?";
		Object[] values = (rno == null) ? new Object[] { ono, review, star } : new Object[] { ono, review, star, rno };
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (rno == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				rno = rs.getInt(1);
			}
			cache.put(rno, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM review WHERE rno =?", rno)) {
			stmt.executeUpdate();
			cache.remove(rno);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
