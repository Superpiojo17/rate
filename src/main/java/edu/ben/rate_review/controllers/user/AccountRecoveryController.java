package edu.ben.rate_review.controllers.user;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.RecoveringUser;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Account Recovery controller
 * 
 * @author Mike
 * @version 2-4-2017
 */
public class AccountRecoveryController {
	/**
	 * Displays view for account recovery page
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView showAccountRecoveryEmailPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/accountrecovery.hbs");
	}

	/**
	 * Displays view for new info page
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView showAccountRecoveryNewInfoPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/newinfo.hbs");
	}

	/**
	 * Method which is called after submitting the new info form. Recovers a
	 * user's lost account
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String recoverAccount(Request req, Response res) {

		UserDao userDao = DaoManager.getInstance().getUserDao();
		User user = new User();
		RecoveringUser rUser = new RecoveringUser();

		if (!req.queryParams("email").isEmpty() && !req.queryParams("temp_password").isEmpty()
				&& !req.queryParams("new_password").isEmpty() && !req.queryParams("verify_password").isEmpty()) {
			// enters if no fields are empty
			if (req.queryParams("new_password").equals(req.queryParams("verify_password"))) {
				// enters if new password matches verify password
				user = userDao.findByEmail(req.queryParams("email"));
				if (user != null) {
					// enters if user is found
					rUser = userDao.recoveryFindByEmail(user.getEmail());
					if (rUser != null) {
						// enters if recovering user is found
						if (SecurePassword.getCheckPassword(req.queryParams("temp_password"), rUser.getTempPass())) {
							// assigns new password to user
							user.setPassword(SecurePassword.getHashPassword(req.queryParams("new_password")));
							// updates user's password
							userDao.updatePassword(user);
							// removes requested temporary password
							userDao.removeRecoveryRequest(user);
							res.redirect(Application.LOGIN_PATH);

						} else {
							// incorrect temporary password or outdated
							res.redirect(Application.NEWINFO_PATH);
						}
					} else {
						// recovering user was not found
						res.redirect(Application.NEWINFO_PATH);
					}
				} else {
					// user was not found
					res.redirect(Application.NEWINFO_PATH);
				}
			} else {
				// new password and verify password do not match
				res.redirect(Application.NEWINFO_PATH);
			}
		} else {
			// all forms must be filled out
			res.redirect(Application.NEWINFO_PATH);
		}

		return "";
	}

	/**
	 * Finds user's account in database, calls method to send user a new
	 * password, and redirects to new location.
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String enterEmailRecoverAccount(Request req, Response res) {

		UserDao userDao = DaoManager.getInstance().getUserDao();
		User user = new User();
		RecoveringUser rUser = new RecoveringUser();

		if (!req.queryParams("email").isEmpty()) {
			user = userDao.findByEmail(req.queryParams("email"));
			if (user != null) {
				String tempPass = sendRecoveryEmail(user);
				// checks to see if user already has request in table
				rUser = userDao.recoveryFindByEmail(user.getEmail());
				if (rUser != null){
					// if previous request in table, deletes
					userDao.removeRecoveryRequest(user);
				}
				// creates entry for user attempted to recover account
				userDao.storeTempPassword(user, tempPass);
				res.redirect(Application.NEWINFO_PATH);
			} else {
				// user not found
				res.redirect(Application.ACCOUNTRECOVERY_PATH);
			}
		} else {
			// one or more fields was empty
			res.redirect(Application.ACCOUNTRECOVERY_PATH);
		}

		return "";
	}

	/**
	 * Sends recovery email to the user which contains a temporary password and
	 * the link in which to enter it.
	 * 
	 * @param user
	 */
	private String sendRecoveryEmail(User user) {

		String domain = "localhost:3000";
		String tempPassword = createTempPass(user);

		String subject = "Rate&Review Account Recovery";
		String messageHeader = "Hello " + user.getFirst_name() + ",\n\n\n";

		String messageBody = "This message is to confirm that you have requested"
				+ " to recover your account. If you did not request an account recovery,"
				+ " please change your password, as your account may have been compromised. "
				+ "To proceed, use the provided temporary password at " + domain + "/newinfo\n\n";
		String temporaryPassword = "Temporary password: " + tempPassword;
		String messageFooter = "\n\n\nSincerely,\n\nThe Rate&Review Team";
		String message = messageHeader + messageBody + temporaryPassword + messageFooter;
		System.out.println(tempPassword);
		Email.deliverEmail(user.getFirst_name(), user.getEmail(), subject, message);
		return tempPassword;
	}

	/**
	 * Generates a temporary password for the user
	 * 
	 * @param user
	 * @return
	 */
	private String createTempPass(User user) {
		String tempPass = "";
		int[] numbers = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0 };
		String[] lowerCase = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
				"s", "t", "u", "v", "w", "x", "y", "z" };
		String[] upperCase = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };

		for (int i = 0; i < 10; i++) {
			int rand = (int) (1 + (Math.random() * 4));
			int selectChar;

			switch (rand) {
			case 1:
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
