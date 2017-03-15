package edu.ben.rate_review.models;

/**
 * Object which stores a tutor appointment
 * 
 * @author Mike
 *@version 3-15-2017
 */
public class TutorAppointment {
	
	private long student_id;
	private long tutor_id;
	private String date;
	private String student_message;
	private String tutor_message;
	private int appointment_status;

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

	public int getAppointment_status() {
		return appointment_status;
	}

	public void setAppointment_status(int appointment_status) {
		this.appointment_status = appointment_status;
	}

	
	
}
