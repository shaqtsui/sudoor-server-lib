package net.gplatform.sudoor.server.jpa.autoconfig;

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

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import net.gplatform.sudoor.server.jpa.autoconfig.EclipselinkJpaAutoConfiguration.EclipselinkEntityManagerCondition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jta.JtaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaBaseConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ClassUtils;
/**
 * To avoid error "Cannot apply class transformer without LoadTimeWeaver specified"
 * Need to:
 * 	add spring.jpa.eclipselink.eclipselink.weaving=false to disable loadTimeWeaver
 * Or:
 * 	Config loadTimeWeaver
 * @author xufucheng
 *
 */
@Configuration
@ConditionalOnClass({ LocalContainerEntityManagerFactoryBean.class, EnableTransactionManagement.class, EntityManager.class })
@Conditional(EclipselinkEntityManagerCondition.class)
@AutoConfigureAfter({ DataSourceAutoConfiguration.class, JtaAutoConfiguration.class })
@ConfigurationProperties(prefix = "spring.jpa")
public class EclipselinkJpaAutoConfiguration extends JpaBaseConfiguration {
	private Map<String, String> eclipselink = new HashMap<String, String>();
	
	public Map<String, String> getEclipselink() {
		return eclipselink;
	}

	public void setEclipselink(Map<String, String> eclipselink) {
		this.eclipselink = eclipselink;
	}

	@Autowired
	private JpaProperties properties;

	@Autowired
	private DataSource dataSource;

	@Override
	protected AbstractJpaVendorAdapter createJpaVendorAdapter() {
		EclipseLinkJpaVendorAdapter adapter = new EclipseLinkJpaVendorAdapter();
		return adapter;
	}

	@Override
	protected Map<String, Object> getVendorProperties() {
		Map<String, Object> vendorProperties = new HashMap<String, Object>();
		vendorProperties.putAll(eclipselink);
		return vendorProperties;
	}

	@Order(Ordered.HIGHEST_PRECEDENCE + 20)
	static class EclipselinkEntityManagerCondition extends SpringBootCondition {

		private static String[] CLASS_NAMES = { "org.eclipse.persistence.jpa.JpaEntityManager" };

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			for (String className : CLASS_NAMES) {
				if (ClassUtils.isPresent(className, context.getClassLoader())) {
					return ConditionOutcome.match("found EclipselinkEntityManager class");
				}
			}
			return ConditionOutcome.noMatch("did not find EclipselinkEntityManager class");
		}
	}

}