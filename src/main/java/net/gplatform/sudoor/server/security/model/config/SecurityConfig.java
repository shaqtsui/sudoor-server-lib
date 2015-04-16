package net.gplatform.sudoor.server.security.model.config;

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

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ConditionalOnClass(AuthenticationManager.class)
@Configuration
public class SecurityConfig{
	
	/*
	 * Enable JDBC authentication provider
	 */
	@Configuration
	@Order(Ordered.HIGHEST_PRECEDENCE + 10)
	public static class AuthenticationSecurity extends GlobalAuthenticationConfigurerAdapter {

		@Autowired
		private DataSource dataSource;

		@Override
		public void init(AuthenticationManagerBuilder auth) throws Exception {
			auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder()).dataSource(this.dataSource);
		}
	}
	
	/**
	 * Ignore config for non-dispatchServlet requests, dispatchServlet resource ignore should configed in application.properties
	 * normally only static content ingored, since no SS context available if not go through SS filters
	 * @author xufucheng
	 *
	 */
	@Configuration
	@Order(SecurityProperties.IGNORED_ORDER + 10)
	public static class IgnoredPathsWebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web
				.ignoring()
					.antMatchers("/", "/index.html");
		}
		
		@Override
		public void init(WebSecurity builder) throws Exception {
		}
	}
	
	/*
	 * Enable form http config, The order will only impact http filter sequence
	 * This conifg for jerseyServlet Resource
	 */
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	public static class JerseyServletResourceSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.regexMatcher("/data/odata.svc/\\$metadata")
				.httpBasic()
				.and()
					.authorizeRequests()
						.regexMatchers("/data/odata.svc/\\$metadata").permitAll()
				.and()
					.csrf().disable();
		}
	}
	
	/*
	 * Enable form http config, The order will only impact http filter sequence
	 * This conifg for nonODataRestJerseyServlet Resource
	 */
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER-1)
	public static class NonODataRestJerseyServletResourceSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.regexMatcher("/data/ws/rest/sudoor/.*")
				.httpBasic()
				.and()
					.authorizeRequests()
						.regexMatchers("/data/ws/rest/sudoor/.*").permitAll()
				.and()
					.csrf().disable();
		}
	}
	
	/*
	 * Enable form http config, The order will only impact http filter sequence
	 * This config for DispatcherServlet Resource
	 */
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER-2)
	public static class DispatcherServletResourceSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.regexMatcher("/|/index.html|/app/index.html|/login|/logout|/app/connect.*|/app/signin.*|/app/signup.*|/app/linkedin.*|/app/linkedin.*|/app/sudoor/.*")
				.formLogin().defaultSuccessUrl("/app/index.html")
				.and()
					.logout()
						.deleteCookies("JSESSIONID")
				.and()
					.authorizeRequests()
						.regexMatchers("/", "/index.html", "/app/index.html", "/login", "/logout", "/app/connect.*","/app/signin.*", "/app/signup.*", "/app/linkedin.*", "/app/sudoor/.*").permitAll()
				.and()
					.csrf().disable();
		}
	}
}