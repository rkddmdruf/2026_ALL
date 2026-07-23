package orms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class stationEntity {
	public Integer sno;
	public String name;
	public String line;
	public Integer x;
	public Integer y;

	public static final Map<Integer, stationEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from station"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new stationEntity();
				e.sno = rs.getInt("sno");
				e.name = rs.getString("name");
				e.line = rs.getString("line");
				e.x = rs.getInt("x");
				e.y = rs.getInt("y");
				cache.put(e.sno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "stationEntity [sno=" + sno + ", name=" + name + ", line=" + line + ", x=" + x + ", y=" + y + "]";
	}

	public static Optional<stationEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<stationEntity> findAll() {
		
		return new ArrayList<>(cache.values());
	}

	public static List<stationEntity> findBy(Predicate<stationEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<stationEntity> findFirst(Predicate<stationEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (sno == null) ? "insert into station (name,line,x,y) values (?,?,?,?)"
				: "update station set name = ?,line = ?,x = ?,y = ? where sno = ?";
		Object[] values = (sno == null) ? new Object[] { name, line, x, y } : new Object[] { name, line, x, y, sno };
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
		try (var stmt = DBManager.execute("DELETE FROM station WHERE sno =?", sno)) {
			stmt.executeUpdate();
			cache.remove(sno);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
