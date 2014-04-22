package net.gplatform.server.infra.olingo.batch;

import net.gplatform.server.infra.spring.SpringContextsUtil;
import net.gplatform.server.infra.ss.SSChecker;
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
