package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * Controller for confirmtion account
 * 
 * @author Mike
 *
 */
public class ConfirmationController {

	public ModelAndView showConfirmationPage(Request req, Response res) {
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
		return new ModelAndView(model, "users/confirmation.hbs");
	}

	/**
	 * Activates a deactivated account
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String confirm(Request req, Response res) {
		// boolean activated = false;
		// checks the email and password fields are filled out
		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()) {
			if (LogInController.confirmRegistered(req.queryParams("email"), req.queryParams("password"))) {
				confirmAccount(req.queryParams("email"), req.queryParams("password"));
				res.redirect("/login");
			} else {
				// email and password copy do not match any registered account
				res.redirect("/confirmation");
			}
		} else {
			// fields were empty
			res.redirect("/confirmation");
		}
		return "";
	}

	/**
	 * Given a registered account, will confirm its registration, allowing them
	 * to login
	 * 
	 * @param email
	 * @param password
	 */
	public static void confirmAccount(String email, String password) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User user = new User();
		user = userDao.findByEmail(email);

		if (user != null) {
			userDao.accountConfirmed(user);
		}

		userDao.close();
	}

}
