package edu.ben.rate_review.app;

import static spark.Spark.*;

/**
 * This application class runs our entire project and is also where we configure the paths to get to all our pages
 */

import java.util.HashMap;

import edu.ben.rate_review.controller.home.*;
import spark.Session;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Application {

	// static variables to control email functionality
	public static boolean ALLOW_EMAIL = true;
	// SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
	// "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"
	public static String DOMAIN = "rateandreview.herokuapp.com";
	// match up paths
	public static String HOME_PATH = "/";
	public static String NOTFOUND_HOME_PATH = "/notfound";
	public static String CALENDAR_PATH = HOME_PATH + "calendar";

	public static String SCHEDULE_PATH = HOME_PATH + "schedule";
	public static String WELCOME_PATH = HOME_PATH + "welcome";
	public static String RULES_PATH = HOME_PATH + "rules";

	public static String ABOUTME_PATH = HOME_PATH + "aboutme";
	public static String OTHER_PATH = HOME_PATH + "other";
	public static String DATA_PATH = HOME_PATH + "data";
	public static String BEHAVIOR_PATH = HOME_PATH + "behavior";
	public static String OURCLASSROOM_PATH = HOME_PATH + "ourclassroom";

	public static void main(String[] args) throws Exception {

		port(getEnvironmentPort());

		// Configure your Asset folder so that your JS, CSS, Images are
		// available from the server endpoint
		staticFiles.location("/public");
		configRoutes();
	}

	static int getEnvironmentPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		// return default port if heroku-port isn't set (i.e. on localhost)
		return 4567;

	}

	/**
	 * Configures the routes based on URL path
	 */
	private static void configRoutes() {

		// Filter that checks things right away before every request
		before("/*", (request, response) -> {
			// create the session and assign it to a variable
			Session session = request.session(true);

		});

		after((request, response) -> {
			response.header("Content-Encoding", "gzip");
		});

		exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});

		notFound((req, res) -> {
			res.type("application/json");

			// page not found - kicks out to home
			res.redirect(NOTFOUND_HOME_PATH);
			halt();

			return "{\"message\":\"Custom 404\"}";
		});

		// path for the home page
		get(HOME_PATH, HomeController::showHomePage, new HandlebarsTemplateEngine());
		get(NOTFOUND_HOME_PATH, HomeController::showHomePage, new HandlebarsTemplateEngine());

		// path for homework

		get(CALENDAR_PATH, HomeController::showCalendar, new HandlebarsTemplateEngine());

		// path for class schedule
		get(SCHEDULE_PATH, HomeController::showClassSchedule, new HandlebarsTemplateEngine());

		// path for supplies
		get(WELCOME_PATH, HomeController::showWelcome, new HandlebarsTemplateEngine());

		// path for rules
		get(RULES_PATH, HomeController::showRules, new HandlebarsTemplateEngine());

		// path for about me
		get(ABOUTME_PATH, HomeController::showAboutMe, new HandlebarsTemplateEngine());

		// path for show other
		get(OTHER_PATH, HomeController::showOther, new HandlebarsTemplateEngine());

		// path for data

		get(DATA_PATH, HomeController::showData, new HandlebarsTemplateEngine());

		// path for behavior
		get(BEHAVIOR_PATH, HomeController::showBehavior, new HandlebarsTemplateEngine());

		// path for our classroom
		get(OURCLASSROOM_PATH, HomeController::showOurClassroom, new HandlebarsTemplateEngine());

	}
}