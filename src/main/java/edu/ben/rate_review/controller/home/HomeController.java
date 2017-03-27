package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class HomeController {

	public ModelAndView showHomePage(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		Session session = req.session(true);
		// Tell the server to render the index page with the data in the
		// model
		return new ModelAndView(model, "home/home.hbs");
	}

}
