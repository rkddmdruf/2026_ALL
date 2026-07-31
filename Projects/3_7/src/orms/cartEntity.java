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


public class cartEntity {
	public Integer ctno;
public Integer pno;
public Integer uno;
public Integer ctcount;
	
	public static final Map<Integer, cartEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from cart");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new cartEntity();
				e.ctno= rs.getInt("ctno");
e.pno= rs.getInt("pno");
e.uno= rs.getInt("uno");
e.ctcount= rs.getInt("ctcount");
				cache.put(e.ctno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<cartEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<cartEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<cartEntity> findBy(Predicate<cartEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<cartEntity> findFirst(Predicate<cartEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (ctno == null)
				? "insert into cart (pno,uno,ctcount) values (?,?,?)"
				: "update cart set pno = ?,uno = ?,ctcount = ? where ctno = ?";
		Object[] values = (ctno == null)
			? new Object[] {pno,uno,ctcount}
			: new Object[] {pno,uno,ctcount, ctno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(ctno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); ctno = rs.getInt(1);}
			cache.put(ctno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM cart WHERE ctno =?", ctno)) {
            stmt.executeUpdate();
            cache.remove(ctno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
