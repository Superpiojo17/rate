package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.formatTime.FormatTime;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class TutorDashboardController {

	public ModelAndView showTutorDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		AuthPolicyManager.getInstance().getUserPolicy().showTutorDashboardPage();

		model.put("current_user", u);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		TutorDao tDao = adao.getTutorDao();

		List<TutorAppointment> appointments = tDao.listAllTutorAppointments(u.getId());
		List<TutorAppointment> unviewed_appointments = tDao.listAllUnviewedTutorAppointments(u.getId());
		List<TutorAppointment> approved_appointments = tDao.listAllApprovedTutorAppointments(u.getId());

		for (int i = 0; i < approved_appointments.size(); i++) {
			approved_appointments.get(i).setTime(FormatTime.formatTime(approved_appointments.get(i).getTime()));
		}
		for (int i = 0; i < appointments.size(); i++) {
			appointments.get(i).setTime(FormatTime.formatTime(appointments.get(i).getTime()));
		}

		boolean appointments_requested = false;
		if (!unviewed_appointments.isEmpty()) {
			appointments_requested = true;
		}
		boolean upcoming_appointments = false;
		if (!approved_appointments.isEmpty()) {
			upcoming_appointments = true;
		}

		model.put("appointments_requested", appointments_requested);
		model.put("upcoming_appointments", upcoming_appointments);

		model.put("appointments", appointments);
		model.put("approved_appointments", approved_appointments);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/tutorDashboard.hbs");
	}

	/**
	 * updates a tutor appointment object with the tutor's response
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String replyToRequest(Request req, Response res) {

		TutorDao tDao = DaoManager.getInstance().getTutorDao();
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
			} else {
				// message too long
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
			} else {
				// message too long
			}
		}

		res.redirect(Application.TUTORDASHBOARD_PATH);
		return "";
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

		Email.deliverEmail(student.getFirst_name(), student.getEmail(), subject, message);

	}

}
