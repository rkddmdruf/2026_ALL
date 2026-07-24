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

public class ordersEntity {
	public Integer ono;
	public LocalDate orderdate;
	public LocalDate paydate;
	public Integer uno;
	public String ordertime;
	public Integer cno;
	public Integer dno;

	public static final Map<Integer, ordersEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from orders"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new ordersEntity();
				e.ono = rs.getInt("ono");
				e.orderdate = rs.getDate("orderdate").toLocalDate();
				try {
					e.paydate = rs.getDate("paydate").toLocalDate();
				} catch (Exception e2) {
					e.paydate = null;
				}
				e.uno = rs.getInt("uno");
				e.ordertime = rs.getString("ordertime");
				e.cno = rs.getInt("cno");
				e.dno = rs.getInt("dno");
				cache.put(e.ono, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Optional<ordersEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<ordersEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<ordersEntity> findBy(Predicate<ordersEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<ordersEntity> findFirst(Predicate<ordersEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (ono == null) ? "insert into orders (orderdate,paydate,uno,ordertime,cno,dno) values (?,?,?,?,?,?)"
				: "update orders set orderdate = ?,paydate = ?,uno = ?,ordertime = ?,cno = ?,dno = ? where ono = ?";
		Object[] values = (ono == null) ? new Object[] { orderdate, paydate, uno, ordertime, cno, dno }
				: new Object[] { orderdate, paydate, uno, ordertime, cno, dno, ono };
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
		try (var stmt = DBManager.execute("DELETE FROM orders WHERE ono =?", ono)) {
			stmt.executeUpdate();
			cache.remove(ono);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
