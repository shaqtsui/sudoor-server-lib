package net.gplatform.sudoor.server.cxf;

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
