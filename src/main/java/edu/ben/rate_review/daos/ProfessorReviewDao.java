package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.jdbc.MysqlDataSource;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;

/**
 * Dao for the review of a professor
 *
 * @author Mike
 * @version 2-17-2017
 */
public class ProfessorReviewDao extends BaseDao {

	private String REVIEW_PROFESSOR_TABLE = "professor_review";

	public ProfessorReviewDao(MysqlDataSource db) {
		super(db);
	}

	/**
	 * Search integration to search in DB and return a list of any results
	 *
	 * @param sType
	 * @param sText
	 * @return
	 * @throws SQLException
	 */
	public List<ProfessorReview> search(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_first_name LIKE '%" + sText
				+ "%' OR professor_last_name LIKE '%" + sText + "%' OR course LIKE '%" + sText
				+ "%' OR semester LIKE '%" + sText + "%'  OR year LIKE '%" + sText
				+ "%'  OR professor_last_name LIKE '%" + sText + "%'";

		List<ProfessorReview> reviews = null;

		try (Connection conn = this.db.getConnection()) {

			PreparedStatement ps = conn.prepareStatement(NAME_SQL);

			reviews = new ArrayList<>();
			ResultSet rs = ps.executeQuery(NAME_SQL);
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * Creates a professor review object
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private ProfessorReview reviewMapRow(ResultSet rs) throws SQLException {
		UserDao uDao = new UserDao(this.db);
		// Create professor review object
		ProfessorReview tmp = new ProfessorReview();
		tmp.setStudent_course_id(rs.getLong("student_course_id"));
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
		tmp.setStudentName(uDao.findById(tmp.getStudent_id()).getFirst_name() + " "
				+ uDao.findById(tmp.getStudent_id()).getLast_name());
		tmp.setUnformattedOverall((float) (tmp.getRate_accessibility() + tmp.getRate_assignments()
				+ tmp.getRate_career_development() + tmp.getRate_challenging() + tmp.getRate_grade_fairly()
				+ tmp.getRate_grade_time() + tmp.getRate_knowledge() + tmp.getRate_objectives()
				+ tmp.getRate_organized() + tmp.getRate_outside_work() + tmp.getRate_pace()) / 11);

		return tmp;
	}

	/**
	 * Method which stores a new review in the database
	 *
	 * @param review
	 */
	public void save(ProfessorReview review) {

		if (findReview(review.getStudent_course_id()) == null) {
			final String sql = "INSERT INTO " + REVIEW_PROFESSOR_TABLE
					+ " (professor_first_name, professor_last_name, course, student_id, year, semester, comment,"
					+ " rate_objectives, rate_organized, rate_challenging, rate_outside_work,"
					+ " rate_pace, rate_assignments, rate_grade_fairly, rate_grade_time, rate_accessibility,"
					+ " rate_knowledge, rate_career_development, student_course_id, professor_email) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try (Connection conn = this.db.getConnection()) {
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
				ps.setLong(19, review.getStudent_course_id());
				ps.setString(20, review.getProfessor_email());

				ps.close();
				ps.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Finds and returns a specific ProfessorReview
	 *
	 * @param student_course_id
	 * @return
	 */
	public ProfessorReview findReview(long student_course_id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE student_course_id = ?";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, student_course_id);

			// Runs query
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				q.close();
				return reviewMapRow(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return null;

	}

	/**
	 * deletes a specific review
	 *
	 * @param id
	 * @return
	 */
	public String deleteReview(long id) {

		String sql = "DELETE FROM " + REVIEW_PROFESSOR_TABLE + " WHERE student_course_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, id);
			// Runs query
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return " ";
	}

	/**
	 * Finds all reviews for a specific professor
	 *
	 * @param prof
	 * @return
	 */
	public List<ProfessorReview> listRecentCoursesByProfessorEmail(User prof) {

		final String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ? LIMIT 3";
		List<ProfessorReview> reviews = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, prof.getEmail());
			reviews = new ArrayList<ProfessorReview>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * Finds all reviews for a specific professor
	 *
	 * @param prof
	 * @return
	 */
	public List<ProfessorReview> listCoursesByProfessorEmail(User prof, String display) {

		String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ?";
		List<ProfessorReview> reviews = null;

		if (!display.equalsIgnoreCase("overview")) {
			sql = sql + " AND course = ?";
		}

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, prof.getEmail());
			if (!display.equalsIgnoreCase("overview")) {
				ps.setString(2, display);
			}
			reviews = new ArrayList<ProfessorReview>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * list all the reviews of a specific course
	 *
	 * @param course
	 * @return
	 */
	public List<ProfessorReview> listReviewsByCourse(String course) {

		String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE course = '" + course + "'";

		List<ProfessorReview> reviews = null;

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			reviews = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * returns all the reviews from a specific departments
	 *
	 * @param dept
	 * @return
	 */
	public List<ProfessorReview> allFromDept(String dept) {

		String sql = "SELECT * FROM professor_review WHERE professor_email IN (Select email from users where major = '"
				+ dept + "')";

		List<ProfessorReview> reviews = null;

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			reviews = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * Allows for deletion of a specific review
	 *
	 * @param review
	 * @return
	 */
	public ProfessorReview removeProfessorReview(ProfessorReview review) {

		String sql = "DELETE FROM " + REVIEW_PROFESSOR_TABLE + " WHERE student_course_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();

			StudentInCourseDao sDao = DaoManager.getInstance().getStudentInCourseDao();

			// marks course not reviewed
			sDao.setCourseNotReviewed(review);
			ps.close();
			return review;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Flags a potentially offensive comment for admin to see
	 *
	 * @param review
	 */
	public void setCommentFlagged(ProfessorReview review) {
		// Declare SQL template query
		String sql = "UPDATE " + REVIEW_PROFESSOR_TABLE
				+ " SET comment_flagged = 1 WHERE student_course_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lists comments from all professor reviews
	 *
	 * @return
	 */
	public List<ProfessorReview> listAllReviews() {

		final String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE;
		List<ProfessorReview> reviews = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			reviews = new ArrayList<ProfessorReview>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * Lists all reviews with comments that have been flagged
	 *
	 * @return
	 */
	public List<ProfessorReview> listAllFlaggedComments() {

		final String sql = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE comment_flagged = 1";
		List<ProfessorReview> reviews = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			reviews = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				reviews.add(reviewMapRow(rs));
			}
			ps.close();
			return reviews;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reviews;
	}

	/**
	 * Sets comment removed
	 *
	 * @param review
	 */
	public void setCommentRemoved(ProfessorReview review) {
		// Declare SQL template query

		String sql = "UPDATE " + REVIEW_PROFESSOR_TABLE
				+ " SET comment_removed = 1 WHERE student_course_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * After admin deletes a comment, it is no longer marked as flagged
	 *
	 * @param review
	 */
	public boolean setCommentNotFlagged(ProfessorReview review) {
		// Declare SQL template query

		String sql = "UPDATE " + REVIEW_PROFESSOR_TABLE
				+ " SET comment_flagged = 0 WHERE student_course_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();
			ps.close();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Once an admin approves a flagged comment, it can no longer be flagged
	 *
	 * @param review
	 */
	public boolean setCommentApproved(ProfessorReview review) {
		// Declare SQL template query

		String sql = "UPDATE " + REVIEW_PROFESSOR_TABLE
				+ " SET comment_approved = 1 WHERE student_course_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();
			ps.close();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Returns average rating from a specific category
	 *
	 * @param prof
	 * @param column
	 * @param display
	 * @return
	 */
	public double avgRate(User prof, String column, String display) {
		String sql = "SELECT AVG(" + column + ") FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ? ";

		if (!display.equalsIgnoreCase("overview")) {
			sql = sql + " AND course = ?";
		}

		try (Connection conn = this.db.getConnection()) {
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
				ps.close();
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

		try (Connection conn = this.db.getConnection()) {
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
				ps.close();
				return rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Lists all courses taught by a professor
	 *
	 * @param prof
	 * @return
	 */
	public List<String> listUniqueCourses(User prof) {

		final String sql = "SELECT DISTINCT course FROM " + REVIEW_PROFESSOR_TABLE + " WHERE professor_email = ?";
		List<String> uniqueCourses = null;

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, prof.getEmail());
			uniqueCourses = new ArrayList<String>();
			try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					uniqueCourses.add(rs.getString("course"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return uniqueCourses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return uniqueCourses;
	}

	/**
	 * list all the reviews for a course
	 *
	 * @param student_course_id
	 * @param name
	 * @return
	 */
	public List<ProfessorReview> allReviewsForCourse(long student_course_id, String name) {
		final String SELECT = "SELECT * FROM " + REVIEW_PROFESSOR_TABLE + " WHERE student_course_id = "
				+ student_course_id;

		List<ProfessorReview> reviews = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			reviews = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					reviews.add(reviewMapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return reviews;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}

}
