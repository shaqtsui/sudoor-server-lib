package net.gplatform.sudoor.server.jaxrs.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
@Cacheable
public class CacheControlDecorator implements ContainerResponseFilter {
	final Logger logger = LoggerFactory.getLogger(CacheControlDecorator.class);

	@Override
	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException {
		for (Annotation a : responseContext.getEntityAnnotations()) {
			if (a.annotationType() == Cacheable.class) {
				String cc = ((Cacheable) a).cc();
				logger.debug("Add Cache-Control:{} to {}", cc, responseContext);
				responseContext.getHeaders().add("Cache-Control", cc);
			}
		}
	}
}
