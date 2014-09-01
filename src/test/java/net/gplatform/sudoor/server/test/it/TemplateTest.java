package net.gplatform.sudoor.server.test.it;

import net.gplatform.sudoor.server.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class TemplateTest {
	@Autowired
	TemplateEngine templateEngine;

	@Test
	public void test() {
		Context ctx = new Context();
		ctx.setVariable("key", "dyna content");
		String res = templateEngine.process("xml/paymentRequest", ctx);
		System.out.println(res);
	}

}
