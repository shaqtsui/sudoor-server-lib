package net.gplatform.sudoor.server.cxf;

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

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Use this to dyna register cxf servlet, so no need to have
 * web-fragment.xml/web.xml
 * 
 * @author xufucheng
 *
 */
@Configuration
public class CxfServletRegister {
	
	@Bean
	public ServletRegistrationBean getCxfServletRegistrationBean() {
		ServletRegistrationBean cxfServletRegistrationBean = new ServletRegistrationBean(new CXFServlet(), "/data/ws/*");
		return cxfServletRegistrationBean;
	}
}
