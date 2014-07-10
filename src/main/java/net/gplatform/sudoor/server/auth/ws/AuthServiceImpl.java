package net.gplatform.sudoor.server.auth.ws;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

@Component
@WebService
public class AuthServiceImpl implements AuthService {

	@Override
	public String authenticate() {
		return "SUCCESS";
	}
}
