package edu.ben.rate_review.controller.user;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.StudentInCourseDao;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.formatTime.FormatTimeAndDate;
import edu.ben.rate_review.massRegistration.MassEnroll;
import edu.ben.rate_review.massRegistration.MassRegistration;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.StudentInCourse;
//import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorAppointment;
//import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class AdminDashboardController {
	public ModelAndView showAdminDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		model.put("current_user", u);

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		List<ProfessorReview> flagged = reviewDao.listAllFlaggedComments();

		List<ProfessorReview> allComments = reviewDao.listAllComments();
		model.put("all_comments", allComments);

		boolean unseen_flagged_comments = false;
		if (!flagged.isEmpty()) {
			unseen_flagged_comments = true;
		}

		ad.close();
		reviewDao.close();
		// list of flagged comments
		model.put("flagged", flagged);
		// count of unreviewed flagged comments
		model.put("number_of_flagged", flagged.size());
		// boolean returns true if comments need to be reviewed
		model.put("unseen_flagged_comments", unseen_flagged_comments);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/admindashboard.hbs");
	}

	public ModelAndView showAllUsersPage(Request req, Response res) throws AuthException, SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
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

		model.put("current_user", u);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		ad.close();
		ud.close();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/allusers.hbs");
	}

	public ModelAndView showManageCoursesLandingPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		ad.close();
		model.put("announcements", announcements);

		// Render the page
		return new ModelAndView(model, "users/courseslanding.hbs");
	}

	public ModelAndView showManageReviewsLandingPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		ad.close();
		// Render the page
		return new ModelAndView(model, "users/reviewslanding.hbs");
	}

	public ModelAndView showEditApt(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		AnnouncementDao ad = DaoManager.getInstance().getAnnouncementDao();
		UserDao ud = DaoManager.getInstance().getUserDao();

		// Get the :id from the url
		String idString = req.params("id");
		long id = Long.parseLong(idString);

		TutorDao tdao = DaoManager.getInstance().getTutorDao();
		TutorAppointment apt = tdao.findAppointmentByID(id);
		apt.setTime(FormatTimeAndDate.formatTime(apt.getTime()));
		apt.setDate(FormatTimeAndDate.formatDate(apt.getDate()));

		User user = ud.findById(apt.getTutor_id());

		model.put("department", user.getMajor());

		model.put("depttutors", ud.allTutorsByMajor(user.getMajor()));

		model.put("apt", apt);

		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ud.close();
		ad.close();
		tdao.close();
		// Render the page
		return new ModelAndView(model, "users/editappointment.hbs");
	}

	public ModelAndView adminDeleteApt(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		String idString = req.params("id");
		long id = Long.parseLong(idString);

		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		UserDao uDao = DaoManager.getInstance().getUserDao();

		TutorAppointment tempApt = tDao.findAppointmentByID(id);

		User user = uDao.findById(tempApt.getTutor_id());

		tDao.cancelTutorAppointment(tempApt.getAppointment_id());

		model.put("error", "You have deleted an appointment on " + tempApt.getDate());

		List<TutorAppointment> appointments = tDao.listUpcomingAllApptByDept(user.getMajor());

		for (int i = 0; i < appointments.size(); i++) {
			appointments.get(i).setTime(FormatTimeAndDate.formatTime(appointments.get(i).getTime()));
			appointments.get(i).setDate(FormatTimeAndDate.formatDate(appointments.get(i).getDate()));
		}
		model.put("upcomingappointments", appointments);

		List<TutorAppointment> pastAppointments = tDao.listPastAllApptByDept(user.getMajor());

		for (int i = 0; i < pastAppointments.size(); i++) {
			pastAppointments.get(i).setTime(FormatTimeAndDate.formatTime(pastAppointments.get(i).getTime()));
			pastAppointments.get(i).setDate(FormatTimeAndDate.formatDate(pastAppointments.get(i).getDate()));
		}
		model.put("pastappointments", pastAppointments);

		AnnouncementDao ad = DaoManager.getInstance().getAnnouncementDao();
		List<Announcement> announcements = ad.all();

		ad.close();
		tDao.close();
		uDao.close();

		model.put("announcements", announcements);
		model.put("department", user.getMajor());

		tempApt.setAppointment_status(Boolean.parseBoolean(req.queryParams("status")));
		return new ModelAndView(model, "users/appointments.hbs");
	}

	public ModelAndView adminUpdateApt(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		String idString = req.params("id");
		long id = Long.parseLong(idString);

		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		UserDao uDao = DaoManager.getInstance().getUserDao();

		TutorAppointment tempApt = tDao.findAppointmentByID(id);

		User user = uDao.findById(Integer.parseInt(req.queryParams("selecttutor")));
		Long userID = user.getId();

		tempApt.setTutor_firstname(user.getFirst_name());
		tempApt.setTutor_lastname(user.getLast_name());
		tempApt.setTutor_id(userID);
		tempApt.setAppointment_status(Boolean.parseBoolean(req.queryParams("status")));

		tempApt.setDate(FormatTimeAndDate.formatDate(tempApt.getDate()));

		tDao.updateApt(tempApt);

		model.put("message", "You have edited an appointment on " + tempApt.getDate());

		List<TutorAppointment> appointments = tDao.listUpcomingAllApptByDept(user.getMajor());

		for (int i = 0; i < appointments.size(); i++) {
			appointments.get(i).setTime(FormatTimeAndDate.formatTime(appointments.get(i).getTime()));
			appointments.get(i).setDate(FormatTimeAndDate.formatDate(appointments.get(i).getDate()));
		}
		model.put("upcomingappointments", appointments);

		List<TutorAppointment> pastAppointments = tDao.listPastAllApptByDept(user.getMajor());

		for (int i = 0; i < pastAppointments.size(); i++) {
			pastAppointments.get(i).setTime(FormatTimeAndDate.formatTime(pastAppointments.get(i).getTime()));
			pastAppointments.get(i).setDate(FormatTimeAndDate.formatDate(pastAppointments.get(i).getDate()));
		}
		model.put("pastappointments", pastAppointments);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		model.put("department", user.getMajor());

		tempApt.setAppointment_status(Boolean.parseBoolean(req.queryParams("status")));

		tDao.close();
		uDao.close();
		ad.close();
		return new ModelAndView(model, "users/appointments.hbs");
	}

	public ModelAndView showManageAptLandingPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ad.close();
		// Render the page
		return new ModelAndView(model, "users/adminaptlanding.hbs");
	}

	public ModelAndView showAllDeptApt(Request req, Response res) throws AuthException, SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		// Get the :id from the url
		String department = req.params("department");
		model.put("department", department);

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		TutorDao td = dao.getTutorDao();

		if (req.queryParams("search") != null) {

			String searchType = "name";
			String searchTxt = req.queryParams("search").toLowerCase();

			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<TutorAppointment> tempApts = td.searchApt(searchType, searchTxt);
				if (tempApts.size() > 0) {

					for (int i = 0; i < tempApts.size(); i++) {
						tempApts.get(i).setTime(FormatTimeAndDate.formatTime(tempApts.get(i).getTime()));
						tempApts.get(i).setDate(FormatTimeAndDate.formatDate(tempApts.get(i).getDate()));
					}
					model.put("upcomingappointments", tempApts);
				} else {
					List<TutorAppointment> apts = td.searchApt(searchType, searchTxt);
					model.put("error", "No Results Found");

					for (int i = 0; i < apts.size(); i++) {
						apts.get(i).setTime(FormatTimeAndDate.formatTime(apts.get(i).getTime()));
						apts.get(i).setDate(FormatTimeAndDate.formatDate(apts.get(i).getDate()));
					}

					model.put("upcomingapointments", apts);
				}
			} else {
				List<TutorAppointment> apts = td.listUpcomingAllApptByDept(department);
				model.put("error", "Cannot leave search bar blank");
				for (int i = 0; i < apts.size(); i++) {
					apts.get(i).setTime(FormatTimeAndDate.formatTime(apts.get(i).getTime()));
					apts.get(i).setDate(FormatTimeAndDate.formatDate(apts.get(i).getDate()));
				}
				model.put("upcomingappointments", apts);

				List<TutorAppointment> pastAppointments = td.listPastAllApptByDept(department);

				for (int i = 0; i < pastAppointments.size(); i++) {
					pastAppointments.get(i).setTime(FormatTimeAndDate.formatTime(pastAppointments.get(i).getTime()));
					pastAppointments.get(i).setDate(FormatTimeAndDate.formatDate(pastAppointments.get(i).getDate()));
				}
				model.put("pastappointments", pastAppointments);

			}
		} else {
			List<TutorAppointment> appointments = td.listUpcomingAllApptByDept(department);

			for (int i = 0; i < appointments.size(); i++) {
				appointments.get(i).setTime(FormatTimeAndDate.formatTime(appointments.get(i).getTime()));
				appointments.get(i).setDate(FormatTimeAndDate.formatDate(appointments.get(i).getDate()));
			}
			model.put("upcomingappointments", appointments);

			List<TutorAppointment> pastAppointments = td.listPastAllApptByDept(department);

			for (int i = 0; i < pastAppointments.size(); i++) {
				pastAppointments.get(i).setTime(FormatTimeAndDate.formatTime(pastAppointments.get(i).getTime()));
				pastAppointments.get(i).setDate(FormatTimeAndDate.formatDate(pastAppointments.get(i).getDate()));
			}
			model.put("pastappointments", pastAppointments);
		}

		model.put("current_user", u);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ud.close();
		td.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/appointments.hbs");
	}

	public ModelAndView showManageTutorsLandingPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		ad.close();
		model.put("announcements", announcements);

		// Render the page
		return new ModelAndView(model, "users/managetutorslanding.hbs");
	}

	public ModelAndView showEditAnnouncements(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/announcement.hbs");
	}

	public String massRegister(Request req, Response res)
			throws FileNotFoundException, NoSuchMethodException, SecurityException {
		// Just a hash to pass data from the servlet to the page
		// HashMap<String, Object> model = new HashMap<>();
		MassRegistration.massRegisterUsers();
		res.redirect("/allusers");
		// Tell the server to render the index page with the data in the model
		return "";
	}

	public ModelAndView addAnnouncement(Request req, Response res) {

		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		AnnouncementDao ad = DaoManager.getInstance().getAnnouncementDao();
		Announcement announcement = new Announcement();

		SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");

		try {
			String formatteddate = myFormat.format(fromUser.parse(req.queryParams("date")));
			announcement.setDate(formatteddate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		announcement.setAnnouncement_content(req.queryParams("message"));

		ad.save(announcement);

		// DaoManager dao = DaoManager.getInstance();
		// AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		ad.close();
		model.put("error", "You have added an event for " + announcement.getDate());

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/announcement.hbs");

	}

	public String sortByLastName(Request req, Response res) {

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		ud.sortByLastName();
		ud.close();
		res.redirect("/allusers");
		return "";

	}

	/**
	 * Deletes a flagged comment that the admin deems offensive
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String handleFlaggedComment(Request req, Response res) {
		DaoManager dao = DaoManager.getInstance();
		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		StudentInCourseDao sDao = dao.getStudentInCourseDao();

		// grabs course id from review comment
		String courseToRemoveString = req.queryParams("flagged_comment");
		long courseID = Long.parseLong(courseToRemoveString);
		if (courseID > 0) {
			// finds and removes the review
			ProfessorReview review = reviewDao.findReview(courseID);
			reviewDao.setCommentRemoved(review);
			reviewDao.setCommentNotFlagged(review);
			StudentInCourse course = sDao.findByStudentCourseId(courseID);
			sDao.disableEditReview(course);
		} else {
			courseID *= -1;
			ProfessorReview review = reviewDao.findReview(courseID);
			reviewDao.setCommentNotFlagged(review);
			reviewDao.setCommentApproved(review);
		}
		reviewDao.close();
		sDao.close();
		// redirects back to dashboard
		res.redirect(Application.ADMINDASHBOARD_PATH);
		return "";
	}

	/**
	 * Calls method which enrolls students in mass
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String massEnrollStudents(Request req, Response res) {
		//DaoManager dao = DaoManager.getInstance();

		MassEnroll.enrollStudents();
		
		res.redirect(Application.COURSELANDING_PATH);
		return "";
	}

}
