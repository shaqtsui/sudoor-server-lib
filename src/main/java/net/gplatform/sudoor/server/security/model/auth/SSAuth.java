package net.gplatform.sudoor.server.security.model.auth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.gplatform.sudoor.server.security.model.entity.CredentialAuthority;
import net.gplatform.sudoor.server.security.model.entity.CredentialUser;
import net.gplatform.sudoor.server.security.model.repository.CredentialUserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
		return register(username, password, new String[] { "ROLE_USER" });
	}

	public String register(String username, String password, String[] roles) {
		logger.debug("Register:" + username);

		CredentialUser credentialUser = new CredentialUser();
		credentialUser.setUsername(username);
		credentialUser.setPassword(password);
		credentialUser.setEnabled(true);
		credentialUser.setCredentialAuthorities(createCredentialAuthorities(username, roles));

		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}

	public String updatePassword(String username, String password) {
		CredentialUser credentialUser = credentialUserRepository.findOne(username);
		credentialUser.setPassword(password);
		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}

	public String disableUser(String username) {
		CredentialUser credentialUser = credentialUserRepository.findOne(username);
		credentialUser.setEnabled(false);
		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}

	public String enableUser(String username) {
		CredentialUser credentialUser = credentialUserRepository.findOne(username);
		credentialUser.setEnabled(true);
		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}

	public String addRole(String username, String[] roles) {
		CredentialUser credentialUser = credentialUserRepository.findOne(username);
		credentialUser.setCredentialAuthorities(createCredentialAuthorities(username, roles));
		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}

	public String removeRole(String username, String[] roles) {
		CredentialUser credentialUser = credentialUserRepository.findOne(username);
		List<CredentialAuthority> credentialAuthorities = credentialUser.getCredentialAuthorities();
		Iterator rolesIterator = CollectionUtils.arrayToList(roles).iterator();

		for (Iterator iterator = credentialAuthorities.iterator(); iterator.hasNext();) {
			CredentialAuthority credentialAuthority = (CredentialAuthority) iterator.next();
			if (CollectionUtils.contains(rolesIterator, credentialAuthority.getAuthority())) {
				iterator.remove();
			}
		}
		credentialUserRepository.saveAndFlush(credentialUser);
		return "SUCCESS";
	}

	private List<CredentialAuthority> createCredentialAuthorities(String username, String[] roles) {
		List<CredentialAuthority> credentialAuthorities = new ArrayList<CredentialAuthority>();
		for (int i = 0; i < roles.length; i++) {
			String role = roles[i];
			CredentialAuthority credentialAuthority = new CredentialAuthority();
			credentialAuthority.setUsername(username);
			credentialAuthority.setAuthority(role);
			credentialAuthorities.add(credentialAuthority);
		}
		return credentialAuthorities;
	}
}
