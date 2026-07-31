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


public class likesEntity {
	public Integer lno;
public Integer pno;
public Integer uno;
public LocalDate ldate;
	
	public static final Map<Integer, likesEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from likes");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new likesEntity();
				e.lno= rs.getInt("lno");
e.pno= rs.getInt("pno");
e.uno= rs.getInt("uno");
e.ldate= rs.getDate("ldate").toLocalDate();
				cache.put(e.lno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<likesEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<likesEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<likesEntity> findBy(Predicate<likesEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<likesEntity> findFirst(Predicate<likesEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (lno == null)
				? "insert into likes (pno,uno,ldate) values (?,?,?)"
				: "update likes set pno = ?,uno = ?,ldate = ? where lno = ?";
		Object[] values = (lno == null)
			? new Object[] {pno,uno,ldate}
			: new Object[] {pno,uno,ldate, lno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(lno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); lno = rs.getInt(1);}
			cache.put(lno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM likes WHERE lno =?", lno)) {
            stmt.executeUpdate();
            cache.remove(lno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
