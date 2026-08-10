package orms;

import java.util.*;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;
public class reviewEntity{
	public Integer rno;
public Integer ono;
public String review;
public Integer star;
	
	public static final Map<Integer, reviewEntity> cache = new HashMap<>();
	static {reload();}
	
	private static void reload(){
		cache.clear();
		try (var rs = DBManager.execute("select * from review"); var re = rs.executeQuery()){
			while(re.next()) {
				var e = new reviewEntity();
				e.rno= re.getInt("rno");
e.ono= re.getInt("ono");
e.review= re.getString("review");
e.star= re.getInt("star");
				cache.put(e.rno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<reviewEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<reviewEntity> findBy(Predicate<reviewEntity> pre){
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}
	public static Optional<reviewEntity> findById(int i){
		return Optional.ofNullable(cache.get(i));
	}
	public static Optional<reviewEntity> findById(Predicate<reviewEntity> f){
		return findAll().stream().filter(f).findFirst();
	}
	
	public void save() {
		String sql = (rno == null)
				? "insert into review set ono = ?,review = ?,star = ?)"
				: "update review set ono = ?,review = ?,star = ? where rno = ?";
		Object[] values = (rno == null)
			? new Object[] {ono,review,star}
			: new Object[] {ono,review,star, rno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(rno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); rno = rs.getInt(1);}
			cache.put(rno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
	
	public void delete() {
		try (var ps = DBManager.execute("delete from review where rno = ?", rno)){
			ps.executeUpdate();
			cache.remove(rno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}