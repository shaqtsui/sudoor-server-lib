package net.gplatform.sudoor.server.springboot;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class SudoorServletContainerCustomizer implements EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		// This is to return null for 401, so that error will send to client directly
		ErrorPage errorPage401 = new ErrorPage(HttpStatus.UNAUTHORIZED, null);
		container.addErrorPages(errorPage401);
	}
}
