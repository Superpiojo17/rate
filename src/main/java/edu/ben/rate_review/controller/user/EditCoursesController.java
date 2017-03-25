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
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.CourseForm;
import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class EditCoursesController {
	public ModelAndView showDeptCoursesPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		String department = req.params("department");

		Session session = req.session();
		User u = (User) session.attribute("current_user");
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		CourseDao cd = dao.getCourseDao();
		List<Course> courses = cd.allByDept(department);

		model.put("courses", courses);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/courses.hbs");
	}

	public ModelAndView showEditCoursesPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();
		CourseDao course = DaoManager.getInstance().getCourseDao();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		Course c = course.findById(id);

		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		model.put("course_form", new CourseForm(c));

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		System.out.println(c.getSubject());
		List<User> professors = ud.allProfessorsByDept(c.getSubject());
		model.put("professors", professors);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/editcourse.hbs");
	}

	public String updateCourse(Request req, Response res) {
		Session session = req.session();
		User u = (User) session.attribute("current_user");
		CourseDao courseDao = DaoManager.getInstance().getCourseDao();

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		CourseDao cDao = DaoManager.getInstance().getCourseDao();
		CourseForm course = new CourseForm();

		// tutor.setCourse(req.queryParams("course"));
		course.setProfessor_id(Integer.parseInt(req.queryParams("professor_id")));
		course.setId(id);
		Course c = courseDao.findById(id);
		cDao.updateCourse(course);

		res.redirect("/courses/" + c.getSubject());
		return " ";

	}

}
