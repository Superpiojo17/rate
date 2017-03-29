package edu.ben.rate_review.models;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.ProfessorReviewDao;

/**
 * Stores the counts of all the ratings of one professor
 * 
 * @author Mike
 * @version 3-32017
 */
public class RatingsModel {

	ProfessorReviewDao rDao = DaoManager.getInstance().getProfessorReviewDao();
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

	public int[] getObjectives(User professor, long id) {
		String column = "rate_objectives";

		for (int i = 0; i < 5; i++) {
			objectives[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return objectives;
	}

	public int[] getOrganized(User professor, long id) {
		String column = "rate_organized";

		for (int i = 0; i < 5; i++) {
			organized[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return organized;
	}

	public int[] getChallenging(User professor, long id) {
		String column = "rate_challenging";

		for (int i = 0; i < 5; i++) {
			challenging[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return challenging;
	}

	public int[] getOutside_work(User professor, long id) {
		String column = "rate_outside_work";

		for (int i = 0; i < 5; i++) {
			outside_work[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return outside_work;
	}

	public int[] getPace(User professor, long id) {
		String column = "rate_pace";

		for (int i = 0; i < 5; i++) {
			pace[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return pace;
	}

	public int[] getAssignments(User professor, long id) {
		String column = "rate_assignments";

		for (int i = 0; i < 5; i++) {
			assignments[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return assignments;
	}

	public int[] getGrade_fairly(User professor, long id) {
		String column = "rate_grade_fairly";
		int[] grade_fairly = new int[5];

		for (int i = 0; i < 5; i++) {
			grade_fairly[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return grade_fairly;
	}

	public int[] getGrade_time(User professor, long id) {
		String column = "rate_grade_time";

		for (int i = 0; i < 5; i++) {
			grade_time[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return grade_time;
	}

	public int[] getAccessibility(User professor, long id) {
		String column = "rate_accessibility";

		for (int i = 0; i < 5; i++) {
			accessibility[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return accessibility;
	}

	public int[] getKnowledge(User professor, long id) {
		String column = "rate_knowledge";

		for (int i = 0; i < 5; i++) {
			knowledge[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return knowledge;
	}

	public int[] getCareer_development(User professor, long id) {
		String column = "rate_career_development";

		for (int i = 0; i < 5; i++) {
			career_development[i] = rDao.ratings(professor, column, i + 1, id);
		}
		return career_development;
	}

}
