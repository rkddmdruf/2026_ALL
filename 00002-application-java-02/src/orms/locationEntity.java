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

public class locationEntity {

    public Integer l_no;
public String l_name;

    public static final Map<Integer, locationEntity> cache = new HashMap<>();
    static { reload(); }

    public static void reload() {
        cache.clear();
        try (var stmt = DBManager.execute("SELECT * FROM location");
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                var e = new locationEntity();
                e.l_no= rs.getInt("l_no");
e.l_name= rs.getString("l_name");
                cache.put(e.l_no, e);
            }
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static Optional<locationEntity> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    public static List<locationEntity> findAll() {
        return new ArrayList<>(cache.values());
    }

    public static List<locationEntity> findBy(Predicate<locationEntity> filter) {
        return findAll().stream().filter(filter).collect(Collectors.toList());
    }

    public static Optional<locationEntity> findFirst(Predicate<locationEntity> filter) {
        return findAll().stream().filter(filter).findFirst();
    }

    public locationEntity() {}

    public locationEntity(Integer l_no,String l_name) {
        this.l_no = l_no;
this.l_name = l_name;
    }

    public void save() {
        String sql = (l_no == null)
                ? "INSERT INTO location (l_name) VALUES (?)"
                : "UPDATE location SET l_name = ? WHERE l_no = ?";
        Object[] values = (l_no == null)
                ? new Object[]{l_name}
                : new Object[]{l_name, l_no};

        try (var stmt = DBManager.execute(sql, values)) {
            stmt.executeUpdate();
            if (l_no == null) { var rs = stmt.getGeneratedKeys(); rs.next(); l_no = rs.getInt(1); }
            cache.put(l_no, this);
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM location WHERE l_no =?", l_no)) {
            stmt.executeUpdate();
            cache.remove(l_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
