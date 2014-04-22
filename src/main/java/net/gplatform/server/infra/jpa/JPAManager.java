package net.gplatform.server.infra.jpa;

import net.gplatform.server.infra.spring.SpringContextsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Administrator on 14-3-5.
 */
public class JPAManager extends HttpServlet {
	final Logger logger = LoggerFactory.getLogger(JPAManager.class);
	public final static String REFRESH_L1_CACHE = "refresh_l1_cache";
	public final static String REFRESH_L2_CACHE = "refresh_l2_cache";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		logger.debug("Enter JPAManager with parameters: {}", req.getParameterMap());

		if (req.getParameter(REFRESH_L2_CACHE) != null) {
			EntityManagerFactory factory = (EntityManagerFactory) SpringContextsUtil.getBean(Constants.ENTITY_MANAGER_FACTORY_ID);
			factory.getCache().evictAll();
			logger.debug("Successfully Evict L2 Cache");
			Writer out = resp.getWriter();
			out.write("Successfully Evict L2 Cache\n");
		}
	}
}
