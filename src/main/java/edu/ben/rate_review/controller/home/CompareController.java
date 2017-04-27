package edu.ben.rate_review.controller.home;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
//import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class CompareController {
	/**
	 * Show log in page
	 * 
	 * @throws SQLException
	 */
	public ModelAndView showComparePage(Request req, Response res) throws SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		
		if (u != null){
			if (u.getRole() == 1){
				model.put("user_admin", true);
			} else if (u.getRole() == 2){
				model.put("user_professor", true);
			} else if (u.getRole() == 3){
				model.put("user_tutor", true);
			} else {
				model.put("user_student", true);
			}
		} else {
			model.put("user_null", true);
		}

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		UserDao ud = dao.getUserDao();

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

		ad.close();
		ud.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/compare.hbs");
	}

}
