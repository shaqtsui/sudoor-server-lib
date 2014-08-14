package net.gplatform.sudoor.server.security.model.config;

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
					.antMatchers("/index.html", "/data/ws/soap/sudoor/**", "/data/ws/rest/sudoor/**", "/data/odata.svc/$metadata", "/data/odata.svc/$batch");
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
				.regexMatcher("/app/connect.*|/app/linkedin.*|/data/odata.svc/Credential.*")
				.formLogin()
				.and()
					.logout()
						.deleteCookies("JSESSIONID")
				.and()
					.authorizeRequests()
						.antMatchers("/app/connect*", "/app/linkedin*").permitAll()
						.antMatchers(HttpMethod.GET, "/data/odata.svc/Credential*").denyAll()
				.and()
					.rememberMe();
		}
	}
}