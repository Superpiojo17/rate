package edu.ben.rate_review.controller.home;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.ProfessorReview;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class ProfessorController {

	public ModelAndView showProfessorPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		
		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		Session session = req.session();
		String idString = req.params("course_id");
		long id = Long.parseLong(idString);
		List<ProfessorReview> reviews = reviewDao.professorByCourseId(id);
		CoursesToReview course = reviewDao.findByCourseId(id);
		// create the form object, put it into request
		model.put("courses", reviews);
		model.put("course", course);
		
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/professor.hbs");
	}
	
	public ModelAndView display(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		model.put("course_id", req.params("course_id"));

		return new ModelAndView(model, "/professor.hbs");
	}
}
