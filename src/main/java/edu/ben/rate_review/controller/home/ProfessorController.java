package edu.ben.rate_review.controller.home;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.daos.AnnouncementDao;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.AllRatingsModel;
import edu.ben.rate_review.models.Announcement;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

import static spark.Spark.halt;

/**
 * Controller for the professor page
 *
 * @author Mike
 * @version 3-3-2017
 */
public class ProfessorController {

    public static ModelAndView showProfessorPage(Request req, Response res) {
        // Just a hash to pass data from the servlet to the page
        HashMap<String, Object> model = new HashMap<>();

        Session session = req.session();
        User u = (User) session.attribute("current_user");

        model.put("domain", Application.DOMAIN);

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
        // gets instance of review dao
        DaoManager dao = DaoManager.getInstance();

        // gets professor id from url, finds professor in user table
        String idString = req.params("professor_id");
        long id = Long.parseLong(idString);
        UserDao uDao = DaoManager.getInstance().getUserDao();
        User prof = uDao.findById(id);

        // if user attempts to access using a non-professor's ID
        if (prof == null || prof.getRole() != 2) {
            res.redirect(Application.AUTHORIZATIONERROR_PATH);
            halt();
        } else {
            ProfessorReviewDao reviewDao = dao.getProfessorReviewDao();
            // gets a list of all unique courses a given professor teaches
            List<String> uniqueCourses = reviewDao.listUniqueCourses(prof);
            // adds overview option and initial SELECT COURSE
            uniqueCourses.add(0, "Overview");
            // uniqueCourses.add(0, "SELECT COURSE");
            // lists unique courses for top of page
            model.put("unique_courses", uniqueCourses);

            // used for url - overview or specific class
            String display = req.params("display");

            // displays whether rating is overall or for a specific class
            boolean isOverview = true;
            if (!display.equalsIgnoreCase("overview")) {
                isOverview = false;
            }
            model.put("is_overview", isOverview);
            model.put("display", display);

            // creates a list of all reviews for that given professor/course
            List<ProfessorReview> reviews = reviewDao.listCoursesByProfessorEmail(prof, display);

            // if user attempts to access a course not offered by professor
            if (reviews.isEmpty()) {
                res.redirect(Application.AUTHORIZATIONERROR_PATH);
                halt();
            }
            // lists all reviews which will be use when rendering the page
            model.put("courses", reviews);

            // handles retrieving, calculating, and putting ratings
            handleRatings(model, reviewDao, prof, display);

            // puts professor information for use on the page
            model.put("prof_first_name", prof.getFirst_name());
            model.put("prof_last_name", prof.getLast_name());
            model.put("prof_id", prof.getId());

            // puts announcements on the page
            AnnouncementDao ad = DaoManager.getInstance().getAnnouncementDao();
            List<Announcement> announcements = ad.all();
            model.put("announcements", announcements);
        }

        // Tell the server to render the index page with the data in the model
        return new ModelAndView(model, "home/professor.hbs");
    }

    /**
     * Put route method
     *
     * @param req
     * @param res
     * @return
     */
    public static ModelAndView display(Request req, Response res) {
        HashMap<String, Object> model = new HashMap<>();

        model.put("course_id", req.params("course_id"));

        return new ModelAndView(model, "/professor.hbs");
    }

    /**
     * Flags potentially offensive comment
     *
     * @param req
     * @param res
     * @return
     */
    public static String flag(Request req, Response res) {
        ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();

        // grabs course id from review comment
        String idString = req.queryParams("course_flagged");
        String display = req.params("display");
        long id = Long.parseLong(idString);

        // sets the comment flagged in the database
        ProfessorReview review = reviewDao.findReview(id);
        reviewDao.setCommentFlagged(review);
        // redirects back to same professor page
        res.redirect(Application.HOME_PATH + "professor/" + req.params("professor_id") + "/" + display);
        halt();
        return "";
    }

