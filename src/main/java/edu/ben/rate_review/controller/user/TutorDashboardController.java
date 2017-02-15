package edu.ben.rate_review.controller.user;

import java.util.HashMap;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class TutorDashboardController {

	public ModelAndView showTutorDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		AuthPolicyManager.getInstance().getUserPolicy().showTutorDashboardPage();

		model.put("current_user", u);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/tutorDashboard.hbs");
	}

}
