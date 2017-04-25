package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;

/**
 * Dao for the review of a professor
 * 
 * @author DEX
 * @version 4-2-2017
 */
public class AnalysisDao {

	String REVIEW_PROFESSOR_TABLE = "professor_review";
	String COURSES_TABLE = "student_courses";
	Connection conn = null;

	/**
	 * Professor Review Dao connection
	 * 
	 * @param conn
	 */
	public AnalysisDao(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Creates a courses object
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
//	private CoursesToReview courseMapRow(ResultSet rs) throws SQLException {
//		// create student course object
//		CoursesToReview tmp = new CoursesToReview();
//		tmp.setCourse_id(rs.getLong("course_id"));
//		tmp.setStudent_id(rs.getInt("users_user_id"));
//		tmp.setCourse_name(rs.getString("course_name"));
//		tmp.setSemester(rs.getString("semester"));
//		tmp.setYear(rs.getInt("year"));
//		tmp.setProfessor_first_name(rs.getString("professor_first_name"));
//		tmp.setProfessor_last_name(rs.getString("professor_last_name"));
//		tmp.setProfessor_email(rs.getString("professor_email"));
//		tmp.setDisable_edit(rs.getBoolean("disable_edit"));
//
//		return tmp;
//	}

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
		//	tmp.setCourse_id(rs.getLong("course_id"));
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
		tmp.setProfessor_email(rs.getString("professor_email"));
		tmp.setComment_flagged(rs.getBoolean("comment_flagged"));
		tmp.setComment_removed(rs.getBoolean("comment_removed"));
		tmp.setComment_approved(rs.getBoolean("comment_approved"));

		return tmp;
	}

	/**
	 * Finds all reviews for a specific department but there is no department category for professors.
	 * 
	 * @param prof
	 * @return
	 */
	public List<ProfessorReview> listRecentCoursesByProfessorEmail(User prof) {

		final String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE department = CMSC";
		List<ProfessorReview> reviews = null;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			//ps.setString(1, prof.getEmail());
			ps.setString(1, prof.getDepartment());/////////////////
			reviews = new ArrayList<ProfessorReview>();
			try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					reviews.add(reviewMapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return reviews;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}

	
	/**
	 * Returns average rating from a specific category
	 * 
	 * @param prof
	 * @param table
	 * @return
	 */
	public double avgRate(User prof, String column, String display) {
		String sql = "SELECT AVG(" + column + ") FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ? ";

		if (!display.equalsIgnoreCase("overview")) {
			sql = sql + " AND course = ?";
		}

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, prof.getEmail());
			if (!display.equalsIgnoreCase("overview")) {
				ps.setString(2, display);
			}
			// Runs query
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Returns counts of a specific rating from a specific category
	 * 
	 * @param prof
	 * @param column
	 * @param ratingScore
	 * @return
	 */
	public int allRatings(User prof, String column, int ratingScore, String display) {

		String sql = "SELECT COUNT(" + column + ") FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ? AND "
				+ column + " = ?";

		if (!display.equalsIgnoreCase("overview")) {
			sql = sql + " AND course = ?";
		}

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, prof.getEmail());
			ps.setInt(2, ratingScore);
			if (!display.equalsIgnoreCase("overview")) {
				ps.setString(3, display);
			}
			// Runs query
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
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
