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

public class categoryEntity {

    public Integer c_no;
public String c_name;

    public static final Map<Integer, categoryEntity> cache = new HashMap<>();
    static { reload(); }

    public static void reload() {
        cache.clear();
        try (var stmt = DBManager.execute("SELECT * FROM category");
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                var e = new categoryEntity();
                e.c_no= rs.getInt("c_no");
e.c_name= rs.getString("c_name");
                cache.put(e.c_no, e);
            }
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static Optional<categoryEntity> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    public static List<categoryEntity> findAll() {
        return new ArrayList<>(cache.values());
    }

    public static List<categoryEntity> findBy(Predicate<categoryEntity> filter) {
        return findAll().stream().filter(filter).collect(Collectors.toList());
    }

    public static Optional<categoryEntity> findFirst(Predicate<categoryEntity> filter) {
        return findAll().stream().filter(filter).findFirst();
    }

    public categoryEntity() {}

    public categoryEntity(Integer c_no,String c_name) {
        this.c_no = c_no;
this.c_name = c_name;
    }

    public void save() {
        String sql = (c_no == null)
                ? "INSERT INTO category (c_name) VALUES (?)"
                : "UPDATE category SET c_name = ? WHERE c_no = ?";
        Object[] values = (c_no == null)
                ? new Object[]{c_name}
                : new Object[]{c_name, c_no};

        try (var stmt = DBManager.execute(sql, values)) {
            stmt.executeUpdate();
            if (c_no == null) { var rs = stmt.getGeneratedKeys(); rs.next(); c_no = rs.getInt(1); }
            cache.put(c_no, this);
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM category WHERE c_no =?", c_no)) {
            stmt.executeUpdate();
            cache.remove(c_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
