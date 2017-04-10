package edu.ben.rate_review.controller.home;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.AllRatingsModel;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

/**
 * Controller for the professor page
 * 
 * @author Mike
 * @version 3-3-2017
 */
public class AnalysisController {
	

	public ModelAndView showAnalysisPage(Request req, Response res) {
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
		// Tell the server to render the index page with the data in the
		// model

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/analysis.hbs");
	}

//	/**
//	 * Put route method
//	 * 
//	 * @param req
//	 * @param res
//	 * @return
//	 */
//	public ModelAndView display(Request req, Response res) {
//		HashMap<String, Object> model = new HashMap<>();
//
//		model.put("course_id", req.params("course_id"));
//
//		return new ModelAndView(model, "/analysis.hbs");
//	}




	
}
