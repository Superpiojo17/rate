package edu.ben.rate_review.controller.home;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.StudentInCourseDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.PrePopulatedReviewButtons;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.StudentInCourse;
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

		User u = (User) session.attribute("current_user");

		if (u != null) {
			if (u.getRole() == 1) {
				model.put("user_admin", true);
			} else if (u.getRole() == 2) {
				model.put("user_professor", true);
			} else if (u.getRole() == 3) {
				model.put("user_tutor", true);
			} else {
				model.put("user_student", true);
			}
		} else {
			model.put("user_null", true);
		}

		// if user is not logged in, will store -1 as user_id
		if (user != null) {
			user_id = user.getId();
		}

		// gets course_id from the URL
		String idString = req.params("student_course_id");
		long student_course_id = Long.parseLong(idString);

		// gets the course object from the course_id
		DaoManager dao = DaoManager.getInstance();
		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		UserDao userDao = dao.getUserDao();
		StudentInCourseDao sDao = dao.getStudentInCourseDao();
		StudentInCourse course = sDao.findByStudentCourseId(student_course_id);

		if (u != null) {

			// checks that the person accessing the page has access
			if ((u.getRole() == 1) || (user_id != -1 && user_id == course.getStudent_id())) {
				if (reviewDao.findReview(course.getStudent_course_id()) == null) {

					// create the form object, put it into request
					model.put("course", course);

					model.put("deletekey", course.getStudent_course_id());

					ProfessorReview review = reviewDao.findReview(student_course_id);

					// if user wants to edit a review, this will pre-populate
					if (review != null) {
						model.put("review", review);
						handlePrePopulatedButtons(model, review);
					}

					// DaoManager dao = DaoManager.getInstance();
					AnnouncementDao ad = dao.getAnnouncementDao();
					List<Announcement> announcements = ad.all();
					model.put("announcements", announcements);
					ad.close();
				} else {
					if (sDao.findByStudentCourseId(course.getStudent_course_id()).isDisable_edit()
							|| sDao.findByStudentCourseId(course.getStudent_course_id()).isSemester_past()
							|| u.getRole() == 1) {
						model.put("edit_is_disabled", true);
						ProfessorReview review = reviewDao.findReview(student_course_id);
						String email = review.getProfessor_email();

						User useremail = userDao.findByEmail(email);

						String department = useremail.getMajor();
						model.put("deletekey", course.getStudent_course_id());
						model.put("department", department);
					} else {
						model.put("edit_is_disabled", false);
					}
					model.put("course", course);
					ProfessorReview review = reviewDao.findReview(student_course_id);

					UserDao uDao = DaoManager.getInstance().getUserDao();
					User professor = uDao.findByEmail(review.getProfessor_email());
					model.put("professor_id", professor.getId());

					// if user wants to edit a review, this will pre-populate
					if (review != null) {
						model.put("review", review);
						handlePrePopulatedButtons(model, review);
					}

					// DaoManager dao = DaoManager.getInstance();
					AnnouncementDao ad = dao.getAnnouncementDao();
					List<Announcement> announcements = ad.all();
					model.put("announcements", announcements);

				}
			} else

			{
				userDao.close();
				reviewDao.close();
				sDao.close();
				res.redirect("/authorizationerror");
			}
		} else {
			userDao.close();
			reviewDao.close();
			sDao.close();
			res.redirect("/authorizationerror");
		}
		userDao.close();
		reviewDao.close();
		sDao.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/reviewprofessor.hbs");
	}

	/**
	 * Method which handles putting all the prepopulated radio buttons
	 * 
	 * @param model
	 * @param review
	 */
	private void handlePrePopulatedButtons(HashMap<String, Object> model, ProfessorReview review) {

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
			Long professorID = createReview(Long.parseLong(req.params("student_course_id")), req.queryParams("comment"),
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
			res.redirect("/reviewprofessor/" + req.params("student_course_id") + "/review");
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
	private Long createReview(Long student_course_id, String comment, int rate_objectives, int rate_organized,
			int rate_challenging, int rate_outside_work, int rate_pace, int rate_assignments, int rate_grade_fairly,
			int rate_grade_time, int rate_accessibility, int rate_knowledge, int rate_career_development) {
		DaoManager dao = DaoManager.getInstance();
		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		StudentInCourseDao sDao = dao.getStudentInCourseDao();
		CourseDao cDao = dao.getCourseDao();
		UserDao uDao = DaoManager.getInstance().getUserDao();

		ProfessorReview review = new ProfessorReview();
		// CoursesToReview course = new CoursesToReview();

		StudentInCourse course = sDao.findByStudentCourseId(student_course_id);
		Course c = cDao.findById(course.getCourse_id());

		review.setStudent_course_id(course.getStudent_course_id());
		review.setStudent_id(course.getStudent_id());
		review.setProfessor_first_name(course.getProfessor_first_name());
		review.setProfessor_last_name(course.getProfessor_last_name());
		review.setCourse(course.getCourse_subject_number());
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
		review.setProfessor_email(uDao.findById(c.getProfessor_id()).getEmail());

		// if review already exists, this will remove previous review
		if (reviewDao.findReview(course.getCourse_id()) != null) {
			reviewDao.removeProfessorReview(review);
		}

		reviewDao.save(review);
		sDao.setCourseReviewed(review);

		User professor = uDao.findByEmail(review.getProfessor_email());

		cDao.close();
		uDao.close();
		reviewDao.close();
		sDao.close();
		return professor.getId();
	}

	public ModelAndView passCourse(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		model.put("professor_id", req.params("professor_id"));

		return new ModelAndView(model, "/professor.hbs");
	}

}
