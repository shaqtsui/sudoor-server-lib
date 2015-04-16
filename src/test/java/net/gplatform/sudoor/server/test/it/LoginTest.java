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

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = Application.class)
//@WebIntegrationTest
public class LoginTest {

	static Client client = null;

	@BeforeClass
	public static void init() {
		client = ClientBuilder.newBuilder().build();
		client.register(JacksonJsonProvider.class);
	}

	String url = "http://localhost:8080/sudoor-server-lib";

	@Test
	public void login() {

		WebTarget signin = client.target(url + "/login");
		//WebTarget signin = client.target(url + "/data/odata.svc/$metadata");
		Form f = new Form();
		f.param("username", "admin");
		f.param("password", "admin");
		Response signinResponse = signin.request(MediaType.WILDCARD_TYPE).post(Entity.form(f));
		assert (signinResponse.getStatus() == 302);

		
		WebTarget target = client.target(url + "/data/ws/rest").path("/sudoor/SpringSecurity/Authentication");
		Builder authenticationRequestBuilder = target.request(MediaType.WILDCARD_TYPE);
		
		TestUtils.copyCookies(authenticationRequestBuilder, signinResponse);
		
		Response response = authenticationRequestBuilder.get();
		int statusCode = response.getStatus();
		assert (statusCode == 200);

		Map result = response.readEntity(Map.class);
		assert ("ROLE_ADMIN".equals(((Map)((java.util.List)result.get("authorities")).get(0)).get("authority")));

	}
	
	public static void main(String[] args) {
		String pw ="admin";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.print(passwordEncoder.encode(pw));
	}

}
