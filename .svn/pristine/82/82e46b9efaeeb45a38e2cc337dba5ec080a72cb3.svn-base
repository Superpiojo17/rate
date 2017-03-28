package edu.ben.rate_review.controller.sessionTest;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ben.rate_review.controller.session.SessionsController;

/**
 * Class for testing the validateName and validateEmail methods
 * 
 * @author Mike
 * @version 1-29-2017
 */
public class SessionsControllerTest {

	SessionsController test = new SessionsController();

	/**
	 * Checks that single character first name will be invalid
	 */
	@Test
	public void validateNameTest() {
		String first = "m";
		String last = "webb";
		assertFalse(test.getValidateName(first, last));
	}

	/**
	 * Checks that single character last name will be invalid
	 */
	@Test
	public void validateNameTest2() {
		String first = "mike";
		String last = "w";
		assertFalse(test.getValidateName(first, last));
	}

	/**
	 * Checks that empty strings will be invalid
	 */
	@Test
	public void validateNameTest3() {
		String first = "";
		String last = "";
		assertFalse(test.getValidateName(first, last));
	}

	/**
	 * Checks that white space strings will be invalid
	 */
	@Test
	public void validateNameTest4() {
		String first = " ";
		String last = " ";
		assertFalse(test.getValidateName(first, last));
	}

	/**
	 * Checks last name with symbols/numbers will be invalid
	 */
	@Test
	public void validateNameTest5() {
		String first = "mike";
		String last = "&*87^$%";
		assertFalse(test.getValidateName(first, last));
	}

	/**
	 * Checks first name with symbols/numbers will be invalid
	 */
	@Test
	public void validateNameTest6() {
		String first = "@#78%#$";
		String last = "webb";
		assertFalse(test.getValidateName(first, last));
	}

	/**
	 * Checks edge case for first name will be valid
	 */
	@Test
	public void validateNameTest7() {
		String first = "mi";
		String last = "webb";
		assertTrue(test.getValidateName(first, last));
	}

	/**
	 * Checks edge case for last name will be valid
	 */
	@Test
	public void validateNameTest8() {
		String first = "mike";
		String last = "we";
		assertTrue(test.getValidateName(first, last));
	}

	/**
	 * Checks full name will be valid
	 */
	@Test
	public void validateNameTest9() {
		String first = "mike";
		String last = "webb";
		assertTrue(test.getValidateName(first, last));
	}

	/**
	 * Checks that non-Benedictine email will be invalid
	 */
	@Test
	public void validateEmailTest() {
		String email = "mwebb@gmail.com";
		assertFalse(test.getValidateEmail(email));
	}

	/**
	 * Checks that just @ben.edu will be invalid
	 */
	@Test
	public void validateEmailTest3() {
		String email = "@ben.edu";
		assertFalse(test.getValidateEmail(email));
	}

	/**
	 * Checks edge case
	 */
	@Test
	public void validateEmailTest4() {
		String email = "m@ben.edu";
		assertTrue(test.getValidateEmail(email));
	}

	/**
	 * Checks full ben.edu email
	 */
	@Test
	public void validateEmailTest5() {
		String email = "b2125695@ben.edu";
		assertTrue(test.getValidateEmail(email));
	}

	/**
	 * Confirms the email must end in ben.edu
	 */
	@Test
	public void validateEmailTest6() {
		String email = "b2125695ben.edu";
		assertFalse(test.getValidateEmail(email));
	}

}
