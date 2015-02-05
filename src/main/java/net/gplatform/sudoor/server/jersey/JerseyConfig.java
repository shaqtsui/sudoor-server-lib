package net.gplatform.sudoor.server.jersey;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.apache.olingo.odata2.core.rest.app.ODataApplication;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
@ApplicationPath("/data/odata.svc")
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		Application application = new ODataApplication();
		registerClasses(application.getClasses());
	}
}
