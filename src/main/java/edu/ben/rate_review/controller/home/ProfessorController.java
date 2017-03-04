package edu.ben.rate_review.controller.home;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.AllRatingsModel;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * Controller for the professor page
 * 
 * @author Mike
 * @version 3-3-2017
 */
public class ProfessorController {

	public ModelAndView showProfessorPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		// Session session = req.session();
		String idString = req.params("professor_id");
		long id = Long.parseLong(idString);

		UserDao uDao = DaoManager.getInstance().getUserDao();
		User prof = uDao.findById(id);
		AllRatingsModel ratingModel = new AllRatingsModel();

		List<ProfessorReview> reviews = reviewDao.listCoursesByProfessorEmail(prof);
		
		// Testing new dao enhancements
		//ProfessorReviewDaoTests.listCoursesByProfessorEmailTest();
		//ProfessorReviewDaoTests.avgRateTest();
		//ProfessorReviewDaoTests.allRatingsTest();

		double objectives = reviewDao.avgRate(prof, "rate_objectives");
		double organized = reviewDao.avgRate(prof, "rate_organized");
		double challenging = reviewDao.avgRate(prof, "rate_challenging");
		double outside = reviewDao.avgRate(prof, "rate_outside_work");
		double pace = reviewDao.avgRate(prof, "rate_pace");
		double assignments = reviewDao.avgRate(prof, "rate_assignments");
		double grade_fairly = reviewDao.avgRate(prof, "rate_grade_fairly");
		double grade_time = reviewDao.avgRate(prof, "rate_grade_time");
		double accessibility = reviewDao.avgRate(prof, "rate_accessibility");
		double knowledge = reviewDao.avgRate(prof, "rate_knowledge");
		double career = reviewDao.avgRate(prof, "rate_career_development");
		double overall = ((objectives + organized + challenging + outside + pace + assignments + grade_fairly
				+ grade_time + accessibility + knowledge + career) / 11);

		int[] arr_objectives = ratingModel.getObjectives(prof);
		int[] arr_organized = ratingModel.getOrganized(prof);
		int[] arr_challenging = ratingModel.getChallenging(prof);
		int[] arr_outside_work = ratingModel.getOutside_work(prof);
		int[] arr_pace = ratingModel.getPace(prof);
		int[] arr_assignments = ratingModel.getAssignments(prof);
		int[] arr_grade_fairly = ratingModel.getGrade_fairly(prof);
		int[] arr_grade_time = ratingModel.getGrade_time(prof);
		int[] arr_accessibility = ratingModel.getAccessibility(prof);
		int[] arr_knowledge = ratingModel.getKnowledge(prof);
		int[] arr_career_development = ratingModel.getCareer_development(prof);

		DecimalFormat df = new DecimalFormat("0.0");
		model.put("rate_objective", df.format(objectives));
		model.put("arr_objectives1", arr_objectives[0]);
		model.put("arr_objectives2", arr_objectives[1]);
		model.put("arr_objectives3", arr_objectives[2]);
		model.put("arr_objectives4", arr_objectives[3]);
		model.put("arr_objectives5", arr_objectives[4]);

		model.put("rate_organized", df.format(organized));
		model.put("arr_organized1", arr_organized[0]);
		model.put("arr_organized2", arr_organized[1]);
		model.put("arr_organized3", arr_organized[2]);
		model.put("arr_organized4", arr_organized[3]);
		model.put("arr_organized5", arr_organized[4]);

		model.put("rate_challenging", df.format(challenging));
		model.put("arr_challenging1", arr_challenging[0]);
		model.put("arr_challenging2", arr_challenging[1]);
		model.put("arr_challenging3", arr_challenging[2]);
		model.put("arr_challenging4", arr_challenging[3]);
		model.put("arr_challenging5", arr_challenging[4]);

		model.put("rate_outside_work", df.format(outside));
		model.put("arr_outside_work1", arr_outside_work[0]);
		model.put("arr_outside_work2", arr_outside_work[1]);
		model.put("arr_outside_work3", arr_outside_work[2]);
		model.put("arr_outside_work4", arr_outside_work[3]);
		model.put("arr_outside_work5", arr_outside_work[4]);

		model.put("rate_pace", df.format(pace));
		model.put("arr_pace1", arr_pace[0]);
		model.put("arr_pace2", arr_pace[1]);
		model.put("arr_pace3", arr_pace[2]);
		model.put("arr_pace4", arr_pace[3]);
		model.put("arr_pace5", arr_pace[4]);

		model.put("rate_assignments", df.format(assignments));
		model.put("arr_assignments1", arr_assignments[0]);
		model.put("arr_assignments2", arr_assignments[1]);
		model.put("arr_assignments3", arr_assignments[2]);
		model.put("arr_assignments4", arr_assignments[3]);
		model.put("arr_assignments5", arr_assignments[4]);

		model.put("rate_grade_fairly", df.format(grade_fairly));
		model.put("arr_grade_fairly1", arr_grade_fairly[0]);
		model.put("arr_grade_fairly2", arr_grade_fairly[1]);
		model.put("arr_grade_fairly3", arr_grade_fairly[2]);
		model.put("arr_grade_fairly4", arr_grade_fairly[3]);
		model.put("arr_grade_fairly5", arr_grade_fairly[4]);

		model.put("rate_grade_time", df.format(grade_time));
		model.put("arr_grade_time1", arr_grade_time[0]);
		model.put("arr_grade_time2", arr_grade_time[1]);
		model.put("arr_grade_time3", arr_grade_time[2]);
		model.put("arr_grade_time4", arr_grade_time[3]);
		model.put("arr_grade_time5", arr_grade_time[4]);

		model.put("rate_accessibility", df.format(accessibility));
		model.put("arr_accessibility1", arr_accessibility[0]);
		model.put("arr_accessibility2", arr_accessibility[1]);
		model.put("arr_accessibility3", arr_accessibility[2]);
		model.put("arr_accessibility4", arr_accessibility[3]);
		model.put("arr_accessibility5", arr_accessibility[4]);

		model.put("rate_knowledge", df.format(knowledge));
		model.put("arr_knowledge1", arr_knowledge[0]);
		model.put("arr_knowledge2", arr_knowledge[1]);
		model.put("arr_knowledge3", arr_knowledge[2]);
		model.put("arr_knowledge4", arr_knowledge[3]);
		model.put("arr_knowledge5", arr_knowledge[4]);

		model.put("rate_career_development", df.format(career));
		model.put("arr_career_development1", arr_career_development[0]);
		model.put("arr_career_development2", arr_career_development[1]);
		model.put("arr_career_development3", arr_career_development[2]);
		model.put("arr_career_development4", arr_career_development[3]);
		model.put("arr_career_development5", arr_career_development[4]);

		model.put("overall", df.format(overall));
		model.put("courses", reviews);
		model.put("prof_first_name", prof.getFirst_name());
		model.put("prof_last_name", prof.getLast_name());
		// model.put("course", course);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/professor.hbs");
	}

	public ModelAndView display(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		model.put("course_id", req.params("course_id"));

		return new ModelAndView(model, "/professor.hbs");
	}
}
