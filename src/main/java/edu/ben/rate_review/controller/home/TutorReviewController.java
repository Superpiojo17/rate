package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.TutorReview;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * Handles a tutor review
 * 
 * @author Mike
 * @version 4-14-2017
 */
public class TutorReviewController {

	/**
	 * Get method for tutor review
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showTutorReview(Request req, Response res) throws Exception {
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		if (u != null) {
			if (u.getRole() == 4) {
				model.put("user_student", true);
			} else {
				res.redirect("/authorizationerror");
			}
		} else {
			model.put("user_null", true);
		}

		// gets appointment id from url
		long appointment_id = Long.parseLong(req.params("appointment_id"));
		// gets instance of tutor dao
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		// finds tutor appointment
		TutorAppointment appointment = tDao.findAppointmentByID(appointment_id);

		if (u != null && appointment != null) {
			if (!appointment.isAppointment_reviewed()) {
				if (u.getId() == appointment.getStudent_id()) {
					model.put("appointment", appointment);
				} else {
					res.redirect("/authorizationerror");
				}
			} else {
				res.redirect("/authorizationerror");
			}
		} else {
			// unauthorized user or student not enrolled
			res.redirect("/authorizationerror");
		}

		return new ModelAndView(model, "home/tutorreview.hbs");
	}

	/**
	 * Post method, checks fields and creates review
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String reviewTutor(Request req, Response res) {

		if (req.queryParams("comment").length() <= 500 && req.queryParams("enhance_understanding") != null
				&& req.queryParams("simple_examples") != null && req.queryParams("professional") != null
				&& req.queryParams("prepared") != null && req.queryParams("schedule_again") != null
				&& req.queryParams("recommend") != null) {
			createReview(req, res);
			res.redirect("/studentdashboard");
		} else {
			// comment too long
			res.redirect("/tutorreview/" + req.params("appointment_id"));
		}

		return "";
	}

	/**
	 * Builds review object and sends to dao to store in the database
	 * 
	 * @param res
	 * @param req
	 */
	private void createReview(Request req, Response res) {

		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		TutorAppointment appointment = tDao.findAppointmentByID(Long.parseLong(req.params("appointment_id")));

		TutorReview review = new TutorReview();
		review.setAppointment_id(Long.parseLong(req.params("appointment_id")));
		review.setEnhance_understanding(Integer.parseInt(req.queryParams("enhance_understanding")));
		review.setSimple_examples(Integer.parseInt(req.queryParams("simple_examples")));
		review.setProfessional(Integer.parseInt(req.queryParams("professional")));
		review.setPrepared(Integer.parseInt(req.queryParams("prepared")));
		review.setSchedule_again(Integer.parseInt(req.queryParams("schedule_again")));
		review.setRecommend(Integer.parseInt(req.queryParams("recommend")));
		review.setComment(req.queryParams("comment"));
		review.setStudent_id(appointment.getStudent_id());
		review.setTutor_id(appointment.getTutor_id());

		tDao.saveTutorReview(review);
		tDao.setAppointmentReviewed(appointment);
		;
	}
}
