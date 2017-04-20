package edu.ben.rate_review.models;

public class StudentInCourse {
	private long student_course_id;
	private long course_id;
	private long student_id;
	private boolean course_reviewed;
	private boolean disable_edit;
	private boolean semester_past;
	private String professor_first_name;
	private String professor_last_name;
	private String professor_email;
	private String semester;
	private int year;
	private String course_subject_number;

	public long getStudent_course_id() {
		return student_course_id;
	}

	public void setStudent_course_id(long student_course_id) {
		this.student_course_id = student_course_id;
	}

	public long getCourse_id() {
		return course_id;
	}

	public void setCourse_id(long course_id) {
		this.course_id = course_id;
	}

	public long getStudent_id() {
		return student_id;
	}

	public void setStudent_id(long student_id) {
		this.student_id = student_id;
	}

	public boolean isCourse_reviewed() {
		return course_reviewed;
	}

	public void setCourse_reviewed(boolean course_reviewed) {
		this.course_reviewed = course_reviewed;
	}

	public boolean isDisable_edit() {
		return disable_edit;
	}

	public void setDisable_edit(boolean disable_edit) {
		this.disable_edit = disable_edit;
	}

	public boolean isSemester_past() {
		return semester_past;
	}

	public void setSemester_past(boolean semester_past) {
		this.semester_past = semester_past;
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

	public String getProfessor_email() {
		return professor_email;
	}

	public void setProfessor_email(String professor_email) {
		this.professor_email = professor_email;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCourse_subject_number() {
		return course_subject_number;
	}

	public void setCourse_subject_number(String course_subject_number) {
		this.course_subject_number = course_subject_number;
	}

}
