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


public class index_tickEntity {
	public Integer it_no;
public Integer mi_no;
public Integer it_tick;
public Double it_value;
	
	public static final Map<Integer, index_tickEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from index_tick");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new index_tickEntity();
				e.it_no= rs.getInt("it_no");
e.mi_no= rs.getInt("mi_no");
e.it_tick= rs.getInt("it_tick");
e.it_value= rs.getDouble("it_value");
				cache.put(e.it_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<index_tickEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<index_tickEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<index_tickEntity> findBy(Predicate<index_tickEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<index_tickEntity> findFirst(Predicate<index_tickEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (it_no == null)
				? "insert into index_tick (mi_no,it_tick,it_value) values (?,?,?)"
				: "update index_tick set mi_no = ?,it_tick = ?,it_value = ? where it_no = ?";
		Object[] values = (it_no == null)
			? new Object[] {mi_no,it_tick,it_value}
			: new Object[] {mi_no,it_tick,it_value};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(it_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); it_no = rs.getInt(1);}
			cache.put(it_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM index_tick WHERE it_no =?", it_no)) {
            stmt.executeUpdate();
            cache.remove(it_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
