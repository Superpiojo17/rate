package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.controller.home.ProfessorController;
import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.MassEditForm;
import edu.ben.rate_review.models.RecoveringUser;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;

/**
 * UserDao is a dao that will connect and provide interaction to the database
 * 
 * @author Mike
 * @version 2-2-2017
 */
public class UserDao implements Dao<User> {
	String USER_TABLE = "users";
	String ACCOUNT_RECOVERY_TABLE = "account_recovery";
	Connection conn = null;

	/**
	 * UserDao connection
	 * 
	 * @param conn
	 */
	public UserDao(Connection conn) {
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
		tmp.setId(rs.getLong("user_id"));
		tmp.setEmail(rs.getString("email"));
		tmp.setPassword(rs.getString("encryptedPassword"));
		tmp.setRole(rs.getInt("role_id"));
		tmp.setConfirmed(rs.getBoolean("confirmed"));
		tmp.setActive(rs.getBoolean("active"));
		tmp.setFirst_name(rs.getString("first_name"));
		tmp.setLast_name(rs.getString("last_name"));
		tmp.setRole_string(rs.getString("role_id"));
		tmp.setActive_icon(rs.getString("active"));
		tmp.setConfirmed_icon(rs.getString("confirmed"));
		tmp.setSchool_year(rs.getInt("school_year"));
		tmp.setMajor(rs.getString("major"));
		tmp.setYear_string(rs.getString("school_year"));
		// tmp.setDepartment(rs.getString("department"));
		tmp.setNickname(rs.getString("nickname"));
		tmp.setPersonal_email(rs.getString("personal_email"));
		tmp.setOverall(ProfessorController.getOverall(tmp));

		return tmp;
	}

