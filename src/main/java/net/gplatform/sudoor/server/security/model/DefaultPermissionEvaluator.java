package net.gplatform.sudoor.server.security.model;

/*
 * #%L
 * sudoor-server-lib
 * %%
 * Copyright (C) 2013 - 2014 Shark Xu
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 14-1-15.
 */

@Component
@ConfigurationProperties(prefix = "sudoor.permissionevaluator.default")
public class DefaultPermissionEvaluator implements PermissionEvaluator {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultPermissionEvaluator.class);

	public static final String CONFIG_EXPRESSION_PREFIX = "expression.";

	private Properties properties;

	private ExpressionParser parser = new SpelExpressionParser();
	private EvaluationContext context = new StandardEvaluationContext();
	private Map<String, Map<Object, Expression>> expressions = new HashMap<String, Map<Object, Expression>>();
	
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * Find the expression string recursively from the config file
	 * 
	 * @param name
	 * @param permission
	 * @return
	 */
	public String getExpressionString(String name, Object permission) {
		String expression = properties.getProperty(CONFIG_EXPRESSION_PREFIX + name + "." + permission);

		//Get generic permit
		if (expression == null) {
			expression = properties.getProperty(CONFIG_EXPRESSION_PREFIX + name);
		}

		//Get parent permit
		if (expression == null && StringUtils.contains(name, ".")) {
			String parent = StringUtils.substringBeforeLast(name, ".");
			expression = getExpressionString(parent, permission);
		}

		LOG.debug("Get Expression String: [{}] for name: [{}] permission: [{}]", expression, name, permission);
		return expression;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		String name = targetDomainObject.getClass().getName();
		Map<Object, Expression> m = expressions.get(name);
		if (m == null) {
			m = new HashMap<Object, Expression>();
			expressions.put(name, m);
		}

		Expression expression = m.get(permission);
		if (expression == null) {
			String expressionString = getExpressionString(name, permission);
			if (StringUtils.isNotEmpty(expressionString)) {
				expression = parser.parseExpression(expressionString);
			} else {
				expression = parser.parseExpression("false");
				LOG.warn("No Expression configed for name: [{}] permission: [{}], Default to false Expression!", name, permission);
			}
			m.put(permission, expression);
		}

		context.setVariable("authentication", authentication);
		context.setVariable("target", targetDomainObject);

		boolean res = false;
		try {
			res = expression.getValue(context, Boolean.class);
			LOG.debug("Evaluate Result : [{}] for name: [{}] permission: [{}]", res, name, permission);
		} catch (Exception e) {
			LOG.error("Error parse expression", e);
		}
		return res;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
		LOG.error("Not implemented!!!");
		return false;
	}
}
