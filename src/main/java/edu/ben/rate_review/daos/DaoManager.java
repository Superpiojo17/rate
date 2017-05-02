package edu.ben.rate_review.daos;

import java.sql.*;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.sql.DataSource;

public class DaoManager {

    // Private
    private MysqlDataSource src;
    private Connection con;
    private static DaoManager instance = null;
    // private static Logger logger = LoggerFactory.getLogger(DaoManager.class);

    /**
     * Constructor that will create the singleton instance if it doesn't exist
     * already
     *
     * @throws Exception
     */
    public DaoManager() throws Exception {
        final String HOST = System.getenv("DATABASE_HOST");
        final String USERNAME = System.getenv("DATABASE_USERNAME");
        final String PASSWORD = System.getenv("DATABASE_PASSWORD");
        final String DATABASE_NAME = System.getenv("DATABASE_NAME");
        if (instance == null) {
            try {
                MysqlDataSource mysql = new MysqlDataSource();
                mysql.setURL(HOST + "/" + DATABASE_NAME);
                mysql.setDatabaseName(DATABASE_NAME);
                mysql.setUser(USERNAME);
                mysql.setPassword(PASSWORD);
                this.src = mysql;
                instance = this;
            } catch (Exception e) {
                throw e;
            }
        }
    }


    /**
     * gets instance of user dao
     *
     * @return
     */
    public UserDao getUserDao() {
        try {
            return new UserDao(this.src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets instance of course dao
     *
     * @return
     */
    public CourseDao getCourseDao() {
        try {
            return new CourseDao(this.src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets instance of student in course dao
     *
     * @return
     */
    public StudentInCourseDao getStudentInCourseDao() {
        try {
            return new StudentInCourseDao(this.src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets instance of announcement dao
     *
     * @return
     */
    public AnnouncementDao getAnnouncementDao() {
        try {
            return new AnnouncementDao(this.src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets instance of tutor dao
     *
     * @return
     */
    public TutorDao getTutorDao() {
        try {
            return new TutorDao(this.src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * gets instance of professor review dao
     *
     * @return
     */
    public ProfessorReviewDao getProfessorReviewDao() {
        try {
            return new ProfessorReviewDao(this.src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Used to return the singleton instance
     *
     * @return
     */
    public static DaoManager getInstance() {
        return instance;
    }


}