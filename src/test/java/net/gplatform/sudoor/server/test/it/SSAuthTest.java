package net.gplatform.sudoor.server.test.it;

import net.gplatform.sudoor.server.Application;
import net.gplatform.sudoor.server.security.model.auth.SSAuth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SSAuthTest {
	@Autowired
	SSAuth SSAuth;

	@Test
	public void test() {
		SSAuth.register("Shark", "Shark");

		SSAuth.authenticate("Shark", "Shark");

		String currentUserName = SSAuth.getCurrentUser();
		
		assert (currentUserName.equals("Shark"));
	}
}
