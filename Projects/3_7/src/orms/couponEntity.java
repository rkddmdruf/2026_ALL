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


public class couponEntity {
	public Integer cpno;
public Integer reno;
public Integer uno;
public LocalDate cpdate;
	
	public static final Map<Integer, couponEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from coupon");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new couponEntity();
				e.cpno= rs.getInt("cpno");
e.reno= rs.getInt("reno");
e.uno= rs.getInt("uno");
e.cpdate= rs.getDate("cpdate").toLocalDate();
				cache.put(e.cpno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<couponEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<couponEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<couponEntity> findBy(Predicate<couponEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<couponEntity> findFirst(Predicate<couponEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (cpno == null)
				? "insert into coupon (reno,uno,cpdate) values (?,?,?)"
				: "update coupon set reno = ?,uno = ?,cpdate = ? where cpno = ?";
		Object[] values = (cpno == null)
			? new Object[] {reno,uno,cpdate}
			: new Object[] {reno,uno,cpdate, cpno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(cpno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); cpno = rs.getInt(1);}
			cache.put(cpno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM coupon WHERE cpno =?", cpno)) {
            stmt.executeUpdate();
            cache.remove(cpno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
