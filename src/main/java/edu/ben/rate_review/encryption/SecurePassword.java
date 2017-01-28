package edu.ben.rate_review.encryption;

/**
 * Class which contains the methods which interact with BCrypt to hash and
 * decipher user passwords.
 * 
 * @author Mike
 * @version 1-26-2017
 */
public class SecurePassword {

	/**
	 * When registering, password is passed to this method. It will then encrypt
	 * the password and return the hashed password.
	 * 
	 * @param password
	 * @return
	 */
	private static String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * When logging in, this method checks that the entered password matches the
	 * stored hashed password.
	 * 
	 * @param password
	 * @param hashed_password
	 * @return
	 */
	private static boolean checkPassword(String password, String hashed_password) {
		return BCrypt.checkpw(password, hashed_password);
	}

	/**
	 * Getter for the hashPassword method.
	 * 
	 * @param password
	 * @return
	 */
	public static String getHashPassword(String password) {
		return hashPassword(password);
	}

	/**
	 * Getter for the checkPassword method.
	 * 
	 * @param password
	 * @param hashed_password
	 * @return
	 */
	public static boolean getCheckPassword(String password, String hashed_password) {
		return checkPassword(password, hashed_password);
	}
}
