package edu.ben.rate_review.app;

import static spark.Spark.*;

/**
 * This application class runs our entire project and is also where we configure the paths to get to all our pages
 */

import java.util.HashMap;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.controller.session.*;
import edu.ben.rate_review.controller.user.*;
import edu.ben.rate_review.controller.home.*;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.policy.AuthPolicyManager;
import spark.Session;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Application {

	// static variables to control email functionality
	public static boolean ALLOW_EMAIL = true;
	// SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
	// "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"
	public static String DOMAIN = "rateandreview.herokuapp.com";
	// match up paths
	public static String HOME_PATH = "/";
	public static String NOTFOUND_HOME_PATH = "/notfound";
	public static String USERS_PATH = HOME_PATH + "users";
	public static String USER_PATH = HOME_PATH + "user";
	public static String EDITUSER_PATH = HOME_PATH + "user/:id/edit";
	public static String DELETEUSER_PATH = HOME_PATH + "deleteuser/:id";
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
	public static String EDITTUTORS_PATH = HOME_PATH + "tutor/:id/edit";
	public static String ADDTUTOR_PATH = HOME_PATH + "tutor/:id/add";

	public static String PROFESSOR_PATH = HOME_PATH + "professor/:professor_id/:display";
	public static String REVIEWPROFESSOR_PATH = HOME_PATH + "reviewprofessor/:student_course_id/review";
	public static String SELECTTUTOR_PATH = HOME_PATH + "selecttutors";
	public static String ADDPROFESSOR_PATH = HOME_PATH + "addprofessor";
	public static String TUTORAPPOINTMENT_PATH = HOME_PATH + "tutorappointment";
	public static String TEACHERADDTUTOR_PATH = HOME_PATH + "teacheraddtutor";

	public static String NOTLOGGEDIN_PATH = HOME_PATH + "notloggedinerror";
	public static String AUTHORIZATIONERROR_PATH = HOME_PATH + "notauthorized";
	public static String FAQ_PATH = HOME_PATH + "faq";

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

	public static String DELETETUTOR_PATH = HOME_PATH + "deletetutor/:id";
	public static String DELETECOURSE_PATH = HOME_PATH + "deletecourse/:id";

	public static String ADDCOURSE_PATH = HOME_PATH + "addCourse/:department";
	public static String ALLSTUDENTS_PATH = HOME_PATH + "selectstudents";
	public static String APPOINTMENT_PATH = HOME_PATH + "appointment";
	public static String MESSAGE_PATH = HOME_PATH + "instantmessage";
	public static String CALENDAR_PATH = HOME_PATH + "calendar";
	public static String COURSELANDING_PATH = HOME_PATH + "courseslanding";
	public static String COURSES_PATH = HOME_PATH + "courses/:department";

	public static String EDITCOURSES_PATH = HOME_PATH + "course/:id/edit";
	public static String CLASSLIST_PATH = HOME_PATH + "course/:id/classlist";
	public static String PROFCLASSLIST_PATH = HOME_PATH + "course/:id/profclasslist";
	public static String ADD_STUDENT_COURSE = HOME_PATH + "course/:id/addstudent";

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

	public static String REMOVEFROMCLASSLIST_PATH = HOME_PATH + "classlistremove/:id";
	public static String ADDTOCLASSLIST_PATH = HOME_PATH + "classlistadd/:id";

	public static String COMPARE_PATH = HOME_PATH + "compare";
	public static String STUDENTDASHBOARD_APPOINTMENT_PATH = HOME_PATH + "studentdashboard/appointment";
	public static String TUTORDASHBOARD_APPOINTMENT_PATH = HOME_PATH + "tutordashboard/appointment";

	public static void main(String[] args) throws Exception {

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
		// return default port if heroku-port isn't set (i.e. on localhost)
		return 4567;

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

		notFound((req, res) -> {
			res.type("application/json");
			
			// page not found - kicks out to home
			res.redirect(NOTFOUND_HOME_PATH);
			halt();
			
			return "{\"message\":\"Custom 404\"}";
		});

		// paths for adding a student to a course
		get(ADDTOCLASSLIST_PATH, EditCoursesController::showAddToClassListPage, new HandlebarsTemplateEngine());
		post(ADDTOCLASSLIST_PATH, EditCoursesController::addStudentToCourse, new HandlebarsTemplateEngine());

		// paths for removing a student for a course
		get(REMOVEFROMCLASSLIST_PATH, EditCoursesController::showRemoveFromClassListPage,
				new HandlebarsTemplateEngine());
		post(REMOVEFROMCLASSLIST_PATH, EditCoursesController::removeStudentFromCourse, new HandlebarsTemplateEngine());

		// paths for admin viewing and editing appointments
		get(APPOINTMENTS_PATH, AdminDashboardController::showAllDeptApt, new HandlebarsTemplateEngine());
		get(EDITAPPOINTMENTS_PATH, AdminDashboardController::showEditApt, new HandlebarsTemplateEngine());

		// path to edit a review
		get(DEPTREVIEWS_PATH, EditReviewsController::showDeptReviews, new HandlebarsTemplateEngine());

		// path to show admin landing page
		get(ADMINREVIEWLANDING_PATH, AdminDashboardController::showManageReviewsLandingPage,
				new HandlebarsTemplateEngine());

		// path for admin to update an appointment
		post(UPDATEAPPOINTMENT_PATH, AdminDashboardController::adminUpdateApt, new HandlebarsTemplateEngine());

		// path to delete review
		post(DELETEREVIEW_PATH, EditReviewsController::deleteReview, new HandlebarsTemplateEngine());

		// path to delete an appointment
		post(DELETEAPPOINTMENT_PATH, AdminDashboardController::adminDeleteApt, new HandlebarsTemplateEngine());

		// path for admin to delete at tutor relationship
		post(ADMINDELETETUTOR_PATH, AdminEditTutorController::adminDeleteTutor, new HandlebarsTemplateEngine());

		// paths for admins to add tutors
		get(TUTORLANDING_PATH, AdminDashboardController::showManageTutorsLandingPage, new HandlebarsTemplateEngine());
		post(ADMINADDTUTOR_PATH, AdminEditTutorController::adminAddTutor, new HandlebarsTemplateEngine());
		get(ADMINADDTUTOR_PATH, AdminEditTutorController::showAddTutorsPage, new HandlebarsTemplateEngine());
		get(ADMINADDTUTORLANDING_PATH, AdminEditTutorController::showAddTutorsLandingPage,
				new HandlebarsTemplateEngine());

		// path for the landing page for admin managing tutors by department
		get(ADMINAPTLANDING_PATH, AdminDashboardController::showManageAptLandingPage, new HandlebarsTemplateEngine());

		// log out path
		get(LOGOUT_PATH, SessionsController::logout, new HandlebarsTemplateEngine());

		// paths for admin editting tutors
		get(ADMINTUTOREDIT_PATH, AdminEditTutorController::showAdminEditTutorPage, new HandlebarsTemplateEngine());
		post(ADMINTUTOREDIT_PATH, AdminEditTutorController::adminUpdateTutor, new HandlebarsTemplateEngine());
		get(ADMINTUTOR_PATH, AdminEditTutorController::showDeptTutorsPage, new HandlebarsTemplateEngine());

		// paths for completing profiles for all roles
		get(COMPLETETUTOR_PATH, TutorDashboardController::showCompleteProfileTutorPage, new HandlebarsTemplateEngine());
		get(COMPLETEPROF_PATH, FacultyDashboardController::showCompleteProfileProfPage, new HandlebarsTemplateEngine());
		get(COMPLETESTUDENT_PATH, StudentDashboardController::showCompleteProfileStudentPage,
				new HandlebarsTemplateEngine());

		post(COMPLETETUTOR_PATH, TutorDashboardController::completeProfile);
		post(COMPLETEPROF_PATH, FacultyDashboardController::completeProfile);
		post(COMPLETESTUDENT_PATH, StudentDashboardController::completeProfile);

		// paths for an admin manage courses

		// adding courses
		get(ADDCOURSE_PATH, EditCoursesController::showAddCoursesPage, new HandlebarsTemplateEngine());

		post(ADDCOURSE_PATH, EditCoursesController::addCourse, new HandlebarsTemplateEngine());

		// Deleting courses
		post(DELETECOURSE_PATH, EditCoursesController::deleteCourse, new HandlebarsTemplateEngine());

		// editing course
		get(EDITCOURSES_PATH, EditCoursesController::showEditCoursesPage, new HandlebarsTemplateEngine());
		post(EDITCOURSES_PATH, EditCoursesController::updateCourse, new HandlebarsTemplateEngine());

		// paths for admin to view courses
		get(COURSES_PATH, EditCoursesController::showDeptCoursesPage, new HandlebarsTemplateEngine());

		get(COURSELANDING_PATH, AdminDashboardController::showManageCoursesLandingPage, new HandlebarsTemplateEngine());

		// mass enroll students as an admin
		post(COURSELANDING_PATH, AdminDashboardController::massEnrollStudents);

		// Faq path
		get(FAQ_PATH, FaqController::showFaqPage, new HandlebarsTemplateEngine());

		// path for faculty to add a tutor
		get(ALLSTUDENTS_PATH, FacultyDashboardController::showSelectStudentsPage, new HandlebarsTemplateEngine());
		get(ALLTUTORS_PATH, FacultyDashboardController::showAllTutorsPage, new HandlebarsTemplateEngine());

		// paths for faculty to manage tutors

		// path for landing page
		get(SELECTTUTOR_PATH, FacultyDashboardController::showSelectTutorsPage, new HandlebarsTemplateEngine());

		// adding a tutor
		get(ADDTUTOR_PATH, FacultyDashboardController::showAddTutorPage, new HandlebarsTemplateEngine());
		post(ADDTUTOR_PATH, FacultyDashboardController::addTutor, new HandlebarsTemplateEngine());

		// editing a tutor
		get(EDITTUTORS_PATH, EditTutorController::showEditTutorPage, new HandlebarsTemplateEngine());
		post(EDITTUTORS_PATH, EditTutorController::updateTutor, new HandlebarsTemplateEngine());

		// deleting a tutor
		post(DELETETUTOR_PATH, EditTutorController::deleteTutor, new HandlebarsTemplateEngine());

		// paths for 404 errors
		get(AUTHORIZATIONERROR_PATH, UnauthorizedController::showNotAuthorized, new HandlebarsTemplateEngine());

		get(NOTLOGGEDIN_PATH, UnauthorizedController::showNotLoggedIn, new HandlebarsTemplateEngine());

		// paths for admin to edit a user
		get(EDITUSER_PATH, EditUserController::showEditUserPage, new HandlebarsTemplateEngine());
		post(EDITUSER_PATH, EditUserController::updateUser, new HandlebarsTemplateEngine());
		post(DELETEUSER_PATH, EditUserController::deleteUser);

		// paths for admin managing announcementsF
		post(ADDANNOUNCEMENT_PATH, EditAnnouncementController::addAnnouncement, new HandlebarsTemplateEngine());
		get(ADDANNOUNCEMENT_PATH, EditAnnouncementController::showAddAnnouncementPage, new HandlebarsTemplateEngine());
		get(EDITANNOUNCEMENT_PATH, EditAnnouncementController::showEditAnnouncementPage,
				new HandlebarsTemplateEngine());
		post(EDITANNOUNCEMENT_PATH, EditAnnouncementController::updateAnnouncement, new HandlebarsTemplateEngine());
		post(DELETEANNOUNCEMENT_PATH, EditAnnouncementController::deleteAnnouncement, new HandlebarsTemplateEngine());
		get(ANNOUNCEMENTS_PATH, AdminDashboardController::showEditAnnouncements, new HandlebarsTemplateEngine());
		post(ANNOUNCEMENTS_PATH, AdminDashboardController::addAnnouncement, new HandlebarsTemplateEngine());

		// path for professor overview page
		get(PROFESSOR_PATH, ProfessorController::showProfessorPage, new HandlebarsTemplateEngine());
		post(PROFESSOR_PATH, ProfessorController::flag);

		put(PROFESSOR_PATH, ProfessorController::display);

		// paths for mass editing all users from admin perspective
		post(MASSEDITCONFIRMED_PATH, EditUserController::massEditConfirmed, new HandlebarsTemplateEngine());
		post(MASSEDITACTIVE_PATH, EditUserController::massEditActive, new HandlebarsTemplateEngine());
		post(MASSEDITYEAR_PATH, EditUserController::massEditYear, new HandlebarsTemplateEngine());
		post(MASSEDITROLE_PATH, EditUserController::massEditRole, new HandlebarsTemplateEngine());

		// paths for reviewing a professor
		get(REVIEWPROFESSOR_PATH, ProfessorReviewController::showReviewProfessorPage, new HandlebarsTemplateEngine());
		post(REVIEWPROFESSOR_PATH, ProfessorReviewController::reviewProfessor);

		put(REVIEWPROFESSOR_PATH, ProfessorReviewController::passCourse);

		// paths for revieing a tutor
		get(TUTORREVIEW_PATH, TutorReviewController::showTutorReview, new HandlebarsTemplateEngine());
		post(TUTORREVIEW_PATH, TutorReviewController::reviewTutor);

		// path for browse teacher path
		get(TEACHER_PATH, TeacherController::showTeacherPage, new HandlebarsTemplateEngine());

		get(DEPARTMENTS_PATH, DepartmentsController::showDepartmentsPage, new HandlebarsTemplateEngine());

		// path for the home page
		get(HOME_PATH, HomeController::showHomePage, new HandlebarsTemplateEngine());
		get(NOTFOUND_HOME_PATH, HomeController::showHomePage, new HandlebarsTemplateEngine());

		// paths for the admin dash boards

		get(ADMINDASHBOARD_PATH, AdminDashboardController::showAdminDashboardPage, new HandlebarsTemplateEngine());

		post(ADMINDASHBOARD_PATH, AdminDashboardController::handleFlaggedComment);

		// paths for admin managing all users
		get(ALLUSERS_PATH, AdminDashboardController::showAllUsersPage, new HandlebarsTemplateEngine());

		post(SORTBYLASTNAME_PATH, AdminDashboardController::sortByLastName);

		get(FACULTYDASHBOARD_PATH, FacultyDashboardController::showFacultyDashboardPage,
				new HandlebarsTemplateEngine());

		// path for tutor dashboards
		get(TUTORDASHBOARD_PATH, TutorDashboardController::showTutorDashboardPage, new HandlebarsTemplateEngine());
		post(TUTORDASHBOARD_PATH, TutorDashboardController::replyToRequest);

		get(TUTORDASHBOARD_APPOINTMENT_PATH, TutorDashboardController::showTutorDashboardPage,
				new HandlebarsTemplateEngine());
		post(TUTORDASHBOARD_APPOINTMENT_PATH, TutorDashboardController::replyToRequest, new HandlebarsTemplateEngine());

		// paths for student dashboard
		get(STUDENTDASHBOARD_PATH, StudentDashboardController::showStudentDashboardPage,
				new HandlebarsTemplateEngine());
		post(STUDENTDASHBOARD_PATH, StudentDashboardController::requestAppointment);

		// path for admin adding professor
		get(ADDPROFESSOR_PATH, AdminController::showAddProfessorPage, new HandlebarsTemplateEngine());

		// about us path
		get(ABOUTUS_PATH, AboutUsController::showAboutUsPage, new HandlebarsTemplateEngine());

		get(DEPARTMENTS_PATH, DepartmentsController::showDepartmentsPage, new HandlebarsTemplateEngine());

		// Change password paths
		get(CHANGEPASSWORD_PATH, ChangePasswordController::showChangePasswordPage, new HandlebarsTemplateEngine());
		post(CHANGEPASSWORD_PATH, ChangePasswordController::changePassword);

		// Confirmation paths
		get(CONFIRMATION_PATH, ConfirmationController::showConfirmationPage, new HandlebarsTemplateEngine());
		post(CONFIRMATION_PATH, ConfirmationController::confirm);

		// Account activation and deactivation
		get(ACTIVATION_PATH, ActivationController::showActivationPage, new HandlebarsTemplateEngine());
		get(DEACTIVATION_PATH, ActivationController::showDeActivationPage, new HandlebarsTemplateEngine());
		post(ACTIVATION_PATH, ActivationController::activate);
		post(DEACTIVATION_PATH, ActivationController::deactivate);
		post(ALLUSERS_PATH, AdminDashboardController::massRegister);

		// request recovery
		get(ACCOUNTRECOVERY_PATH, AccountRecoveryController::showAccountRecoveryEmailPage,
				new HandlebarsTemplateEngine());
		post(ACCOUNTRECOVERY_PATH, AccountRecoveryController::enterEmailRecoverAccount);

		// recover account with new info
		get(NEWINFO_PATH, AccountRecoveryController::showAccountRecoveryNewInfoPage, new HandlebarsTemplateEngine());
		post(NEWINFO_PATH, AccountRecoveryController::recoverAccount);

		// Session Routes

		// register route
		get(REGISTER_PATH, RegisterController::showRegisterPage, new HandlebarsTemplateEngine());
		post(REGISTER_PATH, SessionsController::register);
		// login route
		get(LOGIN_PATH, LogInController::showLoginPage, new HandlebarsTemplateEngine());
		post(LOGIN_PATH, LogInController::login, new HandlebarsTemplateEngine());

		// User Routes

		// List all Users
		path(USER_PATH, () -> {
			get("", UsersController::index, new HandlebarsTemplateEngine());
			get("/", UsersController::index, new HandlebarsTemplateEngine());

			// Perform the creation of the User
			post("", UsersController::create);
			post("/", UsersController::create);

			// New User form
			get("/new", UsersController::newEntity, new HandlebarsTemplateEngine());
			// Display single User
			get("/:id", UsersController::show, new HandlebarsTemplateEngine());

			// Perform the update of a User
			put("/:id", UsersController::update);
			// Delete the User
			delete("/:id", UsersController::destroy);
		});

		// analysis page
		get(ANALYSIS_PATH, AnalysisController::showAnalysisPage, new HandlebarsTemplateEngine());

		// paths contact US
		get(CONTACTUS_PATH, ContactUsController::showContactUsPage, new HandlebarsTemplateEngine());
		post(CONTACTUS_PATH, ContactUsController::contact);

		get(STUDENTDASHBOARD_PATH, StudentDashboardController::showStudentDashboardPage,
				new HandlebarsTemplateEngine());
		get(STUDENTDASHBOARD_APPOINTMENT_PATH, StudentDashboardController::showStudentDashboardPage,
				new HandlebarsTemplateEngine());
		post(STUDENTDASHBOARD_APPOINTMENT_PATH, StudentDashboardController::requestAppointment,
				new HandlebarsTemplateEngine());

		// paths for classlist for both admin and professor
		get(CLASSLIST_PATH, EditCoursesController::showClassListPage, new HandlebarsTemplateEngine());

		get(PROFCLASSLIST_PATH, EditCoursesController::showProfClassListPage, new HandlebarsTemplateEngine());

		post(CLASSLIST_PATH, EditCoursesController::showClassListPage);

		// paths my account
		get(MYACCOUNT_PATH, MyAccountController::showMyAccountPage, new HandlebarsTemplateEngine());
		post(MYACCOUNT_PATH, MyAccountController::completeProfile);

		// Compare/ Find page
		get(COMPARE_PATH, CompareController::showComparePage, new HandlebarsTemplateEngine());
		get(COURSESPROFESSOR_PATH, StudentDashboardController::showCourseAllProfessorsPage,
				new HandlebarsTemplateEngine());

		post(COURSESPROFESSOR_PATH, StudentDashboardController::showCourseAllProfessorsPage,
				new HandlebarsTemplateEngine());

		get(MAJORTUTOR_PATH, StudentDashboardController::showMajorTutorsPage, new HandlebarsTemplateEngine());
		post(MAJORTUTOR_PATH, StudentDashboardController::showMajorTutorsPage, new HandlebarsTemplateEngine());
	}
}