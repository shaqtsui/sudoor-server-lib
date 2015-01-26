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
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
			auth.jdbcAuthentication().dataSource(this.dataSource);
		}
	}
	
	/**
	 * Ignore config for non-dispatchServlet requests 
	 * @author xufucheng
	 *
	 */
	@Configuration
	@Order(SecurityProperties.IGNORED_ORDER - 10)
	public static class IgnoredPathsWebSecurityConfigurerAdapter implements WebSecurityConfigurer<WebSecurity> {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web
				.ignoring()
					.antMatchers("/index.html", "/data/ws/*", "/data/ws/soap/sudoor/**", "/data/ws/rest/sudoor/**", "/data/odata.svc/$metadata", "/data/odata.svc/$batch");
		}
		
		@Override
		public void init(WebSecurity builder) throws Exception {
		}
	}
	
	/*
	 * Enable form http config, The order will only impact http filter sequence
	 */
	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	public static class CommonWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
				.regexMatcher("/app/connect.*|/app/signin.*|/app/signup.*|/app/linkedin.*|/data/odata.svc/Credential.*")
				.formLogin()
				.and()
					.logout()
						.deleteCookies("JSESSIONID")
				.and()
					.authorizeRequests()
						.regexMatchers("/app/connect.*","/app/signin.*", "/app/signup.*", "/app/linkedin.*").permitAll()
						.regexMatchers(HttpMethod.GET, "/data/odata.svc/Credential.*").denyAll()
				.and()
					.rememberMe();
		}
	}
}