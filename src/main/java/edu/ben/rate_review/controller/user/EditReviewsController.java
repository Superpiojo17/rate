package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

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

	public ModelAndView showDeptReviews(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		String department = req.params("department");

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ProfessorReviewDao pD = adao.getProfessorReviewDao();

		List<ProfessorReview> deptReviews = pD.allFromDept(department);

		model.put("deptreviews", deptReviews);
		model.put("department", department);

		// Render the page
		return new ModelAndView(model, "users/reviews.hbs");
	}

	public ModelAndView deleteReview(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		String course_id = req.params("courseid");

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		UserDao userDao = adao.getUserDao();

		ProfessorReviewDao reviewDao = adao.getProfessorReviewDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ProfessorReview review = reviewDao.findReview(Long.parseLong(course_id));

		reviewDao.deleteReview(review.getCourse_id());

		model.put("error", "You just deleted " + review.getStudentname() + "'s review for " + review.getCourse());

		String email = review.getProfessor_email();

		User useremail = userDao.findByEmail(email);

		String department = useremail.getMajor();
		model.put("department", department);

		ProfessorReviewDao pD = adao.getProfessorReviewDao();

		List<ProfessorReview> deptReviews = pD.allFromDept(department);

		model.put("deptreviews", deptReviews);
		model.put("department", department);

		// Render the page
		return new ModelAndView(model, "users/reviews.hbs");
	}

}
