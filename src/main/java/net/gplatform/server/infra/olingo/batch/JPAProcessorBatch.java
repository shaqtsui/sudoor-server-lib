package net.gplatform.server.infra.olingo.batch;

import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmEntityType;
import org.apache.olingo.odata2.api.edm.EdmNavigationProperty;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.api.uri.info.PostUriInfo;
import org.apache.olingo.odata2.core.batch.BatchHelper;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPAModelException;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.apache.olingo.odata2.jpa.processor.core.ODataEntityParser;
import org.apache.olingo.odata2.jpa.processor.core.access.data.JPAEntity;
import org.apache.olingo.odata2.jpa.processor.core.access.data.JPAEntityParser;
import org.apache.olingo.odata2.jpa.processor.core.access.data.JPAProcessorImpl;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Administrator on 14-1-6.
 */
public class JPAProcessorBatch extends JPAProcessorImpl {

	/**
	 * Duplicate here for processCreate use
	 */
	ODataJPAContext oDataJPAContext;
	EntityManager em;
	JPAEntityParser jpaEntityParser = new JPAEntityParser();

	public JPAProcessorBatch(ODataJPAContext oDataJPAContext) {
		super(oDataJPAContext);
		this.oDataJPAContext = oDataJPAContext;
		em = oDataJPAContext.getEntityManager();
	}

	@Override
	public Object process(final PostUriInfo createView, final InputStream content,
	                      final String requestedContentType) throws ODataJPAModelException,
			ODataJPARuntimeException {
		Object result = processCreate(createView, content, null, requestedContentType);
		return result;
	}

	@Override
	public Object process(final PostUriInfo createView, final Map<String, Object> content)
			throws ODataJPAModelException, ODataJPARuntimeException {
		Object result = processCreate(createView, null, content, null);
		return result;
	}

	private Map<String, List> getAssociateInfo(ODataEntry oDataEntry) {
		Map<String, List> res = new HashMap();

		Map<String, Object> props = oDataEntry.getProperties();
		for (String key : props.keySet()) {
			Object value = props.get(key);
			if (value instanceof List) {
				List lValue = ((List) value);
				if (lValue.size() > 0) {
					if (lValue.get(0) instanceof ODataEntry) {
						res.put(key, lValue);
					}
				}
			}
		}
		return res;
	}

	private void saveJPAEntity(JPAEntity jpaEntity, ODataJPAContext oDataJPAContext) {
		ODataContext oDataContext = oDataJPAContext.getODataContext();

		Integer contentId = (Integer) oDataContext.getParameter(BatchHelper.REQUEST_HEADER_CONTENT_ID);
		if (contentId == null) {
			contentId = 1;
		} else {
			contentId = contentId + 1;
		}

		oDataContext.setParameter(BatchHelper.REQUEST_HEADER_CONTENT_ID, contentId);
		oDataContext.setParameter(contentId + "", jpaEntity);
	}

	private void populateRelatedJPAEntity(JPAEntity jpaEntity,  ODataEntry oDataEntry, ODataJPAContext oDataJPAContext) {
		ODataContext oDataContext = oDataJPAContext.getODataContext();

		Map<String, List> assoInfo = getAssociateInfo(oDataEntry);
		if (assoInfo.size() > 0) {
			Set fieldNames = assoInfo.keySet();
			for (java.util.Iterator iterator = fieldNames.iterator(); iterator.hasNext(); ) {
				String fName = (String) iterator.next();
				List fValue = assoInfo.get(fName);

				List jpaObjectList = new ArrayList();
				for (int i = 0; i < fValue.size(); i++) {
					ODataEntry oEntry = (ODataEntry) fValue.get(i);
					String refId = oEntry.getMetadata().getUri().substring(1);
					JPAEntity refJPAEntity = (JPAEntity) oDataContext.getParameter(refId);
					Object refJPAObj = refJPAEntity.getJPAEntity();
					jpaObjectList.add(refJPAObj);
				}
				try {
					EdmNavigationProperty edmNavigationProperty = (EdmNavigationProperty) jpaEntity.getEdmEntitySet().getEntityType().getProperty(fName);

					Method accessModifiersWrite = jpaEntityParser.getAccessModifier(jpaEntity.getJPAEntity().getClass(), edmNavigationProperty,							JPAEntityParser.ACCESS_MODIFIER_SET);
					switch (edmNavigationProperty.getMultiplicity()) {
						case MANY:
							accessModifiersWrite.invoke(jpaEntity.getJPAEntity(), jpaObjectList);
							break;
						case ONE:
						case ZERO_TO_ONE:
							accessModifiersWrite.invoke(jpaEntity.getJPAEntity(), jpaObjectList.get(0));
							break;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Object processCreate(final PostUriInfo createView, final InputStream content,
	                             final Map<String, Object> properties,
	                             final String requestedContentType) throws ODataJPAModelException,
			ODataJPARuntimeException {
		try {

			final EdmEntitySet oDataEntitySet = createView.getTargetEntitySet();
			final EdmEntityType oDataEntityType = oDataEntitySet.getEntityType();
			final JPAEntity virtualJPAEntity = new JPAEntity(oDataEntityType, oDataEntitySet, oDataJPAContext);
			Object jpaEntity = null;

			if (content != null) {
				final ODataEntityParser oDataEntityParser = new ODataEntityParser(oDataJPAContext);
				final ODataEntry oDataEntry =
						oDataEntityParser.parseEntry(oDataEntitySet, content, requestedContentType, false);
				virtualJPAEntity.create(oDataEntry);
				populateRelatedJPAEntity(virtualJPAEntity, oDataEntry, oDataJPAContext);
				saveJPAEntity(virtualJPAEntity, oDataJPAContext);
			} else if (properties != null) {
				virtualJPAEntity.create(properties);
			} else {
				return null;
			}

			em.getTransaction().begin();
			jpaEntity = virtualJPAEntity.getJPAEntity();

			em.persist(jpaEntity);
			if (em.contains(jpaEntity)) {
				em.getTransaction().commit();

				return jpaEntity;

			}
		} catch (Exception e) {
			throw ODataJPARuntimeException.throwException(
					ODataJPARuntimeException.ERROR_JPQL_CREATE_REQUEST, e);
		}
		return null;
	}


}
