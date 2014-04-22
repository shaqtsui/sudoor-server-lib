package net.gplatform.server.infra.ss;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
