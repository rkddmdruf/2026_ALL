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


public class stockEntity {
	public Integer s_no;
public String s_code;
public String s_name;
public String s_market;
public String s_sector;
public Double s_initial_price;
public Double s_price;
	
	public static final Map<Integer, stockEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from stock");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new stockEntity();
				e.s_no= rs.getInt("s_no");
e.s_code= rs.getString("s_code");
e.s_name= rs.getString("s_name");
e.s_market= rs.getString("s_market");
e.s_sector= rs.getString("s_sector");
e.s_initial_price= rs.getDouble("s_initial_price");
e.s_price= rs.getDouble("s_price");
				cache.put(e.s_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<stockEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<stockEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<stockEntity> findBy(Predicate<stockEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<stockEntity> findFirst(Predicate<stockEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (s_no == null)
				? "insert into stock (s_code,s_name,s_market,s_sector,s_initial_price,s_price) values (?,?,?,?,?,?)"
				: "update stock set s_code = ?,s_name = ?,s_market = ?,s_sector = ?,s_initial_price = ?,s_price = ? where s_no = ?";
		Object[] values = (s_no == null)
			? new Object[] {s_code,s_name,s_market,s_sector,s_initial_price,s_price}
			: new Object[] {s_code,s_name,s_market,s_sector,s_initial_price,s_price};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(s_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); s_no = rs.getInt(1);}
			cache.put(s_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM stock WHERE s_no =?", s_no)) {
            stmt.executeUpdate();
            cache.remove(s_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
