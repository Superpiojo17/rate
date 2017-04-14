package edu.ben.rate_review.models;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;

public class Tutor {

	private Long id;
	private Long student_id;
	private Long professor_id;
	private String course_name;
	private String tutor_first_name;
	private String tutor_last_name;
	private String tutor_email;
	private float overall;
	// private String subject;
	// private String professor_name;

	public Tutor() {
		super();
	}

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

	public String getSubject() {
		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		User user = ud.findById(professor_id);
		return user.getMajor();
	}

	public String getProfessor_name() {
		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		User user = ud.findById(professor_id);
		String professor_name = user.getFirst_name() + ", " + user.getLast_name();
		return professor_name;
	}

	/**
	 * Returns tutor's overall rating
	 * 
	 * @return
	 */
	public float getOverall() {
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		int numOfReviews = tDao.listTutorReviewsByTutor(student_id).size();
		float sumOfReviews = tDao.getTutorAverageRating(student_id);
		DecimalFormat df = new DecimalFormat("##.##");
		df.setRoundingMode(RoundingMode.DOWN);
		if (numOfReviews != 0) {
			overall = Float.parseFloat(df.format((float) sumOfReviews / (6 * numOfReviews)));
		} else {
			overall = 0;
		}

		return overall;
	}
}
