package edu.ben.rate_review.models;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;

public class Course {
	private long id;
	private String course_name;
	private long professor_id;
	private String professor_name;
	private String subject;
	private String term;
	private long course_number;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCourse_name() {
		return course_name;
	}

	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public long getProfessor_id() {
		return professor_id;
	}

	public void setProfessor_id(long professor_id) {

		this.professor_id = professor_id;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getProfessor_name() {
		if (professor_id == 0) {
			return "This course has not been assigned!";
		} else {

			UserDao user = DaoManager.getInstance().getUserDao();
			User u = user.findById(professor_id);
			return u.getLast_name() + ", " + u.getFirst_name();
		}
	}

	public void setProfessor_name(String professor_name) {
		this.professor_name = professor_name;
	}

	public long getCourse_number() {
		return course_number;
	}

	public void setCourse_number(long course_number) {
		this.course_number = course_number;
	}

}
