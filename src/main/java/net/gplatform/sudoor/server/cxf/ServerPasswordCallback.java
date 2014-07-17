package net.gplatform.sudoor.server.cxf;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * NOT used, as it need to retrieve clear pw from spring security via callback while spring security store hash in DB,
 * Use simple WS to do authenticate instead.
 * @author xufucheng
 *
 */
@Component
public class ServerPasswordCallback implements CallbackHandler {

	@Value("security.user.password")
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
		String username = pc.getIdentifier();
		pc.setPassword(password);
	}

}
