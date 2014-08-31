package net.gplatform.sudoor.server.social.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

@Configuration
public class SocialConfig {
	final Logger logger = LoggerFactory.getLogger(SocialConfig.class);

	@Bean
	public RequestCache requestCache() {
		return new HttpSessionRequestCache();
	}
}