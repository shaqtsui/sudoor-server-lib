package net.gplatform.sudoor.server.olingo.batch;

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


import net.gplatform.sudoor.server.jpa.Constants;
import net.gplatform.sudoor.server.spring.SpringContextsUtil;

import org.apache.olingo.odata2.api.ODataService;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.api.edm.provider.EdmProvider;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAAccessFactory;
import org.apache.olingo.odata2.jpa.processor.api.factory.ODataJPAFactory;

import javax.persistence.EntityManagerFactory;

/**
 * Created by Administrator on 13-12-26.
 */
public class JPAServiceFactory extends ODataServiceFactory {


	public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {
		ODataJPAContext oDataJPAContext = getODataJPAContext();

		EntityManagerFactory factory = (EntityManagerFactory) SpringContextsUtil.getBean(Constants.ENTITY_MANAGER_FACTORY_ID);

		oDataJPAContext.setEntityManagerFactory(factory);
		oDataJPAContext.setPersistenceUnitName(Constants.DEFAULT_ENTITY_UNIT_NAME);

		return oDataJPAContext;
	}

	private ODataJPAContext oDataJPAContext;
	private ODataContext oDataContext;

	/**
	 * Overwrite to use ODataJPAProcessorBatch
	 */
	@Override
	public final ODataService createService(final ODataContext ctx) throws ODataException {

		oDataContext = ctx;

		// Initialize OData JPA Context
		oDataJPAContext = initializeODataJPAContext();

		validatePreConditions();

		ODataJPAFactory factory = ODataJPAFactory.createFactory();
		ODataJPAAccessFactory accessFactory = factory.getODataJPAAccessFactory();

		// OData JPA Processor
		if (oDataJPAContext.getODataContext() == null) {
			oDataJPAContext.setODataContext(ctx);
		}

		ODataJPAProcessorBatch odataJPAProcessor = new ODataJPAProcessorBatch(oDataJPAContext);

		// OData Entity Data Model Provider based on JPA
		EdmProvider edmProvider = accessFactory.createJPAEdmProvider(oDataJPAContext);

		return createODataSingleProcessorService(edmProvider, odataJPAProcessor);
	}

	private void validatePreConditions() throws ODataJPARuntimeException {

		if (oDataJPAContext.getEntityManagerFactory() == null) {
			throw ODataJPARuntimeException.throwException(ODataJPARuntimeException.ENTITY_MANAGER_NOT_INITIALIZED, null);
		}

	}

	/**
	 * @return an instance of type {@link org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext}
	 * @throws org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException
	 */
	public final ODataJPAContext getODataJPAContext() throws ODataJPARuntimeException {
		if (oDataJPAContext == null) {
			oDataJPAContext = ODataJPAFactory.createFactory().getODataJPAAccessFactory().createODataJPAContext();
		}
		if (oDataContext != null) {
			oDataJPAContext.setODataContext(oDataContext);
		}
		return oDataJPAContext;

	}


}
