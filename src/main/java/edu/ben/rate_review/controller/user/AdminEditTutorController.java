package edu.ben.rate_review.controller.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashSet;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class AdminEditTutorController {

	public ModelAndView showDeptTutorsPage(Request req, Response res) throws AuthException, SQLException {
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
			} else {
				model.put("user_admin", true);
			}
		}

		String department = req.params("department");

		DaoManager dao = DaoManager.getInstance();
		TutorDao td = dao.getTutorDao();
		if (req.queryParams("search") != null) {
			String searchType = "name";
			String searchTxt = req.queryParams("search").toLowerCase();
			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<Tutor> tutors = new ArrayList<Tutor>();
				List<Tutor> searchTutors = td.search(searchType, searchTxt);
				for (int i = 0; i < searchTutors.size(); i++) {

					if (searchTutors.get(i).getSubject().equalsIgnoreCase(department)) {

						tutors.add(searchTutors.get(i));

					}
				}
				if (tutors.size() > 0) {
					model.put("tutors", tutors);
				} else {
					List<Tutor> Temptutors = td.search(searchType, searchTxt);
					model.put("searchmessage", "No Results Found");
					model.put("tutors", Temptutors);
				}
			} else {
				List<Tutor> tutors = new ArrayList<Tutor>();
				List<Tutor> Temptutors = td.listAllTutors();

				for (int i = 0; i < Temptutors.size(); i++) {

					if (Temptutors.get(i).getSubject().equalsIgnoreCase(department)) {

						tutors.add(Temptutors.get(i));

					}
				}
				model.put("searchmessage", "Cannot leave search bar blank");
				model.put("tutors", tutors);

			}
		} else {
			List<Tutor> tutors = new ArrayList<Tutor>();
			List<Tutor> Temptutors = td.listAllTutors();

			for (int i = 0; i < Temptutors.size(); i++) {

				if (Temptutors.get(i).getSubject().equalsIgnoreCase(department)) {

					tutors.add(Temptutors.get(i));

				}
			}

			model.put("tutors", tutors);

		}

		model.put("department", department);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		td.close();
		ad.close();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/tutors.hbs");
	}

	public ModelAndView showAdminEditTutorPage(Request req, Response res) throws AuthException {
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
		}
		UserDao user = DaoManager.getInstance().getUserDao();
		TutorDao tutor = DaoManager.getInstance().getTutorDao();

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		Tutor t = tutor.findById(id);

		String department = t.getSubject();

		model.put("tutor_form", new TutorForm(t));

		List<User> deptstudents = user.allByMajor(department);

		model.put("department", department);

		model.put("deptstudents", deptstudents);
		// Authorize that the user can edit the user selected
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// create the form object, put it into request
		// model.put("tutor_form", new TutorForm(u));

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();

		user.close();
		tutor.close();
		ad.close();
		model.put("announcements", announcements);

		// Render the page
		return new ModelAndView(model, "users/adminedittutor.hbs");

	}

	public ModelAndView adminUpdateTutor(Request req, Response res) {
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

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		UserDao uDao = DaoManager.getInstance().getUserDao();
		TutorForm tutor = new TutorForm();

		Tutor tempTutor = tDao.findById(id);

		String department = tempTutor.getSubject();

		model.put("department", tempTutor.getSubject());

		tutor.setStudent_id(Long.parseLong(req.queryParams("selecttutor")));
		tutor.setId(id);
		tutor.setCourse_id(tempTutor.getCourse_id());

		tDao.adminUpdateTutor(tutor);

		User user = uDao.findById(Long.parseLong(req.queryParams("selecttutor")));

		if (user.getRole() == 4) {

			uDao.updateRole(user, 3);
			model.put("message",
					"You have turned " + user.getFirst_name() + " " + user.getLast_name() + " into a tutor");
		}

		// DaoManager dao = DaoManager.getInstance();
		// TutorDao td = dao.getTutorDao();
		List<Tutor> tutors = new ArrayList<Tutor>();
		List<Tutor> Temptutors = tDao.listAllTutors();
		for (int i = 0; i < Temptutors.size(); i++) {

			if (Temptutors.get(i).getSubject().equalsIgnoreCase(department)) {

				tutors.add(Temptutors.get(i));

			}
		}

		model.put("tutors", tutors);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		model.put("error", "You have assigned " + user.getFirst_name() + " " + user.getLast_name() + " to "
				+ tempTutor.getCourse_name());

		tDao.close();
		ad.close();
		uDao.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/tutors.hbs");

	}

	public ModelAndView showAddTutorsLandingPage(Request req, Response res) throws AuthException {
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
		}

		DaoManager adao = DaoManager.getInstance();
		CourseDao cDao = DaoManager.getInstance().getCourseDao();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		String department = req.params("department");

		model.put("department", department);

		List<Course> courses = cDao.allByDept(department);
		model.put("courses", courses);
		cDao.close();
		ad.close();
		// Render the page
		return new ModelAndView(model, "users/addtutorlanding.hbs");
	}

	public ModelAndView showAddTutorsPage(Request req, Response res) throws AuthException {
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
		}
		DaoManager adao = DaoManager.getInstance();
		CourseDao cDao = DaoManager.getInstance().getCourseDao();
		UserDao user = DaoManager.getInstance().getUserDao();
		TutorDao tutor = DaoManager.getInstance().getTutorDao();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		Course course = cDao.findById(id);

		String department = course.getSubject();

		model.put("department", department);

		List<User> deptstudents = user.allByMajor(department);

		model.put("deptstudents", deptstudents);

		model.put("course", course);
		cDao.close();
		user.close();
		tutor.close();
		ad.close();
		// Render the page
		return new ModelAndView(model, "users/adminaddtutor.hbs");
	}

	public ModelAndView adminAddTutor(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();
		CourseDao cDao = DaoManager.getInstance().getCourseDao();
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		UserDao uDao = DaoManager.getInstance().getUserDao();

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		Course course = cDao.findById(id);

		Tutor tutor = new Tutor();

		tutor.setCourse_id(course.getId());
		tutor.setCourse_name(cDao.findById(course.getId()).getCourse_name());
		tutor.setStudent_id(Long.parseLong(req.queryParams("selecttutor")));

		User user = uDao.findById(Long.parseLong(req.queryParams("selecttutor")));

		if (user.getRole() == 4) {

			uDao.updateRole(user, 3);
			model.put("message",
					"You have turned " + user.getFirst_name() + " " + user.getLast_name() + " into a tutor");
		}

		tutor.setProfessor_id(course.getProfessor_id());

		tDao.save(tutor);

		String department = course.getSubject();

		// Session session = req.session();
		// User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// DaoManager dao = DaoManager.getInstance();
		// TutorDao td = dao.getTutorDao();
		List<Tutor> tutors = new ArrayList<Tutor>();
		List<Tutor> Temptutors = tDao.listAllTutors();
		for (int i = 0; i < Temptutors.size(); i++) {

			if (Temptutors.get(i).getSubject().equalsIgnoreCase(department)) {

				tutors.add(Temptutors.get(i));

			}
		}

		model.put("tutors", tutors);

		model.put("department", department);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		cDao.close();
		tDao.close();
		uDao.close();
		ad.close();
		

		model.put("error", "You have assigned " + user.getFirst_name() + " " + user.getLast_name() + " to "
				+ tutor.getCourse_name());

		return new ModelAndView(model, "users/tutors.hbs");

	}

	public ModelAndView adminDeleteTutor(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		// Session session = req.session();
		// User u = (User) session.attribute("current_user");
		String idString = req.params("id");
		long id = Long.parseLong(idString);
		TutorDao tutorDao = DaoManager.getInstance().getTutorDao();
		Long studentID = tutorDao.getStudentId(id);
		Tutor tempTutor = tutorDao.findById(id);
		String department = tempTutor.getSubject();

		tutorDao.deleteTutor(id);

		if (tutorDao.findByStudentId(studentID) == null) {
			tutorDao.changeTutorRole(studentID);

		}

		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// DaoManager dao = DaoManager.getInstance();
		// TutorDao td = dao.getTutorDao();
		List<Tutor> tutors = new ArrayList<Tutor>();
		List<Tutor> Temptutors = tutorDao.listAllTutors();
		for (int i = 0; i < Temptutors.size(); i++) {

			if (Temptutors.get(i).getSubject().equalsIgnoreCase(department)) {

				tutors.add(Temptutors.get(i));

			}
		}

		model.put("message", "You have removed " + tempTutor.getTutor_first_name() + " "
				+ tempTutor.getTutor_last_name() + " from " + tempTutor.getCourse_name());

		model.put("tutors", tutors);

		model.put("department", department);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		tutorDao.close();
		ad.close();
		return new ModelAndView(model, "users/tutors.hbs");

	}

}
