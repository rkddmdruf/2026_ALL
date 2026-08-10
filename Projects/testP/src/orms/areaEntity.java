package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class areaEntity{
	public Integer ano;
public String aname;
public Integer ax;
public Integer ay;
	
	public static final Map<Integer, areaEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from area"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new areaEntity();
				e.ano= re.getInt("ano");
e.aname= re.getString("aname");
e.ax= re.getInt("ax");
e.ay= re.getInt("ay");
				cache.put(e.ano, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<areaEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<areaEntity> findBy(Predicate<areaEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<areaEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<areaEntity> findById(Predicate<areaEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (ano == null)
				? "insert into area set aname = ?,ax = ?,ay = ?)"
				: "update area set aname = ?,ax = ?,ay = ? where ano = ?";
		Object[] values = (ano == null)
			? new Object[] {aname,ax,ay}
			: new Object[] {aname,ax,ay, ano};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(ano == null) {var rs = stmt.getGeneratedKeys(); rs.next(); ano = rs.getInt(1);}
			cache.put(ano, this);
		}catch(Exception e) { e.printStackTrace();}
	}
	
	public void delete() {
		try (var ps = DBManager.execute("delete from area where ano = ?", ano)){
			ps.executeUpdate();
			cache.remove(ano);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}