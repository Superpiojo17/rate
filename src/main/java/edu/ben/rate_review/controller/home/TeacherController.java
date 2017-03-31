package edu.ben.rate_review.controller.home;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class TeacherController {
	/**
	 * Show log in page
	 * 
	 * @throws SQLException
	 */
	public ModelAndView showTeacherPage(Request req, Response res) throws SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		DaoManager udao = DaoManager.getInstance();
		UserDao ud = udao.getUserDao();

		if (req.queryParams("search") != null) {

			String searchType = "name";
			String searchTxt = req.queryParams("search").toLowerCase();

			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<User> tempProf = ud.searchProf(searchType, searchTxt);
				if (tempProf.size() > 0) {

					model.put("professors", tempProf);
				} else {
					List<User> profs = ud.searchProf(searchType, searchTxt);
					model.put("error", "No Results Found");
					model.put("professors", profs);
				}
			} else {
				List<User> profs = ud.allProfessors();
				model.put("error", "Cannot leave search bar blank");
				model.put("professors", profs);
			}
		} else {
			List<User> profs = ud.allProfessors();
			model.put("professors", profs);
		}

		model.put("current_user", u);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/teacher.hbs");
	}

}