    /**
     * Calculates average ratings per category, creates an object of arrays
     * which store counts of each rating (1,2,3,4,5), then formats the results
     * and puts them into the model for display.
     *
     * @param model
     * @param reviewDao
     * @param prof
     * @param display
     */
    private static void handleRatings(HashMap<String, Object> model, ProfessorReviewDao reviewDao, User prof, String display) {
        // obtains average rating per category of the professor
        double objectives = reviewDao.avgRate(prof, "rate_objectives", display);
        double organized = reviewDao.avgRate(prof, "rate_organized", display);
        double challenging = reviewDao.avgRate(prof, "rate_challenging", display);
        double outside = reviewDao.avgRate(prof, "rate_outside_work", display);
        double pace = reviewDao.avgRate(prof, "rate_pace", display);
        double assignments = reviewDao.avgRate(prof, "rate_assignments", display);
        double grade_fairly = reviewDao.avgRate(prof, "rate_grade_fairly", display);
        double grade_time = reviewDao.avgRate(prof, "rate_grade_time", display);
        double accessibility = reviewDao.avgRate(prof, "rate_accessibility", display);
        double knowledge = reviewDao.avgRate(prof, "rate_knowledge", display);
        double career = reviewDao.avgRate(prof, "rate_career_development", display);
        double overall = ((objectives + organized + challenging + outside + pace + assignments + grade_fairly
                + grade_time + accessibility + knowledge + career) / 11);

        // stores all ratings in a rating model
        AllRatingsModel ratingModel = new AllRatingsModel();
        int[] arr_objectives = ratingModel.getObjectives(prof, display);
        int[] arr_organized = ratingModel.getOrganized(prof, display);
        int[] arr_challenging = ratingModel.getChallenging(prof, display);
        int[] arr_outside_work = ratingModel.getOutside_work(prof, display);
        int[] arr_pace = ratingModel.getPace(prof, display);
        int[] arr_assignments = ratingModel.getAssignments(prof, display);
        int[] arr_grade_fairly = ratingModel.getGrade_fairly(prof, display);
        int[] arr_grade_time = ratingModel.getGrade_time(prof, display);
        int[] arr_accessibility = ratingModel.getAccessibility(prof, display);
        int[] arr_knowledge = ratingModel.getKnowledge(prof, display);
        int[] arr_career_development = ratingModel.getCareer_development(prof, display);

        // puts all ratings individually for use in charts
        DecimalFormat df = new DecimalFormat("0.#");
        model.put("rate_objective", df.format(objectives));
        model.put("arr_objectives1", arr_objectives[0]);
        model.put("arr_objectives2", arr_objectives[1]);
        model.put("arr_objectives3", arr_objectives[2]);
        model.put("arr_objectives4", arr_objectives[3]);
        model.put("arr_objectives5", arr_objectives[4]);

        model.put("rate_organized", df.format(organized));
        model.put("arr_organized1", arr_organized[0]);
        model.put("arr_organized2", arr_organized[1]);
        model.put("arr_organized3", arr_organized[2]);
        model.put("arr_organized4", arr_organized[3]);
        model.put("arr_organized5", arr_organized[4]);

        model.put("rate_challenging", df.format(challenging));
        model.put("arr_challenging1", arr_challenging[0]);
        model.put("arr_challenging2", arr_challenging[1]);
        model.put("arr_challenging3", arr_challenging[2]);
        model.put("arr_challenging4", arr_challenging[3]);
        model.put("arr_challenging5", arr_challenging[4]);

        model.put("rate_outside_work", df.format(outside));
        model.put("arr_outside_work1", arr_outside_work[0]);
        model.put("arr_outside_work2", arr_outside_work[1]);
        model.put("arr_outside_work3", arr_outside_work[2]);
        model.put("arr_outside_work4", arr_outside_work[3]);
        model.put("arr_outside_work5", arr_outside_work[4]);

        model.put("rate_pace", df.format(pace));
        model.put("arr_pace1", arr_pace[0]);
        model.put("arr_pace2", arr_pace[1]);
        model.put("arr_pace3", arr_pace[2]);
        model.put("arr_pace4", arr_pace[3]);
        model.put("arr_pace5", arr_pace[4]);

        model.put("rate_assignments", df.format(assignments));
        model.put("arr_assignments1", arr_assignments[0]);
        model.put("arr_assignments2", arr_assignments[1]);
        model.put("arr_assignments3", arr_assignments[2]);
        model.put("arr_assignments4", arr_assignments[3]);
        model.put("arr_assignments5", arr_assignments[4]);

        model.put("rate_grade_fairly", df.format(grade_fairly));
        model.put("arr_grade_fairly1", arr_grade_fairly[0]);
        model.put("arr_grade_fairly2", arr_grade_fairly[1]);
        model.put("arr_grade_fairly3", arr_grade_fairly[2]);
        model.put("arr_grade_fairly4", arr_grade_fairly[3]);
        model.put("arr_grade_fairly5", arr_grade_fairly[4]);

        model.put("rate_grade_time", df.format(grade_time));
        model.put("arr_grade_time1", arr_grade_time[0]);
        model.put("arr_grade_time2", arr_grade_time[1]);
        model.put("arr_grade_time3", arr_grade_time[2]);
        model.put("arr_grade_time4", arr_grade_time[3]);
        model.put("arr_grade_time5", arr_grade_time[4]);

        model.put("rate_accessibility", df.format(accessibility));
        model.put("arr_accessibility1", arr_accessibility[0]);
        model.put("arr_accessibility2", arr_accessibility[1]);
        model.put("arr_accessibility3", arr_accessibility[2]);
        model.put("arr_accessibility4", arr_accessibility[3]);
        model.put("arr_accessibility5", arr_accessibility[4]);

        model.put("rate_knowledge", df.format(knowledge));
        model.put("arr_knowledge1", arr_knowledge[0]);
        model.put("arr_knowledge2", arr_knowledge[1]);
        model.put("arr_knowledge3", arr_knowledge[2]);
        model.put("arr_knowledge4", arr_knowledge[3]);
        model.put("arr_knowledge5", arr_knowledge[4]);

        model.put("rate_career_development", df.format(career));
        model.put("arr_career_development1", arr_career_development[0]);
        model.put("arr_career_development2", arr_career_development[1]);
        model.put("arr_career_development3", arr_career_development[2]);
        model.put("arr_career_development4", arr_career_development[3]);
        model.put("arr_career_development5", arr_career_development[4]);

        // overall score of professor
        model.put("overall", df.format(overall));
    }

