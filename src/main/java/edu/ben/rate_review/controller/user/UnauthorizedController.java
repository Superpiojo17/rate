package edu.ben.rate_review.controller.user;

import java.util.HashMap;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
//import spark.Session;
import spark.Session;

public class UnauthorizedController {

	public ModelAndView showNotLoggedIn(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u.getRole() == 1) {
			model.put("user_admin", true);
		} else if (u.getRole() == 2) {
			model.put("user_professor", true);
		} else if (u.getRole() == 3) {
			model.put("user_tutor", true);
		} else if (u.getRole() == 4) {
			model.put("user_student", true);
		} else {
			model.put("user_null", true);
		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/notloggedinerror.hbs");
	}

	public ModelAndView showNotAuthorized(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u.getRole() == 1) {
			model.put("user_admin", true);
		} else if (u.getRole() == 2) {
			model.put("user_professor", true);
		} else if (u.getRole() == 3) {
			model.put("user_tutor", true);
		} else if (u.getRole() == 4) {
			model.put("user_student", true);
		} else {
			model.put("user_null", true);
		}

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/notauthorized.hbs");
	}

}
