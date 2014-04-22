package net.gplatform.server.infra.olingo;

import net.gplatform.server.infra.jpa.Constants;
import net.gplatform.server.infra.spring.SpringContextsUtil;


import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;


import javax.persistence.EntityManagerFactory;

/**
 * Created by Administrator on 13-12-26.
 */
public class JPAServiceFactory extends ODataJPAServiceFactory {

	@Override
	public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = getODataJPAContext();

		EntityManagerFactory factory = (EntityManagerFactory) SpringContextsUtil.getBean(Constants.ENTITY_MANAGER_FACTORY_ID);

		oDataJPAContext.setEntityManagerFactory(factory);
		oDataJPAContext.setPersistenceUnitName(Constants.DEFAULT_ENTITY_UNIT_NAME);

		return oDataJPAContext;
	}
}
