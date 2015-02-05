package net.gplatform.sudoor.server.jersey;

import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This is for non-odata rest Jersey config, nothing to do with spring.jersey.*
 * 
 * @author xufucheng
 *
 */
@Configuration
public class NonODataRestJerseyRegistration {
	private String path = "/data/ws/rest/*";

	@Value("${application.basepackage}")
	private String applicationBasepackage;

	@Bean
	public ServletRegistrationBean nonODataRestJerseyServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), this.path);
		registration.addInitParameter(ServerProperties.PROVIDER_PACKAGES, "net.gplatform.sudoor.server," + applicationBasepackage);
		registration.addInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true");
		registration.setName("nonODataRestJerseyServlet");
		return registration;
	}
}