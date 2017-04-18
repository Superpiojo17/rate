package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.StudentInCourse;
import edu.ben.rate_review.models.User;

public class StudentInCourseDao {

	String STUDENTINCOURSES_TABLE = "student_in_course";
	Connection conn = null;

	/**
	 * UserDao connection
	 * 
	 * @param conn
	 */
	public StudentInCourseDao(Connection conn) {
		this.conn = conn;
	}

	private StudentInCourse mapRow(ResultSet rs) throws SQLException {
		UserDao uDao = new UserDao(conn);
		CourseDao cDao = new CourseDao(conn);
		// Create user object and pass to array
		StudentInCourse tmp = new StudentInCourse();
		tmp.setStudent_course_id(rs.getLong("student_course_id"));
		tmp.setCourse_id(rs.getLong("course_id"));
		tmp.setStudent_id(rs.getLong("student_id"));
		tmp.setCourse_reviewed(rs.getBoolean("course_reviewed"));
		tmp.setDisable_edit(rs.getBoolean("disable_edit"));
		tmp.setSemester_past(rs.getBoolean("semester_past"));

		tmp.setProfessor_first_name(uDao.findById(cDao.findById(tmp.getCourse_id()).getProfessor_id()).getFirst_name());
		tmp.setProfessor_last_name(uDao.findById(cDao.findById(tmp.getCourse_id()).getProfessor_id()).getLast_name());
		tmp.setSemester(cDao.findById(tmp.getCourse_id()).getSemester());
		tmp.setYear(cDao.findById(tmp.getCourse_id()).getYear());
		tmp.setCourse_subject_number(
				cDao.findById(tmp.getCourse_id()).getSubject() + cDao.findById(tmp.getCourse_id()).getCourse_number());

		return tmp;
	}

	public StudentInCourse save(StudentInCourse studentInCourse) {
		final String sql = "INSERT INTO " + STUDENTINCOURSES_TABLE
				+ "(course_id, student_id, course_reviewed, disable_edit, semester_past) Values(?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, studentInCourse.getCourse_id());
			ps.setLong(2, studentInCourse.getStudent_id());
			ps.setBoolean(3, studentInCourse.isCourse_reviewed());
			ps.setBoolean(4, studentInCourse.isDisable_edit());
			ps.setBoolean(5, studentInCourse.isSemester_past());
			;
			ps.executeUpdate();
			return studentInCourse;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public List<StudentInCourse> allStudentCoursesNotReviewed(User user) {
		final String SELECT = "SELECT * FROM " + STUDENTINCOURSES_TABLE + " WHERE student_id = " + user.getId()
				+ " AND course_reviewed = 0 AND semester_past = 0";
		List<StudentInCourse> studentInCourses = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			studentInCourses = new ArrayList<StudentInCourse>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					studentInCourses.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return studentInCourses;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return studentInCourses;
	}

	public List<StudentInCourse> allStudentCoursesReviewed(User user) {
		final String SELECT = "SELECT * FROM " + STUDENTINCOURSES_TABLE + " WHERE student_id = " + user.getId()
				+ " AND course_reviewed = 1";
		List<StudentInCourse> courses = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			courses = new ArrayList<StudentInCourse>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					courses.add(mapRow(rs));
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

	public List<StudentInCourse> listAllCourses() {
		final String SELECT = "SELECT * FROM " + STUDENTINCOURSES_TABLE;

		List<StudentInCourse> courses = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			courses = new ArrayList<StudentInCourse>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					courses.add(mapRow(rs));
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

	public StudentInCourse findByStudentCourseId(long student_course_id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + STUDENTINCOURSES_TABLE + " WHERE student_course_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, student_course_id);

			// Runs query
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

	public void disableEditReview(StudentInCourse course) {
		// Declare SQL template query

		String sql = "UPDATE " + STUDENTINCOURSES_TABLE + " SET disable_edit = 1 WHERE student_course_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, course.getStudent_course_id());
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setSemesterPast(StudentInCourse course) {
		// Declare SQL template query

		String sql = "UPDATE " + STUDENTINCOURSES_TABLE + " SET semester_past = 1 WHERE student_course_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, course.getStudent_course_id());
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCourseReviewed(ProfessorReview review) {
		// Declare SQL template query
		String sql = "UPDATE " + STUDENTINCOURSES_TABLE
				+ " SET course_reviewed = 1 WHERE student_course_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCourseNotReviewed(ProfessorReview review) {
		// Declare SQL template query
		String sql = "UPDATE " + STUDENTINCOURSES_TABLE
				+ " SET course_reviewed = 0 WHERE student_course_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, review.getStudent_course_id());
			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
