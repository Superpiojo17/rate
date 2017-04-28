package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class ActivationController {

	/**
	 * Show activation page
	 */
	public ModelAndView showActivationPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u != null) {
			if (u.getRole() == 1) {
				model.put("user_admin", true);
			} else if (u.getRole() == 2) {
				model.put("user_professor", true);
			} else if (u.getRole() == 3) {
				model.put("user_tutor", true);
			} else {
				model.put("user_student", true);
			}
		} else {
			model.put("user_null", true);
		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/activation.hbs");
	}

	/**
	 * Show deactivation page
	 */
	public ModelAndView showDeActivationPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u != null) {
			if (u.getRole() == 1) {
				model.put("user_admin", true);
			} else if (u.getRole() == 2) {
				model.put("user_professor", true);
			} else if (u.getRole() == 3) {
				model.put("user_tutor", true);
			} else {
				model.put("user_student", true);
			}
		} else {
			model.put("user_null", true);
		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/deactivation.hbs");
	}

	/**
	 * Activates a deactivated account
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String activate(Request req, Response res) {
		boolean activated = false;
		// checks the email and password fields are filled out
		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()) {
			if (LogInController.confirmRegistered(req.queryParams("email"), req.queryParams("password"))) {
				processActiveState(req.queryParams("email"), req.queryParams("password"), activated);
				res.redirect(Application.HOME_PATH + "/signin");
			} else {
				// email and password copy do not match any registered account
				res.redirect(Application.HOME_PATH + "/activation");
			}
		} else {
			// fields were empty
			res.redirect(Application.HOME_PATH + "/activation");
		}
		return "";
	}

	/**
	 * Deactivates an active account
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String deactivate(Request req, Response res) {
		boolean activated = true;
		// checks the email and password fields are filled out
		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()) {
			if (LogInController.confirmRegistered(req.queryParams("email"), req.queryParams("password"))) {
				processActiveState(req.queryParams("email"), req.queryParams("password"), activated);
				res.redirect(Application.HOME_PATH + "/signin");
			} else {
				// email and password copy do not match any registered account
				res.redirect(Application.HOME_PATH + "/deactivation");
			}
		} else {
			// fields were empty
			res.redirect(Application.HOME_PATH + "/deactivation");
		}
		return "";
	}

	/**
	 * Method which will send the user to the activation or deactivation
	 * methods. If user is found, it checks that their activity status matches
	 * what they're trying to accomplish. If it does not, no processing happens.
	 * 
	 * @param email
	 * @param password
	 * @param currentActivity
	 */
	public static void processActiveState(String email, String password, boolean currentActivity) {
		UserDao userDao = DaoManager.getInstance().getUserDao();

		User user = userDao.findByEmail(email);

		if (user != null) {
			if (user.isActive() && currentActivity) {
				userDao.deactivateAccount(user);
			} else if (!user.isActive() && !currentActivity) {
				userDao.activateAccount(user);
			}
		}
		userDao.close();
	}

}
