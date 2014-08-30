package net.gplatform.sudoor.server.social.config;

import net.gplatform.sudoor.server.social.model.signin.SimpleSignInAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.web.SignInAdapter;

@Configuration
public class SocialConfig{
	
	@Bean
	public SignInAdapter signInAdapter() {
		return new SimpleSignInAdapter(new HttpSessionRequestCache());
	}
}