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

import net.gplatform.sudoor.server.Application;
import net.gplatform.sudoor.server.security.model.auth.SSAuth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class SSAuthTest {

	@Autowired
	SSAuth SSAuth;

	@Test
	public void testSSRegister() {
		SSAuth.register("Shark", "Shark");
		SSAuth.register("admin1", "admin1", new String[] { "ROLE_ADMIN" });
		SSAuth.authenticate("Shark", "Shark");
		SSAuth.authenticate("admin1", "admin1");
	}

	@Test
	public void testSSSignin() {
		SSAuth.register("Shark1", "Shark1");
		SSAuth.signin("Shark1", "Shark1");
	}

	@Test
	public void testIsUserExist() {
		SSAuth.register("Shark2", "Shark2");
		boolean res = SSAuth.isUserExist("Shark2");
		assert (res);
	}

	@Test
	public void testIsUserNotExist() {
		boolean res = SSAuth.isUserExist("NotExistUser");
		assert (!res);
	}

}
