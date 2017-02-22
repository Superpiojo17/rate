package edu.ben.rate_review.daoTest;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;

public class UserDaoTest {

	public UserDaoTest() throws Exception {
		new DaoManager();
	}

	@Test
	public void testAddUser() throws SQLException {
		User user1 = new User();
		// Use normally
		UserDao ud = DaoManager.getInstance().getUserDao();
		user1.setEmail("chanceraps@gmail.com");
		user1.setFirst_name("Joel");
		user1.setLast_name("Sandoval");
		user1.setEncryptedPassword(("Joel"));
		user1.setMajor("Rap");
		user1.setRole(1);
		
		ud.save(user1);
		
		User u = ud.findByEmail("chanceraps@gmail.com");
		String actual = "Joel";
		String expected = u.getFirst_name();
		
		assertEquals(expected, actual);
		
		
	}

}
