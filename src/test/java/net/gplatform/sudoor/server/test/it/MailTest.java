package net.gplatform.sudoor.server.test.it;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.gplatform.sudoor.server.Application;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MailTest {

	@Autowired
	JavaMailSender mailSender;

	@Test
	public void test() throws MessagingException {
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
			mimeMessageHelper.setSubject("test");
			mimeMessageHelper.setTo("xfcjscn@163.com");
			mimeMessageHelper.setFrom("xfcjscn@163.com");
			mimeMessageHelper.setText("我", false);
			mailSender.send(mimeMessage);
		} catch (Exception e) {
		}

	}

}
