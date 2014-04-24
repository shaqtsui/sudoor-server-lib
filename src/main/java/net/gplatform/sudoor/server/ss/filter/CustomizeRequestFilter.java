package net.gplatform.sudoor.server.ss.filter;

import java.io.IOException;
import java.lang.reflect.Constructor;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomizeRequestFilter implements Filter {
	final Logger logger = LoggerFactory.getLogger(CustomizeRequestFilter.class);

	String requestFullName;

	public String getRequestFullName() {
		return requestFullName;
	}

	public void setRequestFullName(String requestFullName) {
		this.requestFullName = requestFullName;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.debug("CustomizeRequestFilter init");

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		logger.debug("CustomizeRequestFilter Start to Customize");
		logger.debug("Original request -> {}", request);

		ServletRequest newRequestObject = null;
		try {
			Class newRequestClass = Class.forName(requestFullName);
			Constructor newRequestConstructor = newRequestClass.getConstructor(HttpServletRequest.class);
			newRequestObject = (ServletRequest) newRequestConstructor.newInstance(request);
		} catch (Exception e) {
			logger.error("Can not customize request", e);
		}

		if (newRequestObject == null) {
			logger.debug("Execute Chain with original request");
			chain.doFilter(request, response);
		} else {
			logger.debug("Execute Chain with new request -> {}", newRequestObject);
			chain.doFilter(newRequestObject, response);
		}

	}

	@Override
	public void destroy() {
		logger.debug("CustomizeRequestFilter destroy");

	}

}
