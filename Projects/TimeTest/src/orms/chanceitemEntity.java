package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class chanceitemEntity{
	public Integer cno;
public String ciname;
public Double chance;
	
	public static final Map<Integer, chanceitemEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from chanceitem"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new chanceitemEntity();
				e.cno= re.getInt("cno");
e.ciname= re.getString("ciname");
e.chance= re.getDouble("chance");
				cache.put(e.cno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<chanceitemEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<chanceitemEntity> findBy(Predicate<chanceitemEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<chanceitemEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<chanceitemEntity> findByFrist(Predicate<chanceitemEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (cno == null)
				? "insert into chanceitem set ciname = ?,chance = ?)"
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
		try (var ps = DBManager.execute("delete from chanceitem where cno = ?", cno)){
			ps.executeUpdate();
			cache.remove(cno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}