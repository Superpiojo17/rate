package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class FacultyDashboardController {
	/**
	 * Show log in page
	 * @throws AuthException 
	 */
	public ModelAndView showFacultyDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
//		AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();
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
		return new ModelAndView(model, "users/facultyDashboard.hbs");
	}
	public ModelAndView showAllTutorsPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
//		AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		
		
		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());
		
		
		
		model.put("tutors", tutors);
		
		System.out.println(u.getMajor());

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/alltutors.hbs");
	}
	
	public ModelAndView showAddTutorsPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
//		AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		
		DaoManager udao = DaoManager.getInstance();
		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		UserDao ud = udao.getUserDao();
		
		
		
		List<User> tutors = ud.allTutorsByMajor(u.getMajor());
		
		
		
		model.put("tutors", tutors);
		
		

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/departmenttutors.hbs");
	}
	
	
	
	

}
