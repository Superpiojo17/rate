package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.models.ProfessorReview;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Controller for the review of a professor
 * 
 * @author Mike
 * @version 2-17-2016
 */
public class ProfessorReviewController {

	/**
	 * Show professor review page
	 */
	public ModelAndView showReviewProfessorPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/reviewprofessor.hbs");
	}

	/**
	 * Grabs information from form and sends it to createReview method
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String reviewProfessor(Request req, Response res) {
		if (!req.queryParams("professor_email").isEmpty() && !req.queryParams("student_email").isEmpty()
				&& !req.queryParams("course").isEmpty() && !req.queryParams("current_year").isEmpty()) {
			createReview(req.queryParams("professor_email"), req.queryParams("course"),
					req.queryParams("student_email"), Integer.parseInt(req.queryParams("current_year")),
					req.queryParams("comment"), Integer.parseInt(req.queryParams("rate_objectives")),
					Integer.parseInt(req.queryParams("rate_organized")),
					Integer.parseInt(req.queryParams("rate_challenging")),
					Integer.parseInt(req.queryParams("rate_outside_work")),
					Integer.parseInt(req.queryParams("rate_pace")),
					Integer.parseInt(req.queryParams("rate_assignments")),
					Integer.parseInt(req.queryParams("rate_grade_fairly")),
					Integer.parseInt(req.queryParams("rate_grade_time")),
					Integer.parseInt(req.queryParams("rate_accessibility")),
					Integer.parseInt(req.queryParams("rate_knowledge")),
					Integer.parseInt(req.queryParams("rate_career_development")));

			res.redirect("/professor");
		} else {
			// fields were empty
			res.redirect("/reviewprofessor");
		}
		return "";
	}

	/**
	 * Packages information into ProfessorReview object and sends it to the Dao
	 * to store in the database
	 * 
	 * @param professor_email
	 * @param course
	 * @param student_email
	 * @param current_year
	 * @param comment
	 * @param rate_objectives
	 * @param rate_organized
	 * @param rate_challenging
	 * @param rate_outside_work
	 * @param rate_pace
	 * @param rate_assignments
	 * @param rate_grade_fairly
	 * @param rate_grade_time
	 * @param rate_accessibility
	 * @param rate_knowledge
	 * @param rate_career_development
	 */
	private void createReview(String professor_email, String course, String student_email, int current_year,
			String comment, int rate_objectives, int rate_organized, int rate_challenging, int rate_outside_work,
			int rate_pace, int rate_assignments, int rate_grade_fairly, int rate_grade_time, int rate_accessibility,
			int rate_knowledge, int rate_career_development) {

		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		ProfessorReview review = new ProfessorReview();

		review.setProfessor_email(professor_email);
		review.setCourse(course);
		review.setStudent_email(student_email);
		review.setCurrent_year(current_year);
		review.setComment(comment);
		review.setRate_objectives(rate_objectives);
		review.setRate_organized(rate_organized);
		review.setRate_challenging(rate_challenging);
		review.setRate_outside_work(rate_outside_work);
		review.setRate_pace(rate_pace);
		review.setRate_assignments(rate_assignments);
		review.setRate_grade_fairly(rate_grade_fairly);
		review.setRate_grade_time(rate_grade_time);
		review.setRate_accessibility(rate_accessibility);
		review.setRate_knowledge(rate_knowledge);
		review.setRate_career_development(rate_career_development);

		reviewDao.save(review);
	}

}
