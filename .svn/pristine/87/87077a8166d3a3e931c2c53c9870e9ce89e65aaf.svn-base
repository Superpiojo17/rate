package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.controller.BaseController;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorsDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * ProfessorsController is a controller used to process requests from the end-user
 */
public class ProfessorsController implements BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(ProfessorsController.class);

	/**
	 * Show page to list all Users
	 */
	public ModelAndView index(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		 
		// Using a Dao
        DaoManager dao = DaoManager.getInstance();
        ProfessorsDao ud = dao.getProfessorsDao();
        List<User> users = ud.all();
        ud.close();

		// Attach users to an attribute to be accessed in the view
		model.put("professors", users);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "professors/index.hbs");
	}

	/**
	 * Shows form page to create new user
	 */
	public ModelAndView newEntity(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		model.put("professor_id", req.params("id"));

		return new ModelAndView(model, "professors/new.hbs");
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

		model.put("professors_id", req.params("id"));

		return new ModelAndView(model, "professors/show.hbs");
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

		model.put("professor_id", req.params("id"));

		return new ModelAndView(model, "professors/edit.hbs");
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
