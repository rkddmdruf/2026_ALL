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

public class userEntity {
	public Integer uno;
	public String uname;
	public String uid;
	public String upw;
	public String unick;
	public String uemail;
	public String unumber;
	public Integer uprice;

	public static final Map<Integer, userEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from user"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new userEntity();
				e.uno = rs.getInt("uno");
				e.uname = rs.getString("uname");
				e.uid = rs.getString("uid");
				e.upw = rs.getString("upw");
				e.unick = rs.getString("unick");
				e.uemail = rs.getString("uemail");
				e.unumber = rs.getString("unumber");
				e.uprice = rs.getInt("uprice");
				cache.put(e.uno, e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Optional<userEntity> findById(int id) {
		return Optional.ofNullable(cache.get(id));
	}

	public static List<userEntity> findAll() {
		return new ArrayList<>(cache.values());
	}

	public static List<userEntity> findBy(Predicate<userEntity> filter) {
		return findAll().stream().filter(filter).collect(Collectors.toList());
	}

	public static Optional<userEntity> findFirst(Predicate<userEntity> filter) {
		return findAll().stream().filter(filter).findFirst();
	}

	public void save() {
		String sql = (uno == null)
				? "insert into user (uname,uid,upw,unick,uemail,unumber,uprice) values (?,?,?,?,?,?,?)"
				: "update user set uname = ?,uid = ?,upw = ?,unick = ?,uemail = ?,unumber = ?,uprice = ? where uno = ?";
		Object[] values = (uno == null) ? new Object[] { uname, uid, upw, unick, uemail, unumber, uprice }
				: new Object[] { uname, uid, upw, unick, uemail, unumber, uprice, uno };
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (uno == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				uno = rs.getInt(1);
			}
			cache.put(uno, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM user WHERE uno =?", uno)) {
			stmt.executeUpdate();
			cache.remove(uno);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public boolean isAdmin() {
		return uid.equals("admin");
	}
}
