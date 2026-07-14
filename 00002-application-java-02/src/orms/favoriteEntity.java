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

public class favoriteEntity {

    public Integer f_no;
public Integer u_no;
public Integer j_no;

    public static final Map<Integer, favoriteEntity> cache = new HashMap<>();
    static { reload(); }

    public static void reload() {
        cache.clear();
        try (var stmt = DBManager.execute("SELECT * FROM favorite");
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                var e = new favoriteEntity();
                e.f_no= rs.getInt("f_no");
e.u_no= rs.getInt("u_no");
e.j_no= rs.getInt("j_no");
                cache.put(e.f_no, e);
            }
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static Optional<favoriteEntity> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    public static List<favoriteEntity> findAll() {
        return new ArrayList<>(cache.values());
    }

    public static List<favoriteEntity> findBy(Predicate<favoriteEntity> filter) {
        return findAll().stream().filter(filter).collect(Collectors.toList());
    }

    public static Optional<favoriteEntity> findFirst(Predicate<favoriteEntity> filter) {
        return findAll().stream().filter(filter).findFirst();
    }

    public favoriteEntity() {}

    public favoriteEntity(Integer f_no,Integer u_no,Integer j_no) {
        this.f_no = f_no;
this.u_no = u_no;
this.j_no = j_no;
    }

    public void save() {
        String sql = (f_no == null)
                ? "INSERT INTO favorite (u_no,j_no) VALUES (?,?)"
                : "UPDATE favorite SET u_no = ?,j_no = ? WHERE f_no = ?";
        Object[] values = (f_no == null)
                ? new Object[]{u_no,j_no}
                : new Object[]{u_no,j_no, f_no};

        try (var stmt = DBManager.execute(sql, values)) {
            stmt.executeUpdate();
            if (f_no == null) { var rs = stmt.getGeneratedKeys(); rs.next(); f_no = rs.getInt(1); }
            cache.put(f_no, this);
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM favorite WHERE f_no =?", f_no)) {
            stmt.executeUpdate();
            cache.remove(f_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
