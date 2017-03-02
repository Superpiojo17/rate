package edu.ben.rate_review.models;

public class Announcement {
	private String announcement_content;
	private String date;
	private Long id;
	
	public String getDate() {
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	

}
