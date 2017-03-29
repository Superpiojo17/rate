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

	public int[] getObjectives(User professor, String display) {
		String column = "rate_objectives";

		for (int i = 0; i < 5; i++) {
			objectives[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return objectives;
	}

	public int[] getOrganized(User professor, String display) {
		String column = "rate_organized";

		for (int i = 0; i < 5; i++) {
			organized[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return organized;
	}

	public int[] getChallenging(User professor, String display) {
		String column = "rate_challenging";

		for (int i = 0; i < 5; i++) {
			challenging[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return challenging;
	}

	public int[] getOutside_work(User professor, String display) {
		String column = "rate_outside_work";

		for (int i = 0; i < 5; i++) {
			outside_work[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return outside_work;
	}

	public int[] getPace(User professor, String display) {
		String column = "rate_pace";

		for (int i = 0; i < 5; i++) {
			pace[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return pace;
	}

	public int[] getAssignments(User professor, String display) {
		String column = "rate_assignments";

		for (int i = 0; i < 5; i++) {
			assignments[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return assignments;
	}

	public int[] getGrade_fairly(User professor, String display) {
		String column = "rate_grade_fairly";
		int[] grade_fairly = new int[5];

		for (int i = 0; i < 5; i++) {
			grade_fairly[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return grade_fairly;
	}

	public int[] getGrade_time(User professor, String display) {
		String column = "rate_grade_time";

		for (int i = 0; i < 5; i++) {
			grade_time[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return grade_time;
	}

	public int[] getAccessibility(User professor, String display) {
		String column = "rate_accessibility";

		for (int i = 0; i < 5; i++) {
			accessibility[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return accessibility;
	}

	public int[] getKnowledge(User professor, String display) {
		String column = "rate_knowledge";

		for (int i = 0; i < 5; i++) {
			knowledge[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return knowledge;
	}

	public int[] getCareer_development(User professor, String display) {
		String column = "rate_career_development";

		for (int i = 0; i < 5; i++) {
			career_development[i] = rDao.allRatings(professor, column, i + 1, display);
		}
		return career_development;
	}

}
