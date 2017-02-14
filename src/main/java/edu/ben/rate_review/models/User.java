package edu.ben.rate_review.models;

import edu.ben.rate_review.authorization.AuthorizationUser;
import edu.ben.rate_review.encryption.SecurePassword;

public class User implements AuthorizationUser {
	private Long id;
	private String first_name;
	private String last_name;
	private String email;
	private String encryptedPassword;
	private String role_string;
	private int role;
	private boolean confirmed;
	private boolean active;

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

	public void setPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	@Override
	public int getRole() {
		return role;
	}

	@Override
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

	@Override
	public boolean hasRole(int role) {
		return this.role == role;
	}

	public String getRole_string() {
		return role_string;
	}

	public void setRole_string(String role_string) {
		if (role == 1) {
			role_string = "Admin";
		} else if (role == 2) {
			role_string = "Faculty";
		} else if (role == 3) {
			role_string = "Tutor";
		} else if (role == 4) {
			role_string = "Student";
		}

		this.role_string = role_string;
	}

}
