package net.gplatform.sudoor.server.security.auth.ws;

import javax.jws.WebService;


@WebService
public interface AuthService {
	public String authenticate(String username, String password);
	public String register(String username, String password);
}