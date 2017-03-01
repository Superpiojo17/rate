package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;
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
		
		CoursesToReview courses = new CoursesToReview();
		courses.setStudent_id(Long.parseLong(req.params("student_id")));
		courses.setProfessor_first_name(req.params("professor_first_name"));
		courses.setProfessor_last_name(req.params("professor_last_name"));
		courses.setCourse_name(req.params("course_name"));
		courses.setSemester(req.params("semester"));
		courses.setYear(Integer.parseInt(req.params("year")));

		// create the form object, put it into request
		//model.put("courses", courses);
		
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

		createReview(req, req.queryParams("comment"), Integer.parseInt(req.queryParams("rate_objectives")),
				Integer.parseInt(req.queryParams("rate_organized")),
				Integer.parseInt(req.queryParams("rate_challenging")),
				Integer.parseInt(req.queryParams("rate_outside_work")), Integer.parseInt(req.queryParams("rate_pace")),
				Integer.parseInt(req.queryParams("rate_assignments")),
				Integer.parseInt(req.queryParams("rate_grade_fairly")),
				Integer.parseInt(req.queryParams("rate_grade_time")),
				Integer.parseInt(req.queryParams("rate_accessibility")),
				Integer.parseInt(req.queryParams("rate_knowledge")),
				Integer.parseInt(req.queryParams("rate_career_development")));

		res.redirect("/professor");

		return "";
	}

	/**
	 * Packages information into ProfessorReview object and sends it to the Dao
	 * to store in the database
	 * 
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
	private void createReview(Request req, String comment, int rate_objectives, int rate_organized, int rate_challenging,
			int rate_outside_work, int rate_pace, int rate_assignments, int rate_grade_fairly, int rate_grade_time,
			int rate_accessibility, int rate_knowledge, int rate_career_development) {

		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();

		ProfessorReview review = new ProfessorReview(null);
		review.setStudent_id(Long.parseLong(req.queryParams("student_id")));
		review.setProfessor_first_name(req.queryParams("professor_first_name"));
		review.setProfessor_last_name(req.queryParams("professor_last_name"));
		review.setCourse(req.queryParams("course_name"));
		review.setSemester(req.queryParams("semester"));
		review.setYear(Integer.parseInt(req.queryParams("year")));
		
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
