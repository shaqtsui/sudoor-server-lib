package net.gplatform.server.infra.jaxrs;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 14-1-3.
 */
public class RestApplication extends Application {
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(MultiPartFeature.class);
		return classes;
	}
}
