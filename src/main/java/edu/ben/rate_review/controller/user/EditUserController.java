package edu.ben.rate_review.controller.user;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.RecoveringUser;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class EditUserController {

	public ModelAndView showEditUserPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		UserDao user = DaoManager.getInstance().getUserDao();
		Session session = req.session();

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long (be careful because what if I put a string in?
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);
		// Get user if ID is valid
		User u = user.findById(id);

		// Authorize that I can edit the user selected
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// create the form object, put it into request
		model.put("user_form", new UserForm(u));

		// Render the page and let the form do it's work!
		return new ModelAndView(model, "users/edituser.hbs");
	}

	public String updateUser(Request req, Response res) {
		String idString = req.params("id");
		long id = Long.parseLong(idString);
		UserDao userDao = DaoManager.getInstance().getUserDao();
		UserForm user = new UserForm();
		user.setFirst_name(req.queryParams("first_name"));
		user.setLast_name(req.queryParams("last_name"));
		user.setEmail(req.queryParams("email"));
		user.setRole(Integer.parseInt(req.queryParams("role")));
		user.setMajor(req.queryParams("major"));
		user.setSchool_year(Integer.parseInt(req.queryParams("year")));
		user.setId(id);
		

		userDao.updateUser(user);

		System.out.println(user.getEmail());

		res.redirect(Application.ALLUSERS_PATH);
		return " ";

	}

}
