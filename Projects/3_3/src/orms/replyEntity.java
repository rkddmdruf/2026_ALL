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


public class replyEntity {
	public Integer r_no;
public Integer p_no;
public Integer u_no;
public String r_content;
public LocalDateTime r_date;
	
	public static final Map<Integer, replyEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from reply");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new replyEntity();
				e.r_no= rs.getInt("r_no");
e.p_no= rs.getInt("p_no");
e.u_no= rs.getInt("u_no");
e.r_content= rs.getString("r_content");
e.r_date= rs.getTimestamp("r_date").toLocalDateTime();
				cache.put(e.r_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<replyEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<replyEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<replyEntity> findBy(Predicate<replyEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<replyEntity> findFirst(Predicate<replyEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (r_no == null)
				? "insert into reply (p_no,u_no,r_content,r_date) values (?,?,?,?)"
				: "update reply set p_no = ?,u_no = ?,r_content = ?,r_date = ? where r_no = ?";
		Object[] values = (r_no == null)
			? new Object[] {p_no,u_no,r_content,r_date}
			: new Object[] {p_no,u_no,r_content,r_date, r_no};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(r_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); r_no = rs.getInt(1);}
			cache.put(r_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM reply WHERE r_no =?", r_no)) {
            stmt.executeUpdate();
            cache.remove(r_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
