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

public class jobEntity {

	@Override
	public String toString() {
		return "jobEntity [j_no=" + j_no + ", s_no=" + s_no + ", j_title=" + j_title + ", j_content=" + j_content
				+ ", j_salary=" + j_salary + ", j_start=" + j_start + ", j_end=" + j_end + ", j_day=" + j_day
				+ ", j_people=" + j_people + ", j_state=" + j_state + ", j_regdate=" + j_regdate + "]";
	}

	public Integer j_no;
	public Integer s_no;
	public String j_title;
	public String j_content;
	public Integer j_salary;
	public LocalTime j_start;
	public LocalTime j_end;
	public String j_day;
	public Integer j_people;
	public Integer j_state;
	public LocalDate j_regdate;

	public static final Map<Integer, jobEntity> cache = new HashMap<>();
	static {
		reload();
	}

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * FROM job"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new jobEntity();
				e.j_no = rs.getInt("j_no");
				e.s_no = rs.getInt("s_no");
				e.j_title = rs.getString("j_title");
				e.j_content = rs.getString("j_content");
				e.j_salary = rs.getInt("j_salary");
				e.j_start = rs.getTime("j_start").toLocalTime();
				e.j_end = rs.getTime("j_end").toLocalTime();
				e.j_day = rs.getString("j_day");
				e.j_people = rs.getInt("j_people");
				e.j_state = rs.getInt("j_state");
				e.j_regdate = rs.getDate("j_regdate").toLocalDate();
				cache.put(e.j_no, e);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static Optional<jobEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<jobEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<jobEntity> findBy(Predicate<jobEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<jobEntity> findFirst(Predicate<jobEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public jobEntity() {
	}

	public jobEntity(Integer j_no, Integer s_no, String j_title, String j_content, Integer j_salary, LocalTime j_start,
			LocalTime j_end, String j_day, Integer j_people, Integer j_state, LocalDate j_regdate) {
		this.j_no = j_no;
		this.s_no = s_no;
		this.j_title = j_title;
		this.j_content = j_content;
		this.j_salary = j_salary;
		this.j_start = j_start;
		this.j_end = j_end;
		this.j_day = j_day;
		this.j_people = j_people;
		this.j_state = j_state;
		this.j_regdate = j_regdate;
	}

	public void save() {
		String sql = (j_no == null)
				? "INSERT INTO job (s_no,j_title,j_content,j_salary,j_start,j_end,j_day,j_people,j_state,j_regdate) VALUES (?,?,?,?,?,?,?,?,?,?)"
				: "UPDATE job SET s_no = ?,j_title = ?,j_content = ?,j_salary = ?,j_start = ?,j_end = ?,j_day = ?,j_people = ?,j_state = ?,j_regdate = ? WHERE j_no = ?";
		Object[] values = (j_no == null)
				? new Object[] { s_no, j_title, j_content, j_salary, j_start, j_end, j_day, j_people, j_state,
						j_regdate }
				: new Object[] { s_no, j_title, j_content, j_salary, j_start, j_end, j_day, j_people, j_state,
						j_regdate, j_no };

		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (j_no == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				j_no = rs.getInt(1);
			}
			cache.put(j_no, this);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM job WHERE j_no =?", j_no)) {
			stmt.executeUpdate();
			cache.remove(j_no);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public String stateText() {
		return j_state.intValue() == 1 ? "상시모집" : "마감";
	}
}
