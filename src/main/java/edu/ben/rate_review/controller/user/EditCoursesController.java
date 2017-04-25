package edu.ben.rate_review.controller.user;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.CourseDao;
import edu.ben.rate_review.daos.DaoManager;
//import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.StudentInCourseDao;
//import edu.ben.rate_review.daos.TutorDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.Course;
import edu.ben.rate_review.models.CourseForm;
//import edu.ben.rate_review.models.ProfessorReview;
//import edu.ben.rate_review.models.TutorForm;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

public class EditCoursesController {
	public ModelAndView showDeptCoursesPage(Request req, Response res) throws AuthException, SQLException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		String department = req.params("department");

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		if (u != null) {
			if (u.getRole() == 1) {
				model.put("user_admin", true);
			}
		}
		DaoManager dao = DaoManager.getInstance();
		CourseDao cd = dao.getCourseDao();
		if (req.queryParams("search") != null) {

			String searchType = "name";
			String searchTxt = req.queryParams("search").toLowerCase();

			// Make sure you
			if (searchType.equalsIgnoreCase("email") || searchType.equalsIgnoreCase("name") && searchTxt.length() > 0) {
				// valid search, can proceed
				List<Course> tempCourses = cd.search(searchType, searchTxt);
				if (tempCourses.size() > 0) {

					model.put("courses", tempCourses);
				} else {
					List<Course> courses = cd.search(searchType, searchTxt);
					model.put("error", "No Results Found");
					model.put("courses", courses);
				}
			} else {

				List<Course> courses = cd.allByDept(department);
				model.put("error", "Cannot leave search bar blank");
				model.put("courses", courses);
			}
		} else {

			List<Course> courses = cd.allByDept(department);

			model.put("courses", courses);
		}

