package edu.ben.rate_review.models;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;

/**
 * Model for the review of a professor
 * 
 * @author Mike
 * @version 2-17-2017
 */

public class ProfessorReview {

	public ProfessorReview() {

	}

	/**
	 * Constructor
	 * 
	 * @param course
	 */
	public ProfessorReview(StudentInCourse course) {
		super();
		this.student_course_id = course.getStudent_course_id();
		this.student_id = course.getStudent_id();
		this.professor_first_name = course.getProfessor_first_name();
		this.professor_last_name = course.getProfessor_last_name();
		this.course = course.getCourse_subject_number();
		this.semester = course.getSemester();
		this.year = course.getYear();
		this.professor_email = course.getProfessor_email();
	}

	private long student_course_id;
	private long student_id;
	private String professor_first_name;
	private String professor_last_name;
	private String course;
	private int year;
	private String semester;
	private String comment;
	private String professor_email;
	private boolean comment_flagged;
	private boolean comment_removed;
	private boolean comment_approved;
	private String studentname;
	// private float overall;

	public boolean getComment_approved() {
		return comment_approved;
	}

	public void setComment_approved(boolean comment_approved) {
		this.comment_approved = comment_approved;
	}

	public boolean getComment_flagged() {
		return comment_flagged;
	}

	public void setComment_flagged(boolean comment_flagged) {
		this.comment_flagged = comment_flagged;
	}

	public boolean getComment_removed() {
		return comment_removed;
	}

	public void setComment_removed(boolean comment_removed) {
		this.comment_removed = comment_removed;
	}

	public String getProfessor_email() {
		return professor_email;
	}

	public void setProfessor_email(String professor_email) {
		this.professor_email = professor_email;
	}

	// The course objectives were met:
	private int rate_objectives;
	// The course material was presented in an organized manner:
	private int rate_organized;
	// I found the course material to be challenging:
	private int rate_challenging;
	// Amount of work I performed outside scheduled class time:
	private int rate_outside_work;
	// Course material was presented at a good pace:
	private int rate_pace;
	// Assignments contributed effectively to my learning experience:
	private int rate_assignments;
	// Homework and exams were graded fairly:
	private int rate_grade_fairly;
	// Homework and exams were graded in reasonable time:
	private int rate_grade_time;
	// Accessibility of the instructor outside of scheduled class time:
	private int rate_accessibility;
	// The instructor's knowledge of the subject was relevant and up-to-date:
	private int rate_knowledge;
	// I found this course was valuable for my career development:
	private int rate_career_development;

	public long getStudent_course_id() {
		return student_course_id;
	}

	public void setStudent_course_id(long student_course_id) {
		this.student_course_id = student_course_id;
	}

	/**
	 * Getter for student
	 * 
	 * @return
	 */
	public long getStudent_id() {
		return student_id;
	}

	/**
	 * Setter for student
	 * 
	 * @param student
	 */
	public void setStudent_id(long student_id) {
		this.student_id = student_id;
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
	 * Getter for course
	 * 
	 * @return
	 */
	public String getCourse() {
		return course;
	}

	/**
	 * Setter for course
	 * 
	 * @param comment
	 */
	public void setCourse(String course) {
		this.course = course;
	}

	/**
	 * Getter for course
	 * 
	 * @return
	 */
	public int getYear() {
		return year;
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
	 * Setter for course
	 * 
	 * @param comment
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Getter for comment
	 * 
	 * @return
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Setter for comment
	 * 
	 * @param comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Getter for objective review
	 * 
	 * @return
	 */
	public int getRate_objectives() {
		return rate_objectives;
	}

	/**
	 * Setter for rate objective review
	 * 
	 * @return
	 */
	public void setRate_objectives(int rate_objectives) {
		this.rate_objectives = rate_objectives;
	}

	/**
	 * Getter for organized review
	 * 
	 * @return
	 */
	public int getRate_organized() {
		return rate_organized;
	}

	/**
	 * Setter for rate organized review
	 * 
	 * @return
	 */
	public void setRate_organized(int rate_organized) {
		this.rate_organized = rate_organized;
	}

	/**
	 * Getter for challenging review
	 * 
	 * @return
	 */
	public int getRate_challenging() {
		return rate_challenging;
	}

	/**
	 * Setter for rate challenging review
	 * 
	 * @return
	 */
	public void setRate_challenging(int rate_challenging) {
		this.rate_challenging = rate_challenging;
	}

	/**
	 * Getter for outside work review
	 * 
	 * @return
	 */
	public int getRate_outside_work() {
		return rate_outside_work;
	}

	/**
	 * Setter for rate outside work review
	 * 
	 * @return
	 */
	public void setRate_outside_work(int rate_outside_work) {
		this.rate_outside_work = rate_outside_work;
	}

	/**
	 * Getter for rate pace review
	 * 
	 * @return
	 */
	public int getRate_pace() {
		return rate_pace;
	}

	/**
	 * Setter for rate prace review
	 * 
	 * @return
	 */
	public void setRate_pace(int rate_pace) {
		this.rate_pace = rate_pace;
	}

	/**
	 * Getter for rate assignments review
	 * 
	 * @return
	 */
	public int getRate_assignments() {
		return rate_assignments;
	}

	/**
	 * Setter for rate assignments review
	 * 
	 * @return
	 */
	public void setRate_assignments(int rate_assignments) {
		this.rate_assignments = rate_assignments;
	}

	/**
	 * Getter for grade fairly review
	 * 
	 * @return
	 */
	public int getRate_grade_fairly() {
		return rate_grade_fairly;
	}

	/**
	 * Setter for rate grade fairly review
	 * 
	 * @return
	 */
	public void setRate_grade_fairly(int rate_grade_fairly) {
		this.rate_grade_fairly = rate_grade_fairly;
	}

	/**
	 * Getter for grade time review
	 * 
	 * @return
	 */
	public int getRate_grade_time() {
		return rate_grade_time;
	}

	/**
	 * Setter for rate grade time review
	 * 
	 * @return
	 */
	public void setRate_grade_time(int rate_grade_time) {
		this.rate_grade_time = rate_grade_time;
	}

	/**
	 * Getter for rate accessibility review
	 * 
	 * @return
	 */
	public int getRate_accessibility() {
		return rate_accessibility;
	}

	/**
	 * Setter for rate accessibility review
	 * 
	 * @return
	 */
	public void setRate_accessibility(int rate_accessibility) {
		this.rate_accessibility = rate_accessibility;
	}

	/**
	 * Getter for rate knowledge review
	 * 
	 * @return
	 */
	public int getRate_knowledge() {
		return rate_knowledge;
	}

	/**
	 * Setter for rate knowledge review
	 * 
	 * @return
	 */
	public void setRate_knowledge(int rate_knowledge) {
		this.rate_knowledge = rate_knowledge;
	}

	/**
	 * Getter for rate career development review
	 * 
	 * @return
	 */
	public int getRate_career_development() {
		return rate_career_development;
	}

	/**
	 * Setter for rate career development review
	 * 
	 * @return
	 */
	public void setRate_career_development(int rate_career_development) {
		this.rate_career_development = rate_career_development;
	}

	public String getOverall() {
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		return (df.format((float) (rate_objectives + rate_organized + rate_challenging + rate_outside_work + rate_pace
				+ rate_assignments + rate_grade_fairly + rate_grade_time + rate_accessibility + rate_knowledge
				+ rate_career_development) / 11));
	}

	public String getStudentname() {
		return studentname;
	}

	public void setStudentName(String studentname) {
		this.studentname = studentname;
	}
}
