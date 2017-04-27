package edu.ben.rate_review.controller.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
//import edu.ben.rate_review.controller.home.LogInController;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.StudentInCourseDao;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.formatTime.CheckIfExpired;
import edu.ben.rate_review.formatTime.FormatTimeAndDate;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.StudentInCourse;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorAppointment;
import edu.ben.rate_review.models.User;
//import edu.ben.rate_review.policy.AuthPolicyManager;
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
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		} else {
			User u = (User) session.attribute("current_user");

			if (u.getRole() != 4) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
			}

			if (u.getMajor() == null) {
				model.put("completeProfile", true);
			}

			// AuthPolicyManager.getInstance().getUserPolicy().showStudentDashboardPage();

			DaoManager dao = DaoManager.getInstance();
			ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
			StudentInCourseDao sDao = dao.getStudentInCourseDao();
			flagPastCourses(sDao);

			List<StudentInCourse> coursesNotReviewed = sDao.allStudentCoursesNotReviewed(u);
			List<StudentInCourse> coursesReviewed = sDao.allStudentCoursesReviewed(u);

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
			flagPastAppointments(tDao);

			List<Tutor> tutors = tDao.listAllTutorsByMajor(u);

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

			// lists appointments which have not been reviewed
			List<TutorAppointment> notReviewed = tDao.listAllNotReviewedTutorAppointments(u);
			for (int i = 0; i < notReviewed.size(); i++) {
				notReviewed.get(i).setTime(FormatTimeAndDate.formatTime(notReviewed.get(i).getTime()));
				notReviewed.get(i).setDate(FormatTimeAndDate.formatDate(notReviewed.get(i).getDate()));
			}
			model.put("num_of_appointments", notReviewed.size());
			model.put("appointments_not_reviewed", notReviewed);

			reviewDao.close();
			sDao.close();
			tDao.close();
			ad.close();
		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/studentdashboard.hbs");
	}

	public ModelAndView showCompleteProfileStudentPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		} else {
			User u = (User) session.attribute("current_user");

			if (u.getRole() != 4) {
				// return new ModelAndView(model, "home/notauthorized.hbs");
				res.redirect(Application.AUTHORIZATIONERROR_PATH);
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

			ad.close();
			cd.close();
			td.close();
		}
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "home/completeprofilestudent.hbs");
	}

	/**
	 * Displays view for my account page
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView showMyAccountPage(Request req, Response res) {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		/////////////////////////////////////////////////////////////////
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
		}
		// AuthPolicyManager.getInstance().getUserPolicy().showStudentDashboardPage();

		// DaoManager dao = DaoManager.getInstance();

		// DaoManager adao = DaoManager.getInstance();

		model.put("current_user", u);

		// Tell the server to render the my account page.
		return new ModelAndView(model, "home/myaccount.hbs");
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

		if (req.queryParams("appointment_id") == null) {
			// enters if not for a tutor review

			// created to split the tutor's id from the tutor/professor
			// relationship id
			String[] splitTutorId = req.queryParams("tutor_id").split(",");

			// this check is for a student scheduling an appointment
			if (Long.parseLong(splitTutorId[0]) > 0) {
				User tutor = uDao.findById(Long.parseLong(splitTutorId[0]));

				if (!req.queryParams("date").isEmpty() && !req.queryParams("time").isEmpty()) {

					// only allows appointments between 8AM and 8PM
					if (FormatTimeAndDate.checkValidDateTime(req.queryParams("time"), req.queryParams("date"))) {

						TutorAppointment appointment = new TutorAppointment();
						appointment.setStudent_id(u.getId());
						appointment.setTutor_id(Long.parseLong(splitTutorId[0]));
						appointment.setDate(req.queryParams("date"));
						appointment.setTime(req.queryParams("time"));
						appointment.setStudent_message(req.queryParams("student_message"));
						appointment.setRelationship_id(Long.parseLong(splitTutorId[1]));

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
				long appointment_id = -1 * Long.parseLong(splitTutorId[0]);

				// only sends email when approved appointment is canceled
				if (tDao.findAppointmentByID(appointment_id).getAppointment_status()) {
					emailCancelAppointment(appointment_id, uDao, tDao);
				}
				tDao.cancelTutorAppointment(appointment_id);
			}
		} else {
			// handles tutor review
			System.out.println(req.queryParams("appointment_id"));
		}
		tDao.close();
		uDao.close();
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

		uDao.close();
		res.redirect(Application.STUDENTDASHBOARD_PATH);
		return "";
	}

	/**
	 * Flips flag in database if an appointment has past
	 * 
	 * @param tDao
	 */
	private void flagPastAppointments(TutorDao tDao) {
		List<TutorAppointment> appointments = tDao.listAllAppointments();
		for (int i = 0; i < appointments.size(); i++) {
			if (!CheckIfExpired.checkDateCurrentOrUpcoming(appointments.get(i).getDate())
					&& !appointments.get(i).isAppointment_past()) {

				tDao.setAppointmentPast(appointments.get(i));

				if (appointments.get(i).getAppointment_status() && appointments.get(i).isAppointment_past()) {
					// appointment past, review appointment
				} else if (!appointments.get(i).getTutor_has_responded() && appointments.get(i).isAppointment_past()) {
					// appointment past, tutor did not respond
				}
			}
		}
	}

	/**
	 * Flips a flag which will mark that a course's review window has past
	 * 
	 * @param reviewDao
	 */
	private void flagPastCourses(StudentInCourseDao sDao) {
		List<StudentInCourse> courses = sDao.listAllCourses();
		for (int i = 0; i < courses.size(); i++) {
			if (!CheckIfExpired.checkSemesterCurrentOrUpcoming(courses.get(i).getSemester(),
					courses.get(i).getYear())) {
				sDao.setSemesterPast(courses.get(i));
			}
		}
	}

	public ModelAndView showMajorTutorsPage(Request req, Response res) throws SQLException {
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
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		}

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		UserDao ud = dao.getUserDao();
		TutorDao tDao = dao.getTutorDao();

		model.put("current_user", u);

		String major = u.getMajor();

		model.put("user_major", major);

		List<User> tutors = ud.allTutorsByMajor(major);

		model.put("tutors", tutors);

		ad.close();
		ud.close();
		tDao.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/majortutors.hbs");
	}

	public ModelAndView showCourseAllProfessorsPage(Request req, Response res) throws SQLException {
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
			// return new ModelAndView(model, "home/notauthorized.hbs");
			res.redirect(Application.AUTHORIZATIONERROR_PATH);
		}

		DaoManager dao = DaoManager.getInstance();
		AnnouncementDao ad = dao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		UserDao ud = dao.getUserDao();

		model.put("current_user", u);

		ProfessorReviewDao pDao = dao.getProfessorReviewDao();
		CourseDao cDao = dao.getCourseDao();

		List<String> courses = cDao.allCoursesString();
		List<Course> courses2 = cDao.allCourses();

		// adds overview option and initial SELECT COURSE
		courses.add(0, "SELECT COURSE");

		model.put("courses", courses);

		String courseName = req.queryParams("course_name");

		if (courseName != null && !courseName.equalsIgnoreCase("select course")) {

			// List<ProfessorReview> temp =
			// pDao.listReviewsByCourse(courseName);
			long courseID = 0;
			String name = "";

			for (int i = 0; i < courses2.size(); i++) {
				if (courseName.equalsIgnoreCase(courses2.get(i).getCourse_name())
						&& !name.equalsIgnoreCase(courses2.get(i).getProfessor_name())) {
					courseID = courses2.get(i).getId();
					name = courses2.get(i).getProfessor_name();
				}
			}

			List<ProfessorReview> currentReviews = pDao.allReviewsForCourse(courseID, name);

			ad.close();
			ud.close();
			cDao.close();
			pDao.close();
			model.put("current_reviews", currentReviews);

		}

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/courseprofessors.hbs");
	}

}
