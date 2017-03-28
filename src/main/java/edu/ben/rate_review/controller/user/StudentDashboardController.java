package edu.ben.rate_review.controller.user;

import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.formatTime.FormatTimeAndDate;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.CoursesToReview;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class StudentDashboardController {

	/**
	 * Show log in page
	 * 
	 * @throws AuthException
	 */
	public ModelAndView showStudentDashboardPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u.getMajor() == null) {
			model.put("completeProfile", true);
		}

		// AuthPolicyManager.getInstance().getUserPolicy().showStudentDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
		List<CoursesToReview> coursesNotReviewed = reviewDao.allStudentCoursesNotReviewed(u);
		List<CoursesToReview> coursesReviewed = reviewDao.allStudentCoursesReviewed(u);

		// no classes listed in courses to review
		boolean noCoursesToReview = false;
		// no classes listed in recent reviews
		boolean noCoursesReviewed = false;

		// checks if either lists are empty
		if (coursesNotReviewed.isEmpty()) {
			noCoursesToReview = true;
		}
		if (coursesReviewed.isEmpty()) {
			noCoursesReviewed = true;
		}

		// booleans for whether or not to display table
		model.put("no_courses_to_review", noCoursesToReview);
		model.put("no_courses_reviewed", noCoursesReviewed);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();

		model.put("announcements", announcements);

		model.put("courses_not_reviewed", coursesNotReviewed);
		model.put("courses_reviewed", coursesReviewed);

		// count of courses not reviewed
		model.put("number_of_courses", coursesNotReviewed.size());

		model.put("current_user", u);

		TutorDao tDao = dao.getTutorDao();
		List<Tutor> tutors = tDao.listAllTutors();
		model.put("tutors", tutors);

		List<TutorAppointment> appointments = tDao.listAllStudentAppointments(u);

		for (int i = 0; i < appointments.size(); i++) {
			appointments.get(i).setTime(FormatTimeAndDate.formatTime(appointments.get(i).getTime()));
			appointments.get(i).setDate(FormatTimeAndDate.formatDate(appointments.get(i).getDate()));
		}

		model.put("upcoming_appointments", appointments);

		// checks whether or not there are appointments scheduled
		boolean upcoming_appointments_scheduled = false;
		if (!appointments.isEmpty()) {
			upcoming_appointments_scheduled = true;
		}
		model.put("upcoming_appointments_scheduled", upcoming_appointments_scheduled);

		boolean available_tutors = false;
		if (!tutors.isEmpty()) {
			available_tutors = true;
		}
		model.put("available_tutors", available_tutors);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/studentDashboard.hbs");
	}

	public ModelAndView showCompleteProfileStudentPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		if (u.getMajor() == null) {
			model.put("completeProfile", true);
		}

		// AuthPolicyManager.getInstance().getUserPolicy().showFacultyDashboardPage();

		DaoManager adao = DaoManager.getInstance();
		DaoManager cdao = DaoManager.getInstance();

		AnnouncementDao ad = adao.getAnnouncementDao();

		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		CourseDao cd = cdao.getCourseDao();
		List<Course> courses = cd.allByProfessor(u.getId());
		model.put("courses", courses);

		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());

		model.put("tutors", tutors);

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/completeprofilestudent.hbs");
	}

	/**
	 * Post method for requesting an appointment with a tutor. Will create and
	 * store a tutor appointment object
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String requestAppointment(Request req, Response res) {

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		DaoManager dao = DaoManager.getInstance();
		TutorDao tDao = dao.getTutorDao();
		UserDao uDao = dao.getUserDao();

		// this check is for a student scheduling an appointment
		if (Long.parseLong(req.queryParams("tutor_id")) > 0) {
			User tutor = uDao.findById(Long.parseLong(req.queryParams("tutor_id")));

			if (!req.queryParams("date").isEmpty() && !req.queryParams("time").isEmpty()) {
				
				// only allows appointments between 8AM and 8PM
				if (FormatTimeAndDate.checkValidDateTime(req.queryParams("time"), req.queryParams("date"))) {

					TutorAppointment appointment = new TutorAppointment();
					appointment.setStudent_id(u.getId());
					appointment.setTutor_id(Long.parseLong(req.queryParams("tutor_id")));
					appointment.setDate(req.queryParams("date"));
					appointment.setTime(req.queryParams("time"));
					appointment.setStudent_message(req.queryParams("student_message"));
					appointment.setStudent_firstname(u.getFirst_name());
					appointment.setStudent_lastname(u.getLast_name());
					appointment.setTutor_firstname(tutor.getFirst_name());
					appointment.setTutor_lastname(tutor.getLast_name());

					if (appointment.getStudent_message().length() < 200) {
						tDao.saveTutorAppointment(appointment);
						emailAppointmentRequest(appointment, uDao);
					} else {
						// message - comment too long
					}
				} else {
					// meeting time is outside of 8AM-8PM window
				}
			} else {
				// message - please set a date
			}
		} else {
			// if tutor_id is negative, it is a student canceling an
			// appointment, and the negative number is the appointment_id
			long appointment_id = -1 * Long.parseLong(req.queryParams("tutor_id"));

			// only sends email when approved appointment is canceled
			if (tDao.findAppointmentByID(appointment_id).getAppointment_status()) {
				emailCancelAppointment(appointment_id, uDao, tDao);
			}
			tDao.cancelTutorAppointment(appointment_id);
		}
		res.redirect(Application.STUDENTDASHBOARD_PATH);
		return "";
	}

	/**
	 * Sends email to tutor to notify them that a student has requested an
	 * appointment
	 * 
	 * @param appointment
	 * @param uDao
	 */
	private static void emailAppointmentRequest(TutorAppointment appointment, UserDao uDao) {

		User tutor = uDao.findById(appointment.getTutor_id());

		String subject = "Rate&Review Tutor Appointment Request";
		String messageHeader = "<p>Hello " + tutor.getFirst_name() + ",</p><br />";
		String messageBody = "<p>You have appointment request(s) waiting for you. " + "<a href=\"http://"
				+ Application.DOMAIN + "/login" + "\">Login</a> to continue.</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + messageFooter;

		Email.deliverEmail(tutor.getFirst_name(), tutor.getEmail(), subject, message);

	}

	/**
	 * Sends an email notifying a tutor that their appointment has been canceled
	 * 
	 * @param appointment_id
	 * @param uDao
	 * @param tDao
	 */
	private static void emailCancelAppointment(long appointment_id, UserDao uDao, TutorDao tDao) {

		TutorAppointment appointment = tDao.findAppointmentByID(appointment_id);
		User tutor = uDao.findById(appointment.getTutor_id());

		String subject = "Rate&Review Tutor Appointment Cancellation";
		String messageHeader = "<p>Hello " + tutor.getFirst_name() + ",</p><br />";
		String messageBody = "<p>Your appointment with " + appointment.getStudent_firstname() + " "
				+ appointment.getStudent_lastname() + " at " + FormatTimeAndDate.formatTime(appointment.getTime())
				+ " on " + FormatTimeAndDate.formatDate(appointment.getDate()) + " has been canceled.</p>";
		String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
		String message = messageHeader + messageBody + messageFooter;

		Email.deliverEmail(tutor.getFirst_name(), tutor.getEmail(), subject, message);

	}

	/*
	 * Complete profile for student
	 */
	public String completeProfile(Request req, Response res) {
		UserDao uDao = DaoManager.getInstance().getUserDao();
		String idString = req.params("id");
		long id = Long.parseLong(idString);

		User user = new User();

		user.setMajor(req.queryParams("department"));
		user.setId(id);
		user.setSchool_year(Integer.parseInt(req.queryParams("year")));

		uDao.completeProfile(user);

		res.redirect(Application.STUDENTDASHBOARD_PATH);
		return "";
	}

}
