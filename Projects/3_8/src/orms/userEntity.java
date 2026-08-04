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


public class userEntity {
	public Integer uno;
public String name;
public String id;
public String pw;
public LocalDate birth;
public Integer price;
	
	public static final Map<Integer, userEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from user");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new userEntity();
				e.uno= rs.getInt("uno");
e.name= rs.getString("name");
e.id= rs.getString("id");
e.pw= rs.getString("pw");
e.birth= rs.getDate("birth").toLocalDate();
e.price= rs.getInt("price");
				cache.put(e.uno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<userEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<userEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<userEntity> findBy(Predicate<userEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<userEntity> findFirst(Predicate<userEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (uno == null)
				? "insert into user (name,id,pw,birth,price) values (?,?,?,?,?)"
				: "update user set name = ?,id = ?,pw = ?,birth = ?,price = ? where uno = ?";
		Object[] values = (uno == null)
			? new Object[] {name,id,pw,birth,price}
			: new Object[] {name,id,pw,birth,price, uno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(uno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); uno = rs.getInt(1);}
			cache.put(uno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM user WHERE uno =?", uno)) {
            stmt.executeUpdate();
            cache.remove(uno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
