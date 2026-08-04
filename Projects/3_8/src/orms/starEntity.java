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

public class starEntity {
	public Integer sno;
	public String title;
	public String detail;
	public Double scope;
	public Integer pno;

	public static final Map<Integer, starEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from star"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new starEntity();
				e.sno = rs.getInt("sno");
				e.title = rs.getString("title");
				e.detail = rs.getString("detail");
				e.scope = rs.getDouble("scope");
				e.pno = rs.getInt("pno");
				cache.put(e.sno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Optional<starEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<starEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<starEntity> findBy(Predicate<starEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<starEntity> findFirst(Predicate<starEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (sno == null) ? "insert into star (title,detail,scope,pno) values (?,?,?,?)"
				: "update star set title = ?,detail = ?,scope = ?,pno = ? where sno = ?";
		Object[] values = (sno == null) ? new Object[] { title, detail, scope, pno }
				: new Object[] { title, detail, scope, pno, sno };
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (sno == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				sno = rs.getInt(1);
			}
			cache.put(sno, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM star WHERE sno =?", sno)) {
			stmt.executeUpdate();
			cache.remove(sno);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
