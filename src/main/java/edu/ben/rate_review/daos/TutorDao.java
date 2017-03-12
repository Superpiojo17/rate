package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.AnnouncementForm;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;

public class TutorDao implements Dao<Tutor> {

	String TUTOR_TABLE = "tutors";
	String USER_TABLE = "users";
	Connection conn = null;

	/**
	 * TutorDao connection
	 * 
	 * @param conn
	 */
	public TutorDao(Connection conn) {
		this.conn = conn;
	}

	private Tutor mapRow(ResultSet rs) throws SQLException {
		UserDao udao = new UserDao(conn);
		
		
		Tutor tmp = new Tutor();

		tmp.setId(rs.getLong("tutor_relationship_id"));
		tmp.setStudent_id(rs.getLong("user_id_student"));
		tmp.setProfessor_id(rs.getLong("user_id_professor"));
		tmp.setCourse_name(rs.getString("course_name"));
		
		tmp.setTutor_email(udao.findById(rs.getLong("user_id_student")).getEmail());
		tmp.setTutor_first_name(udao.findById(rs.getLong("user_id_student")).getFirst_name());
		tmp.setTutor_last_name(udao.findById(rs.getLong("user_id_student")).getLast_name());

		return tmp;
	}

	public Tutor save(Tutor tutor) {
		final String sql = "INSERT INTO " + TUTOR_TABLE
				+ "(user_id_student, user_id_professor, course_name) Values(?,?,?)";

		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, tutor.getStudent_id());
			ps.setLong(2, tutor.getProfessor_id());
			ps.setString(3, tutor.getCourse_name());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}
	
	/**
	 * 
	 * @return all users from the database.
	 */

	public List<Tutor> all(Long id) {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " WHERE user_id_professor = " + id;

		List<Tutor> tutors = null;
		try {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			tutors = new ArrayList<Tutor>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					tutors.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return tutors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tutors;
	}
	

	

	
	public Tutor findById(long id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + TUTOR_TABLE + " WHERE tutor_relationship_id = ? LIMIT 1";
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
	
	public Tutor findByStudentId(long id) {
		// Declare SQL template query
		String sql = "SELECT * FROM " + TUTOR_TABLE + " WHERE user_id_student = ? LIMIT 1";
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
	
	public Long getStudentId(long id) {
		// Declare SQL template query
		String sql = "SELECT USER_ID_STUDENT FROM " + TUTOR_TABLE + " WHERE tutor_relationship_id = ? LIMIT 1";
		try {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return (long) -1;

	}
	
	
	

	
	
	public String deleteTutor(long id) {

		String sql = "DELETE FROM " + TUTOR_TABLE + " WHERE tutor_relationship_id = ? LIMIT 1";

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
	
	public void changeTutorRole(Long id) {
		String sql = "UPDATE " + USER_TABLE
				+ " SET role_id = ? WHERE user_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, 4);
			ps.setLong(2, id);


			// Runs query
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
	}
	
	public TutorForm updateTutor(TutorForm tutor) {
		String sql = "UPDATE " + TUTOR_TABLE
				+ " SET course_name = ? WHERE tutor_relationship_id = ? LIMIT 1";

		try {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, tutor.getCourse());
			ps.setLong(2, tutor.getId());


			// Runs query
			ps.execute();
			return tutor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	@Override
	public Tutor find(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tutor> all() {
		// TODO Auto-generated method stub
		return null;
	}


}
