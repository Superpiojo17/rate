package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.RecoveringUser;
import edu.ben.rate_review.models.User;

/**
 * UserDao is a dao that will connect and provide interaction to the database
 * 
 * @author Mike
 * @version 2-2-2017
 */
public class ProfessorsDao implements Dao<User> {
	String PROFESSORS_TABLE = "professors";
	String ACCOUNT_RECOVERY_TABLE = "account_recovery";
	Connection conn = null;

	/**
	 * UserDao connection
	 * 
	 * @param conn
	 */
	public ProfessorsDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * When searching the database, this method creates a user object to pass to
	 * methods.
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private User mapRow(ResultSet rs) throws SQLException {

		// Create user object and pass to array
		User tmp = new User();
		tmp.setId(rs.getLong("professor_id"));
		tmp.setFirst_name(rs.getString("first_name"));
		tmp.setLast_name(rs.getString("last_name"));
		tmp.setLast_name(rs.getString("image"));
		

		return tmp;
	}

	/**
	 * Method which stores a new user in the database.
	 */
	public User save(User professor) {
		final String sql = "INSERT INTO " + PROFESSORS_TABLE
				+ " (first_name, last_name) VALUES(?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, professor.getFirst_name());
			ps.setString(2, professor.getLast_name());
			
			ps.executeUpdate();
			return professor;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Creates and returns an object for a recovering user
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private RecoveringUser recoveryMapRow(ResultSet rs) throws SQLException {

		// Create user object and pass to array
		RecoveringUser tmp = new RecoveringUser();
		tmp.setEmail(rs.getString("email"));
		tmp.setTempPass(rs.getString("temp_password"));
		return tmp;
	}

	/**
	 * Searched by email in the account recovery table
	 * 
	 * @param email
	 * @return
	 */
	public RecoveringUser recoveryFindByEmail(String email) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + ACCOUNT_RECOVERY_TABLE + " WHERE email = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setString(1, email);
			// runs the query
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return recoveryMapRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return null;

	}

	/**
	 * Stores temporary password in the database
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */
	public User storeTempPassword(User user, String pass) {
		final String sql = "INSERT INTO " + ACCOUNT_RECOVERY_TABLE + " (email, temp_password) VALUES(?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, user.getEmail());
			ps.setString(2, SecurePassword.getHashPassword(pass));
			ps.executeUpdate();
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Method which will confirm a new account in the database. Will not allow
	 * reversing of confirmation.
	 * 
	 * @param email
	 * @return
	 */
	public User accountConfirmed(User user) {
		// Declare SQL template query
		String sql = "UPDATE " + PROFESSORS_TABLE
				+ " SET confirmed = CASE confirmed WHEN 0 THEN 1 ELSE confirmed END WHERE email = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getEmail());
			// Runs query
			ps.execute();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;

	}

	/**
	 * Method which will activate a deactivated account
	 * 
	 * @param email
	 * @return
	 */
	public User activateAccount(User user) {
		// Declare SQL template query
		String sql = "UPDATE " + PROFESSORS_TABLE
				+ " SET active = CASE active WHEN 0 THEN 1 ELSE active END WHERE email = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getEmail());
			// Runs query
			ps.execute();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;

	}

	/**
	 * Method which will deactivate an active account
	 * 
	 * @param email
	 * @return
	 */
	public User deactivateAccount(User user) {
		// Declare SQL template query
		String sql = "UPDATE " + PROFESSORS_TABLE
				+ " SET active = CASE active WHEN 1 THEN 0 ELSE active END WHERE email = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getEmail());
			// Runs query
			ps.execute();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;

	}

	/**
	 * Searched the database for a user by using the user's email
	 * 
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + PROFESSORS_TABLE + " WHERE email = ? LIMIT 1";
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

	public User findById(long id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + PROFESSORS_TABLE + " WHERE professor_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

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
	 * @return all professors from the database.
	 */

	public List<User> all() {
		final String SELECT = "SELECT * FROM " + PROFESSORS_TABLE + " ORDER BY last_name";

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

	/**
	 * Removes recovery requests that have expired
	 * 
	 * @param user
	 * @return
	 */
	public void removeOldRecoveryRequest() {

		String sql = "DELETE FROM " + ACCOUNT_RECOVERY_TABLE + " WHERE datetime < DATE_SUB(NOW(), INTERVAL 24 hour)";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes recovery requests that have expired
	 * 
	 * @param professor
	 * @return
	 */
	public String deleteUser(long id) {

		String sql = "DELETE FROM " + PROFESSORS_TABLE + " WHERE professor_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return " ";
	}

	
	public String sortByLastName() {

		String sql = "SELECT * FROM professors ORDER BY last_name";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return " ";
	}

}
