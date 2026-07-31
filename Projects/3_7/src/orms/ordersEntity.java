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


public class ordersEntity {
	public Integer ono;
public Integer pno;
public Integer uno;
public Integer ocount;
public LocalDate odate;
public Integer oprice;
public Integer type;
	
	public static final Map<Integer, ordersEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from orders");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new ordersEntity();
				e.ono= rs.getInt("ono");
e.pno= rs.getInt("pno");
e.uno= rs.getInt("uno");
e.ocount= rs.getInt("ocount");
e.odate= rs.getDate("odate").toLocalDate();
e.oprice= rs.getInt("oprice");
e.type= rs.getInt("type");
				cache.put(e.ono, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<ordersEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<ordersEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<ordersEntity> findBy(Predicate<ordersEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<ordersEntity> findFirst(Predicate<ordersEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (ono == null)
				? "insert into orders (pno,uno,ocount,odate,oprice,type) values (?,?,?,?,?,?)"
				: "update orders set pno = ?,uno = ?,ocount = ?,odate = ?,oprice = ?,type = ? where ono = ?";
		Object[] values = (ono == null)
			? new Object[] {pno,uno,ocount,odate,oprice,type}
			: new Object[] {pno,uno,ocount,odate,oprice,type, ono};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(ono == null) {var rs = stmt.getGeneratedKeys(); rs.next(); ono = rs.getInt(1);}
			cache.put(ono, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM orders WHERE ono =?", ono)) {
            stmt.executeUpdate();
            cache.remove(ono);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
