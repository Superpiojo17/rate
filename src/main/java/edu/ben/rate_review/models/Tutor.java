package edu.ben.rate_review.models;


public class Tutor {

	private Long id;
	private Long student_id;
	private Long professor_id;
	private String course_name;
	private String tutor_first_name;
	private String tutor_last_name;
	private String tutor_email;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStudent_id() {
		return student_id;
	}

	public void setStudent_id(Long student_id) {
		this.student_id = student_id;
	}

	public Long getProfessor_id() {
		return professor_id;
	}

	public void setProfessor_id(Long professor_id) {
		this.professor_id = professor_id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getTutor_first_name() {
		return tutor_first_name;
	}

	public void setTutor_first_name(String tutor_first_name) {
		this.tutor_first_name = tutor_first_name;
	}

	public String getTutor_last_name() {
		return tutor_last_name;
	}

	public void setTutor_last_name(String tutor_last_name) {
		this.tutor_last_name = tutor_last_name;
	}

	public String getTutor_email() {
		return tutor_email;
	}

	public void setTutor_email(String tutor_email) {
		this.tutor_email = tutor_email;
	}

}
