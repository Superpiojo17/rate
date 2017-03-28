package edu.ben.rate_review.controller.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class FacultyDashboardController {
	/**
	 * Show log in page
	 * 
	 * @throws AuthException
	 */
	public ModelAndView showFacultyDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u.getMajor() == null) {
			model.put("completeProfile", true);
		}
		// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();

		DaoManager adao = DaoManager.getInstance();
		DaoManager cdao = DaoManager.getInstance();

		AnnouncementDao ad = adao.getAnnouncementDao();

		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		CourseDao cd = cdao.getCourseDao();
		List<Course> courses = cd.allByProfessor(u.getId());
		if (courses.size() > 0) {
			model.put("coursesFlag", true);
		}
		model.put("courses", courses);

		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());

		ProfessorReviewDao pdao = tdao.getProfessorReviewDao();
		List<ProfessorReview> ProfessorReview = pdao.listRecentCoursesByProfessorEmail(u);
		if (ProfessorReview.size() > 0) {
			model.put("reviewsFlag", true);
		}

		model.put("reviews", ProfessorReview);

		if (tutors.size() > 0) {
			model.put("tutorFlag", true);
		}

		model.put("tutors", tutors);

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/facultyDashboard.hbs");
	}

	public ModelAndView showCompleteProfileProfPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u.getMajor() == null) {
			model.put("completeProfile", true);
		}
		// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();

		DaoManager adao = DaoManager.getInstance();
		DaoManager cdao = DaoManager.getInstance();

		AnnouncementDao ad = adao.getAnnouncementDao();

		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		CourseDao cd = cdao.getCourseDao();
		List<Course> courses = cd.allByProfessor(u.getId());
		model.put("courses", courses);

		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());

		model.put("tutors", tutors);

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/completeprofileprof.hbs");
	}

	public ModelAndView showAllTutorsPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());

		model.put("tutors", tutors);

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/alltutors.hbs");
	}

	public ModelAndView showSelectTutorsPage(Request req, Response res) throws AuthException, SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		DaoManager udao = DaoManager.getInstance();
		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		UserDao ud = udao.getUserDao();

		if (req.queryParams("search-type") != null & req.queryParams("search") != null) {

			String searchType = req.queryParams("search-type").toLowerCase();
			String searchTxt = req.queryParams("search").toLowerCase();

			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<User> tempTutors = ud.search(searchType, searchTxt);
				if (tempTutors.size() > 0) {
					
					model.put("tutors", tempTutors);
				} else {
					List<User> tutors = ud.search(searchType, searchTxt);
					model.put("error", "No Results Found");
					model.put("tutors", tutors);
				}
			} else {
				List<User> tutors = ud.allTutorsByMajor(u.getMajor());
				model.put("error", "Cannot leave search bar blank");
				model.put("tutors", tutors);
			}
		} else {
			List<User> tutors = ud.allTutorsByMajor(u.getMajor());
			model.put("tutors", tutors);
		}

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/departmenttutors.hbs");
	}

	public ModelAndView showSelectStudentsPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		DaoManager udao = DaoManager.getInstance();
		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		UserDao ud = udao.getUserDao();

		List<User> tutors = ud.allStudentsByMajor(u.getMajor());

		model.put("tutors", tutors);

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/departmentstudents.hbs");
	}

	public ModelAndView showAddTutorPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		UserDao user = DaoManager.getInstance().getUserDao();
		TutorDao tutor = DaoManager.getInstance().getTutorDao();
		UserDao userT = DaoManager.getInstance().getUserDao();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		DaoManager cdao = DaoManager.getInstance();
		CourseDao cd = cdao.getCourseDao();
		List<Course> courses = cd.allByProfessor(u.getId());
		model.put("courses", courses);

		model.put("current_user", u);
		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		User t = userT.findById(id);

		model.put("tutor_form", new UserForm(t));

		// Authorize that the user can edit the user selected
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// create the form object, put it into request
		// model.put("tutor_form", new TutorForm(u));

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Render the page
		return new ModelAndView(model, "users/addtutor.hbs");
	}

	public String completeProfile(Request req, Response res) {
		Session session = req.session();
		User u = (User) session.attribute("current_user");
		UserDao uDao = DaoManager.getInstance().getUserDao();
		String idString = req.params("id");
		long id = Long.parseLong(idString);

		User user = new User();

		user.setMajor(req.queryParams("department"));
		user.setId(id);

		uDao.completeProfProfile(user);

		res.redirect(Application.FACULTYDASHBOARD_PATH);
		return "";
	}

	public String addTutor(Request req, Response res) {
		Session session = req.session();
		User u = (User) session.attribute("current_user");

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		UserDao uDao = DaoManager.getInstance().getUserDao();
		if (uDao.findById(id).getRole() == 4) {
			uDao.updateRole(uDao.findById(id), 3);
		}

		Tutor tutor = new Tutor();

		tutor.setCourse_name(req.queryParams("course"));

		tutor.setProfessor_id(u.getId());
		tutor.setStudent_id(id);

		tDao.save(tutor);

		res.redirect(Application.ALLTUTORS_PATH + u.getId());
		return " ";

	}

	public String addStudentAsTutor(Request req, Response res) {
		Session session = req.session();
		User u = (User) session.attribute("current_user");

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		Tutor tutor = new Tutor();

		tutor.setCourse_name(req.queryParams("course"));

		tutor.setProfessor_id(u.getId());
		tutor.setStudent_id(id);

		tDao.save(tutor);

		res.redirect(Application.ALLTUTORS_PATH + u.getId());
		return " ";

	}

}
