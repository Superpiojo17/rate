package edu.ben.rate_review.models;

import edu.ben.rate_review.encryption.SecurePassword;

/**
 * Class to make RecoveringUser objects
 * 
 * @author Mike
 * @version 2-7-2017
 */
public class RecoveringUser {

	private String email;
	private String temp_password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTempPass() {
		return temp_password;
	}
	
	public void setEncryptedPassword(String temp_password) {
		this.temp_password = SecurePassword.getHashPassword(temp_password);
	}

	public void setTempPass(String temp_password) {
		this.temp_password = temp_password;
	}
}
