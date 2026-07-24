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


public class doctorEntity {
	public Integer dno;
public String dname;
public String id;
public String pw;
public Integer day_off;
public Integer cno;
public Integer lno;
	
	public static final Map<Integer, doctorEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from doctor");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new doctorEntity();
				e.dno= rs.getInt("dno");
e.dname= rs.getString("dname");
e.id= rs.getString("id");
e.pw= rs.getString("pw");
e.day_off= rs.getInt("day_off");
e.cno= rs.getInt("cno");
e.lno= rs.getInt("lno");
				cache.put(e.dno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<doctorEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<doctorEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<doctorEntity> findBy(Predicate<doctorEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<doctorEntity> findFirst(Predicate<doctorEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (dno == null)
				? "insert into doctor (dname,id,pw,day_off,cno,lno) values (?,?,?,?,?,?)"
				: "update doctor set dname = ?,id = ?,pw = ?,day_off = ?,cno = ?,lno = ? where dno = ?";
		Object[] values = (dno == null)
			? new Object[] {dname,id,pw,day_off,cno,lno}
			: new Object[] {dname,id,pw,day_off,cno,lno, dno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(dno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); dno = rs.getInt(1);}
			cache.put(dno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM doctor WHERE dno =?", dno)) {
            stmt.executeUpdate();
            cache.remove(dno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
