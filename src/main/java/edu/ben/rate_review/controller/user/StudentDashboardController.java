package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class StudentDashboardController {

	/**
	 * Show log in page
	 * 
	 * @throws AuthException
	 */
	public ModelAndView showStudentDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		AuthPolicyManager.getInstance().getUserPolicy().showStudentDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		List<CoursesToReview> coursesNotReviewed = reviewDao.allStudentCoursesNotReviewed(u);
		List<CoursesToReview> coursesReviewed = reviewDao.allStudentCoursesReviewed(u);

		// no classes listed in courses to review
		boolean noCoursesToReview = false;
		// no classes listed in recent reviews
		boolean noCoursesReviewed = false;

		// checks if either lists are empty
		if (coursesNotReviewed.isEmpty()) {
			noCoursesToReview = true;
		}
		if (coursesReviewed.isEmpty()) {
			noCoursesReviewed = true;
		}

		model.put("no_courses_to_review", noCoursesToReview);
		model.put("no_courses_reviewed", noCoursesReviewed);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();

		model.put("announcements", announcements);

		model.put("courses_not_reviewed", coursesNotReviewed);
		model.put("courses_reviewed", coursesReviewed);

		model.put("current_user", u);

		TutorDao tDao = dao.getTutorDao();
		List<Tutor> tutors = tDao.listAllTutors();
		model.put("tutors", tutors);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/studentDashboard.hbs");
	}

	/**
	 * Post method for requesting an appointment with a tutor. Will create and
	 * store a tutor appointment object
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String requestAppointment(Request req, Response res) {

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		DaoManager dao = DaoManager.getInstance();
		TutorDao tDao = dao.getTutorDao();

		if (!req.queryParams("date").isEmpty()) {

			TutorAppointment appointment = new TutorAppointment();

			appointment.setStudent_id(u.getId());
			appointment.setTutor_id(Long.parseLong(req.queryParams("tutor_id")));
			appointment.setDate(req.queryParams("date"));
			appointment.setStudent_message(req.queryParams("student_message"));
			tDao.saveTutorAppointment(appointment);

		} else {
			// message - please set a date
		}

		res.redirect(Application.STUDENTDASHBOARD_PATH);
		return "";
	}

}
