package edu.ben.rate_review.authorization;

public abstract class AuthorizationPolicy<T> {
	private AuthorizationUser currentUser = null;
	
	public AuthorizationPolicy(AuthorizationUser currentUser) {
		this.setCurrentUser(currentUser);
	}
	
	public abstract void list(T entity) throws Exception;
	
	/**
	 * Can the User create a new T?
	 * @param currentUser
	 * @param entity
	 * @throws Exception
	 */
	public abstract void create(T entity) throws AuthException;
	
	/**
	 * Can the User edit/update T?
	 * @param currentUser
	 * @param entity
	 * @throws Exception
	 */
	public abstract void edit(T entity) throws AuthException;
	
	/**
	 * Can the User delete/destroy T?
	 * @param currentUser
	 * @param entity
	 * @throws Exception
	 */
	public abstract void destroy(T entity) throws AuthException;
	
	/**
	 * Can the User view the contents of T
	 * @param currentUser
	 * @param entity
	 * @throws Exception
	 */
	public abstract void view(T entity) throws AuthException;

	public AuthorizationUser currentUser() {
		return currentUser;
	}

	public void setCurrentUser(AuthorizationUser currentUser) {
		this.currentUser = currentUser;
	}

	protected void deny(String message) throws AuthException {
		throw new AuthException(message);
	}
}
