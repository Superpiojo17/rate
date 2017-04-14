package edu.ben.rate_review.models;

public class TutorReview {
	private long review_id;
	private long appointment_id;
	private long student_id;
	private long tutor_id;
	// Did appointment enhance your understanding of the material
	private int enhance_understanding;
	// Did the tutor provide simpler examples of difficult material
	private int simple_examples;
	// Did the tutor conduct the appointment in a professional manner
	private int professional;
	// Was the tutor prepared for the appointment
	private int prepared;
	// Would you schedule another appointment with this tutor
	private int schedule_again;
	// Would you recomment this tutor to other students
	private int recommend;
	private String comment;
	
	public void setReview_id(long review_id) {
		this.review_id = review_id;
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

	public long getReview_id() {
		return review_id;
	}

	public long getAppointment_id() {
		return appointment_id;
	}

	public void setAppointment_id(long appointment_id) {
		this.appointment_id = appointment_id;
	}

	public int getEnhance_understanding() {
		return enhance_understanding;
	}

	public void setEnhance_understanding(int enhance_understanding) {
		this.enhance_understanding = enhance_understanding;
	}

	public int getSimple_examples() {
		return simple_examples;
	}

	public void setSimple_examples(int simple_examples) {
		this.simple_examples = simple_examples;
	}

	public int getProfessional() {
		return professional;
	}

	public void setProfessional(int professional) {
		this.professional = professional;
	}

	public int getPrepared() {
		return prepared;
	}

	public void setPrepared(int prepared) {
		this.prepared = prepared;
	}

	public int getSchedule_again() {
		return schedule_again;
	}

	public void setSchedule_again(int schedule_again) {
		this.schedule_again = schedule_again;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
