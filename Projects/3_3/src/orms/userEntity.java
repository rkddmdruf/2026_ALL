package orms;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class userEntity {
	public Integer u_no;
	public String u_name;
	public String u_id;
	public String u_pw;
	public String u_nick;
	public LocalDate u_birth;
	public String u_phone;
	public String u_email;
	public String u_intro;
	public String u_follow;

	public static final Map<Integer, userEntity> cache = new HashMap<>();
	static {
		reload();
	};

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * from user"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new userEntity();
				e.u_no = rs.getInt("u_no");
				e.u_name = rs.getString("u_name");
				e.u_id = rs.getString("u_id");
				e.u_pw = rs.getString("u_pw");
				e.u_nick = rs.getString("u_nick");
				e.u_birth = rs.getDate("u_birth").toLocalDate();
				e.u_phone = rs.getString("u_phone");
				e.u_email = rs.getString("u_email");
				e.u_intro = rs.getString("u_intro");
				e.u_follow = rs.getString("u_follow");
				cache.put(e.u_no, e);
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
		String sql = (u_no == null)
				? "insert into user (u_name,u_id,u_pw,u_nick,u_birth,u_phone,u_email,u_intro,u_follow) values (?,?,?,?,?,?,?,?,?)"
				: "update user set u_name = ?,u_id = ?,u_pw = ?,u_nick = ?,u_birth = ?,u_phone = ?,u_email = ?,u_intro = ?,u_follow = ? where u_no = ?";
		Object[] values = (u_no == null)
				? new Object[] { u_name, u_id, u_pw, u_nick, u_birth, u_phone, u_email, u_intro, u_follow }
				: new Object[] { u_name, u_id, u_pw, u_nick, u_birth, u_phone, u_email, u_intro, u_follow, u_no };
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (u_no == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				u_no = rs.getInt(1);
			}
			cache.put(u_no, this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Integer> myFollow(){
		List<Integer> list = new ArrayList<>();
		userEntity.findBy(e -> !e.u_no.equals(u_no)).forEach(e -> {
			var a = Arrays.stream(e.u_follow.split(","))
					.map(c -> Integer.parseInt(c))
					.filter(c -> c.equals(u_no))
					.findFirst().orElse(null);
			if(a != null) list.add(a);
		});
		return list;
	}
	
	public List<Integer> myFollowing(){
		return Arrays.stream(u_follow.split(",")).map(e -> Integer.parseInt(e)).collect(Collectors.toList());
	}
	
	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM user WHERE u_no =?", u_no)) {
			stmt.executeUpdate();
			cache.remove(u_no);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
