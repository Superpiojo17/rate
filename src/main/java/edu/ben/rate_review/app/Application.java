package edu.ben.rate_review.app;

import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

import java.util.HashMap;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.controller.home.AdminController;
import edu.ben.rate_review.controller.home.AboutUsController;
import edu.ben.rate_review.controller.home.ActivationController;
import edu.ben.rate_review.controller.home.ChangePasswordController;
import edu.ben.rate_review.controller.home.ConfirmationController;
import edu.ben.rate_review.controller.home.ContactUsController;
import edu.ben.rate_review.controller.home.DepartmentsController;
import edu.ben.rate_review.controller.home.HomeController;
import edu.ben.rate_review.controller.home.LogInController;
import edu.ben.rate_review.controller.home.RegisterController;
import edu.ben.rate_review.controller.home.TeacherController;
import edu.ben.rate_review.controller.home.TutorsController;
import edu.ben.rate_review.controller.session.SessionsController;
import edu.ben.rate_review.controller.user.AccountRecoveryController;
import edu.ben.rate_review.controller.user.AdminDashboardController;
import edu.ben.rate_review.controller.user.FacultyDashboardController;
import edu.ben.rate_review.controller.user.StudentDashboardController;
import edu.ben.rate_review.controller.user.TutorDashboardController;
import edu.ben.rate_review.controller.user.UsersController;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.controller.home.ProfessorController;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
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
	private static AdminDashboardController admindashController = new AdminDashboardController();
	private static FacultyDashboardController facultydashController = new FacultyDashboardController();
	private static TutorDashboardController tutordashController = new TutorDashboardController();
	private static TeacherController teacherController = new TeacherController();
	private static ActivationController activationController = new ActivationController();
	private static ConfirmationController confirmationController = new ConfirmationController();
	private static AccountRecoveryController accountrecoveryController = new AccountRecoveryController();
	private static ChangePasswordController changePasswordController = new ChangePasswordController();
	private static TutorsController tutorController = new TutorsController();
	private static ProfessorController professorController = new ProfessorController();
	private static AdminController adminController = new AdminController();

	// match up paths
	public static String DOMAIN = "localhost:3000";
	public static String HOME_PATH = "/";
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
	public static String ACTIVATION_PATH = "/activation";
	public static String CHANGEPASSWORD_PATH = "/changepassword";
	public static String CONFIRMATION_PATH = "/confirmation";
	public static String DEACTIVATION_PATH = "/deactivation";
	public static String ACCOUNTRECOVERY_PATH = "/accountrecovery";
	public static String NEWINFO_PATH = "/newinfo";
	public static String ALLUSERS_PATH = "/allusers";
	public static String TEST_PATH = "/test";
	public static String TUTOR_PATH = "/tutor";
	public static String PROFESSOR_PATH = "/professor";
	public static String REVIEWPROFESSOR_PATH = "/reviewprofessor";
	public static String ADDTUTOR_PATH = "/addtutor";
	public static String ADDPROFESSOR_PATH = "/addprofessor";

	public static void main(String[] args) throws Exception {

		// Set what port you want to run on
		port(3000);

		// Configure your Asset folder so that your JS, CSS, Images are
		// available from the server endpoint
		staticFiles.location("/public");
		configRoutes();
		new DaoManager();
	}

	/**
	 * Configures the routes based on URL path, you will want to be consistent
	 * and use this structure when you can
	 */
	private static void configRoutes() {

		// Filter that checks things right away before every request
		before("/*", (request, response) -> {
			// create the session and assign it to a variable
			Session session = request.session(true);

			// if the current user is null
			if (session.attribute("current_user") != null) {
				User u = null;
				User currentUser = (User) session.attribute("current_user");
				if (currentUser != null) {
					u = DaoManager.getInstance().getUserDao().findByEmail(currentUser.getEmail());
				}
				AuthPolicyManager.currentUser(u);
				session.attribute("current_user", u);
			}
		});

		exception(AuthException.class, (exception, request, response) -> {
			System.out.println(exception.getMessage());
			HashMap<String, Object> model = new HashMap<>();
			model.put("errors", exception.getMessage());
			//
			response.status(401);
			response.body(exception.getMessage());
		});

		exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});

		get(TUTOR_PATH, (req, res) -> tutorController.showTutorPage(req, res), new HandlebarsTemplateEngine());

		get(PROFESSOR_PATH, (req, res) -> professorController.showProfessorPage(req, res),
				new HandlebarsTemplateEngine());
		get(REVIEWPROFESSOR_PATH, (req, res) -> professorController.showReviewProfessorPage(req, res),
				new HandlebarsTemplateEngine());

		get(TEACHER_PATH, (req, res) -> teacherController.showTeacherPage(req, res), new HandlebarsTemplateEngine());

		get(DEPARTMENTS_PATH, (req, res) -> departmentsController.showDepartmentsPage(req, res),
				new HandlebarsTemplateEngine());
		get(TUTORS_PATH, (req, res) -> tutorsController.showTutorsPage(req, res), new HandlebarsTemplateEngine());

		get(HOME_PATH, (req, res) -> homeController.showHomePage(req, res), new HandlebarsTemplateEngine());

		get(FACULTYDASHBOARD_PATH, (req, res) -> facultydashController.showFacultyDashboardPage(req, res),
				new HandlebarsTemplateEngine());
		get(ADMINDASHBOARD_PATH, (req, res) -> admindashController.showAdminDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		get(ALLUSERS_PATH, (req, res) -> admindashController.showAllUsersPage(req, res),
				new HandlebarsTemplateEngine());

		get(ADDTUTOR_PATH, (req, res) -> adminController.showAddTutorPage(req, res), new HandlebarsTemplateEngine());
		get(ADDPROFESSOR_PATH, (req, res) -> adminController.showAddProfessorPage(req, res),
				new HandlebarsTemplateEngine());

		get(TUTORDASHBOARD_PATH, (req, res) -> tutordashController.showTutorDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		get(ABOUTUS_PATH, (req, res) -> aboutusController.showAboutUsPage(req, res), new HandlebarsTemplateEngine());

		get(CONTACTUS_PATH, (req, res) -> contactusController.showContactUsPage(req, res),
				new HandlebarsTemplateEngine());
		get(DEPARTMENTS_PATH, (req, res) -> departmentsController.showDepartmentsPage(req, res),
				new HandlebarsTemplateEngine());
		get(TUTORS_PATH, (req, res) -> tutorsController.showTutorsPage(req, res), new HandlebarsTemplateEngine());
		get(STUDENTDASHBOARD_PATH, (req, res) -> studentdashController.showStudentDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		// Change password paths
		get(CHANGEPASSWORD_PATH, (req, res) -> changePasswordController.showChangePasswordPage(req, res),
				new HandlebarsTemplateEngine());
		post(CHANGEPASSWORD_PATH, (req, res) -> changePasswordController.changePassword(req, res));

		// Confirmation paths
		get(CONFIRMATION_PATH, (req, res) -> confirmationController.showConfirmationPage(req, res),
				new HandlebarsTemplateEngine());
		post(CONFIRMATION_PATH, (req, res) -> confirmationController.confirm(req, res));

		// Account activation and deactivation
		get(ACTIVATION_PATH, (req, res) -> activationController.showActivationPage(req, res),
				new HandlebarsTemplateEngine());
		get(DEACTIVATION_PATH, (req, res) -> activationController.showDeActivationPage(req, res),
				new HandlebarsTemplateEngine());
		post(ACTIVATION_PATH, (req, res) -> activationController.activate(req, res));
		post(DEACTIVATION_PATH, (req, res) -> activationController.deactivate(req, res));

		post(ALLUSERS_PATH, (req, res) -> admindashController.massRegister(req, res));

		// Account Recoveryso
		// request recovery
		get(ACCOUNTRECOVERY_PATH, (req, res) -> accountrecoveryController.showAccountRecoveryEmailPage(req, res),
				new HandlebarsTemplateEngine());
		post(ACCOUNTRECOVERY_PATH, (req, res) -> accountrecoveryController.enterEmailRecoverAccount(req, res));
		// recover account with new info
		get(NEWINFO_PATH, (req, res) -> accountrecoveryController.showAccountRecoveryNewInfoPage(req, res),
				new HandlebarsTemplateEngine());
		post(NEWINFO_PATH, (req, res) -> accountrecoveryController.recoverAccount(req, res));

		// Session Routes
		// register route
		get(REGISTER_PATH, (req, res) -> registerController.showRegisterPage(req, res), new HandlebarsTemplateEngine());
		post(REGISTER_PATH, (req, res) -> sessionsController.register(req, res));
		// login route
		get(LOGIN_PATH, (req, res) -> loginController.showLoginPage(req, res), new HandlebarsTemplateEngine());
		post(LOGIN_PATH, (req, res) -> loginController.login(req, res));

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