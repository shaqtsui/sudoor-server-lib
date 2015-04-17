package net.gplatform.sudoor.server.security.model.auth;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2015 Shark Xu
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sudoor.security")
public class SSAuth {
	private Logger logger = LoggerFactory.getLogger(SSAuth.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	AuthenticationManagerBuilder authenticationManagerBuilder;

	boolean passwordEncoderEnabled;

	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public boolean isPasswordEncoderEnabled() {
		return passwordEncoderEnabled;
	}

	public void setPasswordEncoderEnabled(boolean passwordEncoderEnabled) {
		this.passwordEncoderEnabled = passwordEncoderEnabled;
	}

	public UserDetailsManager getUserDetailsManager() {
		UserDetailsService uds = authenticationManagerBuilder.getDefaultUserDetailsService();
		UserDetailsManager udm = (UserDetailsManager) uds;
		return udm;
	}

	public String getCurrentUser() {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String username = null;
			if (principal instanceof UserDetails) {
				username = ((UserDetails) principal).getUsername();
			} else {
				username = principal.toString();
			}
			return username;
		} catch (Exception e) {
			return null;
		}
	}

	public boolean getIsPostLogin() {
		boolean result = false;
		String currentUser = getCurrentUser();
		result = StringUtils.isNotEmpty(currentUser) && !StringUtils.equals("anonymousUser", currentUser);
		return result;
	}

	public boolean isUserExist(String username) {
		return getUserDetailsManager().userExists(username);
	}

	/**
	 * WARNING: Normally this is used by non-web interface. For web interface,
	 * pls use Spring Security config to auto authenticate Here there is no
	 * signin, if you want to signin after authenticate pls use
	 * authenticateAndSignin
	 * 
	 * @param username
	 * @param password
	 */
	public void authenticate(String username, String password) {
		logger.debug("Authenticate:" + username);

		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		authenticationManager.authenticate(request);
	}

	/**
	 * WARNING: Normally this is used by non-web interface. For web interface,
	 * pls use Spring Security config to auto authenticate Here there is no
	 * authenticate
	 * 
	 * @param username
	 * @param password
	 */
	public void signin(String username, String password) {
		logger.debug("signin:" + username);

		Authentication request = new UsernamePasswordAuthenticationToken(username, password, null);
		SecurityContextHolder.getContext().setAuthentication(request);
	}

	/**
	 * WARNING: Normally this is used by non-web interface. For web interface,
	 * pls use Spring Security config to auto authenticate
	 * 
	 * @param username
	 * @param password
	 */
	public void authenticateAndSignin(String username, String password) {
		logger.debug("authenticateAndSignin:" + username);

		Authentication request = new UsernamePasswordAuthenticationToken(username, password);
		Authentication result = authenticationManager.authenticate(request);
		SecurityContextHolder.getContext().setAuthentication(result);
	}

	public String register(String username, String password) {
		return register(username, password, new String[] { "ROLE_USER" });
	}

	public String register(String username, String password, String[] roles) {
		logger.debug("Register user: {} , with passwordEncoderEnabled: {}", username, passwordEncoderEnabled);
		String savedPw = password;
		if (passwordEncoderEnabled) {
			savedPw = passwordEncoder.encode(password);
		}
		UserDetails ud = new User(username, savedPw, createSimpleGrantedAuthorities(roles));
		getUserDetailsManager().createUser(ud);
		return "SUCCESS";
	}

	public void updatePassword(String oldPassword, String newPassword) {
		String savedPw = newPassword;
		if (passwordEncoderEnabled) {
			savedPw = passwordEncoder.encode(newPassword);
		}
		getUserDetailsManager().changePassword(oldPassword, savedPw);
	}

	public void updatePasswordByName(String username, String newPassword) {
		String savedPw = newPassword;
		if (passwordEncoderEnabled) {
			savedPw = passwordEncoder.encode(newPassword);
		}
		UserDetails olduds = getUserDetailsManager().loadUserByUsername(username);
		UserDetails newuds = new User(olduds.getUsername(), savedPw, true, true, true, true, olduds.getAuthorities());
		getUserDetailsManager().updateUser(newuds);
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

	@PreAuthorize("hasPermission(#target, #method)")
	public void checkPermission(Object target, String method) {
		logger.debug("Grant permission [{}] to name [{}]", method, target);
	}
}
