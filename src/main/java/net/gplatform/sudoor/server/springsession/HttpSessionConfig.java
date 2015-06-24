package net.gplatform.sudoor.server.springsession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		return new CookieAndHeaderHttpSessionStrategy();
	}
}