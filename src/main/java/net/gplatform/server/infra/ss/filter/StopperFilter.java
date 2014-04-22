package net.gplatform.server.infra.ss.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StopperFilter implements Filter {
	final Logger logger = LoggerFactory.getLogger(StopperFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {
		logger.debug("Spring Security Filter Chain Stopped");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
