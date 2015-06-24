package net.gplatform.sudoor.server.springsession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.MultiHttpSessionStrategy;

public class CookieAndHeaderHttpSessionStrategy implements MultiHttpSessionStrategy {
	private Logger logger = LoggerFactory.getLogger(CookieAndHeaderHttpSessionStrategy.class);

	CookieHttpSessionStrategy cookieHttpSessionStrategy = new CookieHttpSessionStrategy();
	HeaderHttpSessionStrategy headerHttpSessionStrategy = new HeaderHttpSessionStrategy();

	@Override
	public String getRequestedSessionId(HttpServletRequest request) {
		String cookieSessionId = cookieHttpSessionStrategy.getRequestedSessionId(request);
		String headerSessionId = headerHttpSessionStrategy.getRequestedSessionId(request);
		logger.debug("cookieSessionId: [{}] AND headerSessionId: [{}]", cookieSessionId, headerSessionId);
		String sessionId = null;
		if (StringUtils.isNoneBlank(headerSessionId)) {
			sessionId = headerSessionId;
		} else {
			sessionId = cookieSessionId;
		}
		logger.debug("Finally selected sessionId: [{}]", sessionId);
		return sessionId;
	}

	@Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		cookieHttpSessionStrategy.onNewSession(session, request, response);
		headerHttpSessionStrategy.onNewSession(session, request, response);
	}

	@Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		cookieHttpSessionStrategy.onInvalidateSession(request, response);
		headerHttpSessionStrategy.onInvalidateSession(request, response);
	}

	@Override
	public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
		return cookieHttpSessionStrategy.wrapRequest(request, response);
	}

	@Override
	public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
		return cookieHttpSessionStrategy.wrapResponse(request, response);
	}

}
