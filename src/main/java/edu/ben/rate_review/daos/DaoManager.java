package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DaoManager {
	private final String HOST = "jdbc:mysql://localhost:3306";
	private final String DATABASE_NAME = "rate";
	private final String USERNAME = "root";
	private final String PASSWORD = "LiterallyAnything";

	// Private
	private DataSource src;
	private Connection con;
	private static DaoManager instance = null;
	private static Logger logger = LoggerFactory.getLogger(DaoManager.class);

	/**
	 * Constructor that will create the singleton instance if it doesn't exist
	 * already
	 *
	 * @throws Exception
	 */
	public DaoManager() throws Exception {
		if (instance == null) {
			try {
				MysqlDataSource mysql = new MysqlDataSource();
				mysql.setURL(HOST + "/" + DATABASE_NAME);
				mysql.setDatabaseName(DATABASE_NAME);
				mysql.setUser(USERNAME);
				mysql.setPassword(PASSWORD);
				this.src = mysql;
				instance = this;
			} catch (Exception e) {
				throw e;
			}
		}
	}

	public UserDao getUserDao() {
		try {
			return new UserDao(this.src.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public CourseDao getCourseDao() {
		try {
			return new CourseDao(this.src.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AnnouncementDao getAnnouncementDao() {
		try {
			return new AnnouncementDao(this.src.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public TutorDao getTutorDao() {
		try {
			return new TutorDao(this.src.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * gets instance of professor review dao
	 * 
	 * @return
	 */
	public ProfessorReviewDao getProfessorReviewDao() {
		try {
			return new ProfessorReviewDao(this.src.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Used to return the singleton instance
	 *
	 * @return
	 */
	public static DaoManager getInstance() {
		return instance;
	}

	/**
	 * Retrieves a connection to the database
	 *
	 * @throws SQLException
	 */
	public void openConnection() throws SQLException {
		try {
			if (this.con == null || this.con.isClosed()) {
				this.con = src.getConnection();
			}
		} catch (SQLException e) {
			throw e;
		}
	}

	/**
	 * Closes a connection to the database
	 *
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		try {
			if (this.con != null && !this.con.isClosed()) {
				this.con.close();
			}
		} catch (SQLException e) {
			throw e;
		}
	}

}