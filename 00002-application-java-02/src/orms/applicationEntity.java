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

public class applicationEntity {

    public Integer a_no;
public Integer u_no;
public Integer j_no;
public LocalDate a_date;
public Integer a_state;

    public static final Map<Integer, applicationEntity> cache = new HashMap<>();
    static { reload(); }

    public static void reload() {
        cache.clear();
        try (var stmt = DBManager.execute("SELECT * FROM application");
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                var e = new applicationEntity();
                e.a_no= rs.getInt("a_no");
e.u_no= rs.getInt("u_no");
e.j_no= rs.getInt("j_no");
e.a_date= rs.getDate("a_date").toLocalDate();
e.a_state= rs.getInt("a_state");
                cache.put(e.a_no, e);
            }
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static Optional<applicationEntity> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    public static List<applicationEntity> findAll() {
        return new ArrayList<>(cache.values());
    }

    public static List<applicationEntity> findBy(Predicate<applicationEntity> filter) {
        return findAll().stream().filter(filter).collect(Collectors.toList());
    }

    public static Optional<applicationEntity> findFirst(Predicate<applicationEntity> filter) {
        return findAll().stream().filter(filter).findFirst();
    }

    public applicationEntity() {}

    public applicationEntity(Integer a_no,Integer u_no,Integer j_no,LocalDate a_date,Integer a_state) {
        this.a_no = a_no;
this.u_no = u_no;
this.j_no = j_no;
this.a_date = a_date;
this.a_state = a_state;
    }

    public void save() {
        String sql = (a_no == null)
                ? "INSERT INTO application (u_no,j_no,a_date,a_state) VALUES (?,?,?,?)"
                : "UPDATE application SET u_no = ?,j_no = ?,a_date = ?,a_state = ? WHERE a_no = ?";
        Object[] values = (a_no == null)
                ? new Object[]{u_no,j_no,a_date,a_state}
                : new Object[]{u_no,j_no,a_date,a_state, a_no};

        try (var stmt = DBManager.execute(sql, values)) {
            stmt.executeUpdate();
            if (a_no == null) { var rs = stmt.getGeneratedKeys(); rs.next(); a_no = rs.getInt(1); }
            cache.put(a_no, this);
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM application WHERE a_no =?", a_no)) {
            stmt.executeUpdate();
            cache.remove(a_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
    
    public String stateText() {
    	String str = a_state == 0 ? "대기" : (a_state == 1 ? "불합격" : "합격");
    	return str;
    }
}
