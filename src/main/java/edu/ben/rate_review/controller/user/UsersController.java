package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.controller.BaseController;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * UsersController is a controller used to process requests from the end-user
 */
public class UsersController implements BaseController {

	private static Logger logger = LoggerFactory.getLogger(UsersController.class);

	/**
	 * Show page to list all Users
	 */
	public ModelAndView index(Request req, Response res) {
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
		// Using a Dao
		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		List<User> users = ud.all();
		ud.close();

		// Attach users to an attribute to be accessed in the view
		model.put("users", users);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/index.hbs");
	}

	/**
	 * Shows form page to create new user
	 */
	public ModelAndView newEntity(Request req, Response res) {
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
		model.put("user_id", req.params("id"));

		return new ModelAndView(model, "users/new.hbs");
	}

	/**
	 * Performs the creation of the user and saves to database
	 */
	public String create(Request req, Response res) {
		res.redirect(Application.USERS_PATH);
		return "";
	}

	/**
	 * Shows the page to view a single user
	 */
	public ModelAndView show(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		model.put("user_id", req.params("id"));

		return new ModelAndView(model, "users/show.hbs");
	}

	/**
	 * Shows form to edit a single user
	 *
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView edit(Request req, Response res) {
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

		model.put("user_id", req.params("id"));

		return new ModelAndView(model, "users/edit.hbs");
	}

	/**
	 * Performs the update to a user
	 *
	 * @param req
	 * @param res
	 * @throws AuthException
	 */
	public String update(Request req, Response res) throws AuthException {
		AuthPolicyManager.getInstance().getUserPolicy().edit(new User());
		res.redirect(Application.USERS_PATH);
		return "";
	}

	/**
	 * Deletes the user from the database
	 *
	 * @param req
	 * @param res
	 */
	public String destroy(Request req, Response res) {
		res.redirect(Application.USERS_PATH);
		return "";
	}

}
