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


public class accountEntity {
	public Integer a_no;
public Integer u_no;
public String a_type;
public String a_name;
public Double a_balance;
	
	public static final Map<Integer, accountEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from account");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new accountEntity();
				e.a_no= rs.getInt("a_no");
e.u_no= rs.getInt("u_no");
e.a_type= rs.getString("a_type");
e.a_name= rs.getString("a_name");
e.a_balance= rs.getDouble("a_balance");
				cache.put(e.a_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<accountEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<accountEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<accountEntity> findBy(Predicate<accountEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<accountEntity> findFirst(Predicate<accountEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (a_no == null)
				? "insert into account (u_no,a_type,a_name,a_balance) values (?,?,?,?)"
				: "update account set u_no = ?,a_type = ?,a_name = ?,a_balance = ? where a_no = ?";
		Object[] values = (a_no == null)
			? new Object[] {u_no,a_type,a_name,a_balance}
			: new Object[] {u_no,a_type,a_name,a_balance};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(a_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); a_no = rs.getInt(1);}
			cache.put(a_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM account WHERE a_no =?", a_no)) {
            stmt.executeUpdate();
            cache.remove(a_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
