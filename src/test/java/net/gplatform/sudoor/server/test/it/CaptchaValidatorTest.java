package net.gplatform.sudoor.server.test.it;

import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@EnableTransactionManagement
@IntegrationTest
public class CaptchaValidatorTest {

	@Autowired
	TestUtils testUtils;

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
	}

	@Test
	public void validateCaptchaFalse() {

		WebTarget captchaImgTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/app/sudoor/captcha-image.html");
		Response captchaImgResponse = captchaImgTarget.request(MediaType.WILDCARD_TYPE).get();
		Map<String, NewCookie> captchaImgCookies = captchaImgResponse.getCookies();
		assert (captchaImgResponse.getStatus() == 200);

		String pageCaptcha = "abc";
		WebTarget validateTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/sudoor/captcha/validate")
				.queryParam("_captcha", pageCaptcha);
		Builder validateBuilder = validateTarget.request(MediaType.WILDCARD_TYPE);
		for (Iterator iterator = captchaImgCookies.values().iterator(); iterator.hasNext();) {
			Cookie cookie = (Cookie) iterator.next();
			validateBuilder.cookie(cookie);
		}
		Response validateResponse = validateBuilder.get();
		assert (validateResponse.getStatus() == 200);

		boolean res = validateResponse.readEntity(boolean.class);
		assert (res == false);
	}

}
