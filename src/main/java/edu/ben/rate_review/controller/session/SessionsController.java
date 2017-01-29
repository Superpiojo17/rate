package edu.ben.rate_review.controller.session;

import static spark.Spark.redirect;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.email;
//import edu.ben.rate_review.daos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class SessionsController {

	public ModelAndView showRegister(Request req, Response res) {

		return new ModelAndView(null, "sessions/register.hbs");
	}

	public String register(Request req, Response res) {
		UserDao userDao = DaoManager.getInstance().getUserDao();
		// String data = req.params("the_name_field_of_the_input");
		// variables to keep track of empty field
		boolean emptyField = false;
		String emptyFieldName = "";

		// Checks to see if each field is filled out.
		if (req.params("first_name").isEmpty()) {
			emptyFieldName = "First name";
			emptyField = true;
		} else if (req.params("last_name").isEmpty()) {
			emptyFieldName = "Last name";
			emptyField = true;
		} else if (req.params("email").isEmpty()) {
			emptyFieldName = "Email";
			emptyField = true;
		} else if (req.params("password").isEmpty()) {
			emptyFieldName = "Password";
			emptyField = true;
		}

		// If a field is left empty, the user will be sent back to the
		// registration page. They will be told which was the first field they
		// left empty.
		if (emptyField) {
			// error message here - emptyFieldMessage(emptyFieldName)
			res.redirect("/register");
		}

		// Checks that the user's first and last name are valid. They must each
				// be at least 2 characters long and only contain letters.
				if (!validateName(req.params("first_name"), req.params("last_name"))) {
					// error message - "First and last name must be at least 2 characters long and only contain letters."
					res.redirect("/register");
				}
				// Checks that the password and verify password fields match.
				else if (!req.params("password").equals(req.params("verify_password"))) {
					//"Passwords do not match."
					res.redirect("/register");
				}
				// Checks that a benedictine email is being used to register.
				else if (!validateEmail(req.params("email"))) {
					//"You must use a benedictine e-mail to register."
					res.redirect("/register");
				}
				// creates the user
				else {


					// checks if email has already been registered
					if (!userDao.checkEmail(req.params("email"))) {

						// will determine if account has confirmed registration through
						// email
						boolean registrationConfirmed = false;
						// will determine whether or not account is currently active
						boolean accountActive = true;

						createUser(req.params("first_name"), req.params("last_name"),
								req.params("email"), req.params("password"),
								Integer.parseInt(req.params("role_id")), registrationConfirmed, accountActive);

						res.redirect("/login");

					} else {
						//"This email is already in use."
						res.redirect("/register");
					}

				}
		
		return "";
	}
	
	/**
	 * Creates the output the user sees when they did not fill in all their
	 * fields. It will tell them the name of the first field they failed to fill
	 * out.
	 * 
	 * @param field
	 * @return
	 */
	private String emptyFieldMessage(String field) {
		String firstEmptyField = "You are missing one or more fields starting with: ";
		return firstEmptyField + field;
	}

	/**
	 * Validates that the user's name is a valid length and only contains
	 * letters.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	private boolean validateName(String firstName, String lastName) {

		// checks that first and last name are at least 2 characters long
		if (firstName.length() >= 2 && lastName.length() >= 2) {
			// concatenates name
			String fullName = firstName + lastName;
			// sends full name to character array
			char[] name = fullName.toCharArray();

			// checks that each character of name is a letter
			for (int i = 0; i < fullName.length(); i++) {
				if (!Character.isAlphabetic(name[i])) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;

	}

	/**
	 * Checks that an e-mail address ends with a benedictine address
	 * 
	 * @param email
	 * @return
	 */
	private boolean validateEmail(String email) {
		if (email.length() > 8 && email.endsWith("@ben.edu")) {
			return true;
		}
		return false;
	}

	/**
	 * Method which creates the user
	 * 
	 * @param first_name
	 * @param last_name
	 * @param email
	 * @param password
	 * @param role_id
	 * @param confirmed
	 * @param active
	 */
	public static void createUser(String first_name, String last_name, String email, String password, int role_id,
			boolean confirmed, boolean active) {
		UserDao user = DaoManager.getInstance().getUserDao();
		//User newUser = new User(first_name, last_name, email, SecurePassword.getHashPassword(password), role_id, confirmed, active);
		//user.addUser(newUser);
		confirmRegistration(first_name, email, role_id);
	}

	/**
	 * Method which will send an email to the registering user.
	 * 
	 * @param first_name
	 * @param email
	 * @param role_id
	 */
	private static void confirmRegistration(String first_name, String email, int role_id) {

		String accountType = "";
		if (role_id == 1) {
			accountType = "ADMIN";
		} else if (role_id == 2) {
			accountType = "PROFESSOR";
		} else if (role_id == 3) {
			accountType = "TUTOR";
		} else {
			accountType = "STUDENT";
		}

		String subject = "Rate&Review Registration";
		String messageHeader = "Hello " + first_name + ",\n\n\n";
		String messageBody = "This message is to confirm that you have successfully signed up for Rate&Review as a "
				+ accountType + ". Please join us on the website to get started!";
		String messageFooter = "\n\n\nSincerely,\n\nThe Rate&Review Team";
		String message = messageHeader + messageBody + messageFooter;

		//Email.deliverEmail(first_name, email, subject, message);

	}
}
