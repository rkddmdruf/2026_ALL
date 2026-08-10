package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class userEntity{
	public Integer uno;
public String id;
public String pw;
public String uname;
public Integer point;
public Integer sno;
public Integer chance;
	
	public static final Map<Integer, userEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from user"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new userEntity();
				e.uno= re.getInt("uno");
e.id= re.getString("id");
e.pw= re.getString("pw");
e.uname= re.getString("uname");
e.point= re.getInt("point");
e.sno= re.getInt("sno");
e.chance= re.getInt("chance");
				cache.put(e.uno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<userEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<userEntity> findBy(Predicate<userEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<userEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<userEntity> findByFrist(Predicate<userEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (uno == null)
				? "insert into user set id = ?,pw = ?,uname = ?,point = ?,sno = ?,chance = ?)"
				: "update user set id = ?,pw = ?,uname = ?,point = ?,sno = ?,chance = ? where uno = ?";
		Object[] values = (uno == null)
			? new Object[] {id,pw,uname,point,sno,chance}
			: new Object[] {id,pw,uname,point,sno,chance, uno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(uno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); uno = rs.getInt(1);}
			cache.put(uno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
	
	public void delete() {
		try (var ps = DBManager.execute("delete from user where uno = ?", uno)){
			ps.executeUpdate();
			cache.remove(uno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}