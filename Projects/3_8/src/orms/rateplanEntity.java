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


public class rateplanEntity {
	public Integer rno;
public String rname;
public String service;
public Integer price;
public String effect;
	
	public static final Map<Integer, rateplanEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from rateplan");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new rateplanEntity();
				e.rno= rs.getInt("rno");
e.rname= rs.getString("rname");
e.service= rs.getString("service");
e.price= rs.getInt("price");
e.effect= rs.getString("effect");
				cache.put(e.rno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<rateplanEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<rateplanEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<rateplanEntity> findBy(Predicate<rateplanEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<rateplanEntity> findFirst(Predicate<rateplanEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (rno == null)
				? "insert into rateplan (rname,service,price,effect) values (?,?,?,?)"
				: "update rateplan set rname = ?,service = ?,price = ?,effect = ? where rno = ?";
		Object[] values = (rno == null)
			? new Object[] {rname,service,price,effect}
			: new Object[] {rname,service,price,effect, rno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(rno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); rno = rs.getInt(1);}
			cache.put(rno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM rateplan WHERE rno =?", rno)) {
            stmt.executeUpdate();
            cache.remove(rno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
