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


public class detailEntity {
	public Integer dno;
public String dname;
public Integer cno;
	
	public static final Map<Integer, detailEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from detail");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new detailEntity();
				e.dno= rs.getInt("dno");
e.dname= rs.getString("dname");
e.cno= rs.getInt("cno");
				cache.put(e.dno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<detailEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<detailEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<detailEntity> findBy(Predicate<detailEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<detailEntity> findFirst(Predicate<detailEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (dno == null)
				? "insert into detail (dname,cno) values (?,?)"
				: "update detail set dname = ?,cno = ? where dno = ?";
		Object[] values = (dno == null)
			? new Object[] {dname,cno}
			: new Object[] {dname,cno, dno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(dno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); dno = rs.getInt(1);}
			cache.put(dno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM detail WHERE dno =?", dno)) {
            stmt.executeUpdate();
            cache.remove(dno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
