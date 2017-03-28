package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.CourseForm;

public class CourseDao {

	String COURSES_TABLE = "student_courses";
	Connection conn = null;

	/**
	 * UserDao connection
	 * 
	 * @param conn
	 */
	public CourseDao(Connection conn) {
		this.conn = conn;
	}

	private Course mapRow(ResultSet rs) throws SQLException {

		// Create user object and pass to array
		Course tmp = new Course();
		tmp.setId(rs.getLong("course_id"));
		tmp.setCourse_name(rs.getString("course_name"));
		tmp.setProfessor_id(rs.getLong("course_professor_id"));
		tmp.setSubject(rs.getString("course_subject"));
		tmp.setTerm(rs.getString("course_term"));
		tmp.setProfessor_name(rs.getString("course_professor"));
		tmp.setCourse_number(rs.getLong("course_number"));

		return tmp;
	}

	public CourseForm updateCourse(CourseForm course) {
		String sql = "UPDATE " + COURSES_TABLE + " SET course_professor_id = ? WHERE course_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, course.getProfessor_id());
			ps.setLong(2, course.getId());

			// Runs query
			ps.execute();
			return course;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	// public Course save(Course course) {
	// final String sql = "INSERT INTO " + COURSES_TABLE + " (announcement_date,
	// announcement_content) VALUES(?,?)";
	// try {
	// PreparedStatement ps = conn.prepareStatement(sql);
	// ps.setString(1, announcement.getDate());
	// ps.setString(2, announcement.getAnnouncement_content());
	// ;
	// ps.executeUpdate();
	// return announcement;
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return null;
	//
	// }

	public Course findById(long id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + COURSES_TABLE + " WHERE course_id = ? LIMIT 1";
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

	public String deletAnnouncement(long id) {

		String sql = "DELETE FROM " + COURSES_TABLE + " WHERE announcement_id = ? LIMIT 1";

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
	 * @throws ParseException
	 */

	public List<Course> allByDept(String subject) {
		final String SELECT = "SELECT * FROM " + COURSES_TABLE + " WHERE course_subject = '" + subject + "'";
		List<Course> courses = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			courses = new ArrayList<Course>();
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

}
