package net.gplatform.sudoor.server.test.it;

import net.gplatform.sudoor.server.Application;
import net.gplatform.sudoor.server.integration.EventMessageGateway;

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
public class EventMessageGatewayTest {

	@Autowired
	EventMessageGateway eventMessageGateway;

	@Test
	public void test() {
		try {
			eventMessageGateway.publishEvent("TestPublisher");
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println("Complete");

	}

}
