package edu.ben.rate_review.models;

public class Course {
	private long id;
	private String course_name;
	private long professor_id;
	private String subject;
	private String term;

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

}
