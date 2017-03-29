package edu.ben.rate_review.controller.user;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.AnnouncementForm;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.Tutor;
import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class EditTutorController {

	public ModelAndView showEditTutorPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		UserDao user = DaoManager.getInstance().getUserDao();
		TutorDao tutor = DaoManager.getInstance().getTutorDao();
		DaoManager cdao = DaoManager.getInstance();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		model.put("current_user", u);

		CourseDao cd = cdao.getCourseDao();
		List<Course> courses = cd.allByProfessor(u.getId());
		model.put("courses", courses);

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		Tutor t = tutor.findById(id);

		model.put("tutor_form", new TutorForm(t));

		// Authorize that the user can edit the user selected
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		// create the form object, put it into request
		// model.put("tutor_form", new TutorForm(u));

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Render the page
		return new ModelAndView(model, "users/edittutor.hbs");
	}

	public ModelAndView updateTutor(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		TutorDao tDao = DaoManager.getInstance().getTutorDao();
		TutorForm tutor = new TutorForm();

		Tutor tempTutor = tDao.findById(id);

		tutor.setCourse(req.queryParams("course"));
		tutor.setId(id);

		tDao.updateTutor(tutor);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());

		model.put("tutors", tutors);

		model.put("error", "You have assigned " + tutor.getCourse() + " to " + tempTutor.getTutor_first_name() + " "
				+ tempTutor.getTutor_last_name());

		model.put("current_user", u);
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/alltutors.hbs");

	}

	public ModelAndView deleteTutor(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		String idString = req.params("id");
		long id = Long.parseLong(idString);
		TutorDao tutorDao = DaoManager.getInstance().getTutorDao();
		Long studentID = tutorDao.getStudentId(id);
		Tutor tempTutor = tutorDao.findById(id);

		tutorDao.deleteTutor(id);

		if (tutorDao.findByStudentId(studentID) == null) {
			tutorDao.changeTutorRole(studentID);

		}

		DaoManager tdao = DaoManager.getInstance();
		TutorDao td = tdao.getTutorDao();
		List<Tutor> tutors = td.all(u.getId());

		model.put("tutors", tutors);

		model.put("error", "You have removed " + tempTutor.getCourse_name() + " from " + tempTutor.getTutor_first_name()
				+ " " + tempTutor.getTutor_last_name());

		return new ModelAndView(model, "users/alltutors.hbs");

	}

}
