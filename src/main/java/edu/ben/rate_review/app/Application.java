package edu.ben.rate_review.app;

import static spark.Spark.*;
import edu.ben.rate_review.controller.session.SessionsController;
import edu.ben.rate_review.controller.home.AboutUsController;
import edu.ben.rate_review.controller.home.ContactUsController;
import edu.ben.rate_review.controller.home.DepartmentsController;
import edu.ben.rate_review.controller.home.HomeController;
import edu.ben.rate_review.controller.home.LogInController;
import edu.ben.rate_review.controller.home.ProfessorController;
import edu.ben.rate_review.controller.home.RegisterController;
import edu.ben.rate_review.controller.home.TeacherController;
import edu.ben.rate_review.controller.home.TutorsController;
import edu.ben.rate_review.controllers.user.AdminDashboardController;
import edu.ben.rate_review.controllers.user.FacultyDashboardController;
import edu.ben.rate_review.controllers.user.StudentDashboardController;
import edu.ben.rate_review.controllers.user.TutorDashboardController;
import edu.ben.rate_review.controllers.user.UsersController;
import edu.ben.rate_review.daos.DaoManager;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Application {
	// controllers
	private static UsersController usersController = new UsersController();
	private static SessionsController sessionsController = new SessionsController();
	private static HomeController homeController = new HomeController();
	private static LogInController loginController = new LogInController();
	private static AboutUsController aboutusController = new AboutUsController();
	private static RegisterController registerController = new RegisterController();
	private static ContactUsController contactusController = new ContactUsController();
	private static StudentDashboardController studentdashController = new StudentDashboardController();
	private static DepartmentsController departmentsController = new DepartmentsController();
	private static TutorsController tutorsController = new TutorsController();
	private static TutorsController tutorController = new TutorsController();
	private static AdminDashboardController admindashController = new AdminDashboardController();
	private static FacultyDashboardController facultydashController = new FacultyDashboardController();
	private static TutorDashboardController tutordashController = new TutorDashboardController();
	private static TeacherController teacherController = new TeacherController();
	private static ProfessorController professorController = new ProfessorController();

	// match up paths
	public static String USERS_PATH = "/users";
	public static String USER_PATH = "/user";
	public static String LOGIN_PATH = "/login";
	public static String ABOUTUS_PATH = "/aboutus";
	public static String REGISTER_PATH = "/register";
	public static String CONTACTUS_PATH = "/contactus";
	public static String STUDENTDASHBOARD_PATH = "/studentdashboard";
	public static String FACULTYDASHBOARD_PATH = "/facultydashboard";
	public static String ADMINDASHBOARD_PATH = "/admindashboard";
	public static String TUTORDASHBOARD_PATH = "/tutordashboard";
	public static String TEACHER_PATH = "/teacher";
	public static String DEPARTMENTS_PATH = "/departments";
	public static String TUTORS_PATH = "/tutors";
	public static String TUTOR_PATH = "/tutor";
	public static String PROFESSOR_PATH = "/professor";
	public static String REVIEWPROFESSOR_PATH = "/reviewprofessor";


	public static void main(String[] args) throws Exception {

		// Set what port you want to run on
		port(3000);

		// Configure your Asset folder so that your JS, CSS, Images are
		// available from the server endpoint
		staticFiles.location("/public");
		configRoutes();

		Spark.exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});

		DaoManager d = new DaoManager();
	}

	/**
	 * Configures the routes based on URL path, you will want to be consistent
	 * and use this structure when you can
	 */
	private static void configRoutes() {
		
		get(TEACHER_PATH, (req, res) -> teacherController.showTeacherPage(req, res), new HandlebarsTemplateEngine());
		
		get(DEPARTMENTS_PATH, (req, res) -> departmentsController.showDepartmentsPage(req, res),
				new HandlebarsTemplateEngine());
		get(TUTORS_PATH, (req, res) -> tutorsController.showTutorsPage(req, res),
				new HandlebarsTemplateEngine());

		get("/", (req, res) -> homeController.showHomePage(req, res), new HandlebarsTemplateEngine());

		// Session Routes
		// get("/register", (req, res) -> sessionsController.showRegister(req,
		// res), new HandlebarsTemplateEngine());
		post("/register", (req, res) -> sessionsController.register(req, res));

		get("/", (req, res) -> homeController.showHomePage(req, res), new HandlebarsTemplateEngine());

		get(LOGIN_PATH, (req, res) -> loginController.showLoginPage(req, res), new HandlebarsTemplateEngine());
		post("/login", (req, res) -> loginController.login(req, res));
		get(FACULTYDASHBOARD_PATH, (req, res) -> facultydashController.showFacultyDashboardPage(req, res),
				new HandlebarsTemplateEngine());
		get(ADMINDASHBOARD_PATH, (req, res) -> admindashController.showAdminDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		get(TUTORDASHBOARD_PATH, (req, res) -> tutordashController.showTutorDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		get(ABOUTUS_PATH, (req, res) -> aboutusController.showAboutUsPage(req, res), new HandlebarsTemplateEngine());
		get(REGISTER_PATH, (req, res) -> registerController.showRegisterPage(req, res), new HandlebarsTemplateEngine());
		get(CONTACTUS_PATH, (req, res) -> contactusController.showContactUsPage(req, res),
				new HandlebarsTemplateEngine());
		get(DEPARTMENTS_PATH, (req, res) -> departmentsController.showDepartmentsPage(req, res),
				new HandlebarsTemplateEngine());
		get(TUTORS_PATH, (req, res) -> tutorsController.showTutorsPage(req, res),
				new HandlebarsTemplateEngine());
		get(TUTOR_PATH, (req, res) -> tutorController.showTutorPage(req, res),
				new HandlebarsTemplateEngine());
		get(STUDENTDASHBOARD_PATH, (req, res) -> studentdashController.showStudentDashboardPage(req, res),
				new HandlebarsTemplateEngine());
		get(PROFESSOR_PATH, (req, res) -> professorController.showProfessorPage(req, res),
				new HandlebarsTemplateEngine());
		get(REVIEWPROFESSOR_PATH, (req, res) -> professorController.showReviewProfessorPage(req, res),
				new HandlebarsTemplateEngine());
		// User Routes

		// List all Users
		get(USERS_PATH, (req, res) -> usersController.index(req, res), new HandlebarsTemplateEngine());
		// New User form
		get(USER_PATH + "/new", (req, res) -> usersController.newEntity(req, res), new HandlebarsTemplateEngine());
		// Display single User
		get(USER_PATH + "/:id", (req, res) -> usersController.show(req, res), new HandlebarsTemplateEngine());
		// Perform the creation of the User
		post(USER_PATH, (req, res) -> usersController.create(req, res));
		// Perform the update of a User
		put(USER_PATH + "/:id", (req, res) -> usersController.update(req, res));
		// Delete the User
		delete(USER_PATH + "/:id", (req, res) -> usersController.destroy(req, res));
	}
}