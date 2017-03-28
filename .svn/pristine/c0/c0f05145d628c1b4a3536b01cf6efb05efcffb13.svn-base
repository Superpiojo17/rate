package edu.ben.rate_review.daoTest;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import edu.ben.rate_review.models.UserForm;

public class EditDaoTest {

	public EditDaoTest() throws Exception {
		new DaoManager();
	}

	@Test
	public void testAddUser() throws SQLException {
		UserForm user1 = new UserForm();
		// Use normally
		UserDao ud = DaoManager.getInstance().getUserDao();
		user1.setEmail("chanceraps@gmail.com");
		user1.setFirst_name("Joel");
		user1.setLast_name("Sandoval");
		user1.setMajor("Rap");
		user1.setRole(1);
		user1.setSchool_year(1);
		user1.setId(2);
		

		ud.updateUser(user1);

		User u = ud.findById(2);
		String actual = "chanceraps@gmail.com";
		String expected = u.getEmail();

		assertEquals(expected, actual);

	}

}
