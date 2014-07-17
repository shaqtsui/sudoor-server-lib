package net.gplatform.sudoor.server.cxf;

import java.util.Map;
import java.util.Vector;

import net.gplatform.sudoor.server.auth.SSAuthentication;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.principal.WSUsernameTokenPrincipalImpl;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.WSSecurityEngineResult;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.wss4j.dom.handler.WSHandlerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * NOT used, as it need to retrieve clear pw from spring security via callback while spring security store hash in DB,
 * Use simple WS to do authenticate instead.
 * @author xufucheng
 *
 */
public class WSAuthenticationInInterceptor extends WSS4JInInterceptor {

	final Logger logger = LoggerFactory.getLogger(WSAuthenticationInInterceptor.class);

	@Autowired
	private SSAuthentication SSAuthentication;

	public WSAuthenticationInInterceptor() {
		super();
	}

	public WSAuthenticationInInterceptor(Map<String, Object> properties) {
		super(properties);
	}

	public SSAuthentication getSSAuthentication() {
		return SSAuthentication;
	}

	public void setSSAuthentication(SSAuthentication sSAuthentication) {
		SSAuthentication = sSAuthentication;
	}

	@Override
	public void handleMessage(SoapMessage message) throws Fault {
		try {
			super.handleMessage(message);
			Vector<WSHandlerResult> result = (Vector<WSHandlerResult>) message.getContextualProperty(WSHandlerConstants.RECV_RESULTS);
			if (result != null && !result.isEmpty()) {
				for (WSHandlerResult res : result) {
					// loop through security engine results
					for (WSSecurityEngineResult securityResult : (Vector<WSSecurityEngineResult>) res.getResults()) {
						int action = (Integer) securityResult.get(WSSecurityEngineResult.TAG_ACTION);
						// determine if the action was a username token
						if ((action & WSConstants.UT) > 0) {
							// get the principal object
							WSUsernameTokenPrincipalImpl principal = (WSUsernameTokenPrincipalImpl) securityResult
									.get(WSSecurityEngineResult.TAG_PRINCIPAL);
							if (principal.getPassword() == null) {
								principal.setPassword("");
							}

							SSAuthentication.authenticate(principal.getName(), principal.getPassword());
						}
					}
				}
			}
		} catch (RuntimeException ex) {
			logger.error("Error in handleMessage", ex);
			throw ex;
		}
	}
}