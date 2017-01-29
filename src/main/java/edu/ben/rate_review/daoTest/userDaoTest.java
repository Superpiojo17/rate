//package edu.ben.rate_review.daoTest;
//
//import static org.junit.Assert.*;
//
//import java.sql.SQLException;
//
//import org.junit.Test;
//
//import edu.ben.rate_review.daos.UserDao;
//import edu.ben.rate_review.models.User;
//
//public class userDaoTest {
//
//	// accountID, Balance, Deposits,Withdrawals,active
//	User user1 = new User("Daniel", "Nohl", "dnohl@gmail.com", "123", 1);
//	
//
//	@Test
//	public void testAddAccount() throws SQLException {
//		// Add 2 users
//		aDao.addAccount(user1);
//		aDao.addAccount(user2);
//
//		boolean expected = true;
//		boolean actual = aDao.accountIDExists(user1);
//		assertEquals(expected, actual);
//
//		// Check if the second exists and add another.
//		boolean actual2 = aDao.accountIDExists(user2);
//		boolean expected2 = true;
//		assertEquals(expected2, actual2);
//		aDao.addAccount(user3);
//
//	}
//
//}
