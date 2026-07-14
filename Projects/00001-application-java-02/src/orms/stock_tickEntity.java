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


public class stock_tickEntity {
	public Integer st_no;
public Integer s_no;
public Integer st_tick;
public Double st_open;
public Double st_high;
public Double st_low;
public Double st_close;
public Integer st_volume;
	
	public static final Map<Integer, stock_tickEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from stock_tick");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new stock_tickEntity();
				e.st_no= rs.getInt("st_no");
e.s_no= rs.getInt("s_no");
e.st_tick= rs.getInt("st_tick");
e.st_open= rs.getDouble("st_open");
e.st_high= rs.getDouble("st_high");
e.st_low= rs.getDouble("st_low");
e.st_close= rs.getDouble("st_close");
e.st_volume= rs.getInt("st_volume");
				cache.put(e.st_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<stock_tickEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<stock_tickEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<stock_tickEntity> findBy(Predicate<stock_tickEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<stock_tickEntity> findFirst(Predicate<stock_tickEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (st_no == null)
				? "insert into stock_tick (s_no,st_tick,st_open,st_high,st_low,st_close,st_volume) values (?,?,?,?,?,?,?)"
				: "update stock_tick set s_no = ?,st_tick = ?,st_open = ?,st_high = ?,st_low = ?,st_close = ?,st_volume = ? where st_no = ?";
		Object[] values = (st_no == null)
			? new Object[] {s_no,st_tick,st_open,st_high,st_low,st_close,st_volume}
			: new Object[] {s_no,st_tick,st_open,st_high,st_low,st_close,st_volume};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(st_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); st_no = rs.getInt(1);}
			cache.put(st_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM stock_tick WHERE st_no =?", st_no)) {
            stmt.executeUpdate();
            cache.remove(st_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
