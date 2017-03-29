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

	static ProfessorReviewDao reviewDao = DaoManager.getInstance().getProfessorReviewDao();
	static User testUser = new User();

	/**
	 * Tests listCoursesByProfessorEmail method in the ProfessorReviewDao
	 */
	public static void listCoursesByProfessorEmailTest() {
		testUser.setEmail("fprofessor@ben.edu");
		List<ProfessorReview> testReviews = reviewDao.listCoursesByProfessorEmail(testUser);
		int count = 0;

		for (int i = 0; i < testReviews.size(); i++) {
			count++;
		}
		if (count == 2) {
			System.out.println("pass");
		} else {
			System.out.println("fail");
		}
	}

	/**
	 * Tests avgRate method in the ProfessorReviewDao
	 */
	public static void avgRateTest() {
		testUser.setEmail("fprofessor@ben.edu");
		String column = "rate_pace";
		double avg = reviewDao.avgRate(testUser, column);

		if (avg == 3) {
			System.out.println("pass");
		} else {
			System.out.println("fail");
		}
	}

	/**
	 * Tests allRatings method in the ProfessorReviewDao
	 */
	public static void allRatingsTest() {
		testUser.setEmail("fprofessor@ben.edu");
		String column = "rate_pace";
		int score = 5;

		// if (reviewDao.allRatings(testUser, column, score) == 1) {
		// System.out.println("pass");
		// } else {
		// System.out.println("fail");
		// }
	}
}