	/**
	 * Method which stores a new user in the database.
	 */
	public User save(User user) {
		final String sql = "INSERT INTO " + USER_TABLE
				+ " (first_name, last_name, email, encryptedPassword, role_id) VALUES(?,?,?,?,?)";
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
		String sql = "UPDATE " + USER_TABLE
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
		String sql = "UPDATE " + USER_TABLE
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

	public User completeProfProfile(User user) {
		// Declare SQL template query
		String sql = "UPDATE " + USER_TABLE + " SET major = ? WHERE user_id= ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getMajor());
			ps.setLong(2, user.getId());
			// Runs query
			ps.execute();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;

	}

	public User completeProfile(User user) {
		// Declare SQL template query
		String sql = "UPDATE " + USER_TABLE + " SET major = ?, school_year = ? WHERE user_id= ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getMajor());
			ps.setInt(2, user.getSchool_year());
			ps.setLong(3, user.getId());
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
	 * method will update the users nickname and personal email
	 * 
	 * @param user
	 * @return
	 */
	public User completeProfile2(User user) {
		// Declare SQL template query
		String sql = "UPDATE " + USER_TABLE + " SET nickname = ?, personal_email = ? WHERE user_id= ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getNickname());
			ps.setString(2, user.getPersonal_email());
			ps.setLong(3, user.getId());
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
		String sql = "UPDATE " + USER_TABLE
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
		String sql = "SELECT * FROM " + USER_TABLE + " WHERE email = ? LIMIT 1";
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
		String sql = "SELECT * FROM " + USER_TABLE + " WHERE user_id = ? LIMIT 1";
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
	 * @return all users from the database.
	 */

	public List<User> all() {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " ORDER BY last_name";

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

	/**
	 * 
	 * @return all professors from the database.
	 */

	public List<User> allProfessorsByDept(String department) {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE role_id = 2 and major = '" + department + "'";

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

	/**
	 * 
	 * @return all professors from the database.
	 */

	public List<User> allProfessors() {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE role_id = 2 ";

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

	/**
	 * 
	 * @return all professors from the database.
	 */

	public String getPicString(Long id) {
		// Declare SQL template query
		String sql = "SELECT pic_location FROM profile_pic WHERE user_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return rs.getString("pic_location");
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

	public List<User> allTutorsByMajor(String Major) {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE major = '" + Major + "' AND role_id = 3";

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

	/**
	 * 
	 * @return all users from the database.
	 */

	public List<User> CourseList(long courseID) {
		final String SELECT = "Select * from users where user_id  in (Select student_id from student_in_course where course_id = "
				+ courseID + ")";

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

	// /**
	// *
	// * @return all users from the database.
	// */
	//
	// public List<Integer> StudentIDCourseList(long courseID) {
	// final String SELECT = "Select * from users where user_id in (Select
	// student_id from student_in_course where course_id = "
	// + courseID + ")";
	//
	// List> users = null;
	// try {
	// PreparedStatement ps = conn.prepareStatement(SELECT);
	// users = new ArrayList<User>();
	// try {
	// ResultSet rs = ps.executeQuery(SELECT);
	// while (rs.next()) {
	// users.add(mapRow(rs));
	// }
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return users;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return users;
	// }

	public List<User> allByMajor(String Major) {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE major = '" + Major
				+ "' AND (role_id = 3 or role_id = 4) ORDER BY role_id ASC";

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

	/**
	 * 
	 * @return all users from the database.
	 */
	public List<User> allStudentsByMajor(String Major) {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE major = '" + Major + "' AND role_id = 4";

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

	/**
	 * 
	 * @return all users from the database.
	 */
	public List<User> allStudents() {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE role_id = 4";

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
	 * @param user
	 * @return
	 */
	public String deleteUser(long id) {

		String sql = "DELETE FROM " + USER_TABLE + " WHERE user_id = ? LIMIT 1";

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

	/**
	 * 
	 * @return all users from the database.
	 */

	public List<User> sortbyRole() {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " ORDER BY role_id";

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

	public String sortByLastName() {

		String sql = "SELECT * FROM users ORDER BY last_name";

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

	public List<User> sortByMajor(String string) {

		final String SELECT = "SELECT * FROM users WHERE major = '" + string + "'";

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

	/**
	 * Called when user has successfully recovered their account, or they
	 * request a new temporary password before using their previously requested
	 * password before it has expired.
	 * 
	 * @param user
	 * @return
	 */
	public User removeRecoveryRequest(User user) {

		String sql = "DELETE FROM " + ACCOUNT_RECOVERY_TABLE + " WHERE email = ? LIMIT 1";

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
	 * Updates user's password to their new password
	 * 
	 * @param user
	 * @return
	 */
	public User updatePassword(User user) {
		String sql = "UPDATE " + USER_TABLE + " SET encryptedPassword = ? WHERE email = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getEncryptedPassword());
			ps.setString(2, user.getEmail());
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
	 * Updates user's password to their new password
	 * 
	 * @param user
	 * @return
	 */
	public User updateRole(User user, int role) {
		String sql = "UPDATE " + USER_TABLE + " SET role_id = ? WHERE user_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, role);
			ps.setLong(2, user.getId());
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
	 * Updates user's password to their new password
	 * 
	 * @param user
	 * @return
	 */
	public MassEditForm massEditConfirmed(MassEditForm massedit) {
		String sql = "UPDATE " + USER_TABLE + " SET confirmed = ? WHERE confirmed = ?";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, massedit.getAfter());
			ps.setInt(2, massedit.getBefore());
			// Runs query
			ps.execute();
			return massedit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * Updates user's password to their new password
	 * 
	 * @param user
	 * @return
	 */
	public MassEditForm massEditRole(MassEditForm massedit) {
		String sql = "UPDATE " + USER_TABLE + " SET role_id = ? WHERE role_id = ?";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, massedit.getAfter());
			ps.setInt(2, massedit.getBefore());
			// Runs query
			ps.execute();
			return massedit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	public MassEditForm massEditYear(MassEditForm massedit) {
		String sql = "UPDATE " + USER_TABLE + " SET school_year = ? WHERE school_year = ?";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, massedit.getAfter());
			ps.setInt(2, massedit.getBefore());
			// Runs query
			ps.execute();
			return massedit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	public MassEditForm massEditActive(MassEditForm massedit) {
		String sql = "UPDATE " + USER_TABLE + " SET active = ? WHERE active = ?";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, massedit.getAfter());
			ps.setInt(2, massedit.getBefore());
			// Runs query
			ps.execute();
			return massedit;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	public List<User> search(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM users WHERE first_name LIKE '%" + sText + "%' OR last_name LIKE '%" + sText
				+ "%' OR email LIKE '%" + sText + "%'";

		List<User> users = null;

		try {

			PreparedStatement ps = conn.prepareStatement(NAME_SQL);
			// both have 1 parameter
			// ps.setString(1, sText);
			//
			// // Only name search has a second parameter
			// if (sType.equals("name")) {
			// ps.setString(2, sText);
			// }
			users = new ArrayList<User>();
			try {
				ResultSet rs = ps.executeQuery(NAME_SQL);
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

	public List<User> searchTutor(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM users WHERE (role_id =3 or role_id =4) and (first_name LIKE '%" + sText
				+ "%' OR last_name LIKE '%" + sText + "%' OR email LIKE '%" + sText + "%')";

		List<User> users = null;

		try {

			PreparedStatement ps = conn.prepareStatement(NAME_SQL);
			// both have 1 parameter
			// ps.setString(1, sText);
			//
			// // Only name search has a second parameter
			// if (sType.equals("name")) {
			// ps.setString(2, sText);
			// }
			users = new ArrayList<User>();
			try {
				ResultSet rs = ps.executeQuery(NAME_SQL);
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

	public List<User> searchProf(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM users WHERE role_id = 2 and (first_name LIKE '%" + sText
				+ "%' OR last_name LIKE '%" + sText + "%' OR email LIKE '%" + sText + "%' OR major LIKE '%" + sText
				+ "%')";

		List<User> users = null;

		try {

			PreparedStatement ps = conn.prepareStatement(NAME_SQL);
			// both have 1 parameter
			// ps.setString(1, sText);
			//
			// // Only name search has a second parameter
			// if (sType.equals("name")) {
			// ps.setString(2, sText);
			// }
			users = new ArrayList<User>();
			try {
				ResultSet rs = ps.executeQuery(NAME_SQL);
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

	/**
	 * Updates user's password to their new password
	 * 
	 * @param user
	 * @return
	 */
	public UserForm updateUser(UserForm user) {
		String sql = "UPDATE " + USER_TABLE
				+ " SET first_name = ?, last_name = ?, email = ?, role_id = ?, school_year = ?, major = ? WHERE user_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, user.getFirst_name());
			ps.setString(2, user.getLast_name());
			ps.setString(3, user.getEmail());
			ps.setInt(4, user.getRole());
			ps.setInt(5, user.getSchool_year());
			ps.setString(6, user.getMajor());

			ps.setLong(7, user.getId());

			// Runs query
			ps.execute();
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	public List<User> allTutors() {
		final String SELECT = "SELECT * FROM " + USER_TABLE + " WHERE role_id = 3 ";

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
}
