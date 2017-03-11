package edu.ben.rate_review.models;

public class TutorForm {
	private long id;
	private String Course;
	
	
	public TutorForm() {
		super();
	}

	public TutorForm(Tutor tutor) {
		this.setId(tutor.getId());
	}
	
	public Tutor build() {
		Tutor tutor = new Tutor();

		return tutor;
	}

	public String getCourse() {
		return Course;
	}

	public void setCourse(String course) {
		Course = course;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
