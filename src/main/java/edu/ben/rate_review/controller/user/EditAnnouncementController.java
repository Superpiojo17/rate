package edu.ben.rate_review.controller.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.AnnouncementForm;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class EditAnnouncementController {

	public ModelAndView showEditAnnouncementPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		AnnouncementDao announcement = DaoManager.getInstance().getAnnouncementDao();
		Session session = req.session();

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		// Get user if ID is valid
		// User u = user.findById(id);

		Announcement a = announcement.findById(id);

		// Authorize that the user can edit the user selected
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// create the form object, put it into request
		model.put("announcement_form", new AnnouncementForm(a));

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Render the page
		return new ModelAndView(model, "users/editannouncement.hbs");
	}

	public ModelAndView showAddAnnouncementPage(Request req, Response res) throws AuthException {
		HashMap<String, Object> model = new HashMap<>();
		AnnouncementDao announcement = DaoManager.getInstance().getAnnouncementDao();
		Session session = req.session();

		// Authorize that the user can edit the user selected
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		return new ModelAndView(model, "users/addannouncement.hbs");

	}

	public ModelAndView updateAnnouncement(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		AnnouncementDao aDao = DaoManager.getInstance().getAnnouncementDao();
		AnnouncementForm announcement = new AnnouncementForm();
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
		announcement.setId(id);

		aDao.updateAnnouncement(announcement);

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		model.put("error", "You have edited an event for " + announcement.getDate());

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/announcement.hbs");

	}

	public ModelAndView addAnnouncement(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

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

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		model.put("error", "You have added an event for " + announcement.getDate());

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/announcement.hbs");

	}

	public ModelAndView deleteAnnouncement(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		AnnouncementDao announcementDao = DaoManager.getInstance().getAnnouncementDao();
		

		model.put("error", "You have deleted an event");

		announcementDao.deletAnnouncement(id);
		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/announcement.hbs");

	}

}
