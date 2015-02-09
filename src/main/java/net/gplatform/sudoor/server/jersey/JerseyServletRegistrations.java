package net.gplatform.sudoor.server.jersey;

import java.util.Map.Entry;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.CommonProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jersey.JerseyProperties;
import org.springframework.boot.context.embedded.RegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * This is for non-odata rest Jersey config, nothing to do with spring.jersey.*
 * 
 * @author xufucheng
 *
 */
@Configuration
public class JerseyServletRegistrations {
	@Autowired
	private JerseyProperties jersey;

	@Autowired
	private ResourceConfig config;

	private String nonODataRestJerseyServletRegistrationPath = "/data/ws/rest/*";
	
	/**
	 * Cann not use @ApplicationPath in JerseyConfig refer to JerseyConfig comment
	 */
	private String jerseyServletRegistrationPath = "/data/odata.svc/*";

	@Value("${application.basepackage}")
	private String applicationBasepackage;

	@Bean
	public ServletRegistrationBean nonODataRestJerseyServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(), this.nonODataRestJerseyServletRegistrationPath);
		registration.addInitParameter(ServerProperties.PROVIDER_PACKAGES, "net.gplatform.sudoor.server," + applicationBasepackage);
		registration.addInitParameter(ServerProperties.PROVIDER_SCANNING_RECURSIVE, "true");
		registration.setName("nonODataRestJerseyServlet");
		return registration;
	}

	/**
	 * Introduce this to fix the bug in spring boot Refer to SpringBoot.txt Set
	 * the registration to the Application class name
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean jerseyServletRegistration() {
		ServletRegistrationBean registration = new ServletRegistrationBean(new ServletContainer(this.config), jerseyServletRegistrationPath);
		addInitParameters(registration);
		registration.setName(config.getClass().getName());
		return registration;
	}

	private void addInitParameters(RegistrationBean registration) {
		registration.addInitParameter(CommonProperties.METAINF_SERVICES_LOOKUP_DISABLE, "true");
		for (Entry<String, String> entry : this.jersey.getInit().entrySet()) {
			registration.addInitParameter(entry.getKey(), entry.getValue());
		}
	}

}