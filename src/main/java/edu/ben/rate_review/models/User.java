package edu.ben.rate_review.models;

import edu.ben.rate_review.encryption.SecurePassword;

public class User {
	private Long id;
	private String first_name;
	private String last_name;
	private String email;
	private String encryptedPassword;
	private int role;
	private boolean confirmed;
	private boolean active;

	public static final int ADMIN = 1;
	public static final int PROFESSOR = 2;
	public static final int TUTOR = 3;
	public static final int STUDENT = 4;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = SecurePassword.getHashPassword(encryptedPassword);
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
