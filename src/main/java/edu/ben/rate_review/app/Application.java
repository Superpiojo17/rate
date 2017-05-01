package edu.ben.rate_review.app;

import static spark.Spark.*;

/**
 * This application class runs our entire project and is also where we configure the paths to get to all our pages
 */

import java.util.HashMap;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.controller.home.AboutUsController;
import edu.ben.rate_review.controller.home.ActivationController;
import edu.ben.rate_review.controller.home.AdminController;
import edu.ben.rate_review.controller.home.AnalysisController;
import edu.ben.rate_review.controller.home.ChangePasswordController;
import edu.ben.rate_review.controller.home.CompareController;
import edu.ben.rate_review.controller.home.ConfirmationController;
import edu.ben.rate_review.controller.home.ContactUsController;
import edu.ben.rate_review.controller.home.DepartmentsController;
import edu.ben.rate_review.controller.home.FaqController;
import edu.ben.rate_review.controller.home.HomeController;
import edu.ben.rate_review.controller.home.LogInController;
import edu.ben.rate_review.controller.home.ProfessorController;
import edu.ben.rate_review.controller.home.ProfessorReviewController;
import edu.ben.rate_review.controller.home.RegisterController;
import edu.ben.rate_review.controller.home.TeacherController;
import edu.ben.rate_review.controller.home.TutorReviewController;
import edu.ben.rate_review.controller.session.SessionsController;
import edu.ben.rate_review.controller.user.AccountRecoveryController;
import edu.ben.rate_review.controller.user.AdminDashboardController;
import edu.ben.rate_review.controller.user.AdminEditTutorController;
import edu.ben.rate_review.controller.user.EditAnnouncementController;
import edu.ben.rate_review.controller.user.EditCoursesController;
import edu.ben.rate_review.controller.user.EditReviewsController;
import edu.ben.rate_review.controller.user.EditTutorController;
import edu.ben.rate_review.controller.user.EditUserController;
import edu.ben.rate_review.controller.user.FacultyDashboardController;
import edu.ben.rate_review.controller.user.MyAccountController;
import edu.ben.rate_review.controller.user.StudentDashboardController;
import edu.ben.rate_review.controller.user.TutorDashboardController;
import edu.ben.rate_review.controller.user.UnauthorizedController;
import edu.ben.rate_review.controller.user.UsersController;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.Session;
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
	private static AdminDashboardController admindashController = new AdminDashboardController();
	private static FacultyDashboardController facultydashController = new FacultyDashboardController();
	private static TutorDashboardController tutordashController = new TutorDashboardController();
	private static TeacherController teacherController = new TeacherController();
	private static ActivationController activationController = new ActivationController();
	private static ConfirmationController confirmationController = new ConfirmationController();
	private static AccountRecoveryController accountrecoveryController = new AccountRecoveryController();
	private static ChangePasswordController changePasswordController = new ChangePasswordController();
	private static ProfessorController professorController = new ProfessorController();
	private static AdminController adminController = new AdminController();
	private static EditCoursesController editcoursesController = new EditCoursesController();
	private static AdminEditTutorController adminedittutorController = new AdminEditTutorController();
	private static TutorReviewController tutorReviewController = new TutorReviewController();
	private static FaqController faqController = new FaqController();
	private static EditReviewsController editReviewController = new EditReviewsController();
	private static ProfessorReviewController professorReviewController = new ProfessorReviewController();
	private static EditUserController edituserController = new EditUserController();
	private static UnauthorizedController unauthorizedController = new UnauthorizedController();
	private static EditAnnouncementController editannouncementController = new EditAnnouncementController();
	private static EditTutorController edittutorController = new EditTutorController();
	private static AnalysisController analysisController = new AnalysisController();
	private static MyAccountController myAccountController = new MyAccountController();
	private static CompareController compareController = new CompareController();

	// match up paths
	// public static String DOMAIN = "http://localhost";
	public static String DOMAIN = "cs.ben.edu";
	public static String HOME_PATH = "/";
	public static String USERS_PATH = HOME_PATH + "users";
	public static String USER_PATH = HOME_PATH + "user";
	public static String LOGIN_PATH = HOME_PATH + "signin";
	public static String ABOUTUS_PATH = HOME_PATH + "aboutus";
	public static String REGISTER_PATH = HOME_PATH + "register";
	public static String CONTACTUS_PATH = HOME_PATH + "contactus";
	public static String STUDENTDASHBOARD_PATH = HOME_PATH + "studentdashboard";
	public static String FACULTYDASHBOARD_PATH = HOME_PATH + "facultydashboard";
	public static String ADMINDASHBOARD_PATH = HOME_PATH + "admindashboard";
	public static String TUTORDASHBOARD_PATH = HOME_PATH + "tutordashboard";
	public static String TEACHER_PATH = HOME_PATH + "teacher";
	public static String COURSESPROFESSOR_PATH = HOME_PATH + "courseprofessors";
	public static String MAJORTUTOR_PATH = HOME_PATH + "majortutors";
	public static String DEPARTMENTS_PATH = HOME_PATH + "departments";
	public static String TUTORS_PATH = HOME_PATH + "tutors";
	public static String ACTIVATION_PATH = HOME_PATH + "activation";
	public static String CHANGEPASSWORD_PATH = HOME_PATH + "changepassword";
	public static String CONFIRMATION_PATH = HOME_PATH + "confirmation";
	public static String DEACTIVATION_PATH = HOME_PATH + "deactivation";
	public static String ACCOUNTRECOVERY_PATH = HOME_PATH + "accountrecovery";
	public static String NEWINFO_PATH = HOME_PATH + "newinfo";
	public static String ALLUSERS_PATH = HOME_PATH + "allusers";
	public static String TEST_PATH = HOME_PATH + "test";
	public static String TUTOR_PATH = HOME_PATH + "tutor";
	public static String PROFESSOR_PATH = HOME_PATH + "professor/:professor_id/:display";
	public static String REVIEWPROFESSOR_PATH = HOME_PATH + "reviewprofessor/:student_course_id/review";
	public static String SELECTTUTOR_PATH = HOME_PATH + "selecttutors";
	public static String ADDPROFESSOR_PATH = HOME_PATH + "addprofessor";
	public static String FAQ_PATH = HOME_PATH + "faq";
	public static String TUTORAPPOINTMENT_PATH = HOME_PATH + "tutorappointment";
	public static String TEACHERADDTUTOR_PATH = HOME_PATH + "teacheraddtutor";
	public static String NOTLOGGEDIN_PATH = HOME_PATH + "notloggedinerror";
	public static String AUTHORIZATIONERROR_PATH = HOME_PATH + "notauthorized";
	public static String EDITUSER_PATH = HOME_PATH + "user/:id/edit";
	public static String DELETEUSER_PATH = HOME_PATH + "deleteuser/:id";
	public static String EDITANNOUNCEMENT_PATH = HOME_PATH + "announcement/:id/edit";
	public static String DELETEANNOUNCEMENT_PATH = HOME_PATH + "deleteannouncement/:id";
	public static String ANNOUNCEMENTS_PATH = HOME_PATH + "announcement";
	public static String SORTBYLASTNAME_PATH = HOME_PATH + "sortbylastname";
	public static String MASSEDITCONFIRMED_PATH = HOME_PATH + "masseditconfirmed";
	public static String MASSEDITACTIVE_PATH = HOME_PATH + "masseditactive";
	public static String MASSEDITYEAR_PATH = HOME_PATH + "massedityear";
	public static String MASSEDITROLE_PATH = HOME_PATH + "masseditrole";
	public static String ADDANNOUNCEMENT_PATH = HOME_PATH + "addannouncement";
	public static String ALLTUTORS_PATH = HOME_PATH + "alltutors/:id";
	public static String EDITTUTORS_PATH = HOME_PATH + "tutor/:id/edit";
	public static String DELETETUTOR_PATH = HOME_PATH + "deletetutor/:id";
	public static String DELETECOURSE_PATH = HOME_PATH + "deletecourse/:id";
	public static String ADDTUTOR_PATH = HOME_PATH + "tutor/:id/add";
	public static String ADDCOURSE_PATH = HOME_PATH + "addCourse/:department";
	public static String ALLSTUDENTS_PATH = HOME_PATH + "selectstudents";
	public static String APPOINTMENT_PATH = HOME_PATH + "appointment";
	public static String MESSAGE_PATH = HOME_PATH + "instantmessage";
	public static String CALENDAR_PATH = HOME_PATH + "calendar";
	public static String COURSELANDING_PATH = HOME_PATH + "courseslanding";
	public static String COURSES_PATH = HOME_PATH + "courses/:department";
	public static String EDITCOURSES_PATH = HOME_PATH + "course/:id/edit";
	public static String COMPLETEPROF_PATH = HOME_PATH + "completeprofileprof/:id";
	public static String COMPLETESTUDENT_PATH = HOME_PATH + "completeprofilestudent/:id";
	public static String COMPLETETUTOR_PATH = HOME_PATH + "completeprofiletutor/:id";
	public static String TUTORLANDING_PATH = HOME_PATH + "managetutorslanding";
	public static String ADMINTUTOR_PATH = HOME_PATH + "tutors/:department";
	public static String ADMINTUTOREDIT_PATH = HOME_PATH + "adminedittutor/:id/edit";
	public static String ADMINADDTUTORLANDING_PATH = HOME_PATH + "adminAddTutorlanding/:department";
	public static String ADMINADDTUTOR_PATH = HOME_PATH + "adminaddtutor/:id";
	public static String ADMINDELETETUTOR_PATH = HOME_PATH + "admindeletetutor/:id";
	public static String ANALYSIS_PATH = HOME_PATH + "analysis";
	public static String MYACCOUNT_PATH = HOME_PATH + "myaccount";
	public static String ADMINAPTLANDING_PATH = HOME_PATH + "adminaptlanding";
	public static String APPOINTMENTS_PATH = HOME_PATH + "appointments/:department";
	public static String EDITAPPOINTMENTS_PATH = HOME_PATH + "appointment/:id/edit";
	public static String UPDATEAPPOINTMENT_PATH = HOME_PATH + "editapt/:id/edit";
	public static String LOGOUT_PATH = HOME_PATH + "logout";
	public static String DELETEAPPOINTMENT_PATH = HOME_PATH + "deleteapt/:id";
	public static String TUTORREVIEW_PATH = HOME_PATH + "tutorreview/:appointment_id";
	public static String ADMINREVIEWLANDING_PATH = HOME_PATH + "reviewlanding";
	public static String DEPTREVIEWS_PATH = HOME_PATH + "reviews/:department";
	public static String DELETEREVIEW_PATH = HOME_PATH + "deletereview/:student_course_id";
	public static String CLASSLIST_PATH = HOME_PATH + "course/:id/classlist";
	public static String PROFCLASSLIST_PATH = HOME_PATH + "course/:id/profclasslist";
	public static String REMOVEFROMCLASSLIST_PATH = HOME_PATH + "classlistremove/:id";
	public static String ADDTOCLASSLIST_PATH = HOME_PATH + "classlistadd/:id";
	public static String ADD_STUDENT_COURSE = HOME_PATH + "course/:id/addstudent";
	public static String COMPARE_PATH = HOME_PATH + "compare";

	public static void main(String[] args) throws Exception {

		// Set what port you want to run on
		port(getEnvironmentPort());

		// Configure your Asset folder so that your JS, CSS, Images are
		// available from the server endpoint
		staticFiles.location("/public");
		configRoutes();
		new DaoManager();
	}

	static int getEnvironmentPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567; // return default port if heroku-port isn't set (i.e. on
						// localhost)
	}

	/**
	 * Configures the routes based on URL path
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

		after((request, response) -> {
			response.header("Content-Encoding", "gzip");
		});

		exception(AuthException.class, (exception, request, response) -> {
			System.out.println(exception.getMessage());
			HashMap<String, Object> model = new HashMap<>();
			model.put("errors", exception.getMessage());
			//
			response.status(401);
			response.redirect(AUTHORIZATIONERROR_PATH);
		});

		exception(Exception.class, (exception, request, response) -> {
			exception.printStackTrace();
		});

		// paths for adding a student to a course
		get(ADDTOCLASSLIST_PATH, (req, res) -> editcoursesController.showAddToClassListPage(req, res),
				new HandlebarsTemplateEngine());
		post(ADDTOCLASSLIST_PATH, (req, res) -> editcoursesController.addStudentToCourse(req, res),
				new HandlebarsTemplateEngine());

		// paths for removing a student for a course
		get(REMOVEFROMCLASSLIST_PATH, (req, res) -> editcoursesController.showRemoveFromClassListPage(req, res),
				new HandlebarsTemplateEngine());
		post(REMOVEFROMCLASSLIST_PATH, (req, res) -> editcoursesController.removeStudentFromCourse(req, res),
				new HandlebarsTemplateEngine());

		// paths for admin viewing and editing appointments
		get(APPOINTMENTS_PATH, (req, res) -> admindashController.showAllDeptApt(req, res),
				new HandlebarsTemplateEngine());
		get(EDITAPPOINTMENTS_PATH, (req, res) -> admindashController.showEditApt(req, res),
				new HandlebarsTemplateEngine());

		// path to edit a review
		get(DEPTREVIEWS_PATH, (req, res) -> editReviewController.showDeptReviews(req, res),
				new HandlebarsTemplateEngine());

		// path to show admin landing page
		get(ADMINREVIEWLANDING_PATH, (req, res) -> admindashController.showManageReviewsLandingPage(req, res),
				new HandlebarsTemplateEngine());

		// path for admin to update an appointment
		post(UPDATEAPPOINTMENT_PATH, (req, res) -> admindashController.adminUpdateApt(req, res),
				new HandlebarsTemplateEngine());

		// path to delete review
		post(DELETEREVIEW_PATH, (req, res) -> editReviewController.deleteReview(req, res),
				new HandlebarsTemplateEngine());

		// path to delete an appointment
		post(DELETEAPPOINTMENT_PATH, (req, res) -> admindashController.adminDeleteApt(req, res),
				new HandlebarsTemplateEngine());

		// path for admin to delete at tutor relationship
		post(ADMINDELETETUTOR_PATH, (req, res) -> adminedittutorController.adminDeleteTutor(req, res),
				new HandlebarsTemplateEngine());

		// paths for admins to add tutors
		get(TUTORLANDING_PATH, (req, res) -> admindashController.showManageTutorsLandingPage(req, res),
				new HandlebarsTemplateEngine());
		post(ADMINADDTUTOR_PATH, (req, res) -> adminedittutorController.adminAddTutor(req, res),
				new HandlebarsTemplateEngine());
		get(ADMINADDTUTOR_PATH, (req, res) -> adminedittutorController.showAddTutorsPage(req, res),
				new HandlebarsTemplateEngine());
		get(ADMINADDTUTORLANDING_PATH, (req, res) -> adminedittutorController.showAddTutorsLandingPage(req, res),
				new HandlebarsTemplateEngine());

		// path for the landing page for admin managing tutors by department
		get(ADMINAPTLANDING_PATH, (req, res) -> admindashController.showManageAptLandingPage(req, res),
				new HandlebarsTemplateEngine());

		// log out path
		get(LOGOUT_PATH, (req, res) -> sessionsController.logout(req, res), new HandlebarsTemplateEngine());

		// paths for admin editting tutors
		get(ADMINTUTOREDIT_PATH, (req, res) -> adminedittutorController.showAdminEditTutorPage(req, res),
				new HandlebarsTemplateEngine());
		post(ADMINTUTOREDIT_PATH, (req, res) -> adminedittutorController.adminUpdateTutor(req, res),
				new HandlebarsTemplateEngine());
		get(ADMINTUTOR_PATH, (req, res) -> adminedittutorController.showDeptTutorsPage(req, res),
				new HandlebarsTemplateEngine());

		// paths for completing profiles for all roles
		get(COMPLETETUTOR_PATH, (req, res) -> tutordashController.showCompleteProfileTutorPage(req, res),
				new HandlebarsTemplateEngine());
		get(COMPLETEPROF_PATH, (req, res) -> facultydashController.showCompleteProfileProfPage(req, res),
				new HandlebarsTemplateEngine());
		get(COMPLETESTUDENT_PATH, (req, res) -> studentdashController.showCompleteProfileStudentPage(req, res),
				new HandlebarsTemplateEngine());

		post(COMPLETETUTOR_PATH, (req, res) -> tutordashController.completeProfile(req, res));
		post(COMPLETEPROF_PATH, (req, res) -> facultydashController.completeProfile(req, res));
		post(COMPLETESTUDENT_PATH, (req, res) -> studentdashController.completeProfile(req, res));

		// paths for an admin manage courses

		// adding courses
		get(ADDCOURSE_PATH, (req, res) -> editcoursesController.showAddCoursesPage(req, res),
				new HandlebarsTemplateEngine());

		post(ADDCOURSE_PATH, (req, res) -> editcoursesController.addCourse(req, res), new HandlebarsTemplateEngine());

		// Deleting courses
		post(DELETECOURSE_PATH, (req, res) -> editcoursesController.deleteCourse(req, res),
				new HandlebarsTemplateEngine());

		// editing course
		get(EDITCOURSES_PATH, (req, res) -> editcoursesController.showEditCoursesPage(req, res),
				new HandlebarsTemplateEngine());
		post(EDITCOURSES_PATH, (req, res) -> editcoursesController.updateCourse(req, res),
				new HandlebarsTemplateEngine());

		// paths for admin to view courses
		get(COURSES_PATH, (req, res) -> editcoursesController.showDeptCoursesPage(req, res),
				new HandlebarsTemplateEngine());

		get(COURSELANDING_PATH, (req, res) -> admindashController.showManageCoursesLandingPage(req, res),
				new HandlebarsTemplateEngine());

		// mass enroll students as an admin
		post(COURSELANDING_PATH, (req, res) -> admindashController.massEnrollStudents(req, res));

		// Faq path
		get(FAQ_PATH, (req, res) -> faqController.showFaqPage(req, res), new HandlebarsTemplateEngine());

		// path for faculty to add a tutor
		get(ALLSTUDENTS_PATH, (req, res) -> facultydashController.showSelectStudentsPage(req, res),
				new HandlebarsTemplateEngine());
		get(ALLTUTORS_PATH, (req, res) -> facultydashController.showAllTutorsPage(req, res),
				new HandlebarsTemplateEngine());

		// paths for faculty to manage tutors

		// path for landing page
		get(SELECTTUTOR_PATH, (req, res) -> facultydashController.showSelectTutorsPage(req, res),
				new HandlebarsTemplateEngine());

		// adding a tutor
		get(ADDTUTOR_PATH, (req, res) -> facultydashController.showAddTutorPage(req, res),
				new HandlebarsTemplateEngine());
		post(ADDTUTOR_PATH, (req, res) -> facultydashController.addTutor(req, res), new HandlebarsTemplateEngine());

		// editing a tutor
		get(EDITTUTORS_PATH, (req, res) -> edittutorController.showEditTutorPage(req, res),
				new HandlebarsTemplateEngine());
		post(EDITTUTORS_PATH, (req, res) -> edittutorController.updateTutor(req, res), new HandlebarsTemplateEngine());

		// deleting a tutor
		post(DELETETUTOR_PATH, (req, res) -> edittutorController.deleteTutor(req, res), new HandlebarsTemplateEngine());

		// paths for 404 errors
		get(AUTHORIZATIONERROR_PATH, (req, res) -> unauthorizedController.showNotAuthorized(req, res),
				new HandlebarsTemplateEngine());

		get(NOTLOGGEDIN_PATH, (req, res) -> unauthorizedController.showNotLoggedIn(req, res),
				new HandlebarsTemplateEngine());

		// paths for admin to edit a user
		get(EDITUSER_PATH, (req, res) -> edituserController.showEditUserPage(req, res), new HandlebarsTemplateEngine());
		post(EDITUSER_PATH, (req, res) -> edituserController.updateUser(req, res), new HandlebarsTemplateEngine());
		post(DELETEUSER_PATH, (req, res) -> edituserController.deleteUser(req, res));

		// paths for admin managing announcementsF
		post(ADDANNOUNCEMENT_PATH, (req, res) -> editannouncementController.addAnnouncement(req, res),
				new HandlebarsTemplateEngine());
		get(ADDANNOUNCEMENT_PATH, (req, res) -> editannouncementController.showAddAnnouncementPage(req, res),
				new HandlebarsTemplateEngine());
		get(EDITANNOUNCEMENT_PATH, (req, res) -> editannouncementController.showEditAnnouncementPage(req, res),
				new HandlebarsTemplateEngine());
		post(EDITANNOUNCEMENT_PATH, (req, res) -> editannouncementController.updateAnnouncement(req, res),
				new HandlebarsTemplateEngine());
		post(DELETEANNOUNCEMENT_PATH, (req, res) -> editannouncementController.deleteAnnouncement(req, res),
				new HandlebarsTemplateEngine());
		get(ANNOUNCEMENTS_PATH, (req, res) -> admindashController.showEditAnnouncements(req, res),
				new HandlebarsTemplateEngine());
		post(ANNOUNCEMENTS_PATH, (req, res) -> admindashController.addAnnouncement(req, res),
				new HandlebarsTemplateEngine());

		// path for professor overview page
		get(PROFESSOR_PATH, (req, res) -> professorController.showProfessorPage(req, res),
				new HandlebarsTemplateEngine());
		post(PROFESSOR_PATH, (req, res) -> professorController.flag(req, res));

		put(PROFESSOR_PATH, (req, res) -> professorController.display(req, res));

		// paths for mass editing all users from admin perspective
		post(MASSEDITCONFIRMED_PATH, (req, res) -> edituserController.massEditConfirmed(req, res),
				new HandlebarsTemplateEngine());
		post(MASSEDITACTIVE_PATH, (req, res) -> edituserController.massEditActive(req, res),
				new HandlebarsTemplateEngine());
		post(MASSEDITYEAR_PATH, (req, res) -> edituserController.massEditYear(req, res),
				new HandlebarsTemplateEngine());
		post(MASSEDITROLE_PATH, (req, res) -> edituserController.massEditRole(req, res),
				new HandlebarsTemplateEngine());

		// paths for reviewing a professor
		get(REVIEWPROFESSOR_PATH, (req, res) -> professorReviewController.showReviewProfessorPage(req, res),
				new HandlebarsTemplateEngine());
		post(REVIEWPROFESSOR_PATH, (req, res) -> professorReviewController.reviewProfessor(req, res));

		put(REVIEWPROFESSOR_PATH, (req, res) -> professorReviewController.passCourse(req, res));

		// paths for revieing a tutor
		get(TUTORREVIEW_PATH, (req, res) -> tutorReviewController.showTutorReview(req, res),
				new HandlebarsTemplateEngine());
		post(TUTORREVIEW_PATH, (req, res) -> tutorReviewController.reviewTutor(req, res));

		// path for browse teacher path
		get(TEACHER_PATH, (req, res) -> teacherController.showTeacherPage(req, res), new HandlebarsTemplateEngine());

		get(DEPARTMENTS_PATH, (req, res) -> departmentsController.showDepartmentsPage(req, res),
				new HandlebarsTemplateEngine());

		// path for the home page
		get(HOME_PATH, (req, res) -> homeController.showHomePage(req, res), new HandlebarsTemplateEngine());

		// paths for the admin dash boards

		get(ADMINDASHBOARD_PATH, (req, res) -> admindashController.showAdminDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		post(ADMINDASHBOARD_PATH, (req, res) -> admindashController.handleFlaggedComment(req, res));

		// paths for admin managing all users
		get(ALLUSERS_PATH, (req, res) -> admindashController.showAllUsersPage(req, res),
				new HandlebarsTemplateEngine());

		post(SORTBYLASTNAME_PATH, (req, res) -> admindashController.sortByLastName(req, res));

		get(FACULTYDASHBOARD_PATH, (req, res) -> facultydashController.showFacultyDashboardPage(req, res),
				new HandlebarsTemplateEngine());

		// path for tutor dashboards
		get(TUTORDASHBOARD_PATH, (req, res) -> tutordashController.showTutorDashboardPage(req, res),
				new HandlebarsTemplateEngine());
		post(TUTORDASHBOARD_PATH, (req, res) -> tutordashController.replyToRequest(req, res));
		post(TUTORDASHBOARD_PATH, (req, res) -> tutordashController.replyToRequest(req, res));

		// paths for student dashboard
		get(STUDENTDASHBOARD_PATH, (req, res) -> studentdashController.showStudentDashboardPage(req, res),
				new HandlebarsTemplateEngine());
		post(STUDENTDASHBOARD_PATH, (req, res) -> studentdashController.requestAppointment(req, res));

		// path for admin adding professor
		get(ADDPROFESSOR_PATH, (req, res) -> adminController.showAddProfessorPage(req, res),
				new HandlebarsTemplateEngine());

		// about us path
		get(ABOUTUS_PATH, (req, res) -> aboutusController.showAboutUsPage(req, res), new HandlebarsTemplateEngine());

		get(DEPARTMENTS_PATH, (req, res) -> departmentsController.showDepartmentsPage(req, res),
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

		// analysis page
		get(ANALYSIS_PATH, (req, res) -> analysisController.showAnalysisPage(req, res), new HandlebarsTemplateEngine());

		// paths contact US
		get(CONTACTUS_PATH, (req, res) -> contactusController.showContactUsPage(req, res),
				new HandlebarsTemplateEngine());
		post(CONTACTUS_PATH, (req, res) -> ContactUsController.contact(req, res));

		get(STUDENTDASHBOARD_PATH, (req, res) -> studentdashController.showStudentDashboardPage(req, res),
				new HandlebarsTemplateEngine());
		post(STUDENTDASHBOARD_PATH, (req, res) -> studentdashController.requestAppointment(req, res));

		// paths for classlist for both admin and professor
		get(CLASSLIST_PATH, (req, res) -> editcoursesController.showClassListPage(req, res),
				new HandlebarsTemplateEngine());

		get(PROFCLASSLIST_PATH, (req, res) -> editcoursesController.showProfClassListPage(req, res),
				new HandlebarsTemplateEngine());

		post(CLASSLIST_PATH, (req, res) -> editcoursesController.showClassListPage(req, res));

		// paths my account
		get(MYACCOUNT_PATH, (req, res) -> myAccountController.showMyAccountPage(req, res),
				new HandlebarsTemplateEngine());
		post(MYACCOUNT_PATH, (req, res) -> myAccountController.completeProfile(req, res));

		// Compare/ Find page
		get(COMPARE_PATH, (req, res) -> compareController.showComparePage(req, res), new HandlebarsTemplateEngine());
		// post(COMPARE_PATH, (req, res) ->
		// compareController.completeProfile(req, res));
		// MAYBE DELETE???
		get(COURSESPROFESSOR_PATH, (req, res) -> studentdashController.showCourseAllProfessorsPage(req, res),
				new HandlebarsTemplateEngine());

		post(COURSESPROFESSOR_PATH, (req, res) -> studentdashController.showCourseAllProfessorsPage(req, res),
				new HandlebarsTemplateEngine());

		get(MAJORTUTOR_PATH, (req, res) -> studentdashController.showMajorTutorsPage(req, res),
				new HandlebarsTemplateEngine());
		post(MAJORTUTOR_PATH, (req, res) -> studentdashController.showMajorTutorsPage(req, res),
				new HandlebarsTemplateEngine());
	}
}