package net.gplatform.sudoor.server.social.config;

import net.gplatform.sudoor.server.social.model.SimpleSignInAdapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.SignInAdapter;

@Configuration
public class SocialConfig{
	@Autowired
	ConnectionSignUp connectionSignUp;
	
	@Bean
	public SignInAdapter signInAdapter() {
		return new SimpleSignInAdapter(new HttpSessionRequestCache());
	}
	
	@Autowired
	public void configProviderSignInController(ProviderSignInController providerSignInController){
		providerSignInController.setSignUpUrl("/app/signup");
	}
	
	@Autowired
	public void configJdbcUsersConnectionRepository(JdbcUsersConnectionRepository jdbcUsersConnectionRepository){
		jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
	}
}