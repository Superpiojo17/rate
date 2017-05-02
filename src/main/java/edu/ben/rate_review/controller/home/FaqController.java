package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class FaqController {

	public static ModelAndView showFaqPage(Request req, Response res) throws Exception {
		//System.out.println("HELLO");
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u != null){
			if (u.getRole() == 1){
				model.put("user_admin", true);
			} else if (u.getRole() == 2){
				model.put("user_professor", true);
			} else if (u.getRole() == 3){
				model.put("user_tutor", true);
			} else {
				model.put("user_student", true);
			}
		} else {
			model.put("user_null", true);
		}
	//	System.out.println(session);
		// Tell the server to render the index page with the data in the
		// model
		return new ModelAndView(model, "home/faq.hbs");
	}

}
