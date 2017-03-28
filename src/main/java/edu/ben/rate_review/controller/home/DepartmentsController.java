package edu.ben.rate_review.controller.home;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class DepartmentsController {

	public ModelAndView showDepartmentsPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		UserDao uDao = DaoManager.getInstance().getUserDao();
		Session session = req.session();
		//List list = uDao.all();
		List list = uDao.sortByMajor("CMSC");

		for (int i = 0; i < list.size(); i++) {
			User user = (User) list.get(i);

			System.out.println(user.getFirst_name() + " " + user.getMajor());

		}

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/departments.hbs");
	}
}
