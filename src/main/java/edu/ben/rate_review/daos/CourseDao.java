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
//import edu.ben.rate_review.models.User;

public class CourseDao {

	String COURSES_TABLE = "courses";
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
		tmp.setSemester(rs.getString("course_semester"));
		tmp.setYear(rs.getInt("course_year"));
		tmp.setProfessor_name(rs.getString("course_professor_id"));
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

	public Course save(Course course) {
		final String sql = "INSERT INTO " + COURSES_TABLE
				+ "(course_subject, course_number, course_name, course_professor_id, course_semester, course_year) Values(?,?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, course.getSubject());
			ps.setLong(2, course.getCourse_number());
			ps.setString(3, course.getCourse_name());
			ps.setLong(4, course.getProfessor_id());
			ps.setString(5, course.getSemester());
			ps.setInt(6, course.getYear());
			;
			ps.executeUpdate();
			return course;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

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

	public String deleteCourse(long id) {

		String sql = "DELETE FROM " + COURSES_TABLE + " WHERE course_id = ? LIMIT 1";

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

	public List<Course> allCourses() {
		final String SELECT = "SELECT * FROM " + COURSES_TABLE;
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

	public List<String> allCoursesString() {
		final String SELECT = "SELECT * FROM " + COURSES_TABLE;
		List<String> courses = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			courses = new ArrayList<String>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					courses.add(rs.getString("course_name"));
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

	public List<Course> allCoursesByID(long id) {
		final String SELECT = "SELECT * FROM " + COURSES_TABLE + " WHERE course_id = '" + id + "'";
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

	/**
	 * 
	 * @return all users from the database.
	 * @throws ParseException
	 */

	public List<Course> allByProfessor(Long id) {
		final String SELECT = "SELECT * FROM " + COURSES_TABLE + " WHERE  course_professor_id = '" + id + "'";
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

	public List<Course> search(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM courses WHERE course_name LIKE '%" + sText + "%' OR course_number LIKE '%"
				+ sText + "%'";

		List<Course> courses = null;

		try {

			PreparedStatement ps = conn.prepareStatement(NAME_SQL);
			// both have 1 parameter
			// ps.setString(1, sText);
			//
			// // Only name search has a second parameter
			// if (sType.equals("name")) {
			// ps.setString(2, sText);
			// }
			courses = new ArrayList<Course>();
			try {
				ResultSet rs = ps.executeQuery(NAME_SQL);
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
	
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
