package edu.ben.rate_review.controller.home;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
//import edu.ben.rate_review.controller.user.AccountRecoveryController;
//import java.util.ArrayList;
//import java.util.HashMap;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.SendGridManager;
import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.RecoveringUser;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
import static spark.Spark.*;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * Login controller
 *
 * @author Mike
 * @version 2-2-2017
 */
public class LogInController {
	// private static int wrongCount = 0;
	// private static String checkEmail = "";

	/**
	 * Show log in page
	 */
	public static ModelAndView showLoginPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		Session session = req.session();
		User u = (User) session.attribute("current_user");

		// if (u != null) {
		// if (u.getRole() == 1) {
		// model.put("user_admin", true);
		// } else if (u.getRole() == 2) {
		// model.put("user_professor", true);
		// } else if (u.getRole() == 3) {
		// model.put("user_tutor", true);
		// } else {
		// model.put("user_student", true);
		// }
		// } else {
		// model.put("user_null", true);
		// }

		if (u != null) {
			if (u.getRole() == 1) {
				res.redirect(Application.ADMINDASHBOARD_PATH);
				halt();
			} else if (u.getRole() == 2) {
				res.redirect(Application.FACULTYDASHBOARD_PATH);
				halt();
			} else if (u.getRole() == 3) {
				res.redirect(Application.TUTORDASHBOARD_PATH);
				halt();
			} else if (u.getRole() == 4) {
				res.redirect(Application.STUDENTDASHBOARD_PATH);
				halt();
			}
		} else {
			model.put("user_null", true);
		}
		// if (req.queryParams("email") != null && req.queryParams("password")
		// != null) {
		//
		// if (!req.queryParams("email").isEmpty() &&
		// !req.queryParams("password").isEmpty()) {
		// if (login(req, res) == "error") {
		//
		// model.put("error", "Invalid Username or Password Entries: " +
		// wrongCount + "/3");
		//
		// }
		// if (wrongCount == 0) {
		// model.put("error", "Account has been LOCKED");
		// }
		// }
		//// else {
		//// model.put("error", "error");
		//// }
		// }
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "sessions/signin.hbs");
	}

	/**
	 * Login method. Confirms fields are properly filled out and user conditions
	 * are met to allow login such as accurate email and password, and an
	 * account which is confirmed and activated. Depending on role type of the
	 * account, the user will be directed to the appropriate dashboard.
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	public static ModelAndView login(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();
		Session session = req.session();

		// checks the email and password fields are filled out
		if (!req.queryParams("email").isEmpty() && !req.queryParams("password").isEmpty()) {
			// checks that the login credentials match a registered, confirmed,
			// and active account
			if (confirmRegistered(req.queryParams("email"), req.queryParams("password"))
					&& accountConfirmed(req.queryParams("email")) && accountActive(req.queryParams("email"))) {
				// determines role of user
				// int role = getAccountType(req.queryParams("email"),
				// req.queryParams("password"));
				// directs user to correct dashboard
				UserDao ud = DaoManager.getInstance().getUserDao();
				User u = ud.findByEmail(req.queryParams("email"));
				if (u != null) {
					session.attribute("current_user", u);
					if (u.getRole() == 4) {
						res.redirect(Application.STUDENTDASHBOARD_PATH);
						halt();
					} else if (u.getRole() == 3) {
						res.redirect(Application.TUTORDASHBOARD_PATH);
						halt();
					} else if (u.getRole() == 2) {
						res.redirect(Application.FACULTYDASHBOARD_PATH);
						halt();
					} else if (u.getRole() == 1) {
						res.redirect(Application.ADMINDASHBOARD_PATH);
						halt();
					} else {
						res.redirect(Application.LOGIN_PATH);
						halt();
					}
				} else {
					model.put("error_message", "Invalid User Credentials");
				}

				// wrongCount = 0;
				// checkEmail = "";

				// if (checkEmail.equalsIgnoreCase("")) {
				// System.out.println("set new email");
				// checkEmail = req.queryParams("email");
				// wrongCount++;
				// } else {
				// if (checkEmail.equalsIgnoreCase(req.queryParams("email"))) {
				// wrongCount++;
				// }
				// // else if
				// // (!checkEmail.equalsIgnoreCase(req.queryParams("email")))
				// // {
				// else {
				// System.out.println("email did not match must be new sp SET
				// new email");
				// wrongCount = 0;
				// checkEmail = "";
				// checkEmail = req.queryParams("email");
				// wrongCount++;
				// }
				// // if count increases 3 times generate a temp password and
				// // replace the users password
				// if (wrongCount == 3) {
				// System.out.println("Account is now locked");
				// enterEmailRecoverAccount(req, res);
				// wrongCount = 0;
				// checkEmail = "";
				// }
			}
			// showLoginPage(req, res);
			// model.put("error", error)
			// return "error";
			// res.redirect("/login");

			// "Incorrect E-mail or Password. Please try again."
			// }
		}
		return new ModelAndView(model, "sessions/signin.hbs");
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
		User user = new User();
		user = userDao.findByEmail(email);

		return user != null && user.isActive();
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
		User user = new User();
		user = userDao.findByEmail(email);

		return user != null && user.isConfirmed();
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
		User u = userDao.findByEmail(email);
		if (u != null) {
			return SecurePassword.getCheckPassword(password, u.getEncryptedPassword());
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
		User u = userDao.findByEmail(email);

		return u.getRole();
	}

	// changing the password when too many wrong entries

	/**
	 * Finds user's account in database, calls method to send user a new
	 * password, and redirects to new location.
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	public static String enterEmailRecoverAccount(Request req, Response res) {

		UserDao userDao = DaoManager.getInstance().getUserDao();
		User user;
		RecoveringUser rUser;

		if (!req.queryParams("email").isEmpty()) {
			user = userDao.findByEmail(req.queryParams("email"));
			if (user != null) {
				String tempPass = passwordRecoveryEmail(user);
				// checks to see if user already has request in table
				rUser = userDao.recoveryFindByEmail(user.getEmail());

				// changes the users passwords with the temp generated password
				// assigns new password to user
				user.setPassword(SecurePassword.getHashPassword(req.queryParams(tempPass)));
				// updates user's password
				userDao.updatePassword(user);
				// removes requested temporary password

				if (rUser != null) {
					// if previous request in table, deletes
					userDao.removeRecoveryRequest(user);
				}
				// creates entry for user attempted to recover account
				userDao.storeTempPassword(user, tempPass);
			} else {
				// user not found
				res.redirect(Application.ACCOUNTRECOVERY_PATH);
				halt();
			}
		} else {
			// one or more fields was empty
			res.redirect(Application.ACCOUNTRECOVERY_PATH);
			halt();
		}

		return "";
	}

	/**
	 * Sends recovery email to the user that has the generated temporary
	 * password and the link for the password reset.
	 *
	 * @param user
	 */
	private static String passwordRecoveryEmail(User user) {

		String tempPassword = createTempPass(user);

		String messageHeader = "<p>Hello " + user.getFirst_name() + ",</p><br />";
		String messageBody = "<p>Your account has been locked due too many " + " inccorect password entries."
				+ " Please change your password to regain access to your account by following the link below. "
				+ "Please use the provided temporary password : " + "<a href=\"http://" + Application.DOMAIN
				+ "/newinfo" + "\">Rate & Review</a></p>";
		String temporaryPassword = "<p>Temporary password: " + tempPassword + "</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + temporaryPassword + messageFooter;


		HashMap<String, String> params = new HashMap<>();
		params.put("name", user.getFirst_name());
		params.put("subject", "Rate & Review Password Recovery");
		params.put("to", user.getEmail());
		params.put("message", message);
		if (Application.ALLOW_EMAIL) {
			SendGridManager.getInstance().send(params);
		}
		return tempPassword;
	}

	/**
	 * Generates a temporary password using uppercase, lowercase, special
	 * characters and numbers
	 *
	 * @param user
	 * @return
	 */
	private static String createTempPass(User user) {
		String tempPass = "";
		String[] upperCase = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };

		String[] lowerCase = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
				"s", "t", "u", "v", "w", "x", "y", "z" };

		String[] specialCase = { "!", "@", "#", "$", "%", "^", "&", "&", "*", "?" };
		int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };

		for (int i = 0; i < 10; i++) {

			int rand = (int) (1 + (Math.random() * 10));
			int selectChar;

			switch (rand) {
			case 1:
				selectChar = (int) (Math.random() * 10);
				tempPass = tempPass + specialCase[selectChar];
				break;
			case 2:
				selectChar = (int) (Math.random() * 26);
				tempPass = tempPass + lowerCase[selectChar];
				break;
			case 3:
				selectChar = (int) (Math.random() * 26);
				tempPass = tempPass + upperCase[selectChar];
				break;
			default:
				selectChar = (int) (Math.random() * 10);
				tempPass = tempPass + numbers[selectChar];
			}

		}

		return tempPass;
	}

}
