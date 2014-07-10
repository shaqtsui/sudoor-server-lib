package net.gplatform.sudoor.server.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SSAuthentication {
	@Autowired
	private static AuthenticationManager authenticationManager;

	public void authenticate(String username, String password) {
		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}
}