		model.put("department", department);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		cd.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/courses.hbs");
	}

	public ModelAndView showEditCoursesPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		// Get the :id from the url
		String idString = req.params("id");

		// Convert to Long
		// /user/uh-oh/edit for example
		long id = Long.parseLong(idString);

		CourseDao cd = DaoManager.getInstance().getCourseDao();
		Course c = cd.findById(id);

		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		model.put("course_form", new CourseForm(c));

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		List<User> professors = ud.allProfessorsByDept(c.getSubject());
		model.put("professors", professors);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);
		cd.close();
		ud.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/editcourse.hbs");
	}

	public ModelAndView showAddCoursesPage(Request req, Response res) throws AuthException {
		// Just a hash to pass data from the servlet to the page
		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		if (session.attribute("current_user") == null) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}
		User u = (User) session.attribute("current_user");

		if (u.getRole() != 1) {
			return new ModelAndView(model, "home/notauthorized.hbs");
		}

		// Get the :id from the url
		String department = req.params("department");
		model.put("department", department);

		// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

		DaoManager dao = DaoManager.getInstance();
		UserDao ud = dao.getUserDao();
		List<User> professors = ud.allProfessorsByDept(department);
		model.put("professors", professors);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		ud.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/addCourse.hbs");
	}

	public ModelAndView updateCourse(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		// Session session = req.session();
		// User u = (User) session.attribute("current_user");
		CourseDao courseDao = DaoManager.getInstance().getCourseDao();

		String idString = req.params("id");
		long id = Long.parseLong(idString);
		CourseDao cDao = DaoManager.getInstance().getCourseDao();
		CourseForm course = new CourseForm();

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		// tutor.setCourse(req.queryParams("course"));
		course.setProfessor_id(Integer.parseInt(req.queryParams("professor_id")));
		course.setId(id);
		Course c = courseDao.findById(id);
		cDao.updateCourse(course);

		// DaoManager dao = DaoManager.getInstance();
		// CourseDao cd = dao.getCourseDao();

		// Get the :id from the url
		String department = c.getSubject();
		System.out.println(department);
		model.put("department", department);

		List<Course> courses = courseDao.allByDept(c.getSubject());

		model.put("courses", courses);

		model.put("error",
				"You just edited " + c.getSubject() + " " + " " + c.getCourse_number() + " " + c.getCourse_name());
		courseDao.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/courses.hbs");

	}

	public ModelAndView addCourse(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		// Session session = req.session();
		// User u = (User) session.attribute("current_user");
		CourseDao cd = DaoManager.getInstance().getCourseDao();

		String department = req.params("department");
		Course course = new Course();
		if (!req.queryParams("coursename").isEmpty()) {
			// tutor.setCourse(req.queryParams("course"));
			course.setCourse_name(req.queryParams("coursename"));
			course.setCourse_number(Long.parseLong(req.queryParams("coursenumber")));
			course.setSubject(department);
			String term = req.queryParams("semester");
			System.out.println(term);
			String[] lineArray = term.split(" ");
			course.setSemester(lineArray[0]);
			course.setYear(Integer.parseInt(lineArray[1]));
			course.setProfessor_id(Long.parseLong(req.queryParams("professor_id")));
			cd.save(course);
			String tempdepartment = req.params("department");
			model.put("department", tempdepartment);
		} else {
			// Get the :id from the url
			String tempdepartment = req.params("department");
			model.put("department", tempdepartment);

			// AuthPolicyManager.getInstance().getUserPolicy().showAdminDashboardPage();

			DaoManager dao = DaoManager.getInstance();
			UserDao ud = dao.getUserDao();
			List<User> professors = ud.allProfessorsByDept(department);
			model.put("professors", professors);

			DaoManager adao = DaoManager.getInstance();
			AnnouncementDao ad = adao.getAnnouncementDao();
			List<Announcement> announcements = ad.all();
			model.put("announcements", announcements);

			model.put("error", "You may not leave field blank");
			ud.close();
			cd.close();
			return new ModelAndView(model, "users/addcourse.hbs");
		}
		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		List<Course> courses = cd.allByDept(course.getSubject());

		model.put("courses", courses);

		model.put("error", "You just added " + course.getSubject() + " " + " " + course.getCourse_number() + " "
				+ course.getCourse_name() + " to " + course.getProfessor_name());

		cd.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/courses.hbs");

	}

	public ModelAndView deleteCourse(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		// Session session = req.session();
		// User u = (User) session.attribute("current_user");
		String idString = req.params("id");
		long id = Long.parseLong(idString);
		CourseDao cd = DaoManager.getInstance().getCourseDao();
		Course c = cd.findById(id);
		cd.deleteCourse(id);

		DaoManager adao = DaoManager.getInstance();
		AnnouncementDao ad = adao.getAnnouncementDao();
		List<Announcement> announcements = ad.all();
		model.put("announcements", announcements);

		List<Course> courses = cd.allByDept(c.getSubject());

		model.put("courses", courses);

		// Get the :id from the url
		String department = c.getSubject();
		System.out.println(department);
		model.put("department", department);

		model.put("error", "You just deleted " + c.getSubject() + " " + " " + c.getCourse_number() + " "
				+ c.getCourse_name() + " taught by " + c.getProfessor_name());
		cd.close();
		ad.close();
		// Tell the server to render the index page with the data in the model
		return new ModelAndView(model, "users/courses.hbs");

	}

	/**
	 * Displays the add students page
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws AuthException
	 */
	public ModelAndView showClassListPage(Request req, Response res) throws AuthException {

		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		// if (u == null || u.getRole() != 1) {
		// return new ModelAndView(model, "home/notauthorized.hbs");
		// }

		String idString = req.params("id");
		long course_id = Long.parseLong(idString);

		DaoManager dao = DaoManager.getInstance();
		CourseDao cDao = dao.getCourseDao();
		UserDao uDao = dao.getUserDao();
		AnnouncementDao aDao = dao.getAnnouncementDao();

		Course course = cDao.findById(course_id);
		model.put("course", course);

		model.put("id", course_id);

		List<User> classlist = uDao.CourseList(course_id);
		model.put("classlist", classlist);

		List<Announcement> announcements = aDao.all();
		model.put("announcements", announcements);

		cDao.close();
		uDao.close();
		aDao.close();
		return new ModelAndView(model, "users/classlist.hbs");
	}

	public ModelAndView showRemoveFromClassListPage(Request req, Response res) throws AuthException {

		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		// if (u == null || u.getRole() != 1) {
		// return new ModelAndView(model, "home/notauthorized.hbs");
		// }

		String idString = req.params("id");
		long course_id = Long.parseLong(idString);

		DaoManager dao = DaoManager.getInstance();
		CourseDao cDao = dao.getCourseDao();
		UserDao uDao = dao.getUserDao();
		AnnouncementDao aDao = dao.getAnnouncementDao();

		Course course = cDao.findById(course_id);
		model.put("course", course);

		List<User> classlist = uDao.CourseList(course_id);
		model.put("classlist", classlist);

		List<Announcement> announcements = aDao.all();
		model.put("announcements", announcements);

		cDao.close();
		uDao.close();
		aDao.close();
		return new ModelAndView(model, "users/removefromclasslist.hbs");
	}

	public ModelAndView showAddToClassListPage(Request req, Response res) throws AuthException {

		HashMap<String, Object> model = new HashMap<>();

		Session session = req.session();
		User u = (User) session.attribute("current_user");

		// if (u == null || u.getRole() != 1) {
		// return new ModelAndView(model, "home/notauthorized.hbs");
		// }

		String idString = req.params("id");
		long course_id = Long.parseLong(idString);

		DaoManager dao = DaoManager.getInstance();
		CourseDao cDao = dao.getCourseDao();
		UserDao uDao = dao.getUserDao();
		AnnouncementDao aDao = dao.getAnnouncementDao();

		Course course = cDao.findById(course_id);
		model.put("course", course);

		List<User> classlist = uDao.allStudents();
		model.put("classlist", classlist);

		List<Announcement> announcements = aDao.all();
		model.put("announcements", announcements);

		cDao.close();
		uDao.close();
		aDao.close();
		return new ModelAndView(model, "users/addtoclasslist.hbs");
	}

	/**
	 * Post method to add a student to the course
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public String addStudentToCourse(Request req, Response res) {
		String idString = req.params("id");
		Long course_id = Long.parseLong(idString);

		DaoManager dao = DaoManager.getInstance();
		CourseDao cDao = dao.getCourseDao();
		StudentInCourseDao sDao = dao.getStudentInCourseDao();
		UserDao uDao = dao.getUserDao();
		Course course = cDao.findById(course_id);

		// grab student id from the form
		// make student_in_course object using course and student id

		cDao.close();
		sDao.close();
		uDao.close();
		res.redirect("/course/" + req.params("id") + "/addstudent");
		return "";
	}

	/**
	 * Post method to add a student to the course
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView removeStudentFromCourse(Request req, Response res) {
		HashMap<String, Object> model = new HashMap<>();

		String idString = req.params("id");
		Long course_id = Long.parseLong(idString);

		DaoManager dao = DaoManager.getInstance();
		CourseDao cDao = dao.getCourseDao();
		StudentInCourseDao sDao = dao.getStudentInCourseDao();
		UserDao uDao = dao.getUserDao();
		AnnouncementDao aDao = dao.getAnnouncementDao();
		Course course = cDao.findById(course_id);

		List<Long> ids = new ArrayList<>();

		List<User> classlist = uDao.CourseList(course_id);

		for (int i = 0; i < classlist.size(); i++) {
			ids.add(classlist.get(i).getId());
		}

		String[] results = req.queryParamsValues("s1_t1");

		if (results != null && results.length > 0) {
			for (int i = 0; i < results.length; i++) {
				sDao.removeFromCourse(Long.parseLong(results[i]), course_id);
			}
		}

		model.put("course", course);

		model.put("id", course_id);

		model.put("classlist", classlist);

		List<Announcement> announcements = aDao.all();
		model.put("announcements", announcements);

		cDao.close();
		sDao.close();
		uDao.close();
		aDao.close();

		return new ModelAndView(model, "users/removefromclasslist.hbs");
	}

}
