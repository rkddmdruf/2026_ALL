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


public class productEntity {
	public Integer pno;
public Integer dno;
public String pname;
public String pcontent;
public String pcompany;
public Integer pprice;
public Integer pcount;
	
	public static final Map<Integer, productEntity> cache = new HashMap<>();
	static {reload();};
	
	public static void reload(){
		cache.clear();
		try(var stmt = DBManager.execute("SELECT * from product");
				var rs = stmt.executeQuery()){
			while(rs.next()) {
				var e = new productEntity();
				e.pno= rs.getInt("pno");
e.dno= rs.getInt("dno");
e.pname= rs.getString("pname");
e.pcontent= rs.getString("pcontent");
e.pcompany= rs.getString("pcompany");
e.pprice= rs.getInt("pprice");
e.pcount= rs.getInt("pcount");
				cache.put(e.pno, e);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static Optional<productEntity> findById(int id){
		return Optional.ofNullable(cache.get(id));
	}
	
	public static List<productEntity> findAll(){
		return new ArrayList<>(cache.values());
	}
	
	public static List<productEntity> findBy(Predicate<productEntity> filter){
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}
	
	public static Optional<productEntity> findFirst(Predicate<productEntity> filter){
		return findAll().stream().filter(filter).findFirst();
	}
	
	public void save() {
		String sql = (pno == null)
				? "insert into product (dno,pname,pcontent,pcompany,pprice,pcount) values (?,?,?,?,?,?)"
				: "update product set dno = ?,pname = ?,pcontent = ?,pcompany = ?,pprice = ?,pcount = ? where pno = ?";
		Object[] values = (pno == null)
			? new Object[] {dno,pname,pcontent,pcompany,pprice,pcount}
			: new Object[] {dno,pname,pcontent,pcompany,pprice,pcount, pno};
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if(pno == null) {var rs = stmt.getGeneratedKeys(); rs.next(); pno = rs.getInt(1);}
			cache.put(pno, this);
		}catch(Exception e) { e.printStackTrace();}
	}
    public void delete() {
        try (var stmt = DBManager.execute("DELETE FROM product WHERE pno =?", pno)) {
            stmt.executeUpdate();
            cache.remove(pno);
        } catch (Exception exception) { exception.printStackTrace(); }
    }
}
