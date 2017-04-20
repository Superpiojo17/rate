package edu.ben.rate_review.daoTest;

import java.util.List;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;
import edu.ben.rate_review.models.ProfessorReview;
import edu.ben.rate_review.models.User;

/**
 * Methods which test the new additions to the ProfessorReviewDao regarding
 * scores given to professors
 * 
 * @author Mike
 * @version 3-3-2017
 */
public class ProfessorReviewDaoTests {

	static User testUser = new User();

	/**
	 * Tests listCoursesByProfessorEmail method in the ProfessorReviewDao
	 */
	public static void listCoursesByProfessorEmailTest() {
		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		testUser.setEmail("fprofessor@ben.edu");
		List<ProfessorReview> testReviews = reviewDao.listCoursesByProfessorEmail(testUser, "overview");
		int count = 0;

		for (int i = 0; i < testReviews.size(); i++) {
			count++;
		}
		if (count == 2) {
			System.out.println("pass");
		} else {
			System.out.println("fail");
		}
		reviewDao.close();
	}

	/**
	 * Tests avgRate method in the ProfessorReviewDao
	 */
	public static void avgRateTest() {
		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		testUser.setEmail("fprofessor@ben.edu");
		String column = "rate_pace";
		double avg = reviewDao.avgRate(testUser, column, "overview");

		if (avg == 3) {
			System.out.println("pass");
		} else {
			System.out.println("fail");
		}
		reviewDao.close();
	}

	/**
	 * Tests allRatings method in the ProfessorReviewDao
	 */
	public static void allRatingsTest() {
		ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
		testUser.setEmail("fprofessor@ben.edu");
		String column = "rate_pace";
		int score = 5;

		if (reviewDao.allRatings(testUser, column, score, "overview") == 1) {
			System.out.println("pass");
		} else {
			System.out.println("fail");
		}
		reviewDao.close();
	}
}
