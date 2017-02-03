package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Controller for the change password page
 * 
 * @author Mike
 * @version 2-3-2017
 */
public class ChangePasswordController {

	public ModelAndView showChangePasswordPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/changepassword.hbs");
	}

	/**
	 * Method which checks completion of change password form and necessary
	 * validations.
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String changePassword(Request req, Response res) {
		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()
				&& !req.queryParams("new_password").isEmpty() && !req.queryParams("verify_password").isEmpty()) {
			if (LogInController.confirmRegistered(req.queryParams("email"), req.queryParams("password"))) {
				if (req.queryParams("new_password") == req.queryParams("verify_password")) {
					attemptUpdatePassword(req.queryParams("email"));
					res.redirect("/login");
				} else {
					res.redirect("/changepassword");
				}
			} else {
				// email and password copy do not match any registered account
				res.redirect("/changepassword");
			}
		} else {
			// fields were empty
			res.redirect("/changepassword");
		}
		return "";
	}

	/**
	 * Method which interacts with the dao to attempt to update the user's
	 * password
	 * 
	 * @param email
	 */
	public static void attemptUpdatePassword(String email) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User user = new User();
		user = userDao.findByEmail(email);

		if (user != null) {
			userDao.updatePassword(user);
		}
	}

	public void sendEmail() {

	}
}
