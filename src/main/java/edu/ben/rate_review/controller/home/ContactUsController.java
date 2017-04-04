package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ContactUsController {
	/**
	 * Show log in page
	 */
	public ModelAndView showContactUsPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/contactus.hbs");
	}
	
	
	
	/**
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	// if you need something, do request.getParamter and its ID name on the JSP
	public static String contact(Request req, Response res){
		if (!req.queryParams("name").isEmpty() && !req.queryParams("email").isEmpty()
				&& !req.queryParams("message").isEmpty()) {
			contactUs(req.queryParams("name"), req.queryParams("email"),
					 req.queryParams("message"));
			res.redirect("/contactus");
		} else {
			// if something was not filled out, output error
			req.attribute("error", "Please fill out all fields.");

		}
		return "";

	}

	/**
	 * Method which will send an email to the registering user.
	 * 
	 * @param first_name
	 * @param email
	 * @param role_id
	 */
	private static void contactUs(String name, String email, String message) {
//	  

		String subject = "Rate&Review from: " + name;
		String messageBody = "Sent from:\n " + email + "\r\n\n\r   " + message;

		Email.deliverEmail(name, "ratereviewsite@gmail.com", subject, messageBody);
	//	return "";

	}

	

}
