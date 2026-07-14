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


public class transfer_historyEntity {
	public Integer t_no;
public Integer from_a_no;
public Integer to_a_no;
public Double t_amount;
public LocalDateTime t_date;
	
	public static final Map<Integer, transfer_historyEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from transfer_history");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new transfer_historyEntity();
				e.t_no= rs.getInt("t_no");
e.from_a_no= rs.getInt("from_a_no");
e.to_a_no= rs.getInt("to_a_no");
e.t_amount= rs.getDouble("t_amount");
e.t_date= rs.getTimestamp("t_date").toLocalDateTime();
				cache.put(e.t_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<transfer_historyEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<transfer_historyEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<transfer_historyEntity> findBy(Predicate<transfer_historyEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<transfer_historyEntity> findFirst(Predicate<transfer_historyEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (t_no == null)
				? "insert into transfer_history (from_a_no,to_a_no,t_amount,t_date) values (?,?,?,?)"
				: "update transfer_history set from_a_no = ?,to_a_no = ?,t_amount = ?,t_date = ? where t_no = ?";
		Object[] values = (t_no == null)
			? new Object[] {from_a_no,to_a_no,t_amount,t_date}
			: new Object[] {from_a_no,to_a_no,t_amount,t_date};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(t_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); t_no = rs.getInt(1);}
			cache.put(t_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM transfer_history WHERE t_no =?", t_no)) {
            stmt.executeUpdate();
            cache.remove(t_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
