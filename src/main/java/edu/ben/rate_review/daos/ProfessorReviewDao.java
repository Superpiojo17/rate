package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;

/**
 * Dao for the review of a professor
 * 
 * @author Mike
 * @version 2-17-2017
 */
public class ProfessorReviewDao {

	String REVIEW_PROFESSOR_TABLE = "professor_review";
	String COURSES_TABLE = "student_courses";
	Connection conn = null;

	/**
	 * Professor Review Dao connection
	 * 
	 * @param conn
	 */
	public ProfessorReviewDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Returns all courses to review for current semester
	 * 
	 * @return
	 */
	public List<CoursesToReview> allCourses(User user) {
		final String SELECT = "SELECT * FROM " + COURSES_TABLE + " WHERE users_user_id = " + user.getId()
				+ " AND semester = 'Spring' AND year = 2017";
		List<CoursesToReview> courses = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			courses = new ArrayList<CoursesToReview>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					courses.add(courseMapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return courses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return courses;
	}

	/**
	 * Creates a courses object
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private CoursesToReview courseMapRow(ResultSet rs) throws SQLException {
		// create student course object
		CoursesToReview tmp = new CoursesToReview();
		tmp.setCourse_id(rs.getLong("course_id"));
		tmp.setStudent_id(rs.getInt("users_user_id"));
		tmp.setCourse_name(rs.getString("course_name"));
		tmp.setSemester(rs.getString("semester"));
		tmp.setYear(rs.getInt("year"));
		tmp.setProfessor_first_name(rs.getString("professor_first_name"));
		tmp.setProfessor_last_name(rs.getString("professor_last_name"));

		return tmp;
	}

	/**
	 * Creates a professor review object
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ProfessorReview reviewMapRow(ResultSet rs) throws SQLException {
		// Create professor review object
		ProfessorReview tmp = new ProfessorReview();
		tmp.setCourse_id(rs.getLong("course_id"));
		tmp.setProfessor_first_name(rs.getString("professor_first_name"));
		tmp.setProfessor_last_name(rs.getString("professor_last_name"));
		tmp.setStudent_id(rs.getLong("student_id"));
		tmp.setCourse(rs.getString("course"));
		tmp.setYear(rs.getInt("year"));
		tmp.setSemester(rs.getString("semester"));
		tmp.setComment(rs.getString("comment"));
		tmp.setRate_objectives(rs.getInt("rate_objectives"));
		tmp.setRate_organized(rs.getInt("rate_organized"));
		tmp.setRate_challenging(rs.getInt("rate_challenging"));
		tmp.setRate_outside_work(rs.getInt("rate_outside_work"));
		tmp.setRate_pace(rs.getInt("rate_pace"));
		tmp.setRate_assignments(rs.getInt("rate_assignments"));
		tmp.setRate_grade_fairly(rs.getInt("rate_grade_fairly"));
		tmp.setRate_grade_time(rs.getInt("rate_grade_time"));
		tmp.setRate_accessibility(rs.getInt("rate_accessibility"));
		tmp.setRate_knowledge(rs.getInt("rate_knowledge"));
		tmp.setRate_career_development(rs.getInt("rate_career_development"));

		return tmp;
	}

	/**
	 * Method which stores a new review in the database
	 * 
	 * @param review
	 */
	public void save(ProfessorReview review) {

		if (findReview(review.getCourse_id()) == null) {
			final String sql = "INSERT INTO " + REVIEW_PROFESSOR_TABLE
					+ " (professor_first_name, professor_last_name, course, student_id, year, semester, comment,"
					+ " rate_objectives, rate_organized, rate_challenging, rate_outside_work,"
					+ " rate_pace, rate_assignments, rate_grade_fairly, rate_grade_time, rate_accessibility,"
					+ " rate_knowledge, rate_career_development, course_id) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, review.getProfessor_first_name());
				ps.setString(2, review.getProfessor_last_name());
				ps.setString(3, review.getCourse());
				ps.setLong(4, review.getStudent_id());
				ps.setInt(5, review.getYear());
				ps.setString(6, review.getSemester());
				ps.setString(7, review.getComment());
				ps.setInt(8, review.getRate_objectives());
				ps.setInt(9, review.getRate_organized());
				ps.setInt(10, review.getRate_challenging());
				ps.setInt(11, review.getRate_outside_work());
				ps.setInt(12, review.getRate_pace());
				ps.setInt(13, review.getRate_assignments());
				ps.setInt(14, review.getRate_grade_fairly());
				ps.setInt(15, review.getRate_grade_time());
				ps.setInt(16, review.getRate_accessibility());
				ps.setInt(17, review.getRate_knowledge());
				ps.setInt(18, review.getRate_career_development());
				ps.setLong(19, review.getCourse_id());
				ps.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Finds and returns a specific ProfessorReview
	 * 
	 * @param email
	 * @return
	 */
	public ProfessorReview findReview(long course_id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE course_id = ?";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, course_id);

			// Runs query
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return reviewMapRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return null;

	}
	
	/**
	 * Finds and returns a specific ProfessorReview
	 * 
	 * @param email
	 * @return
	 */
	public CoursesToReview findByCourseId(long course_id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + COURSES_TABLE + " WHERE course_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, course_id);

			// Runs query
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return courseMapRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return null;

	}

	/**
	 * Allows for deletion of a specific review
	 * 
	 * @param review
	 * @return
	 */
	public ProfessorReview removeProfessorReview(ProfessorReview review) {

		String sql = "DELETE FROM " + REVIEW_PROFESSOR_TABLE + " WHERE course_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.setLong(1, review.getCourse_id());

			// Runs query
			ps.execute();

			return review;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Closes the connection
	 */
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
