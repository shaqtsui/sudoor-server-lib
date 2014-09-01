package net.gplatform.sudoor.server.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

@Configuration
public class ThymeleafConfig implements EnvironmentAware {
	public static final String DEFAULT_PREFIX = "classpath:/templates/";

	public static final String DEFAULT_SUFFIX = ".xml";

	private RelaxedPropertyResolver environment;

	@Autowired
	SpringResourceResourceResolver springResourceResourceResolver;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = new RelaxedPropertyResolver(environment, "spring.thymeleaf.");
	}

	@Bean
	public ITemplateResolver xmlTemplateResolver() {
		TemplateResolver resolver = new TemplateResolver();
		resolver.setResourceResolver(springResourceResourceResolver);
		resolver.setPrefix(this.environment.getProperty("prefix", DEFAULT_PREFIX));
		resolver.setSuffix(DEFAULT_SUFFIX);
		resolver.setTemplateMode(this.environment.getProperty("mode", "xml"));
		resolver.setCharacterEncoding(this.environment.getProperty("encoding", "UTF-8"));
		resolver.setCacheable(this.environment.getProperty("cache", Boolean.class, true));
		return resolver;
	}

}
