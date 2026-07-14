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


public class watchlistEntity {
	public Integer w_no;
public Integer u_no;
public Integer s_no;
	
	public static final Map<Integer, watchlistEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from watchlist");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new watchlistEntity();
				e.w_no= rs.getInt("w_no");
e.u_no= rs.getInt("u_no");
e.s_no= rs.getInt("s_no");
				cache.put(e.w_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<watchlistEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<watchlistEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<watchlistEntity> findBy(Predicate<watchlistEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<watchlistEntity> findFirst(Predicate<watchlistEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (w_no == null)
				? "insert into watchlist (u_no,s_no) values (?,?)"
				: "update watchlist set u_no = ?,s_no = ? where w_no = ?";
		Object[] values = (w_no == null)
			? new Object[] {u_no,s_no}
			: new Object[] {u_no,s_no};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(w_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); w_no = rs.getInt(1);}
			cache.put(w_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM watchlist WHERE w_no =?", w_no)) {
            stmt.executeUpdate();
            cache.remove(w_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