    /**
     * Pass in a professor user and this will return their overall rating
     *
     * @param prof
     * @return
     */
    public static double getOverall(User prof) {
        if (prof.getRole() == 2) {
            ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
            DecimalFormat df = new DecimalFormat("0.#");

            double objectives = reviewDao.avgRate(prof, "rate_objectives", "overview");
            double organized = reviewDao.avgRate(prof, "rate_organized", "overview");
            double challenging = reviewDao.avgRate(prof, "rate_challenging", "overview");
            double outside = reviewDao.avgRate(prof, "rate_outside_work", "overview");
            double pace = reviewDao.avgRate(prof, "rate_pace", "overview");
            double assignments = reviewDao.avgRate(prof, "rate_assignments", "overview");
            double grade_fairly = reviewDao.avgRate(prof, "rate_grade_fairly", "overview");
            double grade_time = reviewDao.avgRate(prof, "rate_grade_time", "overview");
            double accessibility = reviewDao.avgRate(prof, "rate_accessibility", "overview");
            double knowledge = reviewDao.avgRate(prof, "rate_knowledge", "overview");
            double career = reviewDao.avgRate(prof, "rate_career_development", "overview");

            double overall = ((objectives + organized + challenging + outside + pace + assignments + grade_fairly
                    + grade_time + accessibility + knowledge + career) / 11);

            return Double.parseDouble(df.format(overall));
        } else {
            return 0;
        }

    }
}
