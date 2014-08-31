package net.gplatform.sudoor.server.social.model;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

@Component
public class SimpleConnectionSignUp implements ConnectionSignUp {

	@Override
	public String execute(Connection<?> connection) {
		String userName = connection.fetchUserProfile().getUsername();
		return userName;
	}
}
