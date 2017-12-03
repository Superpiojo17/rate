package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.email.SendGridManager;

public class HomeController {

	public static ModelAndView showHomePage(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/home.hbs");
	}

	public static ModelAndView showCalendar(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/calendar.hbs");
	}

	public static ModelAndView showClassSchedule(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/schedule.hbs");
	}

	public static ModelAndView showWelcome(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/welcome.hbs");
	}

	public static ModelAndView showRules(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/rules.hbs");
	}

	public static ModelAndView showAboutMe(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/aboutme.hbs");
	}

	public static ModelAndView showOther(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/other.hbs");
	}

	public static ModelAndView showData(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/data.hbs");
	}

	public static ModelAndView showBehavior(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/behavior.hbs");
	}

	public static ModelAndView showOurClassroom(Request req, Response res) throws Exception {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		return new ModelAndView(model, "home/ourclassroom.hbs");
	}

	public static ModelAndView contact(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		String student_name = req.queryParams("student_name");
		String parent_name = req.queryParams("parent_name");
		String id = req.queryParams("id");
		String email = req.queryParams("email");
		String message = req.queryParams("message");
		String subject = "New Notification From Your Classroom";
		String header = "Student Name:\n " + student_name + "<br/>  Student ID " + id + "<br/> Parent Name " + parent_name + " <br/> Email " + email + " <br/> Message: ";

		String messageBody = "<p>" + message + "</p>";
		String finalMessage = header + messageBody;

		HashMap<String, String> params = new HashMap<>();
		params.put("name", parent_name);
		params.put("subject", subject);
		params.put("email", email);
		params.put("to", "paredes_erika@yahoo.com");
		params.put("message", finalMessage);
		if (Application.ALLOW_EMAIL) {
			SendGridManager.getInstance().send(params);
		}
		res.redirect(Application.OTHER_PATH);
		return new ModelAndView(model, "home/other.hbs");

	}

}
