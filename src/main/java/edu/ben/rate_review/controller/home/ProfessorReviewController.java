package edu.ben.rate_review.controller.home;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.PrePopulatedReviewButtons;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
//import spark.Session;
import spark.Session;

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

		// gets user from session
		Session session = req.session();
		User user = (User) session.attribute("current_user");
		long user_id = -1;

		// if user is not logged in, will store -1 as user_id
		if (user != null) {
			user_id = user.getId();
		}

		// gets course_id from the URL
		String idString = req.params("course_id");
		long course_id = Long.parseLong(idString);

		// gets the course object from the course_id
		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		CoursesToReview course = reviewDao.findByCourseId(course_id);

		// checks that the person accessing the page has access
		if (user_id != -1 && user_id == course.getStudent_id()) {
			if (!reviewDao.findReview(course.getCourse_id()).getComment_removed()) {

				// create the form object, put it into request
				model.put("course", course);

				ProfessorReview review = reviewDao.findReview(course_id);

				// if user wants to edit a review, this will pre-populate old
				// ratings
				if (review != null) {
					model.put("review", review);

					PrePopulatedReviewButtons objectives = new PrePopulatedReviewButtons();
					objectives.setRating(review.getRate_objectives());
					model.put("objectives_1", objectives.getRate_1());
					model.put("objectives_2", objectives.getRate_2());
					model.put("objectives_3", objectives.getRate_3());
					model.put("objectives_4", objectives.getRate_4());
					model.put("objectives_5", objectives.getRate_5());
					PrePopulatedReviewButtons organized = new PrePopulatedReviewButtons();
					organized.setRating(review.getRate_organized());
					model.put("organized_1", organized.getRate_1());
					model.put("organized_2", organized.getRate_2());
					model.put("organized_3", organized.getRate_3());
					model.put("organized_4", organized.getRate_4());
					model.put("organized_5", organized.getRate_5());
					PrePopulatedReviewButtons challenging = new PrePopulatedReviewButtons();
					challenging.setRating(review.getRate_challenging());
					model.put("challenging_1", challenging.getRate_1());
					model.put("challenging_2", challenging.getRate_2());
					model.put("challenging_3", challenging.getRate_3());
					model.put("challenging_4", challenging.getRate_4());
					model.put("challenging_5", challenging.getRate_5());
					PrePopulatedReviewButtons outside_work = new PrePopulatedReviewButtons();
					outside_work.setRating(review.getRate_outside_work());
					model.put("outside_work_1", outside_work.getRate_1());
					model.put("outside_work_2", outside_work.getRate_2());
					model.put("outside_work_3", outside_work.getRate_3());
					model.put("outside_work_4", outside_work.getRate_4());
					model.put("outside_work_5", outside_work.getRate_5());
					PrePopulatedReviewButtons pace = new PrePopulatedReviewButtons();
					pace.setRating(review.getRate_pace());
					model.put("pace_1", pace.getRate_1());
					model.put("pace_2", pace.getRate_2());
					model.put("pace_3", pace.getRate_3());
					model.put("pace_4", pace.getRate_4());
					model.put("pace_5", pace.getRate_5());
					PrePopulatedReviewButtons assignments = new PrePopulatedReviewButtons();
					assignments.setRating(review.getRate_assignments());
					model.put("assignments_1", assignments.getRate_1());
					model.put("assignments_2", assignments.getRate_2());
					model.put("assignments_3", assignments.getRate_3());
					model.put("assignments_4", assignments.getRate_4());
					model.put("assignments_5", assignments.getRate_5());
					PrePopulatedReviewButtons grade_fairly = new PrePopulatedReviewButtons();
					grade_fairly.setRating(review.getRate_grade_fairly());
					model.put("grade_fairly_1", grade_fairly.getRate_1());
					model.put("grade_fairly_2", grade_fairly.getRate_2());
					model.put("grade_fairly_3", grade_fairly.getRate_3());
					model.put("grade_fairly_4", grade_fairly.getRate_4());
					model.put("grade_fairly_5", grade_fairly.getRate_5());
					PrePopulatedReviewButtons grade_time = new PrePopulatedReviewButtons();
					grade_time.setRating(review.getRate_grade_time());
					model.put("grade_time_1", grade_time.getRate_1());
					model.put("grade_time_2", grade_time.getRate_2());
					model.put("grade_time_3", grade_time.getRate_3());
					model.put("grade_time_4", grade_time.getRate_4());
					model.put("grade_time_5", grade_time.getRate_5());
					PrePopulatedReviewButtons accessibility = new PrePopulatedReviewButtons();
					accessibility.setRating(review.getRate_accessibility());
					model.put("accessibility_1", accessibility.getRate_1());
					model.put("accessibility_2", accessibility.getRate_2());
					model.put("accessibility_3", accessibility.getRate_3());
					model.put("accessibility_4", accessibility.getRate_4());
					model.put("accessibility_5", accessibility.getRate_5());
					PrePopulatedReviewButtons knowledge = new PrePopulatedReviewButtons();
					knowledge.setRating(review.getRate_knowledge());
					model.put("knowledge_1", knowledge.getRate_1());
					model.put("knowledge_2", knowledge.getRate_2());
					model.put("knowledge_3", knowledge.getRate_3());
					model.put("knowledge_4", knowledge.getRate_4());
					model.put("knowledge_5", knowledge.getRate_5());
					PrePopulatedReviewButtons career_development = new PrePopulatedReviewButtons();
					career_development.setRating(review.getRate_career_development());
					model.put("career_development_1", career_development.getRate_1());
					model.put("career_development_2", career_development.getRate_2());
					model.put("career_development_3", career_development.getRate_3());
					model.put("career_development_4", career_development.getRate_4());
					model.put("career_development_5", career_development.getRate_5());
				}

				DaoManager dao = DaoManager.getInstance();
				AnnouncementDao ad = dao.getAnnouncementDao();
				List<Announcement> announcements = ad.all();
				model.put("announcements", announcements);
			} else {
				// if student tries to access page directly through url after
				// getting their comment removed
				res.redirect("/authorizationerror");
			}
		} else {
			res.redirect("/authorizationerror");
		}
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

		if (req.queryParams("comment").length() <= 500 && req.queryParams("rate_objectives") != null
				&& req.queryParams("rate_organized") != null && req.queryParams("rate_challenging") != null
				&& req.queryParams("rate_outside_work") != null && req.queryParams("rate_pace") != null
				&& req.queryParams("rate_assignments") != null && req.queryParams("rate_grade_fairly") != null
				&& req.queryParams("rate_grade_time") != null && req.queryParams("rate_accessibility") != null
				&& req.queryParams("rate_knowledge") != null && req.queryParams("rate_career_development") != null) {
			Long professorID = createReview(Long.parseLong(req.params("course_id")), req.queryParams("comment"),
					Integer.parseInt(req.queryParams("rate_objectives")),
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

			res.redirect("/professor/" + professorID + "/overview");
		} else {
			// comment too long
			res.redirect("/reviewprofessor/" + req.params("course_id") + "/review");
		}

		return "";
	}

	/**
	 * Packages information into ProfessorReview object and sends it to the Dao
	 * to store in the database
	 * 
	 * @param course_id
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
	private Long createReview(Long course_id, String comment, int rate_objectives, int rate_organized,
			int rate_challenging, int rate_outside_work, int rate_pace, int rate_assignments, int rate_grade_fairly,
			int rate_grade_time, int rate_accessibility, int rate_knowledge, int rate_career_development) {
		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();

		ProfessorReview review = new ProfessorReview();
		CoursesToReview course = new CoursesToReview();

		course = reviewDao.findByCourseId(course_id);

		review.setCourse_id(course.getCourse_id());
		review.setStudent_id(course.getStudent_id());
		review.setProfessor_first_name(course.getProfessor_first_name());
		review.setProfessor_last_name(course.getProfessor_last_name());
		review.setCourse(course.getCourse_name());
		review.setSemester(course.getSemester());
		review.setYear(course.getYear());
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
		review.setProfessor_email(course.getProfessor_email());

		// if review already exists, this will remove previous review
		if (reviewDao.findReview(course.getCourse_id()) != null) {
			reviewDao.removeProfessorReview(review);
		}

		reviewDao.save(review);
		reviewDao.setCourseReviewed(review);

		UserDao uDao = DaoManager.getInstance().getUserDao();
		User u = uDao.findByEmail(course.getProfessor_email());
		return u.getId();
	}

	public ModelAndView passCourse(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		model.put("professor_id", req.params("professor_id"));

		return new ModelAndView(model, "/professor.hbs");
	}

}
