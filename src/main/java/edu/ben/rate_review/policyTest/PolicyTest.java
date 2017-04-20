package edu.ben.rate_review.policyTest;

import org.junit.Test;

//import edu.ben.rate_review.daos.DaoManager;
//import edu.ben.rate_review.daos.UserDao;
import edu.ben.rate_review.models.User;
import junit.framework.Assert;
//import spark.Request;
//import spark.Session;

@SuppressWarnings("deprecation")
public class PolicyTest {

	@Test
	public void testAdminPolicy() {

		// boolean confirmed = false;
		// boolean active = true;
		//
		// User tmp = new User();
		// tmp.setFirst_name("Juan");
		// tmp.setLast_name("Legend");
		// tmp.setEmail("jlegend@ben.edu");
		// tmp.setPassword("321");
		// tmp.setRole(1);
		//
		// UserDao user = DaoManager.getInstance().getUserDao();
		// User newUser = new User();
		// newUser.setFirst_name(tmp.getFirst_name());
		// newUser.setLast_name(tmp.getLast_name());
		// newUser.setEmail(tmp.getEmail());
		// newUser.setEncryptedPassword(tmp.getEncryptedPassword());
		// newUser.setRole(tmp.getRole());
		// newUser.setConfirmed(confirmed);
		// newUser.setActive(active);
		//
		// session.attribute("current_user", newUser);
	}

	@Test
	public void checkSession() {
		// Request req = null;
		// Session session = req.session();

		boolean confirmed = false;
		boolean active = true;

		User tmp = new User();
		tmp.setFirst_name("Juan");
		tmp.setLast_name("Legend");
		tmp.setEmail("jlegend@ben.edu");
		tmp.setPassword("321");
		tmp.setRole(1);

		// UserDao user = DaoManager.getInstance().getUserDao();
		User newUser = new User();
		newUser.setFirst_name(tmp.getFirst_name());
		newUser.setLast_name(tmp.getLast_name());
		newUser.setEmail(tmp.getEmail());
		newUser.setEncryptedPassword(tmp.getEncryptedPassword());
		newUser.setRole(tmp.getRole());
		newUser.setConfirmed(confirmed);
		newUser.setActive(active);

		// session.attribute("current_user", newUser);

		boolean flag = false;

		// if(req.session().attribute("current_user") != null){
		// flag = true;
		// }

		// if(req.session().attribute("current_user") != null){
		// User u = (User) session.attribute("current_user");
		// if(u.getEmail() != null ){
		//
		// }
		// }

		boolean expected = true;
		boolean actual = flag;
		Assert.assertEquals(expected, actual);
	}

}
