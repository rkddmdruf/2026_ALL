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


public class areaEntity {
	public Integer ano;
public String aname;
public Integer ax;
public Integer ay;
	
	public static final Map<Integer, areaEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from area");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new areaEntity();
				e.ano= rs.getInt("ano");
e.aname= rs.getString("aname");
e.ax= rs.getInt("ax");
e.ay= rs.getInt("ay");
				cache.put(e.ano, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<areaEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<areaEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<areaEntity> findBy(Predicate<areaEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<areaEntity> findFirst(Predicate<areaEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (ano == null)
				? "insert into area (aname,ax,ay) values (?,?,?)"
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
        try (var stmt = DBManager.execute("DELETE FROM area WHERE ano =?", ano)) {
            stmt.executeUpdate();
            cache.remove(ano);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
