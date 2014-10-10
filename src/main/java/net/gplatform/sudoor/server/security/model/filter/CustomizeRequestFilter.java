package net.gplatform.sudoor.server.security.model.filter;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2014 Shark Xu
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomizeRequestFilter implements Filter {
	final Logger logger = LoggerFactory.getLogger(CustomizeRequestFilter.class);

	@Value("net.gplatform.sudoor.server.security.model.MultipleReadRequestWrapper")
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
		logger.debug("Before process: request.getCharacterEncoding(): {}", request.getCharacterEncoding());

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
		
		logger.debug("After process: request.getCharacterEncoding(): {}", request.getCharacterEncoding());

	}

	@Override
	public void destroy() {
		logger.debug("CustomizeRequestFilter destroy");

	}

}
