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

public class resumeEntity {

    public Integer r_no;
public Integer u_no;
public String r_title;
public String r_content;

    public static final Map<Integer, resumeEntity> cache = new HashMap<>();
    static { reload(); }

    public static void reload() {
        cache.clear();
        try (var stmt = DBManager.execute("SELECT * FROM resume");
             var rs = stmt.executeQuery()) {
            while (rs.next()) {
                var e = new resumeEntity();
                e.r_no= rs.getInt("r_no");
e.u_no= rs.getInt("u_no");
e.r_title= rs.getString("r_title");
e.r_content= rs.getString("r_content");
                cache.put(e.r_no, e);
            }
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public static Optional<resumeEntity> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    public static List<resumeEntity> findAll() {
        return new ArrayList<>(cache.values());
    }

    public static List<resumeEntity> findBy(Predicate<resumeEntity> filter) {
        return findAll().stream().filter(filter).collect(Collectors.toList());
    }

    public static Optional<resumeEntity> findFirst(Predicate<resumeEntity> filter) {
        return findAll().stream().filter(filter).findFirst();
    }

    public resumeEntity() {}

    public resumeEntity(Integer r_no,Integer u_no,String r_title,String r_content) {
        this.r_no = r_no;
this.u_no = u_no;
this.r_title = r_title;
this.r_content = r_content;
    }

    public void save() {
        String sql = (r_no == null)
                ? "INSERT INTO resume (u_no,r_title,r_content) VALUES (?,?,?)"
                : "UPDATE resume SET u_no = ?,r_title = ?,r_content = ? WHERE r_no = ?";
        Object[] values = (r_no == null)
                ? new Object[]{u_no,r_title,r_content}
                : new Object[]{u_no,r_title,r_content, r_no};

        try (var stmt = DBManager.execute(sql, values)) {
            stmt.executeUpdate();
            if (r_no == null) { var rs = stmt.getGeneratedKeys(); rs.next(); r_no = rs.getInt(1); }
            cache.put(r_no, this);
        } catch (Exception exception) { exception.printStackTrace(); }
    }

    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM resume WHERE r_no =?", r_no)) {
            stmt.executeUpdate();
            cache.remove(r_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
    
   
public userEntity getuserEntity() { 
	return userEntity.findById(u_no).get(); 
}
}
