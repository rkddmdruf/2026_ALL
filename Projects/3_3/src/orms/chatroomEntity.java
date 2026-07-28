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


public class chatroomEntity {
	public Integer c_no;
public Integer send_u;
public Integer get_u;
public Integer c_type;
public Integer c_ref;
public String c_text;
public LocalDateTime c_date;
	
	public static final Map<Integer, chatroomEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from chatroom");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new chatroomEntity();
				e.c_no= rs.getInt("c_no");
e.send_u= rs.getInt("send_u");
e.get_u= rs.getInt("get_u");
e.c_type= rs.getInt("c_type");
e.c_ref= rs.getInt("c_ref");
e.c_text= rs.getString("c_text");
e.c_date= rs.getTimestamp("c_date").toLocalDateTime();
				cache.put(e.c_no, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<chatroomEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<chatroomEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<chatroomEntity> findBy(Predicate<chatroomEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<chatroomEntity> findFirst(Predicate<chatroomEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (c_no == null)
				? "insert into chatroom (send_u,get_u,c_type,c_ref,c_text,c_date) values (?,?,?,?,?,?)"
				: "update chatroom set send_u = ?,get_u = ?,c_type = ?,c_ref = ?,c_text = ?,c_date = ? where c_no = ?";
		Object[] values = (c_no == null)
			? new Object[] {send_u,get_u,c_type,c_ref,c_text,c_date}
			: new Object[] {send_u,get_u,c_type,c_ref,c_text,c_date, c_no};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(c_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); c_no = rs.getInt(1);}
			cache.put(c_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM chatroom WHERE c_no =?", c_no)) {
            stmt.executeUpdate();
            cache.remove(c_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
