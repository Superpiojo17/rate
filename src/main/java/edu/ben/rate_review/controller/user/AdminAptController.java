package edu.ben.rate_review.controller.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class AdminAptController {

	public ModelAndView showAllAptByDeptPage(Request req, Response res) throws AuthException, SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		} else {
			User u = (User) session.attribute("current_user");

			if (u.getRole() != 1) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
			}
			model.put("current_user", u);
		}

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		if (req.queryParams("search") != null) {

			String searchType = "name";
			String searchTxt = req.queryParams("search").toLowerCase();

			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<User> tempUsers = ud.search(searchType, searchTxt);
				if (tempUsers.size() > 0) {

					model.put("users", tempUsers);
				} else {
					List<User> users = ud.search(searchType, searchTxt);
					model.put("error", "No Results Found");
					model.put("users", users);
				}
			} else {
				List<User> users = ud.sortbyRole();
				model.put("error", "Cannot leave search bar blank");
				model.put("users", users);
			}
		} else {
			List<User> users = ud.sortbyRole();
			model.put("users", users);
		}

		// DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		ud.close();
		ad.close();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/allusers.hbs");
	}

}
