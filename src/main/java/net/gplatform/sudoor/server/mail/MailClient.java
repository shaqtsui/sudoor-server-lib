package net.gplatform.sudoor.server.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailClient {

	@Value("${spring.mail.subject}")
	String subject;

	@Value("${spring.mail.username}")
	String from;

	@Autowired
	private JavaMailSender mailSender;

	public String send(String to, String msg) throws MessagingException {
		return send(to, msg, false);
	}

	public String send(String to, String msg, boolean isHtml) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setTo(to);
		mimeMessageHelper.setFrom(from);
		mimeMessageHelper.setText(msg, isHtml);
		mailSender.send(mimeMessage);
		return "SUCCESS";
	}

}
