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
	public Integer o_no;
public Integer u_no;
public Integer a_no;
public Integer s_no;
public String o_type;
public String o_order_type;
public Integer o_quantity;
public Double o_price;
public Double o_fee;
public LocalDateTime o_date;
public String o_status;
	
	public static final Map<Integer, ordersEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from orders");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new ordersEntity();
				e.o_no= rs.getInt("o_no");
e.u_no= rs.getInt("u_no");
e.a_no= rs.getInt("a_no");
e.s_no= rs.getInt("s_no");
e.o_type= rs.getString("o_type");
e.o_order_type= rs.getString("o_order_type");
e.o_quantity= rs.getInt("o_quantity");
e.o_price= rs.getDouble("o_price");
e.o_fee= rs.getDouble("o_fee");
e.o_date= rs.getTimestamp("o_date").toLocalDateTime();
e.o_status= rs.getString("o_status");
				cache.put(e.o_no, e);
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
		String sql = (o_no == null)
				? "insert into orders (u_no,a_no,s_no,o_type,o_order_type,o_quantity,o_price,o_fee,o_date,o_status) values (?,?,?,?,?,?,?,?,?,?)"
				: "update orders set u_no = ?,a_no = ?,s_no = ?,o_type = ?,o_order_type = ?,o_quantity = ?,o_price = ?,o_fee = ?,o_date = ?,o_status = ? where o_no = ?";
		Object[] values = (o_no == null)
			? new Object[] {u_no,a_no,s_no,o_type,o_order_type,o_quantity,o_price,o_fee,o_date,o_status}
			: new Object[] {u_no,a_no,s_no,o_type,o_order_type,o_quantity,o_price,o_fee,o_date,o_status};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(o_no == null) {var rs = stmt.getGeneratedKeys(); rs.next(); o_no = rs.getInt(1);}
			cache.put(o_no, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM orders WHERE o_no =?", o_no)) {
            stmt.executeUpdate();
            cache.remove(o_no);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
