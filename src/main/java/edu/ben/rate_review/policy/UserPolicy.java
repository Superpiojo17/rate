package edu.ben.rate_review.policy;

import edu.ben.rate_review.authorization.AuthException;
import edu.ben.rate_review.authorization.AuthorizationPolicy;
import edu.ben.rate_review.authorization.AuthorizationUser;
import edu.ben.rate_review.models.User;

public class UserPolicy extends AuthorizationPolicy<User> {

	public UserPolicy(AuthorizationUser currentUser) {
		super(currentUser);
	}

	public void list(User entity) throws Exception {
		if (currentUser() == null) {
			deny("You must be logged in to access this page");
		}
		// Method to see if the current user is allowed to view all of the users
		if (!currentUser().hasRole(AuthorizationUser.ADMIN) || entity == null) {
			deny("You are not authorized to be here!");
		}

	}

	public void showAdminDashboardPage() throws AuthException {
		if (currentUser() == null) {
			deny("You must be logged in to access this page");
		}
		if (!currentUser().hasRole(AuthorizationUser.ADMIN)) {
			deny("You are not authorized to be here!");
		}
	}

	public void showFacultyDashboardPage() throws AuthException {
		if (currentUser() == null) {
			deny("You must be logged in to access this page");
		}
		if (!currentUser().hasRole(AuthorizationUser.PROFESSOR)) {
			deny("You are not authorized to be here!");
		}
	}

	public void showTutorDashboardPage() throws AuthException {
		if (currentUser() == null) {
			deny("You must be logged in to access this page");
		}
		if (!currentUser().hasRole(AuthorizationUser.TUTOR)) {
			deny("You are not authorized to be here!");
		}

	}

	public void showStudentDashboardPage() throws AuthException {
		if (currentUser() == null) {
			deny("You must be logged in to access this page");
		}
		if (!currentUser().hasRole(AuthorizationUser.STUDENT)) {
			deny("You are not authorized to be here!");
		}
	}

	public void create(User entity) throws AuthException {
		// TODO Auto-generated method stub

	}

	public void edit(User entity) throws AuthException {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy(User entity) throws AuthException {
		// TODO Auto-generated method stub

	}

	@Override
	public void view(User entity) throws AuthException {
		// TODO Auto-generated method stub

	}

	// Not overridden because you only need this for the User stuff.
	public void someUserSpecificCustomPolicyYouMAyNEed(AuthorizationUser currentUser, User entity) {

	}

}
