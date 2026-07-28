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


public class postEntity {
	public Integer p_no;
public Integer u_no;
public String p_content;
public String p_files;
public Integer p_like;
public LocalDateTime p_date;
	
	public static final Map<Integer, postEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from post");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new postEntity();
				e.p_no= rs.getInt("p_no");
e.u_no= rs.getInt("u_no");
e.p_content= rs.getString("p_content");
e.p_files= rs.getString("p_files");
e.p_like= rs.getInt("p_like");
e.p_date= rs.getTimestamp("p_date").toLocalDateTime();
				cache.put(e.p_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<postEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<postEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<postEntity> findBy(Predicate<postEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<postEntity> findFirst(Predicate<postEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (p_no == null)
				? "insert into post (u_no,p_content,p_files,p_like,p_date) values (?,?,?,?,?)"
				: "update post set u_no = ?,p_content = ?,p_files = ?,p_like = ?,p_date = ? where p_no = ?";
		Object[] values = (p_no == null)
			? new Object[] {u_no,p_content,p_files,p_like,p_date}
			: new Object[] {u_no,p_content,p_files,p_like,p_date, p_no};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(p_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); p_no = rs.getInt(1);}
			cache.put(p_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM post WHERE p_no =?", p_no)) {
            stmt.executeUpdate();
            cache.remove(p_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
