package net.gplatform.sudoor.server.auth.ws;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebService
public class AuthServiceImpl implements AuthService {
	@Autowired
	net.gplatform.sudoor.server.auth.SSAuthentication SSAuthentication;

	@Override
	public String authenticate(String username, String password) {
		SSAuthentication.authenticate(username, password);
		return "SUCCESS";
	}
}
