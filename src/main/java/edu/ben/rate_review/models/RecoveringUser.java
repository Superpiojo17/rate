package edu.ben.rate_review.models;

/**
 * Class to make RecoveringUser objects
 * 
 * @author Mike
 * @version 2-7-2017
 */
public class RecoveringUser {

	private String email;
	private String temp_password;

	/**
	 * Getter for email
	 * 
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter for email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter for temporary password
	 * 
	 * @return
	 */
	public String getTempPass() {
		return temp_password;
	}

	/**
	 * Setter for temporary password
	 * 
	 * @param temp_password
	 */
	public void setTempPass(String temp_password) {
		this.temp_password = temp_password;
	}
}
