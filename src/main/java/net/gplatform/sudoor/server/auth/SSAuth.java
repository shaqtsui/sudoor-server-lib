package net.gplatform.sudoor.server.auth;

import java.util.ArrayList;
import java.util.List;

import net.gplatform.sudoor.server.ss.CredentialUserRepository;
import net.gplatform.sudoor.server.ss.entity.CredentialAuthority;
import net.gplatform.sudoor.server.ss.entity.CredentialUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SSAuth {
	private Logger logger = LoggerFactory.getLogger(SSAuth.class);

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CredentialUserRepository credentialUserRepository;

	public void authenticate(String username, String password) {
		logger.debug("Authenticate:" + username);

		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}
	
	public String register(String username, String password) {
		CredentialUser credentialUser = new CredentialUser();
		credentialUser.setUsername(username);
		credentialUser.setPassword(password);
		credentialUser.setEnabled(true);

		CredentialAuthority credentialAuthority = new CredentialAuthority();
		credentialAuthority.setUsername(username);
		credentialAuthority.setAuthority("ROLE_USER");
		List<CredentialAuthority> credentialAuthorities = new ArrayList<CredentialAuthority>();		
		credentialAuthorities.add(credentialAuthority);
		
		credentialUser.setCredentialAuthorities(credentialAuthorities);

		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}
}
