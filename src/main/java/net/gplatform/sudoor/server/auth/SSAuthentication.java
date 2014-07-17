package net.gplatform.sudoor.server.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SSAuthentication {
	private Logger logger = LoggerFactory.getLogger(SSAuthentication.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	public void authenticate(String username, String password) {
		logger.debug("Authenticate:" + username);

		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}
}
