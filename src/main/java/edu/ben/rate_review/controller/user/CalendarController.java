package edu.ben.rate_review.controller.user;

//import java.io.IOException;
import java.sql.Connection;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.google.gson.Gson;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
//import edu.ben.rate_review.formatTime.FormatTimeAndDate;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Calendar;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class CalendarController {
	String TUTOR_TABLE = "tutors";
	String USER_TABLE = "users";
	Connection conn = null;

	// private static final String APPOINTMENT_TABLE = "tutor_appointment";

	public ModelAndView showCalendarPage(Request req, Response res) throws AuthException {

		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

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
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		// AuthPolicyManager.getInstance().getUserPolicy().showCalendarPage();

		model.put("current_user", u);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		ad.close();
		// model.put("title", title);
		// model.put("date", date);

		// Convert to JSON String
		// String json = new Gson().toJson(model);
		//
		// HttpServletResponse response = res.raw();
		// response.setContentType("application/json");
		// response.setCharacterEncoding("UTF-8");
		//
		// JSONObject obj = new JSONObject();
		//
		// obj.put("id", 111);
		// obj.put("title", "event1");
		// obj.put("start", "2017-04-12");
		// obj.put("url", "http://yahoo.com");

		// JSONArray objA = new JSONArray();
		// objA.put(obj);
		//
		// obj.put("id", 111);
		// obj.put("title", "event1");
		// obj.put("start", "2017-04-14");
		// obj.put("url", "http://yahoo.com");
		//
		// objA.put(obj);

		TutorDao tDao = adao.getTutorDao();

		if (u.getRole() == 4) {
			List<TutorAppointment> appointments = tDao.listAllStudentAppointments(u);

			model.put("appointments", appointments);
		} else if (u.getRole() == 3) {

			List<TutorAppointment> appointments = tDao.listAllTutorAppointments(u.getId());
			model.put("appointments", appointments);
		}
		tDao.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/calendar.hbs");
	}

	public void doit(Request req, Response res) {

		// String name = "Gerald", age = "22";
		// String name2 = "Bam", age2 = "23";

		// JSONObject obj = new JSONObject();
		//
		// obj.put("name", name);
		// obj.put("age", age);
		//
		// JSONArray objA = new JSONArray();
		// objA.put(obj);
		// obj.put("name", name2);
		// obj.put("age", age2);
		// objA.put(obj);

		Calendar c = new Calendar();
		c.setId(2);
		c.setStart("2017-03-30");
		c.setEnd("2017-04-01");
		c.setTitle("Task");

	}

}
