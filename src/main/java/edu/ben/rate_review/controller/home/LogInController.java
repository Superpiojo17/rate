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
			if (confirmRegistered(req.queryParams("email"), req.queryParams("password"))) {
				res.redirect("/contactus");
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

		System.out.println(password);
		System.out.println(u.getEncryptedPassword());
		System.out.println(u.getId());
		if (u != null) {
			if (SecurePassword.getCheckPassword(password, u.getEncryptedPassword())) {
				return true;
			}
		}
		return false;
	}

}
