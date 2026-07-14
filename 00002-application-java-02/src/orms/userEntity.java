package orms;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import utils.Image;

public class userEntity extends Account {

	public Integer u_no;
	public String u_name;
	public String u_id;
	public String u_pw;
	public LocalDate u_birth;
	public String u_alba;
	public Integer u_money;
	public Integer l_no;
	public Integer x;
	public Integer y;
	public String u_phone;
	public String u_email;
	public String u_resume;
	public java.awt.Image u_image;

	public static final Map<Integer, userEntity> cache = new HashMap<>();
	static {
		reload();
	}

	public static void reload() {
		cache.clear();
		try (var stmt = DBManager.execute("SELECT * FROM user"); var rs = stmt.executeQuery()) {
			while (rs.next()) {
				var e = new userEntity();
				e.u_no = rs.getInt("u_no");
				e.u_name = rs.getString("u_name");
				e.u_id = rs.getString("u_id");
				e.u_pw = rs.getString("u_pw");
				e.u_birth = rs.getDate("u_birth").toLocalDate();
				e.u_alba = rs.getString("u_alba");
				e.u_money = rs.getInt("u_money");
				e.l_no = rs.getInt("l_no");
				e.x = rs.getInt("x");
				e.y = rs.getInt("y");
				e.u_phone = rs.getString("u_phone");
				e.u_email = rs.getString("u_email");
				e.u_resume = rs.getString("u_resume");
				byte[] bs = rs.getBytes("u_image");
				ByteArrayInputStream bis = new ByteArrayInputStream(bs);
				if (bis != null)
					e.u_image = ImageIO.read(bis);
				cache.put(e.u_no, e);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
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

	public userEntity() {
	}

	public userEntity(Integer u_no, String u_name, String u_id, String u_pw, LocalDate u_birth, String u_alba,
			Integer u_money, Integer l_no, Integer x, Integer y, String u_phone, String u_email, String u_resume,
			java.awt.Image u_image) {
		this.u_no = u_no;
		this.u_name = u_name;
		this.u_id = u_id;
		this.u_pw = u_pw;
		this.u_birth = u_birth;
		this.u_alba = u_alba;
		this.u_money = u_money;
		this.l_no = l_no;
		this.x = x;
		this.y = y;
		this.u_phone = u_phone;
		this.u_email = u_email;
		this.u_resume = u_resume;
		this.u_image = u_image;
	}

	public userEntity copyUser() {
		return new userEntity( u_no,  u_name,  u_id,  u_pw,  u_birth,  u_alba,
				 u_money,  l_no,  x,  y,  u_phone,  u_email,  u_resume,
				 u_image);
	}
	public void save() {
		String sql = (u_no == null)
				? "INSERT INTO user (u_name,u_id,u_pw,u_birth,u_alba,u_money,l_no,x,y,u_phone,u_email,u_resume,u_image) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)"
				: "UPDATE user SET u_name = ?,u_id = ?,u_pw = ?,u_birth = ?,u_alba = ?,u_money = ?,l_no = ?,x = ?,y = ?,u_phone = ?,u_email = ?,u_resume = ?,u_image = ? WHERE u_no = ?";
		Object[] values = (u_no == null)
				? new Object[] { u_name, u_id, u_pw, u_birth, u_alba, u_money, l_no, x, y, u_phone, u_email, u_resume,
						u_image }
				: new Object[] { u_name, u_id, u_pw, u_birth, u_alba, u_money, l_no, x, y, u_phone, u_email, u_resume,
						u_image, u_no };
		
		try (var stmt = DBManager.execute(sql, values)) {
			stmt.executeUpdate();
			if (u_no == null) {
				var rs = stmt.getGeneratedKeys();
				rs.next();
				u_no = rs.getInt(1);
			}
			cache.put(u_no, this);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void delete() {
		try (var stmt = DBManager.execute("DELETE FROM user WHERE u_no =?", u_no)) {
			stmt.executeUpdate();
			cache.remove(u_no);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return u_name + "님 환영합니다.";
	}
}
