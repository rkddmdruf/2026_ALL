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


public class rewardEntity {
	public Integer reno;
public Double resale;
	
	public static final Map<Integer, rewardEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from reward");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new rewardEntity();
				e.reno= rs.getInt("reno");
e.resale= rs.getDouble("resale");
				cache.put(e.reno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<rewardEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<rewardEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<rewardEntity> findBy(Predicate<rewardEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<rewardEntity> findFirst(Predicate<rewardEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (reno == null)
				? "insert into reward (resale) values (?)"
				: "update reward set resale = ? where reno = ?";
		Object[] values = (reno == null)
			? new Object[] {resale}
			: new Object[] {resale, reno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(reno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); reno = rs.getInt(1);}
			cache.put(reno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM reward WHERE reno =?", reno)) {
            stmt.executeUpdate();
            cache.remove(reno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
