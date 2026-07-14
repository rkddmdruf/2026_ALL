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


public class market_indexEntity {
	public Integer mi_no;
public String mi_name;
	
	public static final Map<Integer, market_indexEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from market_index");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new market_indexEntity();
				e.mi_no= rs.getInt("mi_no");
e.mi_name= rs.getString("mi_name");
				cache.put(e.mi_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<market_indexEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<market_indexEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<market_indexEntity> findBy(Predicate<market_indexEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<market_indexEntity> findFirst(Predicate<market_indexEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (mi_no == null)
				? "insert into market_index (mi_name) values (?)"
				: "update market_index set mi_name = ? where mi_no = ?";
		Object[] values = (mi_no == null)
			? new Object[] {mi_name}
			: new Object[] {mi_name};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(mi_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); mi_no = rs.getInt(1);}
			cache.put(mi_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM market_index WHERE mi_no =?", mi_no)) {
            stmt.executeUpdate();
            cache.remove(mi_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
