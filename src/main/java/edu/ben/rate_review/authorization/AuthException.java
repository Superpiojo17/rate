package edu.ben.rate_review.authorization;

/**
 * Authorization exception
 * @author Joel
 *
 */
public class AuthException extends Exception {

	private static final long serialVersionUID = -2597277126667624191L;

	public AuthException(String message) {
		super(message);
	}
}
