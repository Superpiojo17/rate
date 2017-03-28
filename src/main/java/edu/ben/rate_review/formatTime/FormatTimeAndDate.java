package edu.ben.rate_review.formatTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class which takes military time and formats into AM/PM
 * 
 * @author Mike
 * @version 3-17-2017
 */
public class FormatTimeAndDate {

	/**
	 * Formats time from military to AM/PM
	 * 
	 * @param unformattedTime
	 * @return
	 */
	public static String formatTime(String unformattedTime) {

		String[] time = unformattedTime.split(":");
		int hour = Integer.parseInt(time[0]);
		int minute = Integer.parseInt(time[1]);
		String amOrPm = "";
		String minuteString = "";

		if (hour >= 12) {
			if (hour > 12) {
				hour = hour - 12;
			}
			amOrPm = " PM";
		} else {
			if (hour == 0) {
				hour = hour + 12;
			}
			amOrPm = " AM";
		}

		if (minute < 10) {
			minuteString = "0" + minute;
		} else {
			minuteString += minute;
		}

		return hour + ":" + minuteString + amOrPm;
	}

	/**
	 * Formats the date into mm/dd/yyyy format
	 * 
	 * @param unformattedDate
	 * @return
	 */
	public static String formatDate(String unformattedDate) {

		String[] date = unformattedDate.split("-");
		String year = date[0];
		String month = date[1] + "/";
		String day = date[2] + "/";

		return month + day + year;
	}

	/**
	 * Validates time and date. Time must be between the hours of 8AM and 8PM.
	 * Date must be within the year, and take place on a day after the current
	 * day
	 * 
	 * @param time
	 * @param date
	 * @return
	 */
	public static boolean checkValidDateTime(String time, String date) {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();

		String[] timeSplit = time.split(":");
		String[] dateSplit = date.split("-");
		String[] currentDateSplit = dateFormat.format(currentDate).split("-");

		int hour = Integer.parseInt(timeSplit[0]);
		int minute = Integer.parseInt(timeSplit[1]);

		int year = Integer.parseInt(dateSplit[0]);
		int month = Integer.parseInt(dateSplit[1]);
		int day = Integer.parseInt(dateSplit[2]);

		int currentYear = Integer.parseInt(currentDateSplit[0]);
		int currentMonth = Integer.parseInt(currentDateSplit[1]);
		int currentDay = Integer.parseInt(currentDateSplit[2]);

		// checks for valid hours
		if (hour >= 8 && (hour < 20 || (hour == 20 && minute == 0))) {
			// checks for valid year - no more than 1 year in advance
			if (year >= currentYear && year - currentYear <= 1) {
				// checks for valid month
				if (month >= currentMonth || year > currentYear) {
					// checks for valid day
					if (day > currentDay || month > currentMonth || year > currentYear) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
