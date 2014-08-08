package net.gplatform.sudoor.server;

/*
 * #%L
 * sudoor-lib
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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * This class as the entry point for application.
 * SpringBootServletInitializer.configure() is entry point for external
 * container deployment. main() is for spring boot jar direct start model.
 * 
 */
@Configuration
@EnableAutoConfiguration
@ImportResource({ "classpath:META-INF/cxf/cxf.xml", "classpath:META-INF/cxf/cxf-servlet.xml", "classpath*:META-INF/cxf/cxf-extension-*.xml",
		"classpath*:spring/**/*-config-*.xml" })
//TODO: Don't support multiple config, so can only config multiple value. Will change this once multiple config supported
@ComponentScan({ "net.gplatform.sudoor.server", "${application.basepackage}" })
@EntityScan({ "net.gplatform.sudoor.server", "${application.basepackage}" })
@EnableJpaRepositories({ "net.gplatform.sudoor.server", "${application.basepackage}" })
public class Application extends SpringBootServletInitializer {

	/*
	 * Used by spring boot
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}

}
