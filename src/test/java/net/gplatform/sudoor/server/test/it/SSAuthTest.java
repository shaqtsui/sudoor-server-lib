package net.gplatform.sudoor.server.test.it;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.gplatform.sudoor.server.Application;
import net.gplatform.sudoor.server.security.model.auth.SSAuth;

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
public class SSAuthTest {
	public final String REST_SERVICE_URL = "http://localhost:8080/product/data/ws/rest";

	public final String ODATA_SERVICE_URL = "http://localhost:8080/product/data/odata.svc";

	static Client client = null;


	@Autowired
	SSAuth SSAuth;
	
	
	@BeforeClass
	public static void  init() {
		//register(JacksonFeatures.class).
		client = ClientBuilder.newBuilder().build();
	}
	
	@Test
	public void test() {
		SSAuth.register("Shark", "Shark");

		SSAuth.authenticate("Shark", "Shark");

		String currentUserName = SSAuth.getCurrentUser();
		
		assert (currentUserName.equals("Shark"));
	}
	
	@Test
	public void testIsUserExist() {

		boolean res = SSAuth.isUserExist("Shark");

		
		assert (res);
	}
	
	@Test
	public void testIsUserExist_No() {

		boolean res = SSAuth.isUserExist("SharkA");

		
		assert (res);
	}
	
	
	@Test
	public void testUserDetailsService() {
		boolean res = SSAuth.getUserDetailsManager().userExists("Shark");
	}
	
	@Test
	public void testSpringSecurity() {
		WebTarget target = client.target(REST_SERVICE_URL).path("/sudoor/SpringSecurity/Authentication");
		Response response = target.request(MediaType.WILDCARD_TYPE).get();
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);

		System.out.println("retrieveFile1() statusCode:" + statusCode);
		// System.out.println("retrieveFile1() content:" + content);
		assert (statusCode == 200);
	}
	
	
	
}
