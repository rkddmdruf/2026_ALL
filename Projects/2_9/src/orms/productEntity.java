package orms;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from product"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new productEntity();
				e.pno = rs.getInt("pno");
				e.pname = rs.getString("pname");
				e.description = rs.getString("description");
				e.price = rs.getInt("price");
				e.sno = rs.getInt("sno");
				e.cno = rs.getInt("cno");
				ByteArrayInputStream bis = new ByteArrayInputStream(rs.getBytes("img"));
				e.img = ImageIO.read(bis);
				cache.put(e.pno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double review() {
		return reviewEntity.findBy(e -> orderEntity.findById(e.ono).get().pno.equals(pno)).stream().mapToInt(e -> e.star).average().getAsDouble();
	}
	public static Optional<productEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<productEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<productEntity> findBy(Predicate<productEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<productEntity> findFirst(Predicate<productEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (pno == null) ? "insert into product (pname,description,price,sno,cno,img) values (?,?,?,?,?,?)"
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
		try (var stmt = DBManager.execute("DELETE FROM product WHERE pno =?", pno)) {
			stmt.executeUpdate();
			cache.remove(pno);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
