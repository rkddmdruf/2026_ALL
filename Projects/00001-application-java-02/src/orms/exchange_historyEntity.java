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


public class exchange_historyEntity {
	public Integer eh_no;
public Integer from_a_no;
public Integer to_a_no;
public Double eh_amount;
public Double eh_result;
public LocalDateTime eh_date;
	
	public static final Map<Integer, exchange_historyEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from exchange_history");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new exchange_historyEntity();
				e.eh_no= rs.getInt("eh_no");
e.from_a_no= rs.getInt("from_a_no");
e.to_a_no= rs.getInt("to_a_no");
e.eh_amount= rs.getDouble("eh_amount");
e.eh_result= rs.getDouble("eh_result");
e.eh_date= rs.getTimestamp("eh_date").toLocalDateTime();
				cache.put(e.eh_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<exchange_historyEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<exchange_historyEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<exchange_historyEntity> findBy(Predicate<exchange_historyEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<exchange_historyEntity> findFirst(Predicate<exchange_historyEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (eh_no == null)
				? "insert into exchange_history (from_a_no,to_a_no,eh_amount,eh_result,eh_date) values (?,?,?,?,?)"
				: "update exchange_history set from_a_no = ?,to_a_no = ?,eh_amount = ?,eh_result = ?,eh_date = ? where eh_no = ?";
		Object[] values = (eh_no == null)
			? new Object[] {from_a_no,to_a_no,eh_amount,eh_result,eh_date}
			: new Object[] {from_a_no,to_a_no,eh_amount,eh_result,eh_date};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(eh_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); eh_no = rs.getInt(1);}
			cache.put(eh_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM exchange_history WHERE eh_no =?", eh_no)) {
            stmt.executeUpdate();
            cache.remove(eh_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
