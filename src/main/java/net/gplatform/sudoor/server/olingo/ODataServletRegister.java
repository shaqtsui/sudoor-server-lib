package net.gplatform.sudoor.server.olingo;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Use this to dyna register cxf servlet, so no need to have web-fragment.xml/web.xml
 * @author xufucheng
 *
 */
@Configuration
public class ODataServletRegister {
	@Bean
	public ServletRegistrationBean getODataServletRegistrationBean() {
		ServletRegistrationBean odataServletRegistrationBean = new ServletRegistrationBean(new CXFNonSpringJaxrsServlet(), "/data/odata.svc/*");
		Map<String, String> initParameters = new HashMap<String, String>();
		initParameters.put("javax.ws.rs.Application", "org.apache.olingo.odata2.core.rest.app.ODataApplication");
		initParameters.put("org.apache.olingo.odata2.service.factory", "net.gplatform.sudoor.server.olingo.JPAServiceFactory");
		odataServletRegistrationBean.setInitParameters(initParameters);
		return odataServletRegistrationBean;
	}
}
