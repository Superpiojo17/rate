package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.email.Email;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class ProfessorController {

	public ModelAndView showProfessorPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/professor.hbs");
	}
	
	public ModelAndView showReviewProfessorPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/reviewprofessor.hbs");
	}
	
	private static void review(String name, String email) {

		String accountType = "";

	}
}
