package net.gplatform.sudoor.server.test.unit;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2015 Shark Xu
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class CaptchaValidatorTest {

	@Autowired
	TestUtils testUtils;

	static Client client = null;
	
	@Value("${sudoor.masterkey.value}")
	String masterKey = "test"; 

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
	}

	public void validateCaptcha(String inputCaptcha, boolean response) {

		WebTarget captchaImgTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/app/sudoor/captcha-image.html");
		Response captchaImgResponse = captchaImgTarget.request(MediaType.WILDCARD_TYPE).get();
		Map<String, NewCookie> captchaImgCookies = captchaImgResponse.getCookies();
		assert (captchaImgResponse.getStatus() == 200);

		WebTarget validateTarget = client.target(testUtils.getEmbeddedServletContainerBaseURL() + "/data/ws/rest").path("/sudoor/captcha/validate")
				.queryParam("_captcha", inputCaptcha);
		Builder validateBuilder = validateTarget.request(MediaType.WILDCARD_TYPE);
		for (Iterator iterator = captchaImgCookies.values().iterator(); iterator.hasNext();) {
			Cookie cookie = (Cookie) iterator.next();
			validateBuilder.cookie(cookie);
		}
		Response validateResponse = validateBuilder.get();
		assert (validateResponse.getStatus() == 200);

		boolean res = validateResponse.readEntity(boolean.class);
		assert (res == response);
	}
	
	@Test
	public void validateCaptchaFalse() {
		validateCaptcha(masterKey, true);
	}
	
	@Test
	public void validateCaptchaTrue() {
		validateCaptcha("false captcha", false);
	}

}
