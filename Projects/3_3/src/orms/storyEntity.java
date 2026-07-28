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


public class storyEntity {
	public Integer s_no;
public Integer u_no;
public String s_file;
public String s_content;
public LocalDateTime s_date;
public Integer s_view;
	
	public static final Map<Integer, storyEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from story");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new storyEntity();
				e.s_no= rs.getInt("s_no");
e.u_no= rs.getInt("u_no");
e.s_file= rs.getString("s_file");
e.s_content= rs.getString("s_content");
e.s_date= rs.getTimestamp("s_date").toLocalDateTime();
e.s_view= rs.getInt("s_view");
				cache.put(e.s_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<storyEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<storyEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<storyEntity> findBy(Predicate<storyEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<storyEntity> findFirst(Predicate<storyEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (s_no == null)
				? "insert into story (u_no,s_file,s_content,s_date,s_view) values (?,?,?,?,?)"
				: "update story set u_no = ?,s_file = ?,s_content = ?,s_date = ?,s_view = ? where s_no = ?";
		Object[] values = (s_no == null)
			? new Object[] {u_no,s_file,s_content,s_date,s_view}
			: new Object[] {u_no,s_file,s_content,s_date,s_view, s_no};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(s_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); s_no = rs.getInt(1);}
			cache.put(s_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM story WHERE s_no =?", s_no)) {
            stmt.executeUpdate();
            cache.remove(s_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
