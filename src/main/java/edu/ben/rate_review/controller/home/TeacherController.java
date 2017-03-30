package edu.ben.rate_review.controller.home;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class TeacherController {
	/**
	 * Show log in page
	 */
	public ModelAndView showTeacherPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		UserDao ud = dao.getUserDao();
		List<User> professors = ud.allProfessors();
		model.put("professors", professors);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/teacher.hbs");
	}

}
