package edu.ben.rate_review.controller.user;

import java.util.HashMap;

import edu.ben.rate_review.app.Application;
import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.email.Email;
import edu.ben.rate_review.encryption.SecurePassword;
import edu.ben.rate_review.models.RecoveringUser;
import edu.ben.rate_review.models.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;

import static spark.Spark.halt;

/**
 * Account Recovery controller
 *
 * @author Mike
 * @version 2-4-2017
 */
public class AccountRecoveryController {
    /**
     * Displays view for account recovery page
     *
     * @param req
     * @param res
     * @return
     */
    public static ModelAndView showAccountRecoveryEmailPage(Request req, Response res) {
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
        }
        // Tell the server to render the index page with the data in the model
        return new ModelAndView(model, "home/accountrecovery.hbs");
    }

    /**
     * Displays view for new info page
     *
     * @param req
     * @param res
     * @return
     */
    public static ModelAndView showAccountRecoveryNewInfoPage(Request req, Response res) {
        // Just a hash to pass data from the servlet to the page
        HashMap<String, Object> model = new HashMap<>();
        // Tell the server to render the index page with the data in the model
        return new ModelAndView(model, "home/newinfo.hbs");
    }

    /**
     * Method which is called after submitting the new info form. Recovers a
     * user's lost account
     *
     * @param req
     * @param res
     * @return
     */
    public static String recoverAccount(Request req, Response res) {

        UserDao userDao = DaoManager.getInstance().getUserDao();
        User user = new User();
        RecoveringUser rUser = new RecoveringUser();
        // checks account recovery for outdated requests and deletes them
        userDao.removeOldRecoveryRequest();

        if (!req.queryParams("email").isEmpty() && !req.queryParams("temp_password").isEmpty()
                && !req.queryParams("new_password").isEmpty() && !req.queryParams("verify_password").isEmpty()) {
            // enters if no fields are empty
            if (req.queryParams("new_password").equals(req.queryParams("verify_password"))) {
                // enters if new password matches verify password
                user = userDao.findByEmail(req.queryParams("email"));
                if (user != null) {
                    // enters if user is found
                    rUser = userDao.recoveryFindByEmail(user.getEmail());
                    if (rUser != null) {
                        // enters if recovering user is found
                        if (SecurePassword.getCheckPassword(req.queryParams("temp_password"), rUser.getTempPass())) {
                            // assigns new password to user
                            user.setPassword(SecurePassword.getHashPassword(req.queryParams("new_password")));
                            // updates user's password
                            userDao.updatePassword(user);
                            // removes requested temporary password
                            userDao.removeRecoveryRequest(user);
                            res.redirect(Application.LOGIN_PATH);
                            halt();

                        }
                    }
                }
            }
        }

        // all forms must be filled out
        res.redirect(Application.NEWINFO_PATH);
        halt();


        return "";
    }

    /**
     * Finds user's account in database, calls method to send user a new
     * password, and redirects to new location.
     *
     * @param req
     * @param res
     * @return
     */
    public static String enterEmailRecoverAccount(Request req, Response res) {

        if (!req.queryParams("email").isEmpty()) {
            UserDao userDao = DaoManager.getInstance().getUserDao();
            User user = new User();
            RecoveringUser rUser = new RecoveringUser();
            user = userDao.findByEmail(req.queryParams("email"));
            if (user != null) {
                String tempPass = sendRecoveryEmail(user);
                // checks to see if user already has request in table
                rUser = userDao.recoveryFindByEmail(user.getEmail());
                if (rUser != null) {
                    // if previous request in table, deletes
                    userDao.removeRecoveryRequest(user);
                }
                // creates entry for user attempted to recover account
                userDao.storeTempPassword(user, tempPass);
                res.redirect(Application.NEWINFO_PATH);
                halt();
            } else {
                // user not found
                res.redirect(Application.ACCOUNTRECOVERY_PATH);
                halt();
            }
        } else {
            // one or more fields was empty
            res.redirect(Application.ACCOUNTRECOVERY_PATH);
            halt();
        }

        return "";
    }

    /**
     * Sends recovery email to the user which contains a temporary password and
     * the link in which to enter it. Now used HTML
     *
     * @param user
     */
    private static String sendRecoveryEmail(User user) {

        String tempPassword = createTempPass(user);

        String subject = "Rate&Review Account Recovery";
        String messageHeader = "<p>Hello " + user.getFirst_name() + ",</p><br />";
        String messageBody = "<p>This message is to confirm that you have requested"
                + " to recover your account. If you did not request an account recovery,"
                + " please change your password, as your account may have been compromised. "
                + "To proceed, use the provided temporary password here: " + "<a href=\"http://" + Application.DOMAIN + "/newinfo" + "\">Rate & Review</a></p>";
        String temporaryPassword = "<p>Temporary password: " + tempPassword + "</p>";
        String messageFooter = "<br /><p>Sincerely,</p><p>The Rate&Review Team</p>";
        String message = messageHeader + messageBody + temporaryPassword + messageFooter;
        System.out.println(tempPassword);
        Email.deliverEmail(user.getFirst_name(), user.getEmail(), subject, message);
        return tempPassword;
    }


    /**
     * Generates a temporary password for the user
     *
     * @param user
     * @return
     */
    private static String createTempPass(User user) {
        String tempPass = "";
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
        String[] lowerCase = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
                "s", "t", "u", "v", "w", "x", "y", "z"};
        String[] upperCase = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                "S", "T", "U", "V", "W", "X", "Y", "Z"};

        for (int i = 0; i < 10; i++) {
            int rand = (int) (1 + (Math.random() * 4));
            int selectChar;

            switch (rand) {
                case 1:
                case 2:
                    selectChar = (int) (Math.random() * 26);
                    tempPass = tempPass + lowerCase[selectChar];
                    break;
                case 3:
                    selectChar = (int) (Math.random() * 26);
                    tempPass = tempPass + upperCase[selectChar];
                    break;
                default:
                    selectChar = (int) (Math.random() * 10);
                    tempPass = tempPass + numbers[selectChar];
            }

        }

        return tempPass;
    }

}
