package edu.ben.rate_review.controller.home;

import java.text.DecimalFormat;
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
		
		double objectives = reviewDao.avgRateObjectives(course);
		double organized = reviewDao.avgRateOrganized(course);
		double challenging = reviewDao.avgRateChallenging(course);
		double outside = reviewDao.avgRateOutsideWork(course);
		double pace = reviewDao.avgRatePace(course);
		double assignments = reviewDao.avgRateAssignments(course);
		double grade_fairly = reviewDao.avgRateGradeFairly(course);
		double grade_time = reviewDao.avgRateGradeTime(course);
		double accessibility = reviewDao.avgRateAccessibility(course);
		double knowledge = reviewDao.avgRateKnowledge(course);
		double career = reviewDao.avgRateCareerDevelopment(course);
		double overall = ((objectives + organized + challenging + outside + pace + assignments + grade_fairly
				+ grade_time + accessibility + knowledge + career)/11);

		DecimalFormat df = new DecimalFormat("0.0"); 
		
		model.put("rate_objective", df.format(objectives));
		model.put("rate_organized", df.format(organized));
		model.put("rate_challenging", df.format(challenging));
		model.put("rate_outside_work", df.format(outside));
		model.put("rate_pace", df.format(pace));
		model.put("rate_assignments", df.format(assignments));
		model.put("rate_grade_fairly", df.format(grade_fairly));
		model.put("rate_grade_time", df.format(grade_time));
		model.put("rate_accessibility", df.format(accessibility));
		model.put("rate_knowledge", df.format(knowledge));
		model.put("rate_career_development", df.format(career));
		model.put("overall", df.format(overall));

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
