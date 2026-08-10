package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;

public class orderEntity {
	public Integer ono;
	public Integer pno;
	public Integer uno;
	public LocalDate date;
	public Integer quantity;

	public static final Map<Integer, orderEntity> cache = new HashMap<>();
	static {
		reload();
	}

	private static void reload() {
		cache.clear();
		try (var rs = DBManager.execute("select * from `order`"); var re = rs.executeQuery()) {
			while (re.next()) {
				var e = new orderEntity();
				e.ono = re.getInt("ono");
				e.pno = re.getInt("pno");
				e.uno = re.getInt("uno");
				e.date = re.getDate("date").toLocalDate();
				e.quantity = re.getInt("quantity");
				cache.put(e.ono, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<orderEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<orderEntity> findBy(Predicate<orderEntity> pre) {
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}

	public static Optional<orderEntity> findById(int i) {
		return Optional.ofNullable(cache.get(i));
	}

	public static Optional<orderEntity> findByFrist(Predicate<orderEntity> f) {
		return findAll().stream().filter(f).findFirst();
	}

	public void save() {
		String sql = (ono == null) ? "insert into order set pno = ?,uno = ?,date = ?,quantity = ?)"
				: "update order set pno = ?,uno = ?,date = ?,quantity = ? where ono = ?";
		Object[] values = (ono == null) ? new Object[] { pno, uno, date, quantity }
				: new Object[] { pno, uno, date, quantity, ono };
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (ono == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				ono = rs.getInt(1);
			}
			cache.put(ono, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try (var ps = DBManager.execute("delete from order where ono = ?", ono)) {
			ps.executeUpdate();
			cache.remove(ono);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}