package edu.ben.rate_review.controller.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class EditReviewsController {

	public ModelAndView showDeptReviews(Request req, Response res) throws AuthException, SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		String department = req.params("department");

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		} else {
			User u = (User) session.attribute("current_user");
			model.put("current_user", u);

			if (u.getRole() != 1) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
			}
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		ad.close();
		ProfessorReviewDao pD = adao.getProfessorReviewDao();

		if (req.queryParams("search") != null) {

			String searchType = "name";
			String searchTxt = req.queryParams("search").toLowerCase();

			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<ProfessorReview> tempReviews = pD.search(searchType, searchTxt);
				if (tempReviews.size() > 0) {

					model.put("deptreviews", tempReviews);
				} else {
					List<ProfessorReview> reviews = pD.search(searchType, searchTxt);
					model.put("error", "No Results Found");
					model.put("deptreviews", reviews);
				}
			} else {
				List<ProfessorReview> deptReviews = pD.allFromDept(department);
				model.put("error", "Cannot leave search bar blank");
				model.put("deptreviews", deptReviews);
			}
		} else {
			List<ProfessorReview> deptReviews = pD.allFromDept(department);
			model.put("deptreviews", deptReviews);
		}

		model.put("department", department);

		pD.close();
		// Render the page
		return new ModelAndView(model, "users/reviews.hbs");
	}

	public ModelAndView deleteReview(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		String student_course_id = req.params("student_course_id");
		System.out.println(student_course_id);

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		} else {
			User u = (User) session.attribute("current_user");

			if (u.getRole() != 1) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
			}
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		UserDao userDao = adao.getUserDao();

		ProfessorReviewDao reviewDao = adao.getProfessorReviewDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ProfessorReview review = reviewDao.findReview(Long.parseLong(student_course_id));

		reviewDao.deleteReview(review.getStudent_course_id());

		model.put("error", "You just deleted " + review.getStudentname() + "'s review for " + review.getCourse());

		String email = review.getProfessor_email();

		User useremail = userDao.findByEmail(email);

		String department = useremail.getMajor();
		model.put("department", department);

		ProfessorReviewDao pD = adao.getProfessorReviewDao();

		List<ProfessorReview> deptReviews = pD.allFromDept(department);

		model.put("deptreviews", deptReviews);
		model.put("department", department);
		ad.close();
		reviewDao.close();
		userDao.close();
		// Render the page
		return new ModelAndView(model, "users/reviews.hbs");
	}

}
