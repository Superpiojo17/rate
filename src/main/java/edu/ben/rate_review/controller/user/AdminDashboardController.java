package edu.ben.rate_review.controller.user;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.massRegistration.MassRegistration;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class AdminDashboardController {
	public ModelAndView showAdminDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		
		Session session = req.session();
		User u = (User) session.attribute("current_user");
//		AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		model.put("current_user", u);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/admindashboard.hbs");
	}

	public ModelAndView showAllUsersPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		
		Session session = req.session();
		User u = (User) session.attribute("current_user");
//		AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		List<User> users = ud.all();
		model.put("users", users);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/allusers.hbs");
	}

	public String massRegister(Request req, Response res)
			throws FileNotFoundException, NoSuchMethodException, SecurityException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		MassRegistration.massRegisterUsers();
		res.redirect("/allusers");
		// Tell the server to render the index page with the data in the model
		return "";
	}

}
