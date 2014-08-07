package net.gplatform.sudoor.server.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailClient {
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private SimpleMailMessage simpleMailMessage;

	public String send(String to, String msg) {
		simpleMailMessage.setTo(to);
		simpleMailMessage.setText(msg);
		mailSender.send(simpleMailMessage);
		return "SUCCESS";
	}

}
