package net.gplatform.sudoor.server.test.it;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.gplatform.sudoor.server.Application;

import org.junit.BeforeClass;
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
public class SpringSecurityResourceTest {
	@Autowired
	TestUtils testUtils;

	public static Client client;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
	}

	/**
	 * Post login can not be tested here as we can not send the cookie
	 */
	@Test
	public void testSpringSecurityAuthenticationPreLogin() {
		WebTarget target = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path(
				"/sudoor/SpringSecurity/Authentication");
		Response response = target.request(MediaType.WILDCARD_TYPE).get();
		int statusCode = response.getStatus();
		assert (statusCode == 204);
	}

}
