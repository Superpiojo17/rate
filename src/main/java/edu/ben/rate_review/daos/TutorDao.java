package edu.ben.rate_review.daos;

import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.zaxxer.hikari.HikariDataSource;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.TutorReview;
import edu.ben.rate_review.models.User;

public class TutorDao extends BaseDao implements Dao<Tutor> {

	private final String TUTOR_TABLE = "tutors";
	private final String USER_TABLE = "users";
	private final String APPOINTMENT_TABLE = "tutor_appointment";
	private final String REVIEW_TABLE = "tutor_review";

	public TutorDao(HikariDataSource db) {
		super(db);
	}

	private Tutor mapRow(ResultSet rs) throws SQLException {
		UserDao udao = new UserDao(this.db);
		TutorDao tDao = new TutorDao(this.db);
		CourseDao cDao = new CourseDao(this.db);
		Tutor tmp = new Tutor();

		tmp.setId(rs.getLong("tutor_relationship_id"));
		tmp.setStudent_id(rs.getLong("user_id_student"));
		tmp.setProfessor_id(rs.getLong("user_id_professor"));
		tmp.setCourse_id(rs.getLong("course_id"));

		Course course = cDao.findById(tmp.getCourse_id());

		if (course != null) {
			tmp.setCourse_name(course.getCourse_name());
		}

		User tutor = udao.findById(tmp.getStudent_id());

		if (tutor != null) {
			tmp.setTutor_email(tutor.getEmail());
			tmp.setTutor_first_name(tutor.getFirst_name());
			tmp.setTutor_last_name(tutor.getLast_name());
		}

		User professor = udao.findById(tmp.getProfessor_id());

		if (professor != null) {
			tmp.setSubject(professor.getMajor());
			tmp.setProfessor_name(professor.getFirst_name() + " " + professor.getLast_name());
		}
		// tmp.setCourse_name(cDao.findById(rs.getLong("course_id")).getCourse_name());
		// tmp.setTutor_email(udao.findById(rs.getLong("user_id_student")).getEmail());
		// tmp.setTutor_first_name(udao.findById(rs.getLong("user_id_student")).getFirst_name());
		// tmp.setTutor_last_name(udao.findById(rs.getLong("user_id_student")).getLast_name());
		// tmp.setSubject(udao.findById(tmp.getProfessor_id()).getMajor());
		// tmp.setProfessor_name(udao.findById(tmp.getProfessor_id()).getFirst_name()
		// + " "
		// + udao.findById(tmp.getProfessor_id()).getLast_name());

		int numOfReviews = tDao.listTutorReviewsByTutor(tmp.getStudent_id()).size();
		float sumOfReviews = tDao.getTutorAverageRating(tmp.getStudent_id());
		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.DOWN);
		float overall;
		if (numOfReviews != 0) {
			overall = Float.parseFloat(df.format((float) sumOfReviews / (6 * numOfReviews)));
		} else {
			overall = 0;
		}
		tmp.setOverall(overall);

