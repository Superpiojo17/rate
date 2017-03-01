package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class StudentDashboardController {

	/**
	 * Show log in page
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
		List<CoursesToReview> courses = reviewDao.allCourses(u);
		//model.put("users", courses);

		model.put("courses", courses);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/studentDashboard.hbs");
	}

}
