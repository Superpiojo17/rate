package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.User;

/**
 * UserDao is a dao that will connect and provide interaction to the database
 */
public class UserDao implements Dao<User> {
	String TABLE_NAME = "users";
	Connection conn = null;

	public UserDao(Connection conn) {
		this.conn = conn;
	}

	public boolean checkEmail(String email) {
		return false;
	}

	private User mapRow(ResultSet rs) throws SQLException {

		// Create user object and pass to array
		User tmp = new User();
		tmp.setId(rs.getLong("user_id"));
		tmp.setEmail(rs.getString("email"));
		return tmp;
	}

	public User save(User user) {
		final String sql = "INSERT INTO " + TABLE_NAME
				+ "(first_name, last_name, email, encryptedPassword, role_id) VALUES(?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getFirst_name());
			ps.setString(2, user.getLast_name());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getEncryptedPassword());
			ps.setInt(5, user.getRole());
			ps.executeUpdate();
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public User findByEmail(String email) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + TABLE_NAME + "WHERE email = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setString(1, email);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return mapRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return null;

	}

	/**
	 * 
	 * @return all users from the database.
	 */
	// public ArrayList<User> getAll() {
	//
	// List<User> users = new ArrayList<User>();
	// String sql = "SELECT * FROM user";
	//
	// try {
	// users = jdbcTemplate.query(sql, mapRows());
	//
	// return (ArrayList<User>) users;
	// } catch (EmptyResultDataAccessException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	public List<User> all() {
		final String SELECT = "SELECT * FROM " + TABLE_NAME;
		List<User> users = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			users = new ArrayList<User>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					users.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return users;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public User find(Long id) {
		return null;
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
