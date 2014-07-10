package net.gplatform.sudoor.server.cxf;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import net.gplatform.sudoor.server.auth.SSAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityServerPasswordCallback implements CallbackHandler {

	@Autowired
	private static SSAuthentication SSAuthentication;

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		String username = null;
		String password = null;
		for (Callback cb : callbacks) {
			System.out.println(cb);

		}
		SSAuthentication.authenticate(username, password);

	}

}
