package edu.ben.rate_review.controller.home;

import java.util.HashMap;

//import java.util.ArrayList;
//import java.util.HashMap;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Login controller
 * 
 * @author Mike
 * @version 1-30-2017
 */
public class LogInController {

	/**
	 * Show log in page
	 */
	public ModelAndView showLoginPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "sessions/login.hbs");
	}

	public String login(Request req, Response res) {

		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()) {
			if (confirmRegistered(req.queryParams("email"), req.queryParams("password"))
					&& accountConfirmed(req.queryParams("email")) && accountActive(req.queryParams("email"))) {
				int role = getAccountType(req.queryParams("email"), req.queryParams("password"));
				if (role == 4) {
					res.redirect("/studentdashboard");
				} else if (role == 3) {
					res.redirect("/tutordashboard");
				} else if (role == 2) {
					res.redirect("/facultydashboard");
				} else if (role == 1) {
					res.redirect("/admindashboard");
				}

			} else {
				// if email is not found in the system, outputs message
				// "Incorrect E-mail or Password. Please try again."
				res.redirect("/login");
			}

		} else {
			res.redirect("/login");
		}

		// res.redirect("/login");

		return "";
	}
	
	/**
	 * While attempting to log-in, this method checks that the account the user
	 * is logging in with is currently activated.
	 * 
	 * @param email
	 * @return
	 */
	public static boolean accountActive(String email) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User u = new User();
		u = userDao.findByEmail(email);

		if (u != null && u.isActive()) {
			return true;
		}
		return false;
	}
	
	/**
	 * While attempting to log-in, this method checks that the account the user
	 * is logging in with has been confirmed.
	 * 
	 * @param email
	 * @return
	 */
	public static boolean accountConfirmed(String email) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User u = new User();
		u = userDao.findByEmail(email);

		if (u != null && u.isConfirmed()) {
			return true;
		}
		return false;
	}

	/**
	 * Checks that the account logging in is registered for the site
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static boolean confirmRegistered(String email, String password) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User u = new User();
		u = userDao.findByEmail(email);

		if (u != null) {
			if (SecurePassword.getCheckPassword(password, u.getEncryptedPassword())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks that the account logging in is registered for the site
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public static int getAccountType(String email, String password) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User u = new User();
		u = userDao.findByEmail(email);

		return u.getRole();
	}

}
