package edu.ben.rate_review.models;

/**
 * Creates an object which stores which courses the user can review
 * 
 * @author Mike
 * @version 2-28-2017
 */
public class CoursesToReview {

	private long course_id;
	private long student_id;
	private String course_name;
	private String semester;
	private int year;
	private String professor_first_name;
	private String professor_last_name;
	private String professor_email;
	private boolean course_reviewed;
	private boolean disable_edit;

	

	public boolean getDisable_edit() {
		return disable_edit;
	}

	public void setDisable_edit(boolean disable_edit) {
		this.disable_edit = disable_edit;
	}

	public boolean getCourse_reviewed() {
		return course_reviewed;
	}

	public void setCourse_reviewed(boolean course_reviewed) {
		this.course_reviewed = course_reviewed;
	}

	public String getProfessor_email() {
		return professor_email;
	}

	public void setProfessor_email(String professor_email) {
		this.professor_email = professor_email;
	}

	public long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(long course_id) {
		this.course_id = course_id;
	}

	public String getProfessor_first_name() {
		return professor_first_name;
	}

	public void setProfessor_first_name(String professor_first_name) {
		this.professor_first_name = professor_first_name;
	}

	public String getProfessor_last_name() {
		return professor_last_name;
	}

	public void setProfessor_last_name(String professor_last_name) {
		this.professor_last_name = professor_last_name;
	}

	/**
	 * Getter for user_id
	 * 
	 * @return
	 */
	public long getStudent_id() {
		return student_id;
	}

	/**
	 * Setter for user_id
	 * 
	 * @param user_id
	 */
	public void setStudent_id(long student_id) {
		this.student_id = student_id;
	}

	/**
	 * Getter for course_name
	 * 
	 * @return
	 */
	public String getCourse_name() {
		return course_name;
	}

	/**
	 * Setter for course_name
	 * 
	 * @param course_name
	 */
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	/**
	 * Getter for semester
	 * 
	 * @return
	 */
	public String getSemester() {
		return semester;
	}

	/**
	 * Setter for semester
	 * 
	 * @param semester
	 */
	public void setSemester(String semester) {
		this.semester = semester;
	}

	/**
	 * Getter for year
	 * 
	 * @return
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Setter for year
	 * 
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}

}
