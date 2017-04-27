package edu.ben.rate_review.models;

/**
 * Model for announcements
 * 
 * @author Mike
 * @version 4-26-2017
 */
public class Announcement {
	private String announcement_content;
	private String date;
	private Long id;

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
	public Long getId() {
		return id;
	}

	/**
	 * Setter for ID
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
