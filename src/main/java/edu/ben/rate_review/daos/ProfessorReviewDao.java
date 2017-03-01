package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ben.rate_review.models.ProfessorReview;

/**
 * Dao for the review of a professor
 * 
 * @author Mike
 * @version 2-17-2017
 */
public class ProfessorReviewDao {

	String REVIEW_PROFESSOR_TABLE = "professor_review";
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
	 * Creates a professor review object
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ProfessorReview reviewMapRow(ResultSet rs) throws SQLException {
		// Create professor review object
		ProfessorReview tmp = new ProfessorReview();
		tmp.setProfessor_email(rs.getString("professor_email"));
		tmp.setStudent_email(rs.getString("student_email"));
		tmp.setCourse(rs.getString("course"));
		tmp.setCurrent_year(rs.getInt("current_year"));
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

		if (findReview(review.getProfessor_email(), review.getCourse(), review.getCurrent_year(), review.getSemester(),
				review.getStudent_email()) == null) {
			final String sql = "INSERT INTO " + REVIEW_PROFESSOR_TABLE
					+ " (professor_email, course, student_email, current_year, semester, comment,"
					+ " rate_objectives, rate_organized, rate_challenging, rate_outside_work,"
					+ " rate_pace, rate_assignments, rate_grade_fairly, rate_grade_time, rate_accessibility,"
					+ " rate_knowledge, rate_career_development) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, review.getProfessor_email());
				ps.setString(2, review.getCourse());
				ps.setString(3, review.getStudent_email());
				ps.setInt(4, review.getCurrent_year());
				ps.setString(5, review.getSemester());
				ps.setString(6, review.getComment());
				ps.setInt(7, review.getRate_objectives());
				ps.setInt(8, review.getRate_organized());
				ps.setInt(9, review.getRate_challenging());
				ps.setInt(10, review.getRate_outside_work());
				ps.setInt(11, review.getRate_pace());
				ps.setInt(12, review.getRate_assignments());
				ps.setInt(13, review.getRate_grade_fairly());
				ps.setInt(14, review.getRate_grade_time());
				ps.setInt(15, review.getRate_accessibility());
				ps.setInt(16, review.getRate_knowledge());
				ps.setInt(17, review.getRate_career_development());
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
	public ProfessorReview findReview(String professor_email, String course, int current_year, String semester,
			String student_email) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ?"
				+ " AND course = ? AND current_year = ? AND semester = ? AND student_email = ?";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setString(1, professor_email);
			q.setString(2, course);
			q.setInt(3, current_year);
			q.setString(4, semester);
			q.setString(5, student_email);

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
	 * Allows for deletion of a specific review
	 * 
	 * @param review
	 * @return
	 */
	public ProfessorReview removeProfessorReview(ProfessorReview review) {

		String sql = "DELETE FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ? AND course = ? AND "
				+ "student_email = ? AND current_year = ? AND semester = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, review.getProfessor_email());
			ps.setString(2, review.getCourse());
			ps.setString(3, review.getStudent_email());
			ps.setInt(4, review.getCurrent_year());
			ps.setString(5, review.getSemester());
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
