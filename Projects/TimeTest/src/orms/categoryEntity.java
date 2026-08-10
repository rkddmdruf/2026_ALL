package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class categoryEntity{
	public Integer cno;
public String cname;
	
	public static final Map<Integer, categoryEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from category"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new categoryEntity();
				e.cno= re.getInt("cno");
e.cname= re.getString("cname");
				cache.put(e.cno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<categoryEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<categoryEntity> findBy(Predicate<categoryEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<categoryEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<categoryEntity> findByFrist(Predicate<categoryEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (cno == null)
				? "insert into category set cname = ?)"
				: "update category set cname = ? where cno = ?";
		Object[] values = (cno == null)
			? new Object[] {cname}
			: new Object[] {cname, cno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(cno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); cno = rs.getInt(1);}
			cache.put(cno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
	
	public void delete() {
		try (var ps = DBManager.execute("delete from category where cno = ?", cno)){
			ps.executeUpdate();
			cache.remove(cno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}