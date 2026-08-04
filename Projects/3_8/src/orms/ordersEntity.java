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
public Integer sprice;
public Integer cprice;
public Integer price;
public Integer mprice;
public LocalDate opening_date;
public Integer uno;
public Integer pno;
public Integer rno;
	
	public static final Map<Integer, ordersEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from orders");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new ordersEntity();
				e.ono= rs.getInt("ono");
e.sprice= rs.getInt("sprice");
e.cprice= rs.getInt("cprice");
e.price= rs.getInt("price");
e.mprice= rs.getInt("mprice");
e.opening_date= rs.getDate("opening_date").toLocalDate();
e.uno= rs.getInt("uno");
e.pno= rs.getInt("pno");
e.rno= rs.getInt("rno");
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
				? "insert into orders (sprice,cprice,price,mprice,opening_date,uno,pno,rno) values (?,?,?,?,?,?,?,?)"
				: "update orders set sprice = ?,cprice = ?,price = ?,mprice = ?,opening_date = ?,uno = ?,pno = ?,rno = ? where ono = ?";
		Object[] values = (ono == null)
			? new Object[] {sprice,cprice,price,mprice,opening_date,uno,pno,rno}
			: new Object[] {sprice,cprice,price,mprice,opening_date,uno,pno,rno, ono};
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
