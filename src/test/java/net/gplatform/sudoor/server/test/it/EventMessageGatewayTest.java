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
import net.gplatform.sudoor.server.integration.AsyncEventMessageGateway;
import net.gplatform.sudoor.server.integration.EventMessageGateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class EventMessageGatewayTest {

	@Autowired
	EventMessageGateway eventMessageGateway;
	
	@Autowired
	AsyncEventMessageGateway asyncEventMessageGateway;

	@Test
	public void test() {
		boolean res = true;
		try {
			eventMessageGateway.publishEvent("TestPublisher");
		} catch (Exception e) {
			res = false;
		}
		assert (res);

	}
	
	@Test
	public void testAsync() {
		boolean res = true;
		try {
			asyncEventMessageGateway.publishEvent("AsyncTestPublisher");
		} catch (Exception e) {
			res = false;
		}
		assert (res);

	}


}
