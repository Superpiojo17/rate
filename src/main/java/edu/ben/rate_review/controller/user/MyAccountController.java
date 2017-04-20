package edu.ben.rate_review.controller.user;

import java.util.HashMap;
//import java.util.List;

import edu.ben.rate_review.app.Application;
//import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
//import edu.ben.rate_review.daos.ProfessorReviewDao;
//import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
//import edu.ben.rate_review.email.Email;
//import edu.ben.rate_review.encryption.SecurePassword;
//import edu.ben.rate_review.formatTime.FormatTimeAndDate;
//import edu.ben.rate_review.models.Announcement;
//import edu.ben.rate_review.models.RecoveringUser;
//import edu.ben.rate_review.models.Tutor;
//import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * Account Recovery controller
 * 
 * @author Dex
 * @version
 */
public class MyAccountController {
	/**
	 * Displays view for my account page
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView showMyAccountPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		/////////////////////////////////////////////////////////////////
		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u != null) {
			if (u.getRole() == 1) {
				model.put("user_admin", true);
			} else if (u.getRole() == 2) {
				model.put("user_professor", true);
			} else if (u.getRole() == 3) {
				model.put("user_tutor", true);
			} else {
				model.put("user_student", true);
			}
		} else {
			model.put("user_null", true);
		}
		// AuthPolicyManager.getInstance().getUserPolicy().showStudentDashboardPage();

		// DaoManager dao = DaoManager.getInstance();

		// DaoManager adao = DaoManager.getInstance();

		model.put("current_user", u);

		// Tell the server to render the my account page.
		return new ModelAndView(model, "home/myaccount.hbs");
	}

	/*
	 * Complete profile Utilizing the code one of the other guys made to update
	 * the fields
	 */
	public String completeProfile(Request req, Response res) {
		UserDao uDao = DaoManager.getInstance().getUserDao();
		String idString = req.params("id");
		long id = Long.parseLong(idString);

		User user = new User();

		user.setNickname(req.queryParams("nickname"));
		user.setId(id);
		user.setPersonal_email(req.queryParams("personal_email"));

		uDao.completeProfile2(user);

		uDao.close();
		res.redirect(Application.STUDENTDASHBOARD_PATH);
		return "";
	}

}
