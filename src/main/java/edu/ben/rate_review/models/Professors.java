package edu.ben.rate_review.models;

import edu.ben.rate_review.authorization.AuthorizationUser;
import edu.ben.rate_review.encryption.SecurePassword;

public class Professors implements AuthorizationUser {
	private Long id;
	private String first_name;
	private String last_name;
	private Object image;

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

	@Override
	public int getRole() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasRole(int role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setRole(int role) {
		// TODO Auto-generated method stub
		
	}



}