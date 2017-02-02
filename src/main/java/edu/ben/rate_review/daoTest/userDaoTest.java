package edu.ben.rate_review.daoTest;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;

public class userDaoTest {

	User user1 = new User();
	
	UserDao uDao = new UserDao(null);
	

	@Test
	public void testAddUser() throws SQLException {
		user1.setEmail("chanceraps@gmail.com");
		String email = user1.getEmail();
		
		String expected = "chanceraps@gmail.com";
		User actual = uDao.findByEmail(email);
		String actualEmail = actual.getEmail();
		assertEquals(expected, actualEmail);
	}

}
