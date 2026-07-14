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

public class orderEntity {
	public Integer ono;
	public Integer pno;
	public Integer uno;
	public LocalDate date;
	public Integer quantity;

	public static final Map<Integer, orderEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from `order`"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new orderEntity();
				e.ono = rs.getInt("ono");
				e.pno = rs.getInt("pno");
				e.uno = rs.getInt("uno");
				e.date = rs.getDate("date").toLocalDate();
				e.quantity = rs.getInt("quantity");
				cache.put(e.ono, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer getOno() {
		return ono;
	}

	public void setOno(Integer ono) {
		this.ono = ono;
	}

	public Integer getPno() {
		return pno;
	}

	public void setPno(Integer pno) {
		this.pno = pno;
	}

	public Integer getUno() {
		return uno;
	}

	public void setUno(Integer uno) {
		this.uno = uno;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public static Map<Integer, orderEntity> getCache() {
		return cache;
	}

	@Override
	public String toString() {
		return "orderEntity [ono=" + ono + ", pno=" + pno + ", uno=" + uno + ", date=" + date + ", quantity=" + quantity
				+ "]";
	}

	public static Optional<orderEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<orderEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<orderEntity> findBy(Predicate<orderEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<orderEntity> findFirst(Predicate<orderEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (ono == null) ? "insert into order (pno,uno,date,quantity) values (?,?,?,?)"
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
		try (var stmt = DBManager.execute("DELETE FROM order WHERE ono =?", ono)) {
			stmt.executeUpdate();
			cache.remove(ono);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
