package edu.ben.rate_review.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AnnouncementForm {

	private String announcement_content;
	private String date;
	private long id;

	private String formatdate;

	public AnnouncementForm() {
		super();
	}

	public AnnouncementForm(Announcement announcement) {
		// set your stuff
		this.setAnnouncement_content(announcement.getAnnouncement_content());
		this.setDate(announcement.getDate());
		this.setId(announcement.getId());
		this.setFormatdate(announcement.getDate());
	}

	public String getDate() {
		System.out.println(date);
		return date;
	}

	public void setDate(String date) {

		this.date = date;
	}

	public String getAnnouncement_content() {
		return announcement_content;
	}

	public void setAnnouncement_content(String announcement_content) {
		this.announcement_content = announcement_content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFormatdate() {
		return formatdate;
	}

	public void setFormatdate(String formatdate) {
		SimpleDateFormat fromUser = new SimpleDateFormat("MM/dd/yy");
		SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
			formatdate = myFormat.format(fromUser.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.formatdate = formatdate;
	}

}
