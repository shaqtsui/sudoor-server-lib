package net.gplatform.sudoor.server.logging;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.gplatform.sudoor.server.constant.SudoorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Use %X{sessionId} to log sessionId to log file
 * @author xufucheng
 *
 */
@Order(SudoorConstants.ORDER_FILTER_MDC_SESSION_INSERTING)
@Component
public class MDCSessionInsertingServletFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(MDCSessionInsertingServletFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("Init MDCSessionInsertingServletFilter");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.debug("MDCSessionInsertingServletFilter.doFilter() start");
		insertIntoMDC(request);
		try {
			chain.doFilter(request, response);
		} finally {
			clearMDC();
			logger.debug("MDCSessionInsertingServletFilter.doFilter() end");
		}

	}

	void insertIntoMDC(ServletRequest request) {
		logger.debug("MDCSessionInsertingServletFilter.insertIntoMDC() start");
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			HttpSession session = httpServletRequest.getSession();
			String sessionId = session.getId();
			MDC.put("sessionId", sessionId);
		}
	}

	void clearMDC() {
		logger.debug("MDCSessionInsertingServletFilter.clearMDC() start");
		MDC.remove("sessionId");
	}

	@Override
	public void destroy() {
		logger.debug("Destroy MDCSessionInsertingServletFilter");
	}

}
