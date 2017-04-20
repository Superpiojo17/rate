package edu.ben.rate_review.models;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;

/**
 * Stores the counts of all the ratings of one professor
 * 
 * @author Mike
 * @version 3-32017
 */
public class AllRatingsModel {

	DaoManager dao = DaoManager.getInstance();
	int[] objectives = new int[5];
	int[] organized = new int[5];
	int[] challenging = new int[5];
	int[] outside_work = new int[5];
	int[] pace = new int[5];
	int[] assignments = new int[5];
	int[] grade_fairly = new int[5];
	int[] grade_time = new int[5];
	int[] accessibility = new int[5];
	int[] knowledge = new int[5];
	int[] career_development = new int[5];

	public int[] getObjectives(User professor, String display) {
		String column = "rate_objectives";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			objectives[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return objectives;
	}

	public int[] getOrganized(User professor, String display) {
		String column = "rate_organized";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			organized[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return organized;
	}

	public int[] getChallenging(User professor, String display) {
		String column = "rate_challenging";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			challenging[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return challenging;
	}

	public int[] getOutside_work(User professor, String display) {
		String column = "rate_outside_work";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			outside_work[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return outside_work;
	}

	public int[] getPace(User professor, String display) {
		String column = "rate_pace";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			pace[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return pace;
	}

	public int[] getAssignments(User professor, String display) {
		String column = "rate_assignments";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			assignments[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return assignments;
	}

	public int[] getGrade_fairly(User professor, String display) {
		String column = "rate_grade_fairly";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			grade_fairly[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return grade_fairly;
	}

	public int[] getGrade_time(User professor, String display) {
		String column = "rate_grade_time";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			grade_time[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return grade_time;
	}

	public int[] getAccessibility(User professor, String display) {
		String column = "rate_accessibility";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			accessibility[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return accessibility;
	}

	public int[] getKnowledge(User professor, String display) {
		String column = "rate_knowledge";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			knowledge[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return knowledge;
	}

	public int[] getCareer_development(User professor, String display) {
		String column = "rate_career_development";
		ProfessorReviewDao rDao = dao.getProfessorReviewDao();

		for (int i = 0; i < 5; i++) {
			career_development[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		rDao.close();
		return career_development;
	}

}
