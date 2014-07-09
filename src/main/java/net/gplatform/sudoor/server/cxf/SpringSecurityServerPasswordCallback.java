package net.gplatform.sudoor.server.cxf;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityServerPasswordCallback implements CallbackHandler {
	private static AuthenticationManager authenticationManager;

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

		String name = "";
		String password = "";
		Authentication request = new UsernamePasswordAuthenticationToken(name, password);
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);

	}

}
