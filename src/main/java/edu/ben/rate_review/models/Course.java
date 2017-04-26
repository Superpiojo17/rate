package edu.ben.rate_review.models;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;

/**
 * Model for course
 * 
 * @author Mike
 * @version 4-26-2017
 */
public class Course {
	private long id;
	private String course_name;
	private long professor_id;
	private String subject;
	private String semester;
	private int year;
	private long course_number;

	/**
	 * ID getter
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * ID setter
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Course name getter
	 * 
	 * @return
	 */
	public String getCourse_name() {
		return course_name;
	}

	/**
	 * Course name setter
	 * 
	 * @param course_name
	 */
	public void setCourse_name(String course_name) {
		this.course_name = course_name;
	}

	/**
	 * Subject getter
	 * 
	 * @return
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Subject setter
	 * 
	 * @param subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Getter for professor ID
	 * 
	 * @return
	 */
	public long getProfessor_id() {
		return professor_id;
	}

	/**
	 * Setter for professor ID
	 * 
	 * @param professor_id
	 */
	public void setProfessor_id(long professor_id) {

		this.professor_id = professor_id;
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
	 * Getter for professor name
	 * 
	 * @return
	 */
	public String getProfessor_name() {
		if (professor_id == 0) {
			return "This course has not been assigned!";
		} else {

			UserDao user = DaoManager.getInstance().getUserDao();
			User u = user.findById(professor_id);
			user.close();
			return u.getLast_name() + ", " + u.getFirst_name();
		}
	}

	/**
	 * Getter for course number
	 * 
	 * @return
	 */
	public long getCourse_number() {
		return course_number;
	}

	/**
	 * Setter for course number
	 * 
	 * @param course_number
	 */
	public void setCourse_number(long course_number) {
		this.course_number = course_number;
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
