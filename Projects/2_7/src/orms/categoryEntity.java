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


public class categoryEntity {
	public Integer cno;
public String cname;
public Integer price;
	
	public static final Map<Integer, categoryEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from category");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new categoryEntity();
				e.cno= rs.getInt("cno");
e.cname= rs.getString("cname");
e.price= rs.getInt("price");
				cache.put(e.cno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<categoryEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<categoryEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<categoryEntity> findBy(Predicate<categoryEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<categoryEntity> findFirst(Predicate<categoryEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (cno == null)
				? "insert into category (cname,price) values (?,?)"
				: "update category set cname = ?,price = ? where cno = ?";
		Object[] values = (cno == null)
			? new Object[] {cname,price}
			: new Object[] {cname,price, cno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(cno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); cno = rs.getInt(1);}
			cache.put(cno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM category WHERE cno =?", cno)) {
            stmt.executeUpdate();
            cache.remove(cno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
