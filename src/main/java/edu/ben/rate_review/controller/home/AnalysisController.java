package edu.ben.rate_review.controller.home;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * Controller for the data analytics page
 * 
 * @author Mike
 * @version 4-25-2017
 */
public class AnalysisController {

	public ModelAndView showAnalysisPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

//		if (u == null || u.getRole() != 1) {
//			return new ModelAndView(model, "home/notauthorized.hbs");
//		}

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

		DaoManager dao = DaoManager.getInstance();
		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		UserDao uDao = dao.getUserDao();
		TutorDao tDao = dao.getTutorDao();

		List<ProfessorReview> reviews = reviewDao.listAllReviews();
		List<TutorAppointment> appointments = tDao.listAllAppointments();

		// average professor rating by year bar graph
		putAverageRatingByYear(model, uDao, reviews);
		// count of ratings by year pie chart
		putCountOfRatingsByYear(model, uDao, reviews);
		// status of tutor appointments bar graph
		putAppointmentStatus(model, tDao, appointments);

		tDao.close();
		uDao.close();
		reviewDao.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/analysis.hbs");
	}

	/**
	 * Gets the count of each tutor appointment status
	 * 
	 * @param model
	 * @param tDao
	 * @param appointments
	 */
	private void putAppointmentStatus(HashMap<String, Object> model, TutorDao tDao,
			List<TutorAppointment> appointments) {
		int count_approved = 0;
		int count_denied = 0;
		int count_past = 0;
		int count_missed = 0;

		for (int i = 0; i < appointments.size(); i++) {
			TutorAppointment ta = appointments.get(i);

			if (ta.getAppointment_status()) {
				count_approved++;
			}
			if (ta.getAppointment_status() && ta.isAppointment_past()) {
				count_past++;
			}
			if (ta.getTutor_has_responded() && !ta.getAppointment_status()) {
				count_denied++;
			}
			if (!ta.getTutor_has_responded() && ta.isAppointment_past()) {
				count_missed++;
			}
		}

		model.put("approved", count_approved);
		model.put("denied", count_denied);
		model.put("past", count_past);
		model.put("missed", count_missed);

	}

	/**
	 * Handles average professor rating by year in school
	 * 
	 * @param model
	 * @param uDao
	 * @param reviews
	 */
	private void putAverageRatingByYear(HashMap<String, Object> model, UserDao uDao, List<ProfessorReview> reviews) {
		int freshman_count = 0;
		float freshman_score = 0;
		int sophomore_count = 0;
		float sophomore_score = 0;
		int junior_count = 0;
		float junior_score = 0;
		int senior_count = 0;
		float senior_score = 0;

		for (int i = 0; i < reviews.size(); i++) {
			long student_year = uDao.findById(reviews.get(i).getStudent_id()).getSchool_year();

			if (student_year == 1) {
				freshman_score += Float.parseFloat(reviews.get(i).getOverall());
				freshman_count += 1;
			} else if (student_year == 2) {
				sophomore_score += Float.parseFloat(reviews.get(i).getOverall());
				sophomore_count += 1;
			} else if (student_year == 3) {
				junior_score += Float.parseFloat(reviews.get(i).getOverall());
				junior_count += 1;
			} else {
				senior_score += Float.parseFloat(reviews.get(i).getOverall());
				senior_count += 1;
			}
		}

		model.put("freshman_avg_rating", freshman_score / freshman_count);
		model.put("sophomore_avg_rating", sophomore_score / sophomore_count);
		model.put("junior_avg_rating", junior_score / junior_count);
		model.put("senior_avg_rating", senior_score / senior_count);
	}

	/**
	 * This method gets the number of ratings per year in school for the first
	 * graphic
	 * 
	 * @param model
	 * @param req
	 * @param res
	 * @param reviewDao
	 * @param uDao
	 * @param reviews
	 */
	private void putCountOfRatingsByYear(HashMap<String, Object> model, UserDao uDao, List<ProfessorReview> reviews) {

		int freshman_count = 0;
		int sophomore_count = 0;
		int junior_count = 0;
		int senior_count = 0;

		for (int i = 0; i < reviews.size(); i++) {
			long student_year = uDao.findById(reviews.get(i).getStudent_id()).getSchool_year();

			if (student_year == 1) {
				freshman_count += 1;
			} else if (student_year == 2) {
				sophomore_count += 1;
			} else if (student_year == 3) {
				junior_count += 1;
			} else {
				senior_count += 1;
			}
		}

		model.put("freshman_count", freshman_count);
		model.put("sophomore_count", sophomore_count);
		model.put("junior_count", junior_count);
		model.put("senior_count", senior_count);
	}

}
