package edu.ben.rate_review.controller.user;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.massRegistration.MassRegistration;
import edu.ben.rate_review.models.Announcement;
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


		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/admindashboard.hbs");
	}

	public ModelAndView showAllUsersPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		List<User> users = ud.sortbyRole();
		model.put("users", users);
		
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/allusers.hbs");
	}

	public ModelAndView showEditAnnouncements(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/announcement.hbs");
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

	public String addAnnouncement(Request req, Response res) {

		AnnouncementDao announceDao = DaoManager.getInstance().getAnnouncementDao();
		Announcement announcement = new Announcement();

		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");

		try {
			String formatteddate = myFormat.format(fromUser.parse(req.queryParams("date")));
			announcement.setDate(formatteddate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		announcement.setAnnouncement_content(req.queryParams("message"));

		announceDao.save(announcement);

		res.redirect("/announcement");
		return "";

	}

	public String sortByLastName(Request req, Response res) {

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		ud.sortByLastName();

		res.redirect("/allusers");
		return "";

	}

}
