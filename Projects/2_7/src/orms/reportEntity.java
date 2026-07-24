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


public class reportEntity {
	public Integer rno;
public Integer cno;
public Integer ono;
public Integer dno;
public Integer uno;
public LocalDate date;
public String context_1;
public String context_2;
	
	public static final Map<Integer, reportEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from report");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new reportEntity();
				e.rno= rs.getInt("rno");
e.cno= rs.getInt("cno");
e.ono= rs.getInt("ono");
e.dno= rs.getInt("dno");
e.uno= rs.getInt("uno");
e.date= rs.getDate("date").toLocalDate();
e.context_1= rs.getString("context_1");
e.context_2= rs.getString("context_2");
				cache.put(e.rno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<reportEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<reportEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<reportEntity> findBy(Predicate<reportEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<reportEntity> findFirst(Predicate<reportEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (rno == null)
				? "insert into report (cno,ono,dno,uno,date,context_1,context_2) values (?,?,?,?,?,?,?)"
				: "update report set cno = ?,ono = ?,dno = ?,uno = ?,date = ?,context_1 = ?,context_2 = ? where rno = ?";
		Object[] values = (rno == null)
			? new Object[] {cno,ono,dno,uno,date,context_1,context_2}
			: new Object[] {cno,ono,dno,uno,date,context_1,context_2, rno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(rno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); rno = rs.getInt(1);}
			cache.put(rno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM report WHERE rno =?", rno)) {
            stmt.executeUpdate();
            cache.remove(rno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
