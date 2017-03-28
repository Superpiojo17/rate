package edu.ben.rate_review.policy;

import edu.ben.rate_review.authorization.AuthorizationUser;

public class AuthPolicyManager {
	private static AuthPolicyManager instance = null;
	private AuthorizationUser currentUser = null;

	public AuthPolicyManager() {
		
	}

	public static AuthPolicyManager getInstance() {
		if (instance == null) {
			instance = new AuthPolicyManager();
		}
		return instance;
	}

	public void setCurrentUser(AuthorizationUser currentUser) {
		this.currentUser = currentUser;
	}

	public static void currentUser(AuthorizationUser currentUser) {
		if (instance != null) {
			instance.setCurrentUser(currentUser);
		}
	}

	public UserPolicy getUserPolicy() {
		return new UserPolicy(currentUser);
	}
}
