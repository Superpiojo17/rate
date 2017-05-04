package edu.ben.rate_review.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Model for announcement form
 * 
 * @author Mike
 * @version 4-26-2017
 */
public class AnnouncementForm {

	private String announcement_content;
	private String date;
	private long id;
	private String formatdate;

	/**
	 * Base constructor
	 */
	public AnnouncementForm() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param announcement
	 */
	public AnnouncementForm(Announcement announcement) {
		// set your stuff
		this.setAnnouncement_content(announcement.getAnnouncement_content());
		this.setDate(announcement.getDate());
		this.setId(announcement.getId());
		this.setFormatdate(announcement.getDate());
	}

	/**
	 * Getter for date
	 * 
	 * @return
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Setter for date
	 * 
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * Getter for announcement content
	 * 
	 * @return
	 */
	public String getAnnouncement_content() {
		return announcement_content;
	}

	/**
	 * Setter for announcement content
	 * 
	 * @param announcement_content
	 */
	public void setAnnouncement_content(String announcement_content) {
		this.announcement_content = announcement_content;
	}

	/**
	 * Getter for ID
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter for ID
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter for formatted date
	 * 
	 * @return
	 */
	public String getFormatdate() {
		return formatdate;
	}

	/**
	 * Setter for formatted date
	 * 
	 * @param formatdate
	 */
	public void setFormatdate(String formatdate) {
		SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			if (date != null) {
				formatdate = myFormat.format(fromUser.parse(date));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.formatdate = formatdate;
	}

}
