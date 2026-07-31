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


public class reviewEntity {
	public Integer rno;
public Integer uno;
public Integer pno;
public String rtitle;
public String rcontent;
public Double rstar;
	
	public static final Map<Integer, reviewEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from review");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new reviewEntity();
				e.rno= rs.getInt("rno");
e.uno= rs.getInt("uno");
e.pno= rs.getInt("pno");
e.rtitle= rs.getString("rtitle");
e.rcontent= rs.getString("rcontent");
e.rstar= rs.getDouble("rstar");
				cache.put(e.rno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<reviewEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<reviewEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<reviewEntity> findBy(Predicate<reviewEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<reviewEntity> findFirst(Predicate<reviewEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (rno == null)
				? "insert into review (uno,pno,rtitle,rcontent,rstar) values (?,?,?,?,?)"
				: "update review set uno = ?,pno = ?,rtitle = ?,rcontent = ?,rstar = ? where rno = ?";
		Object[] values = (rno == null)
			? new Object[] {uno,pno,rtitle,rcontent,rstar}
			: new Object[] {uno,pno,rtitle,rcontent,rstar, rno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(rno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); rno = rs.getInt(1);}
			cache.put(rno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM review WHERE rno =?", rno)) {
            stmt.executeUpdate();
            cache.remove(rno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