		return tmp;
	}

	public Tutor save(Tutor tutor) {
		final String sql = "INSERT INTO " + TUTOR_TABLE
				+ "(user_id_student, user_id_professor, course_id) Values(?,?,?)";

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, tutor.getStudent_id());
			ps.setLong(2, tutor.getProfessor_id());
			ps.setLong(3, tutor.getCourse_id());
			ps.executeUpdate();
			ps.close();
			return tutor;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Stores a tutor appointment in the database
	 *
	 * @param appointment
	 * @return
	 */
	public TutorAppointment saveTutorAppointment(TutorAppointment appointment) {
		final String sql = "INSERT INTO " + APPOINTMENT_TABLE + "(student_id, tutor_id, date, time, student_message, "
				+ "tutor_message, tutor_has_responded, appointment_status, appointment_past, appointment_reviewed, "
				+ "relationship_id) Values(?,?,?,?,?,?,?,?,?,?,?)";

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, appointment.getStudent_id());
			ps.setLong(2, appointment.getTutor_id());
			ps.setString(3, appointment.getDate());
			ps.setString(4, appointment.getTime());
			ps.setString(5, appointment.getStudent_message());
			ps.setString(6, appointment.getTutor_message());
			ps.setBoolean(7, appointment.getTutor_has_responded());
			ps.setBoolean(8, appointment.getAppointment_status());
			ps.setBoolean(9, appointment.isAppointment_past());
			ps.setBoolean(10, appointment.isAppointment_reviewed());
			ps.setLong(11, appointment.getRelationship_id());
			ps.executeUpdate();
			ps.close();
			return appointment;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Gets a list of appointments for a specific tutor
	 *
	 * @param tutor_id
	 * @return
	 */
	public List<TutorAppointment> listAllTutorAppointments(Long tutor_id) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE tutor_id = " + tutor_id;

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				appointments.add(appointmentMapRow(rs));
			}
			ps.close();
			return appointments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appointments;
	}

	/**
	 * Builds a tutor object
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private TutorAppointment appointmentMapRow(ResultSet rs) throws SQLException {
		// create student course object
		UserDao uDao = new UserDao(this.db);

		TutorAppointment tmp = new TutorAppointment();
		tmp.setAppointment_id(rs.getLong("appointment_id"));
		tmp.setStudent_id(rs.getLong("student_id"));
		tmp.setTutor_id(rs.getLong("tutor_id"));
		tmp.setDate(rs.getString("date"));
		tmp.setTime(rs.getString("time"));
		tmp.setStudent_message(rs.getString("student_message"));
		tmp.setTutor_message(rs.getString("tutor_message"));
		tmp.setTutor_has_responded(rs.getBoolean("tutor_has_responded"));
		tmp.setAppointment_status(rs.getBoolean("appointment_status"));
		tmp.setAppointment_past(rs.getBoolean("appointment_past"));
		tmp.setAppointment_reviewed(rs.getBoolean("appointment_reviewed"));

		User student = uDao.findById(tmp.getStudent_id());

		if (student != null) {
			tmp.setStudent_firstname(student.getFirst_name());
			tmp.setStudent_lastname(student.getLast_name());
		}

		User tutor = uDao.findById(tmp.getTutor_id());

		if (tutor != null) {
			tmp.setTutor_firstname(tutor.getFirst_name());
			tmp.setTutor_lastname(tutor.getLast_name());
		}
		// tmp.setStudent_firstname(uDao.findById(tmp.getStudent_id()).getFirst_name());
		// tmp.setStudent_lastname(uDao.findById(tmp.getStudent_id()).getLast_name());
		// tmp.setTutor_firstname(uDao.findById(tmp.getTutor_id()).getFirst_name());
		// tmp.setTutor_lastname(uDao.findById(tmp.getTutor_id()).getLast_name());
		tmp.setRelationship_id(rs.getLong("relationship_id"));

		return tmp;
	}

	/**
	 * Finds an appointment by appointment ID
	 *
	 * @param id
	 * @return
	 */
	public TutorAppointment findAppointmentByID(long id) {
		// Declare SQL template query
		TutorAppointment appointment = null;
		String sql = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE appointment_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			q.setLong(1, id);

			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				appointment = appointmentMapRow(rs);
				q.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appointment;

	}

	/**
	 * Updates a tutor appointment with the tutor response
	 *
	 * @param appointment
	 * @return
	 */
	public TutorAppointment updateTutorResponse(TutorAppointment appointment) {
		String sql = "UPDATE " + APPOINTMENT_TABLE + " SET tutor_message = ? WHERE appointment_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setString(1, appointment.getTutor_message());
			ps.setLong(2, appointment.getAppointment_id());
			// Runs query
			ps.execute();
			ps.close();
			return appointment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * Updates a tutor appointment with the tutor response
	 *
	 * @param appointment
	 * @return
	 */
	public TutorAppointment updateApt(TutorAppointment appointment) {
		String sql = "UPDATE " + APPOINTMENT_TABLE
				+ " SET tutor_id = ?, appointment_status = ? WHERE appointment_id = ?";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, appointment.getTutor_id());
			ps.setBoolean(2, appointment.getAppointment_status());
			ps.setLong(3, appointment.getAppointment_id());
			// Runs query
			ps.execute();
			ps.close();
			return appointment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * Allows a student to cancel a tutor appointment
	 *
	 * @param appointment_id
	 */
	public void cancelTutorAppointment(long appointment_id) {
		String sql = "DELETE FROM " + APPOINTMENT_TABLE + " WHERE appointment_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, appointment_id);
			// Runs query
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
	}

	/**
	 * Approves an appointment request
	 *
	 * @param appointment
	 * @return
	 */
	public TutorAppointment setTutorResponded(TutorAppointment appointment) {
		String sql = "UPDATE " + APPOINTMENT_TABLE + " SET tutor_has_responded = 1 WHERE appointment_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, appointment.getAppointment_id());
			// Runs query
			ps.execute();
			ps.close();
			return appointment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * If an appointment has already occurred, this flag will be switched
	 *
	 * @param appointment
	 * @return
	 */
	public TutorAppointment setAppointmentPast(TutorAppointment appointment) {
		String sql = "UPDATE " + APPOINTMENT_TABLE + " SET appointment_past = 1 WHERE appointment_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, appointment.getAppointment_id());
			// Runs query
			ps.execute();
			ps.close();
			return appointment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * Denies an appointment request
	 *
	 * @param appointment
	 * @return
	 */
	public TutorAppointment approveAppointment(TutorAppointment appointment) {
		String sql = "UPDATE " + APPOINTMENT_TABLE + " SET appointment_status = 1 WHERE appointment_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, appointment.getAppointment_id());
			// Runs query
			ps.execute();
			ps.close();
			return appointment;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	/**
	 * Lists requests which have not been viewed by tutor
	 *
	 * @param tutor_id
	 * @return
	 */
	public List<TutorAppointment> listAllUnviewedTutorAppointments(Long tutor_id) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE tutor_has_responded = 0 "
				+ "AND appointment_past = 0 AND tutor_id = " + tutor_id;

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<TutorAppointment>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				appointments.add(appointmentMapRow(rs));
			}
			ps.close();
			return appointments;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appointments;
	}

	/**
	 * Lists all tutor appointments
	 *
	 * @return
	 */
	public List<TutorAppointment> listAllAppointments() {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE;

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				appointments.add(appointmentMapRow(rs));
			}
			ps.close();
			return appointments;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appointments;
	}

	/**
	 * Lists all approved tutor appointments
	 *
	 * @param tutor_id
	 * @return
	 */
	public List<TutorAppointment> listAllApprovedTutorAppointments(Long tutor_id) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE appointment_status = 1 AND tutor_id = "
				+ tutor_id;

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				appointments.add(appointmentMapRow(rs));
			}
			ps.close();
			return appointments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appointments;
	}

	/**
	 * Lists a student's upcoming appointments
	 *
	 * @param user
	 * @return
	 */
	public List<TutorAppointment> listAllStudentAppointments(User user) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE student_id = " + user.getId();

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					appointments.add(appointmentMapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return appointments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appointments;
	}

	/**
	 * Lists a student's upcoming appointments
	 *
	 * @param Dept
	 * @return
	 */
	public List<TutorAppointment> listUpcomingAllApptByDept(String Dept) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE
				+ " WHERE tutor_id in (Select user_id from users where major = '" + Dept + "' and role_id = 3)";

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					appointments.add(appointmentMapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return appointments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<TutorAppointment> listPastAllApptByDept(String Dept) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE
				+ " WHERE tutor_id in (Select user_id from users where major = '" + Dept
				+ "' and role_id = 3) AND appointment_past = 1";

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					appointments.add(appointmentMapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return appointments;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return appointments;
	}

	/**
	 * @return all users from the database.
	 */

	public List<Tutor> all(Long id) {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " WHERE user_id_professor = " + id;

		List<Tutor> tutors = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			tutors = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					tutors.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return tutors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tutors;
	}

	/**
	 * Gets tutor for a specific course
	 *
	 * @param course_id
	 * @return
	 */
	public List<Tutor> getTutorByCourseName(Long course_id) {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " WHERE course_id = " + course_id + " LIMIT 1";

		List<Tutor> tutors = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			tutors = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
				while (rs.next()) {
					tutors.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return tutors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tutors;
	}

	public List<Tutor> allbyDept(String department) {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " WHERE user_id_professor = " + department;

		List<Tutor> tutors = null;
		try (Connection conn = this.db.getConnection()) {
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
			ps.close();
			return tutors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tutors;
	}

	/**
	 * Selects all tutors from the table
	 *
	 * @return
	 */
	public List<Tutor> listAllTutors() {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " ORDER BY user_id_student DESC";

		List<Tutor> tutors = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			tutors = new ArrayList<>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				tutors.add(mapRow(rs));
			}
			ps.close();
			return tutors;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tutors;
	}

	/**
	 * Lists tutors by major
	 *
	 * @return
	 */
	public List<Tutor> listAllTutorsByMajor(User user) {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " WHERE user_id_student "
				+ "IN (SELECT user_id FROM users WHERE role_id = 3 AND major = '" + user.getMajor() + "')";

		List<Tutor> tutors = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			tutors = new ArrayList<>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				tutors.add(mapRow(rs));
			}
			ps.close();
			return tutors;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tutors;
	}

	/**
	 * Selects all tutors from the table
	 *
	 * @param course_id
	 * @return
	 */
	public List<Tutor> listAllCourseTutors(Long course_id) {
		final String SELECT = "SELECT * FROM " + TUTOR_TABLE + " WHERE course_id = '" + course_id + "'";

		List<Tutor> tutors = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			tutors = new ArrayList<>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				tutors.add(mapRow(rs));
			}
			ps.close();
			return tutors;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tutors;
	}

	public Tutor findById(long id) {
		return find(id);
	}

	public Tutor findByStudentId(long id) {
		// Declare SQL template query
		Tutor tutor = null;
		String sql = "SELECT * FROM " + TUTOR_TABLE + " WHERE user_id_student = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				tutor = mapRow(rs);
				q.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return tutor;

	}

	public Long getStudentId(long id) {
		// Declare SQL template query
		long studentID = -1;
		String sql = "SELECT USER_ID_STUDENT FROM " + TUTOR_TABLE + " WHERE tutor_relationship_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				studentID = rs.getLong(1);
				q.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If you don't find a model
		return studentID;

	}

	public String deleteTutor(long id) {

		String sql = "DELETE FROM " + TUTOR_TABLE + " WHERE tutor_relationship_id = ? LIMIT 1";

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

	public void changeTutorRole(Long id) {
		String sql = "UPDATE " + USER_TABLE + " SET role_id = ? WHERE user_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setInt(1, 4);
			ps.setLong(2, id);

			// Runs query
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
	}

	public TutorForm updateTutor(TutorForm tutor) {
		String sql = "UPDATE " + TUTOR_TABLE + " SET course_id = ? WHERE tutor_relationship_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, tutor.getCourse_id());
			ps.setLong(2, tutor.getId());

			// Runs query
			ps.execute();
			ps.close();
			return tutor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	public TutorForm adminUpdateTutor(TutorForm tutor) {
		String sql = "UPDATE " + TUTOR_TABLE + " SET user_id_student = ? WHERE tutor_relationship_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement ps = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			ps.setLong(1, tutor.getStudent_id());
			ps.setLong(2, tutor.getId());

			// Runs query
			ps.execute();
			ps.close();
			return tutor;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// If you don't find a model
		return null;
	}

	public List<Tutor> search(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM tutors WHERE course_name LIKE '%" + sText + "%'";

		List<Tutor> tutors = null;

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(NAME_SQL);

			tutors = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(NAME_SQL);
				while (rs.next()) {
					tutors.add(mapRow(rs));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			ps.close();
			return tutors;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tutors;
	}

	public List<TutorAppointment> searchApt(String sType, String sText) throws SQLException {
		String NAME_SQL = "SELECT * FROM tutor_appointment WHERE (student_firstname LIKE '%" + sText
				+ "%' OR student_lastname LIKE '%" + sText + "%' OR tutor_firstname LIKE '%" + sText
				+ "%' OR tutor_lastname LIKE '%" + sText + "%')";

		List<TutorAppointment> apts = null;

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(NAME_SQL);
			apts = new ArrayList<>();
			ResultSet rs = ps.executeQuery(NAME_SQL);
			while (rs.next()) {
				apts.add(appointmentMapRow(rs));
			}
			ps.close();
			return apts;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apts;
	}

	/**
	 * Lists reviews which have not been reviewed
	 *
	 * @param student
	 * @return
	 */
	public List<TutorAppointment> listAllNotReviewedTutorAppointments(User student) {
		final String SELECT = "SELECT * FROM " + APPOINTMENT_TABLE + " WHERE student_id = " + student.getId()
				+ " AND appointment_status = 1 AND appointment_past = 1 AND appointment_reviewed = 0";

		List<TutorAppointment> appointments = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			appointments = new ArrayList<TutorAppointment>();
			ResultSet rs = ps.executeQuery(SELECT);
			while (rs.next()) {
				appointments.add(appointmentMapRow(rs));
			}
			ps.close();
			return appointments;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return appointments;
	}

	@Override
	public Tutor find(Long id) {
		Tutor tutor = null;
		String sql = "SELECT * FROM " + TUTOR_TABLE + " WHERE tutor_relationship_id = ? LIMIT 1";
		try (Connection conn = this.db.getConnection()) {
			// Create Prepared Statement from query
			PreparedStatement q = conn.prepareStatement(sql);
			// Fill in the ? with the parameters you want
			q.setLong(1, id);

			// Run your shit
			ResultSet rs = q.executeQuery();
			if (rs.next()) {
				tutor = mapRow(rs);
				q.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tutor;
	}

	@Override
	public List<Tutor> all() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Saves a tutor review in the database
	 *
	 * @param review
	 */
	public void saveTutorReview(TutorReview review) {

		final String sql = "INSERT INTO " + REVIEW_TABLE + "(appointment_id, enhance_understanding, "
				+ "simple_examples, professional, prepared, schedule_again, recommend, comment,"
				+ "student_id, tutor_id) Values(?,?,?,?,?,?,?,?,?,?)";

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, review.getAppointment_id());
			ps.setInt(2, review.getEnhance_understanding());
			ps.setInt(3, review.getSimple_examples());
			ps.setInt(4, review.getProfessional());
			ps.setInt(5, review.getPrepared());
			ps.setInt(6, review.getSchedule_again());
			ps.setInt(7, review.getRecommend());
			ps.setString(8, review.getComment());
			ps.setLong(9, review.getStudent_id());
			ps.setLong(10, review.getTutor_id());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Sets an appointment in the database as reviewed
	 *
	 * @param appointment
	 */
	public void setAppointmentReviewed(TutorAppointment appointment) {
		String sql = "UPDATE " + APPOINTMENT_TABLE + " SET appointment_reviewed = 1 WHERE appointment_id = ? LIMIT 1";

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, appointment.getAppointment_id());
			ps.execute();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lists all tutor reviews for a given tutor
	 *
	 * @param tutor_id
	 * @return
	 */
	public List<TutorReview> listTutorReviewsByTutor(long tutor_id) {
		final String SELECT = "SELECT * FROM " + REVIEW_TABLE + " WHERE tutor_id = " + tutor_id;

		List<TutorReview> reviews = null;
		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(SELECT);
			reviews = new ArrayList<>();
			try {
				ResultSet rs = ps.executeQuery(SELECT);
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

	/**
	 * returns tutor review object
	 *
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private TutorReview reviewMapRow(ResultSet rs) throws SQLException {
		// UserDao udao = new UserDao(conn);

		TutorReview tmp = new TutorReview();

		tmp.setReview_id(rs.getLong("review_id"));
		tmp.setAppointment_id(rs.getLong("appointment_id"));
		tmp.setEnhance_understanding(rs.getInt("enhance_understanding"));
		tmp.setSimple_examples(rs.getInt("simple_examples"));
		tmp.setProfessional(rs.getInt("professional"));
		tmp.setPrepared(rs.getInt("prepared"));
		tmp.setSchedule_again(rs.getInt("schedule_again"));
		tmp.setRecommend(rs.getInt("recommend"));
		tmp.setComment(rs.getString("comment"));
		tmp.setStudent_id(rs.getLong("student_id"));
		tmp.setTutor_id(rs.getLong("tutor_id"));

		return tmp;
	}

	/**
	 * Gets average rating of a tutor
	 *
	 * @param tutor_id
	 * @return
	 */
	public float getTutorAverageRating(long tutor_id) {
		float average = 0;
		String sql = "SELECT SUM(enhance_understanding + simple_examples + professional + "
				+ "prepared + schedule_again + recommend) FROM " + REVIEW_TABLE + "  WHERE tutor_id = ?";

		try (Connection conn = this.db.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, tutor_id);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				average = rs.getFloat(1);
				ps.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return average;
	}
}
