package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.SendGridManager;
import edu.ben.rate_review.formatTime.CheckIfExpired;
import edu.ben.rate_review.formatTime.FormatTimeAndDate;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

import static spark.Spark.halt;

public class TutorDashboardController {
	static HashMap<String, Object> model = null;

	public static ModelAndView showTutorDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
			halt();
		} else {
			User u = (User) session.attribute("current_user");

			if (u.getRole() != 3) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
				halt();
			}

			model.put("current_user", u);

			if (u.getMajor() == null) {
				model.put("completeProfile", true);
			}

			DaoManager dao = DaoManager.getInstance();
			AnnouncementDao ad = dao.getAnnouncementDao();
			List<Announcement> announcements = ad.all();
			model.put("announcements", announcements);

			TutorDao tDao = dao.getTutorDao();
			flagPastAppointments(tDao);

			List<TutorAppointment> appointments = tDao.listAllTutorAppointments(u.getId());
			List<TutorAppointment> unviewed_appointments = tDao.listAllUnviewedTutorAppointments(u.getId());
			List<TutorAppointment> approved_appointments = tDao.listAllApprovedTutorAppointments(u.getId());

			for (int i = 0; i < approved_appointments.size(); i++) {
				approved_appointments.get(i)
						.setTime(FormatTimeAndDate.formatTime(approved_appointments.get(i).getTime()));
				approved_appointments.get(i)
						.setDate(FormatTimeAndDate.formatDate(approved_appointments.get(i).getDate()));
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

		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/tutordashboard.hbs");
	}

	/**
	 * Flips flag in database if an appointment has past
	 *
	 * @param tDao
	 */
	private static void flagPastAppointments(TutorDao tDao) {
		List<TutorAppointment> appointments = tDao.listAllAppointments();
		for (int i = 0; i < appointments.size(); i++) {
			if (!CheckIfExpired.checkDateCurrentOrUpcoming(appointments.get(i).getDate())) {
				tDao.setAppointmentPast(appointments.get(i));

				if (appointments.get(i).getAppointment_status() && appointments.get(i).isAppointment_past()) {
					// appointment past, review appointment
				} else if (!appointments.get(i).getTutor_has_responded() && appointments.get(i).isAppointment_past()) {
					// appointment past, tutor did not respond
				}
			}
		}
	}

	public static ModelAndView showCompleteProfileTutorPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
			halt();
		} else {
			User u = (User) session.attribute("current_user");

			if (u.getRole() != 3) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
				halt();
			}

			if (u.getMajor() == null) {
				model.put("completeProfile", true);
			}

			// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();

			DaoManager dao = DaoManager.getInstance();

			AnnouncementDao ad = dao.getAnnouncementDao();

			List<Announcement> announcements = ad.all();
			model.put("announcements", announcements);
			CourseDao cd = dao.getCourseDao();
			List<Course> courses = cd.allByProfessor(u.getId());
			model.put("courses", courses);

			TutorDao td = dao.getTutorDao();
			List<Tutor> tutors = td.all(u.getId());

			model.put("tutors", tutors);

			model.put("current_user", u);

		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/completeprofiletutor.hbs");
	}

	/**
	 * updates a tutor appointment object with the tutor's response
	 *
	 * @param req
	 * @param res
	 * @return
	 * @throws AuthException
	 */
	public static ModelAndView replyToRequest(Request req, Response res) throws AuthException {

		showTutorDashboardPage(req, res);

		TutorDao tDao = DaoManager.getInstance().getTutorDao();

		if (req.queryParams("reschedule_appointment_id") == null) {
			// tutor is handling an appointment response

			long id = Long.parseLong(req.queryParams("appointment_id"));

			if (id > 0) {
				TutorAppointment appointment = tDao.findAppointmentByID(id);
				appointment.setTutor_message(req.queryParams("tutor_message"));
				appointment.setTutor_has_responded(true);

				if (appointment.getTutor_message().length() < 200) {
					tDao.updateTutorResponse(appointment);
					tDao.setTutorResponded(appointment);
					tDao.approveAppointment(appointment);
					emailAppointmentResponse(appointment);
					res.redirect(Application.TUTORDASHBOARD_PATH);
					halt();
				} else {
					// message too long
					model.put("error", "Comment was too long. Please submit another appointment response.");
					model.put("response_error", true);
				}
			} else {
				id *= -1;
				TutorAppointment appointment = tDao.findAppointmentByID(id);
				appointment.setTutor_message(req.queryParams("tutor_message"));
				appointment.setTutor_has_responded(true);

				if (appointment.getTutor_message().length() < 200) {
					tDao.updateTutorResponse(appointment);
					tDao.setTutorResponded(appointment);
					emailAppointmentResponse(appointment);
					res.redirect(Application.TUTORDASHBOARD_PATH);
					halt();
				} else {
					// message too long
					model.put("error", "Comment was too long. Please submit another appointment response.");
					model.put("response_error", true);
				}
			}
		} else {
			// tutor is rescheduling an appointment

			if (!req.queryParams("tutor_message").isEmpty()) {
				if (!req.queryParams("reschedule_time").isEmpty() && !req.queryParams("reschedule_date").isEmpty()) {
					// checks if fields are empty
					if (FormatTimeAndDate.checkValidDateTime(req.queryParams("reschedule_time"),
							req.queryParams("reschedule_date"))) {
						// checks if time is valid
						long id = Long.parseLong(req.queryParams("reschedule_appointment_id"));
						TutorAppointment appointment = tDao.findAppointmentByID(id);
						appointment.setTime(req.queryParams("reschedule_time"));
						appointment.setDate(req.queryParams("reschedule_date"));
						appointment.setTutor_message(req.queryParams("tutor_message"));
						tDao.cancelTutorAppointment(id);
						tDao.saveTutorAppointment(appointment);
						emailReschedule(appointment);
						res.redirect(Application.TUTORDASHBOARD_PATH);
						halt();

					} else {
						// invalid time
						model.put("error", "Invalid date/time. Please select an upcoming date, between 8AM and 8PM.");
						model.put("appointment_error", true);
					}
				} else if (!req.queryParams("reschedule_time").isEmpty()
						|| !req.queryParams("reschedule_date").isEmpty()) {
					model.put("error", "Please update the time AND date.");
					model.put("appointment_error", true);
				} else {
					long id = Long.parseLong(req.queryParams("reschedule_appointment_id"));
					TutorAppointment appointment = tDao.findAppointmentByID(id);
					if (req.queryParams("tutor_message") != null) {
						appointment.setTutor_message(req.queryParams("tutor_message"));
					} else {
						appointment.setTutor_message("");
					}
					tDao.cancelTutorAppointment(id);
					tDao.saveTutorAppointment(appointment);
					emailEditTutorMessage(appointment);
					res.redirect(Application.TUTORDASHBOARD_PATH);
					halt();
				}
			} else {
				// need to enter a message
				model.put("error", "Please leave a message for the student.");
				model.put("appointment_error", true);
			}
		}

		// res.redirect(Application.TUTORDASHBOARD_PATH);
		// halt();
		return new ModelAndView(model, "users/tutordashboard.hbs");
	}

	/**
	 * Lets students know when their appointment requests have gotten a response
	 *
	 * @param appointment
	 */
	private static void emailAppointmentResponse(TutorAppointment appointment) {

		UserDao uDao = DaoManager.getInstance().getUserDao();
		User student = uDao.findById(appointment.getStudent_id());

		String subject = "Rate&Review Tutor Appointment Response";
		String messageHeader = "<p>Hello " + student.getFirst_name() + ",</p><br />";
		String messageBody = "<p>You have at least one appointment response waiting for you. " + "<a href=\"http://"
				+ Application.DOMAIN + "/login" + "\">Login</a> to continue.</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + messageFooter;

		HashMap<String, String> params = new HashMap<>();
		params.put("name", student.getFirst_name());
		params.put("subject", subject);
		params.put("to", student.getEmail());
		params.put("message", message);
		if (Application.ALLOW_EMAIL) {
			SendGridManager.getInstance().send(params);
		}

	}

	/**
	 * Emails student notifying them of a tutor rescheduling an appointment
	 *
	 * @param appointment
	 */
	private static void emailReschedule(TutorAppointment appointment) {

		UserDao uDao = DaoManager.getInstance().getUserDao();
		User student = uDao.findById(appointment.getStudent_id());

		String subject = "Rate&Review Tutor Appointment Reschedule";
		String messageHeader = "<p>Hello " + student.getFirst_name() + ",</p><br />";
		String messageBody = "<p>Your appointment with " + appointment.getTutor_firstname() + " "
				+ appointment.getTutor_lastname() + " has been rescheduled for "
				+ FormatTimeAndDate.formatTime(appointment.getTime()) + " on "
				+ FormatTimeAndDate.formatDate(appointment.getDate()) + ".</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + messageFooter;

		HashMap<String, String> params = new HashMap<>();
		params.put("name", student.getFirst_name());
		params.put("subject", subject);
		params.put("to", student.getEmail());
		params.put("message", message);
		if (Application.ALLOW_EMAIL) {
			SendGridManager.getInstance().send(params);
		}

	}

	private static void emailEditTutorMessage(TutorAppointment appointment) {

		UserDao uDao = DaoManager.getInstance().getUserDao();
		User student = uDao.findById(appointment.getStudent_id());

		String subject = "Rate&Review Tutor Edited Message";
		String messageHeader = "<p>Hello " + student.getFirst_name() + ",</p><br />";
		String messageBody = "<p>Your appointment with " + appointment.getTutor_firstname() + " "
				+ appointment.getTutor_lastname() + " at " + FormatTimeAndDate.formatTime(appointment.getTime())
				+ " on " + FormatTimeAndDate.formatDate(appointment.getDate())
				+ " has a new response waiting for you.</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + messageFooter;

		HashMap<String, String> params = new HashMap<>();
		params.put("name", student.getFirst_name());
		params.put("subject", subject);
		params.put("to", student.getEmail());
		params.put("message", message);
		if (Application.ALLOW_EMAIL) {
			SendGridManager.getInstance().send(params);
		}

	}

	/*
	 * Complete profile for student
	 */
	public static String completeProfile(Request req, Response res) {
		UserDao uDao = DaoManager.getInstance().getUserDao();
		String idString = req.params("id");
		long id = Long.parseLong(idString);

		User user = new User();

		user.setMajor(req.queryParams("department"));
		user.setId(id);
		user.setSchool_year(Integer.parseInt(req.queryParams("year")));

		uDao.completeProfile(user);
		res.redirect(Application.TUTORDASHBOARD_PATH);
		halt();
		return "";
	}

}
