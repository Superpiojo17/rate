package edu.ben.rate_review.controller.user;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.formatTime.FormatTimeAndDate;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Calendar;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class CalendarController {
	String TUTOR_TABLE = "tutors";
	String USER_TABLE = "users";
	Connection conn = null;

	private static final String APPOINTMENT_TABLE = "tutor_appointment";

	public ModelAndView showCalendarPage(Request req, Response res) throws AuthException {

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
		
		AuthPolicyManager.getInstance().getUserPolicy().showCalendarPage();

		model.put("current_user", u);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		HttpServletRequest theRequest = req.raw();
		String title = theRequest.getParameter("Title");
		String date = theRequest.getParameter("Date");

		model.put("title", title);
		model.put("date", date);

		TutorDao tDao = adao.getTutorDao();

		List<TutorAppointment> appointments = tDao.listAllTutorAppointments(u.getId());
		List<TutorAppointment> unviewed_appointments = tDao.listAllUnviewedTutorAppointments(u.getId());
		List<TutorAppointment> approved_appointments = tDao.listAllApprovedTutorAppointments(u.getId());

		for (int i = 0; i < approved_appointments.size(); i++) {
			approved_appointments.get(i).setTime(FormatTimeAndDate.formatTime(approved_appointments.get(i).getTime()));
			approved_appointments.get(i).setDate(FormatTimeAndDate.formatDate(approved_appointments.get(i).getDate()));
		}
		for (int i = 0; i < appointments.size(); i++) {
			appointments.get(i).setTime(FormatTimeAndDate.formatTime(appointments.get(i).getTime()));
			appointments.get(i).setDate(FormatTimeAndDate.formatDate(appointments.get(i).getDate()));
		}

		boolean appointments_requested = false;
		if (!unviewed_appointments.isEmpty()) {
			appointments_requested = true;
		}
		boolean upcoming_appointments = false;
		if (!approved_appointments.isEmpty()) {
			upcoming_appointments = true;
		}

		// booleans for whether or not to display table/icon
		model.put("appointments_requested", appointments_requested);
		model.put("upcoming_appointments", upcoming_appointments);

		// lists of appointments/requested appointments
		model.put("appointments", appointments);
		model.put("approved_appointments", approved_appointments);

		// count of appointment requests that need a response
		model.put("number_of_requests", unviewed_appointments.size());

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/calendar.hbs");
	}

	// public String addAnnouncement(Request req, Response res) {
	//
	// AnnouncementDao announceDao =
	// DaoManager.getInstance().getAnnouncementDao();
	// Announcement announcement = new Announcement();
	//
	// SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
	// SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");
	//
	// try {
	// String formatteddate =
	// myFormat.format(fromUser.parse(req.queryParams("date")));
	// announcement.setDate(formatteddate);
	// } catch (ParseException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// announcement.setAnnouncement_content(req.queryParams("message"));
	//
	// announceDao.save(announcement);
	//
	// res.redirect("/announcement");
	// return "";
	//
	// }

	public void doit(Request req, Response res) {

		Calendar c = new Calendar();
		c.setId(2);
		c.setStart("2017-03-30");
		c.setEnd("2017-04-01");
		c.setTitle("Task");

	}

	public void doit2(Request req, Response res) {

		List l = new ArrayList();

		Calendar c = new Calendar();
		c.setId(1);
		c.setStart("2013-07-28");
		c.setEnd("2013-07-29");
		c.setTitle("Task in Progress");

		Calendar d = new Calendar();
		d.setId(2);
		d.setStart("2013-07-26");
		d.setEnd("2013-08-28");
		d.setTitle("Task in Progress");

		l.add(c);
		l.add(d);

		// res.header("Content-Type", "application/json");
		// res.type("application/json");

		// res.setContentType("application/json");
		// res.setCharacterEncoding("UTF-8");
		// PrintWriter out = res.getWriter();
		// out.write(new Gson().toJson(l));

	}

}
