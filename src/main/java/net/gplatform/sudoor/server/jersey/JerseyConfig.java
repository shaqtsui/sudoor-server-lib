package net.gplatform.sudoor.server.jersey;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.olingo.odata2.core.rest.app.ODataApplication;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * This config only used by default jerseyServletRegistration & jerseyFilterRegistration in Spring Auto config
 * If ApplicationPath available, a servlet will be created to handle request, so need to remove it add hard code in Registration
 * @author xufucheng
 *
 */
@Component
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		Application application = new ODataApplication();
		registerClasses(application.getClasses());
	}
}
