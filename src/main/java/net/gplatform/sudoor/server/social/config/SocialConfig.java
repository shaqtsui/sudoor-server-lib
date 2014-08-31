package net.gplatform.sudoor.server.social.config;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInController;

@Configuration
public class SocialConfig {
	final Logger logger = LoggerFactory.getLogger(SocialConfig.class);

	@Autowired
	ConnectionSignUp connectionSignUp;

	@Bean
	public RequestCache requestCache() {
		return new HttpSessionRequestCache();
	}

	@Autowired
	public void configProviderSignInController(ProviderSignInController providerSignInController) {
		providerSignInController.setSignUpUrl("/app/signup");
	}

	@Autowired
	public void configUsersConnectionRepository(UsersConnectionRepository usersConnectionRepository) {
		try {
			BeanUtils.setProperty(usersConnectionRepository, "connectionSignUp", connectionSignUp);
		} catch (Exception e) {
			logger.error("Error configUsersConnectionRepository", e);
		}
	}
}