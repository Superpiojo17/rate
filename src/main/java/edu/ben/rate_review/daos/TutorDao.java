package edu.ben.rate_review.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.User;

public class TutorDao implements Dao<Tutor> {

	String TUTOR_TABLE = "tutors";
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
