package orms;

import java.time.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;


public class chanceitemEntity {
	public Integer cno;
public String ciname;
public Double chance;
	
	public static final Map<Integer, chanceitemEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from chanceitem");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new chanceitemEntity();
				e.cno= rs.getInt("cno");
e.ciname= rs.getString("ciname");
e.chance= rs.getDouble("chance");
				cache.put(e.cno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<chanceitemEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<chanceitemEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<chanceitemEntity> findBy(Predicate<chanceitemEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<chanceitemEntity> findFirst(Predicate<chanceitemEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (cno == null)
				? "insert into chanceitem (ciname,chance) values (?,?)"
				: "update chanceitem set ciname = ?,chance = ? where cno = ?";
		Object[] values = (cno == null)
			? new Object[] {ciname,chance}
			: new Object[] {ciname,chance, cno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(cno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); cno = rs.getInt(1);}
			cache.put(cno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM chanceitem WHERE cno =?", cno)) {
            stmt.executeUpdate();
            cache.remove(cno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
