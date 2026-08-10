package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class sub_areaEntity{
	public Integer sno;
public String sname;
public Integer sx;
public Integer sy;
public Integer ano;
	
	public static final Map<Integer, sub_areaEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from sub_area"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new sub_areaEntity();
				e.sno= re.getInt("sno");
e.sname= re.getString("sname");
e.sx= re.getInt("sx");
e.sy= re.getInt("sy");
e.ano= re.getInt("ano");
				cache.put(e.sno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<sub_areaEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<sub_areaEntity> findBy(Predicate<sub_areaEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<sub_areaEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<sub_areaEntity> findById(Predicate<sub_areaEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (sno == null)
				? "insert into sub_area set sname = ?,sx = ?,sy = ?,ano = ?)"
				: "update sub_area set sname = ?,sx = ?,sy = ?,ano = ? where sno = ?";
		Object[] values = (sno == null)
			? new Object[] {sname,sx,sy,ano}
			: new Object[] {sname,sx,sy,ano, sno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(sno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); sno = rs.getInt(1);}
			cache.put(sno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
	
	public void delete() {
		try (var ps = DBManager.execute("delete from sub_area where sno = ?", sno)){
			ps.executeUpdate();
			cache.remove(sno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}