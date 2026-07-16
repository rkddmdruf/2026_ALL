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


public class reservationEntity {
	public Integer rno;
public Integer start_sno;
public Integer end_sno;
public LocalDate rdate;
public Integer uno;
	
	public static final Map<Integer, reservationEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from reservation");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new reservationEntity();
				e.rno= rs.getInt("rno");
e.start_sno= rs.getInt("start_sno");
e.end_sno= rs.getInt("end_sno");
e.rdate= rs.getDate("rdate").toLocalDate();
e.uno= rs.getInt("uno");
				cache.put(e.rno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<reservationEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<reservationEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<reservationEntity> findBy(Predicate<reservationEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<reservationEntity> findFirst(Predicate<reservationEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (rno == null)
				? "insert into reservation (start_sno,end_sno,rdate,uno) values (?,?,?,?)"
				: "update reservation set start_sno = ?,end_sno = ?,rdate = ?,uno = ? where rno = ?";
		Object[] values = (rno == null)
			? new Object[] {start_sno,end_sno,rdate,uno}
			: new Object[] {start_sno,end_sno,rdate,uno, rno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(rno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); rno = rs.getInt(1);}
			cache.put(rno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM reservation WHERE rno =?", rno)) {
            stmt.executeUpdate();
            cache.remove(rno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
