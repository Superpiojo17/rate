package edu.ben.rate_review.authorization;

public interface AuthorizationUser {
	public static final int ADMIN = 1;
	public static final int PROFESSOR = 2;
	public static final int TUTOR = 3;
	public static final int STUDENT = 4;
	
	/**
	 * Retrieves the role from the User
	 * @return The integer value of the role
	 */
	public int getRole();
	
	/**
	 * Determines if the user has a given role
	 * @param role The role to check
	 * @return T/F based on if the user has the role specified
	 */
	public boolean hasRole(int role);
	
	
	/**
	 * 
	 * @param role
	 */
	public void setRole(int role);
	
}
