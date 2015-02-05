package net.gplatform.sudoor.server.test.it;

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
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest
public class LoginTest {
	
	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
	}

	String url = "http://localhost:8080/sudoor-server-lib";
	
	@Test
	public void login() {

		WebTarget signin = client.target(url + "/login");
		Response signinResponse = signin.request(MediaType.WILDCARD_TYPE).get();
		Map<String, NewCookie> signinCookies = signinResponse.getCookies();
		assert (signinResponse.getStatus() == 200);

/*		String pageCaptcha = "abc";
		WebTarget validateTarget = client.target(url + "/data/ws/rest").path("/sudoor/captcha/validate")
				.queryParam("_captcha", pageCaptcha);
		Builder validateBuilder = validateTarget.request(MediaType.WILDCARD_TYPE);
		for (Iterator iterator = signinCookies.values().iterator(); iterator.hasNext();) {
			Cookie cookie = (Cookie) iterator.next();
			validateBuilder.cookie(cookie);
		}
		Response validateResponse = validateBuilder.get();
		assert (validateResponse.getStatus() == 200);

		boolean res = validateResponse.readEntity(boolean.class);
		assert (res == false);*/
	}

}
