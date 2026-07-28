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


public class likesEntity {
	public Integer l_no;
public Integer p_no;
public Integer u_no;
public LocalDateTime l_date;
	
	public static final Map<Integer, likesEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from likes");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new likesEntity();
				e.l_no= rs.getInt("l_no");
e.p_no= rs.getInt("p_no");
e.u_no= rs.getInt("u_no");
e.l_date= rs.getTimestamp("l_date").toLocalDateTime();
				cache.put(e.l_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<likesEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<likesEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<likesEntity> findBy(Predicate<likesEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<likesEntity> findFirst(Predicate<likesEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (l_no == null)
				? "insert into likes (p_no,u_no,l_date) values (?,?,?)"
				: "update likes set p_no = ?,u_no = ?,l_date = ? where l_no = ?";
		Object[] values = (l_no == null)
			? new Object[] {p_no,u_no,l_date}
			: new Object[] {p_no,u_no,l_date, l_no};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(l_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); l_no = rs.getInt(1);}
			cache.put(l_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM likes WHERE l_no =?", l_no)) {
            stmt.executeUpdate();
            cache.remove(l_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
