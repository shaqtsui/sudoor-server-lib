package net.gplatform.sudoor.server.security;

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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * No empty constructor so can not use annotation Component
 * @author xufucheng
 *
 */
public class MultipleReadRequestWrapper extends HttpServletRequestWrapper {

	final Logger logger = LoggerFactory.getLogger(MultipleReadRequestWrapper.class);

	byte[] bContent;

	public MultipleReadRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		prepareInputStream();

		final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bContent);
		ServletInputStream inputStream = new ServletInputStream() {
			public int read() throws IOException {
				return byteArrayInputStream.read();
			}
		};
		return inputStream;
	}

	private void prepareInputStream() {
		if (bContent == null) {
			try {
				ServletRequest oReq = getRequest();
				bContent = IOUtils.toByteArray(oReq.getInputStream());
			} catch (IOException e) {
				logger.error("Can not read origin request input stream", e);
			}
		}
	}
}
