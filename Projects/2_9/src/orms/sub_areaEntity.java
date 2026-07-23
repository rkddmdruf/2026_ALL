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

public class sub_areaEntity {
	public Integer sno;
	public String sname;
	public Integer sx;
	public Integer sy;
	public Integer ano;

	public static final Map<Integer, sub_areaEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from sub_area"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new sub_areaEntity();
				e.sno = rs.getInt("sno");
				e.sname = rs.getString("sname");
				e.sx = rs.getInt("sx");
				e.sy = rs.getInt("sy");
				e.ano = rs.getInt("ano");
				cache.put(e.sno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "sub_areaEntity [sno=" + sno + ", sname=" + sname + ", sx=" + sx + ", sy=" + sy + ", ano=" + ano + "]";
	}

	public static Optional<sub_areaEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<sub_areaEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<sub_areaEntity> findBy(Predicate<sub_areaEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<sub_areaEntity> findFirst(Predicate<sub_areaEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (sno == null) ? "insert into sub_area (sname,sx,sy,ano) values (?,?,?,?)"
				: "update sub_area set sname = ?,sx = ?,sy = ?,ano = ? where sno = ?";
		Object[] values = (sno == null) ? new Object[] { sname, sx, sy, ano }
				: new Object[] { sname, sx, sy, ano, sno };
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
		try (var stmt = DBManager.execute("DELETE FROM sub_area WHERE sno =?", sno)) {
			stmt.executeUpdate();
			cache.remove(sno);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
