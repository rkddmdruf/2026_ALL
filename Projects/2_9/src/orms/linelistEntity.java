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


public class linelistEntity {
	public Integer lno;
public Integer u;
public Integer v;
	
	public static final Map<Integer, linelistEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from linelist");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new linelistEntity();
				e.lno= rs.getInt("lno");
e.u= rs.getInt("u");
e.v= rs.getInt("v");
				cache.put(e.lno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<linelistEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<linelistEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<linelistEntity> findBy(Predicate<linelistEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<linelistEntity> findFirst(Predicate<linelistEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (lno == null)
				? "insert into linelist (u,v) values (?,?)"
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
        try (var stmt = DBManager.execute("DELETE FROM linelist WHERE lno =?", lno)) {
            stmt.executeUpdate();
            cache.remove(lno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
