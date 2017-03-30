package edu.ben.rate_review.daoTest;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import edu.ben.rate_review.daos.DaoManager;
import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;

public class userDaoTest {

	public userDaoTest() throws Exception {
		// This "hack" runs the public constructor which is what does yoru DB setup and makes the instance().
		new DaoManager();
	}

	@Test
	public void testAddUser() throws SQLException {
		// Use normally
		UserDao ud = DaoManager.getInstance().getUserDao();
		ud.findById(6);
	}

}
