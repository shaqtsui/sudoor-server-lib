package net.gplatform.server.infra.ss;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 14-1-15.
 */
public class DefaultPermissionEvaluator implements PermissionEvaluator {
	final Logger logger = LoggerFactory.getLogger(DefaultPermissionEvaluator.class);

	public final String CONFIG_FILE = "PermissionEvaluator/PermissionEvaluator.properties";
	public final String CONFIG_EXPRESSION_PREFIX = "PermissionEvaluator.expression.";

	private Properties configProperties = new Properties();

	private ExpressionParser parser = new SpelExpressionParser();
	private EvaluationContext context = new StandardEvaluationContext();
	private Map<String, Map<Object, Expression>> expressions = new HashMap<String, Map<Object, Expression>>();

	public DefaultPermissionEvaluator() {
		logger.debug("Start to init DefaultPermissionEvaluator");
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
			configProperties.load(is);
			logger.debug("Successfully Init DefaultPermissionEvaluator");
		} catch (Exception e) {
			logger.error("Can not init DefaultPermissionEvaluator", e);
		}
	}

	/**
	 * Find the expression string recursively from the config file
	 *
	 * @param name
	 * @param permission
	 * @return
	 */
	public String getExpressionString(String name, Object permission) {
		String expression = configProperties.getProperty(CONFIG_EXPRESSION_PREFIX + name + "." + permission);

		//Get generic permit
		if (expression == null) {
			expression = configProperties.getProperty(CONFIG_EXPRESSION_PREFIX + name);
		}

		//Get parent permit
		if (expression == null && StringUtils.contains(name, ".")) {
			String parent = StringUtils.substringBeforeLast(name, ".");
			expression = getExpressionString(parent, permission);
		}

		logger.debug("Get Expression String: [{}] for name: [{}] permission: [{}]", expression, name, permission);
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
				logger.warn("No Expression configed for name: [{}] permission: [{}], Default to false Expression!", name, permission);
			}
			m.put(permission, expression);
		}

		context.setVariable("authentication", authentication);
		context.setVariable("target", targetDomainObject);

		boolean res = false;
		try {
			res = expression.getValue(context, Boolean.class);
			logger.debug("Evaluate Result : [{}] for name: [{}] permission: [{}]", res, name, permission);
		} catch (Exception e) {
			logger.error("Error parse expression", e);
		}
		return res;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
		logger.error("Not implemented!!!");
		return false;
	}
}
