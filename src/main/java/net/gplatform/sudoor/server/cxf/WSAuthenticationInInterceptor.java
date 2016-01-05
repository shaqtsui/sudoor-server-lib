package net.gplatform.sudoor.server.cxf;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2015 Shark Xu
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */


import net.gplatform.sudoor.server.security.model.auth.SSAuth;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.principal.WSUsernameTokenPrincipalImpl;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.engine.WSSecurityEngineResult;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.apache.wss4j.dom.handler.WSHandlerResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Vector;

/**
 * NOT used, as it need to retrieve clear pw from spring security via callback
 * while spring security store hash in DB, Use simple WS to do authenticate
 * instead.
 * 
 * @author xufucheng
 *
 */
public class WSAuthenticationInInterceptor extends WSS4JInInterceptor {

	final Logger logger = LoggerFactory.getLogger(WSAuthenticationInInterceptor.class);

	@Autowired
	private SSAuth SSAuth;

	public WSAuthenticationInInterceptor() {
		super();
	}

	public WSAuthenticationInInterceptor(Map<String, Object> properties) {
		super(properties);
	}

	public SSAuth getSSAuth() {
		return SSAuth;
	}

	public void setSSAuth(SSAuth sSAuth) {
		SSAuth = sSAuth;
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

							SSAuth.authenticate(principal.getName(), principal.getPassword());
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