package edu.ben.rate_review.models;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;

/**
 * Object which stores a tutor appointment
 * 
 * @author Mike
 *@version 3-15-2017
 */
public class TutorAppointment {
	
	private long appointment_id;
	private long student_id;
	private long tutor_id;
	private long relationship_id;
	private String date;
	private String time;
	private String student_message;
	private String tutor_message;
	private boolean tutor_has_responded;
	private boolean appointment_status;
	private boolean appointment_past;
	private boolean appointment_reviewed;
	private String student_firstname;
	private String student_lastname;
	private String tutor_firstname;
	private String tutor_lastname;
	// these are derived
	private String course_name;
	private String department;
	
	public long getRelationship_id() {
		return relationship_id;
	}

	public void setRelationship_id(long relationship_id) {
		this.relationship_id = relationship_id;
	}

	public boolean isAppointment_reviewed() {
		return appointment_reviewed;
	}

	public void setAppointment_reviewed(boolean appointment_reviewed) {
		this.appointment_reviewed = appointment_reviewed;
	}

	public boolean isAppointment_past() {
		return appointment_past;
	}

	public void setAppointment_past(boolean appointment_past) {
		this.appointment_past = appointment_past;
	}

	public long getAppointment_id() {
		return appointment_id;
	}

	public void setAppointment_id(long appointment_id) {
		this.appointment_id = appointment_id;
	}

	public long getStudent_id() {
		return student_id;
	}

	public void setStudent_id(long student_id) {
		this.student_id = student_id;
	}

	public long getTutor_id() {
		return tutor_id;
	}

	public void setTutor_id(long tutor_id) {
		this.tutor_id = tutor_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getStudent_message() {
		return student_message;
	}

	public void setStudent_message(String student_message) {
		this.student_message = student_message;
	}

	public String getTutor_message() {
		return tutor_message;
	}

	public void setTutor_message(String tutor_message) {
		this.tutor_message = tutor_message;
	}

	public boolean getTutor_has_responded() {
		return tutor_has_responded;
	}

	public void setTutor_has_responded(boolean tutor_has_responded) {
		this.tutor_has_responded = tutor_has_responded;
	}

	public boolean getAppointment_status() {
		return appointment_status;
	}

	public void setAppointment_status(boolean appointment_status) {
		this.appointment_status = appointment_status;
	}

	public String getStudent_firstname() {
		return student_firstname;
	}

	public void setStudent_firstname(String student_firstname) {
		this.student_firstname = student_firstname;
	}

	public String getStudent_lastname() {
		return student_lastname;
	}

	public void setStudent_lastname(String student_lastname) {
		this.student_lastname = student_lastname;
	}

	public String getTutor_firstname() {
		return tutor_firstname;
	}

	public void setTutor_firstname(String tutor_firstname) {
		this.tutor_firstname = tutor_firstname;
	}

	public String getTutor_lastname() {
		return tutor_lastname;
	}

	public void setTutor_lastname(String tutor_lastname) {
		this.tutor_lastname = tutor_lastname;
	}
	
	public String getCourse_name(){
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		Tutor t = tDao.findById(relationship_id);
		System.out.println(t.getProfessor_name());
		return t.getCourse_name();
	}

	public String getDepartment() {
		DaoManager dao = DaoManager.getInstance();
		TutorDao tDao = dao.getTutorDao();
		UserDao uDao = dao.getUserDao();
		Tutor tutor = tDao.findById(relationship_id);
		User tutor_account = uDao.findById(tutor.getProfessor_id());
		return tutor_account.getMajor();
	}
	
}
