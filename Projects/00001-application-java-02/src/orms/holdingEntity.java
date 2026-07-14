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


public class holdingEntity {
	public Integer h_no;
public Integer u_no;
public Integer s_no;
public Integer h_quantity;
public Double h_avg_price;
	
	public static final Map<Integer, holdingEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from holding");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new holdingEntity();
				e.h_no= rs.getInt("h_no");
e.u_no= rs.getInt("u_no");
e.s_no= rs.getInt("s_no");
e.h_quantity= rs.getInt("h_quantity");
e.h_avg_price= rs.getDouble("h_avg_price");
				cache.put(e.h_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<holdingEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<holdingEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<holdingEntity> findBy(Predicate<holdingEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<holdingEntity> findFirst(Predicate<holdingEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (h_no == null)
				? "insert into holding (u_no,s_no,h_quantity,h_avg_price) values (?,?,?,?)"
				: "update holding set u_no = ?,s_no = ?,h_quantity = ?,h_avg_price = ? where h_no = ?";
		Object[] values = (h_no == null)
			? new Object[] {u_no,s_no,h_quantity,h_avg_price}
			: new Object[] {u_no,s_no,h_quantity,h_avg_price};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(h_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); h_no = rs.getInt(1);}
			cache.put(h_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM holding WHERE h_no =?", h_no)) {
            stmt.executeUpdate();
            cache.remove(h_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
