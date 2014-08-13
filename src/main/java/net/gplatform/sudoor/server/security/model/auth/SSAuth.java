package net.gplatform.sudoor.server.security.model.auth;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class SSAuth {
	private Logger logger = LoggerFactory.getLogger(SSAuth.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	AuthenticationManagerBuilder authenticationManagerBuilder;

	public UserDetailsManager getUserDetailsManager() {
		UserDetailsService uds = authenticationManagerBuilder.getDefaultUserDetailsService();
		UserDetailsManager udm = (UserDetailsManager) uds;
		return udm;
	}

	public String getCurrentUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = null;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}
		return username;
	}

	public boolean isUserExist(String username) {
		return getUserDetailsManager().userExists(username);
	}

	/**
	 * WARNING: Normally this is used by non-web interface. For web interface,
	 * pls use Spring Security config to auto authenticate
	 * 
	 * @param username
	 * @param password
	 */
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
		UserDetails ud = new User(username, password, createSimpleGrantedAuthorities(roles));
		getUserDetailsManager().createUser(ud);
		return "SUCCESS";
	}

	public void updatePassword(String oldPassword, String newPassword) {
		getUserDetailsManager().changePassword(oldPassword, newPassword);
	}

	public String disableUser(String username) {
		UserDetails olduds = getUserDetailsManager().loadUserByUsername(username);
		UserDetails newuds = new User(olduds.getUsername(), olduds.getPassword(), false, false, false, false, olduds.getAuthorities());
		getUserDetailsManager().updateUser(newuds);
		return "SUCCESS";
	}

	public String enableUser(String username) {
		UserDetails olduds = getUserDetailsManager().loadUserByUsername(username);
		UserDetails newuds = new User(olduds.getUsername(), olduds.getPassword(), true, true, true, true, olduds.getAuthorities());
		getUserDetailsManager().updateUser(newuds);
		return "SUCCESS";
	}

	public String addRole(String username, String[] roles) {
		UserDetails olduds = getUserDetailsManager().loadUserByUsername(username);
		List<GrantedAuthority> newAuth = new ArrayList<GrantedAuthority>();
		newAuth.addAll(olduds.getAuthorities());
		newAuth.addAll(createSimpleGrantedAuthorities(roles));
		UserDetails newuds = new User(olduds.getUsername(), olduds.getPassword(), true, true, true, true, newAuth);
		getUserDetailsManager().updateUser(newuds);
		return "SUCCESS";
	}

	public String removeRole(String username, String[] roles) {
		UserDetails olduds = getUserDetailsManager().loadUserByUsername(username);
		List<GrantedAuthority> newAuth = new ArrayList<GrantedAuthority>();
		newAuth.addAll(olduds.getAuthorities());
		newAuth.removeAll(createSimpleGrantedAuthorities(roles));
		UserDetails newuds = new User(olduds.getUsername(), olduds.getPassword(), true, true, true, true, newAuth);
		getUserDetailsManager().updateUser(newuds);
		return "SUCCESS";
	}

	private List<SimpleGrantedAuthority> createSimpleGrantedAuthorities(String[] roles) {
		List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<SimpleGrantedAuthority>();
		for (int i = 0; i < roles.length; i++) {
			simpleGrantedAuthorities.add(new SimpleGrantedAuthority(roles[i]));
		}
		return simpleGrantedAuthorities;
	}
}
