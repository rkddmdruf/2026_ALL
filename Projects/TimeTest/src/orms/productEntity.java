package orms;

import java.util.*;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.time.*;
import java.util.function.*;
import java.util.stream.*;

import javax.imageio.ImageIO;

public class productEntity {
	public Integer pno;
	public String pname;
	public String description;
	public Integer price;
	public Integer sno;
	public Integer cno;
	public Image img;

	public static final Map<Integer, productEntity> cache = new HashMap<>();
	static {
		reload();
	}

	private static void reload() {
		cache.clear();
		try (var rs = DBManager.execute("select * from product"); var re = rs.executeQuery()) {
			while (re.next()) {
				var e = new productEntity();
				e.pno = re.getInt("pno");
				e.pname = re.getString("pname");
				e.description = re.getString("description");
				e.price = re.getInt("price");
				e.sno = re.getInt("sno");
				e.cno = re.getInt("cno");
				ByteArrayInputStream bis = new ByteArrayInputStream(re.getBytes("img"));
				e.img = ImageIO.read(bis);
				cache.put(e.pno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<productEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<productEntity> findBy(Predicate<productEntity> pre) {
		return findAll().stream().filter(pre).collect(Collectors.toList());
	}

	public static Optional<productEntity> findById(int i) {
		return Optional.ofNullable(cache.get(i));
	}

	public static Optional<productEntity> findByFrist(Predicate<productEntity> f) {
		return findAll().stream().filter(f).findFirst();
	}
	
	public Double avgStar() {
		return orderEntity.findBy(o -> o.pno.equals(this.pno)).stream()
			.filter(o -> reviewEntity.exist(r -> r.ono == o.ono))
			.mapToInt(o -> reviewEntity.findByFrist(r -> r.ono == o.ono).get().star)
			.average().orElse(0);
	}

	public void save() {
		String sql = (pno == null)
				? "insert into product set pname = ?,description = ?,price = ?,sno = ?,cno = ?,img = ?)"
				: "update product set pname = ?,description = ?,price = ?,sno = ?,cno = ?,img = ? where pno = ?";
		Object[] values = (pno == null) ? new Object[] { pname, description, price, sno, cno, img }
				: new Object[] { pname, description, price, sno, cno, img, pno };
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (pno == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				pno = rs.getInt(1);
			}
			cache.put(pno, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try (var ps = DBManager.execute("delete from product where pno = ?", pno)) {
			ps.executeUpdate();
			cache.remove(pno);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}