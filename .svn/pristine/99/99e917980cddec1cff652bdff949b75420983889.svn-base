package edu.ben.rate_review.formatTime;

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
}
