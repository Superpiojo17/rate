package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

import static spark.Spark.halt;

/**
 * Controller for the change password page
 *
 * @author Mike
 * @version 2-3-2017
 */
public class ChangePasswordController {

	public static ModelAndView showChangePasswordPage(Request req, Response res) {
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
		return new ModelAndView(model, "home/changepassword.hbs");
	}

	/**
	 * Method which checks completion of change password form and necessary
	 * validations.
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	public static String changePassword(Request req, Response res) {
		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()
				&& !req.queryParams("new_password").isEmpty() && !req.queryParams("verify_password").isEmpty()) {
			if (LogInController.confirmRegistered(req.queryParams("email"), req.queryParams("password"))) {
				if (req.queryParams("new_password").equals(req.queryParams("verify_password"))) {
					attemptUpdatePassword(req.queryParams("email"), req.queryParams("new_password"));
					res.redirect(Application.HOME_PATH + "signin");
					halt();
				} else {
					res.redirect(Application.HOME_PATH + "changepassword");
					halt();
				}
			} else {
				// email and password copy do not match any registered account
				res.redirect(Application.HOME_PATH + "changepassword");
				halt();
			}
		} else {
			// fields were empty
			res.redirect(Application.HOME_PATH + "changepassword");
			halt();
		}
		return "";
	}

	/**
	 * Method which interacts with the dao to attempt to update the user's
	 * password
	 *
	 * @param email
	 */
	public static void attemptUpdatePassword(String email, String password) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		User user = new User();
		user = userDao.findByEmail(email);

		if (user != null) {
			user.setEncryptedPassword(password);
			userDao.updatePassword(user);
			sendConfirmChangePasswordEmail(user);
		}
	}

	/**
	 * Send email to user confirming their change of password.
	 *
	 * @param user
	 */
	private static void sendConfirmChangePasswordEmail(User user) {

		String subject = "Rate&Review Password Change";
		String messageHeader = "<p>Hello " + user.getFirst_name() + ",</p><br />";
		String messageBody = "<p>This message is to confirm that you have successfully changed "
				+ "your password! If this was not performed by you, please go to the " + "<a href=\"http://"
				+ Application.DOMAIN + "/accountrecovery" + "\">account recovery page</a>!</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + messageFooter;

		if (Application.ALLOW_EMAIL) {
			Email.deliverEmail(user.getFirst_name(), user.getEmail(), subject, message);
		}
	}
}
