package net.gplatform.sudoor.server.test.unit;

import net.gplatform.sudoor.server.Application;
import net.gplatform.sudoor.server.integration.FtpMessageGateway;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest
public class FtpMessageGatewayTest {

	@Autowired
	FtpMessageGateway ftpMessageGateway;

	@Test
	public void test() {
		boolean res = true;
		try {
			org.springframework.messaging.Message<String> msg = MessageBuilder.withPayload("test").setHeader("file_name", "testftp.txt").build();
			ftpMessageGateway.sendFtpMessage(msg);
		} catch (Exception e) {
			e.printStackTrace();
			res = false;
		}
		//assert (res);

	}
}
