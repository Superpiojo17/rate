package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

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

}
