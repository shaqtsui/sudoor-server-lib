package net.gplatform.sudoor.server.auth.ws;

import javax.jws.WebService;


@WebService
public interface AuthService {
	public String authenticate(String username, String password);
}
