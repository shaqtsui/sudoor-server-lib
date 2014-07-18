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


import net.gplatform.sudoor.server.security.SSChecker;
import net.gplatform.sudoor.server.spring.SpringContextsUtil;

import org.apache.olingo.odata2.api.batch.BatchHandler;
import org.apache.olingo.odata2.api.batch.BatchRequestPart;
import org.apache.olingo.odata2.api.batch.BatchResponsePart;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderBatchProperties;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.exception.ODataNotImplementedException;
import org.apache.olingo.odata2.api.processor.ODataRequest;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.PathInfo;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.core.ODataJPAProcessorDefault;
import org.springframework.security.access.prepost.PreAuthorize;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 14-1-5.
 */
public class ODataJPAProcessorBatch extends ODataJPAProcessorDefault {

	public List jpaEntities = new ArrayList();

	public ODataJPAProcessorBatch(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
		jpaProcessor = new JPAProcessorBatch(oDataJPAContext);
	}

	/**
	 * @see org.apache.olingo.odata2.api.processor.part.BatchProcessor
	 */
	@Override
	public ODataResponse executeBatch(final BatchHandler handler, final String contentType, final InputStream content)
			throws ODataException {
		ODataResponse batchResponse;
		List<BatchResponsePart> batchResponseParts = new ArrayList<BatchResponsePart>();
		PathInfo pathInfo = getContext().getPathInfo();
		EntityProviderBatchProperties batchProperties = EntityProviderBatchProperties.init().pathInfo(pathInfo).build();
		List<BatchRequestPart> batchParts = EntityProvider.parseBatchRequest(contentType, content, batchProperties);
		for (BatchRequestPart batchPart : batchParts) {
			batchResponseParts.add(handler.handleBatchPart(batchPart));
		}
		batchResponse = EntityProvider.writeBatchResponse(batchResponseParts);
		return batchResponse;
	}

	/**
	 * @throws ODataNotImplementedException
	 * @see org.apache.olingo.odata2.api.processor.part.BatchProcessor
	 */
	@Override
	public BatchResponsePart executeChangeSet(final BatchHandler handler, final List<ODataRequest> requests)
			throws ODataException {
		List<ODataResponse> responses = new ArrayList<ODataResponse>();
		for (ODataRequest request : requests) {
			//SS Check
			SSChecker ssChecker = (SSChecker) SpringContextsUtil.getBean("SSChecker");
			ssChecker.check(request, request.getMethod().name());

			ODataResponse response = handler.handleRequest(request);
			if (response.getStatus().getStatusCode() >= HttpStatusCodes.BAD_REQUEST.getStatusCode()) {
				// Rollback
				List<ODataResponse> errorResponses = new ArrayList<ODataResponse>(1);
				errorResponses.add(response);
				return BatchResponsePart.responses(errorResponses).changeSet(false).build();
			}
			responses.add(response);
		}
		return BatchResponsePart.responses(responses).changeSet(true).build();
	}
}
