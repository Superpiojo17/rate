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

	private List<User> mapRows(ResultSet rs) {
		List<User> users = new ArrayList<User>();
		try {
			while (rs.next()) {
				// Create user object and pass to array
				User tmp = new User();
				tmp.setId(rs.getLong("user_id"));
				tmp.setEmail(rs.getString("email"));
				users.add(tmp);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	public User save(User entity) {
		return null;
	}

	public List<User> all() {
		final String SELECT = "SELECT * FROM " + TABLE_NAME;
		List<User> users = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			users = mapRows(ps.executeQuery(SELECT));
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
