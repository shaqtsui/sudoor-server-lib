package net.gplatform.sudoor.server.security.controller;

import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@WebService
public class AuthServiceImpl implements AuthService {
	@Autowired
	net.gplatform.sudoor.server.security.model.auth.SSAuth SSAuth;

	@Override
	public String authenticate(String username, String password) {
		SSAuth.authenticate(username, password);
		return "SUCCESS";
	}

	@Override
	public String register(String username, String password) {
		SSAuth.register(username, password);
		return "SUCCESS";
	}
}
