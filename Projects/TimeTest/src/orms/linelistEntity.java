package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class linelistEntity{
	public Integer lno;
public Integer u;
public Integer v;
	
	public static final Map<Integer, linelistEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from linelist"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new linelistEntity();
				e.lno= re.getInt("lno");
e.u= re.getInt("u");
e.v= re.getInt("v");
				cache.put(e.lno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<linelistEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<linelistEntity> findBy(Predicate<linelistEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<linelistEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<linelistEntity> findByFrist(Predicate<linelistEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (lno == null)
				? "insert into linelist set u = ?,v = ?)"
				: "update linelist set u = ?,v = ? where lno = ?";
		Object[] values = (lno == null)
			? new Object[] {u,v}
			: new Object[] {u,v, lno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(lno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); lno = rs.getInt(1);}
			cache.put(lno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
	
	public void delete() {
		try (var ps = DBManager.execute("delete from linelist where lno = ?", lno)){
			ps.executeUpdate();
			cache.remove(lno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}