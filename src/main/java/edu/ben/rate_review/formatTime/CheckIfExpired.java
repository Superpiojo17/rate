package edu.ben.rate_review.formatTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Checks if a given date or semester is current or past
 * 
 * @author Mike
 * @version 4-5-2017
 */
public class CheckIfExpired {
	private final static int CURRENT_YEAR = 2017;
	private final static String CURRENT_SEMESTER = "Spring";

	/**
	 * Returns true if the semester given is current or upcoming. Returns false
	 * if the semester has ended.
	 * 
	 * @param semester
	 * @param year
	 * @return
	 */
	public static boolean checkSemesterCurrentOrUpcoming(String semester, int year) {

		if (year == CURRENT_YEAR) {
			if (CURRENT_SEMESTER.equals("Spring")) {
				return true;
			} else if (CURRENT_SEMESTER.equals("Summer")) {
				if (semester.equals("Summer") || semester.equals("Fall")) {
					return true;
				}
			} else {
				if (semester.equals("Fall")) {
					return true;
				}
			}
		} else if (year > CURRENT_YEAR) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if date is current or upcoming. Returns false if the date
	 * has past.
	 * 
	 * @param date
	 * @return
	 */
	public static boolean checkDateCurrentOrUpcoming(String date) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();

		String[] dateSplit = date.split("-");
		int year = Integer.parseInt(dateSplit[0]);
		int month = Integer.parseInt(dateSplit[1]);
		int day = Integer.parseInt(dateSplit[2]);

		String[] currentDateSplit = dateFormat.format(currentDate).split("-");
		int currentYear = Integer.parseInt(currentDateSplit[0]);
		int currentMonth = Integer.parseInt(currentDateSplit[1]);
		int currentDay = Integer.parseInt(currentDateSplit[2]);

		if (year > currentYear) {
			return true;
		} else if (year == currentYear) {
			if (month > currentMonth) {
				return true;
			} else if (month == currentMonth) {
				if (day >= currentDay) {
					return true;
				}
			}
		}

		return false;
	}
}
