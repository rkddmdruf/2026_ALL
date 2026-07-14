package orms;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class storeEntity extends Account {

	public Integer s_no;
	public String s_name;
	public String s_id;
	public String s_pw;
	public Integer l_no;
	public Integer c_no;
	public String s_info;
	public Integer s_x;
	public Integer s_y;

	public static final Map<Integer, storeEntity> cache = new HashMap<>();
	static {
		reload();
	}

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * FROM store"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new storeEntity();
				e.s_no = rs.getInt("s_no");
				e.s_name = rs.getString("s_name");
				e.s_id = rs.getString("s_id");
				e.s_pw = rs.getString("s_pw");
				e.l_no = rs.getInt("l_no");
				e.c_no = rs.getInt("c_no");
				e.s_info = rs.getString("s_info");
				e.s_x = rs.getInt("s_x");
				e.s_y = rs.getInt("s_y");
				cache.put(e.s_no, e);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Optional<storeEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<storeEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<storeEntity> findBy(Predicate<storeEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<storeEntity> findFirst(Predicate<storeEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public storeEntity() {
	}

	public storeEntity(Integer s_no, String s_name, String s_id, String s_pw, Integer l_no, Integer c_no, String s_info,
			Integer s_x, Integer s_y) {
		this.s_no = s_no;
		this.s_name = s_name;
		this.s_id = s_id;
		this.s_pw = s_pw;
		this.l_no = l_no;
		this.c_no = c_no;
		this.s_info = s_info;
		this.s_x = s_x;
		this.s_y = s_y;
	}

	public void save() {
		String sql = (s_no == null)
				? "INSERT INTO store (s_name,s_id,s_pw,l_no,c_no,s_info,s_x,s_y) VALUES (?,?,?,?,?,?,?,?)"
				: "UPDATE store SET s_name = ?,s_id = ?,s_pw = ?,l_no = ?,c_no = ?,s_info = ?,s_x = ?,s_y = ? WHERE s_no = ?";
		Object[] values = (s_no == null) ? new Object[] { s_name, s_id, s_pw, l_no, c_no, s_info, s_x, s_y }
				: new Object[] { s_name, s_id, s_pw, l_no, c_no, s_info, s_x, s_y, s_no };

		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (s_no == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				s_no = rs.getInt(1);
			}
			cache.put(s_no, this);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM store WHERE s_no =?", s_no)) {
			stmt.executeUpdate();
			cache.remove(s_no);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public categoryEntity getcategoryEntity() {
		return categoryEntity.findById(c_no).get();
	}

	public locationEntity getlocationEntity() {
		return locationEntity.findById(l_no).get();
	}

	public List<jobEntity> getjobEntity() {
		return jobEntity.findBy(e -> e.s_no == s_no);
	}

	@Override
	public String getName() {
		return s_name + "매장 관리자님 환영합니다.";
	}
}
