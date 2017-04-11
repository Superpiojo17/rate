package edu.ben.rate_review.controller.home;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class DepartmentsController {

	public ModelAndView showDepartmentsPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		UserDao uDao = DaoManager.getInstance().getUserDao();

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
		
		// List list = uDao.sortByMajor("CMSC");

		List<User> prof = uDao.all();
		List<User> professors = new ArrayList<User>();

		HttpServletRequest theRequest = req.raw();
		String department = theRequest.getParameter("form_select");

		model.put("table_type", department);

		for (int i = 0; i < prof.size(); i++) {

			User r = prof.get(i);
			if (r.getDepartment() != null) {
				if (r.getDepartment().equalsIgnoreCase(department)) {
					model.put("department", r.getDepartment());
					model.put("first_name", r.getFirst_name());
					model.put("last_name", r.getLast_name());
					model.put("role", r.getRole_string());
					model.put("id", r.getId());
					professors.add(r);
				}
			}

		}
		
		for (int i = 0; i < professors.size(); i++) {

		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		User temp = professors.get(i);
		double objectives = reviewDao.avgRate(temp, "rate_objectives", "overview");
		double organized = reviewDao.avgRate(temp, "rate_organized", "overview");
		double challenging = reviewDao.avgRate(temp, "rate_challenging", "overview");
		double outside = reviewDao.avgRate(temp, "rate_outside_work", "overview");
		double pace = reviewDao.avgRate(temp, "rate_pace", "overview");
		double assignments = reviewDao.avgRate(temp, "rate_assignments", "overview");
		double grade_fairly = reviewDao.avgRate(temp, "rate_grade_fairly", "overview");
		double grade_time = reviewDao.avgRate(temp, "rate_grade_time", "overview");
		double accessibility = reviewDao.avgRate(temp, "rate_accessibility", "overview");
		double knowledge = reviewDao.avgRate(temp, "rate_knowledge", "overview");
		double career = reviewDao.avgRate(temp, "rate_career_development", "overview");
		double overall = ((objectives + organized + challenging + outside + pace + assignments + grade_fairly
				+ grade_time + accessibility + knowledge + career) / 11);
		
		DecimalFormat df = new DecimalFormat("0.#");
		
		model.put("overall", df.format(overall));
		}
		model.put("users", professors);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/departments.hbs");
	}

}
