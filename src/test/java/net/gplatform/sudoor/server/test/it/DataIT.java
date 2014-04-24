package net.gplatform.sudoor.server.test.it;

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Administrator on 14-3-28.
 */
public class DataIT {

	public final String REST_SERVICE_URL = "http://localhost:8080/sudoor-server-lib/data/rest";

	public final String ODATA_SERVICE_URL = "http://localhost:8080/sudoor-server-lib/data/odata.svc";

	Client client = null;

	@BeforeClass
	public void init(){
		client = ClientBuilder.newBuilder().register(JacksonFeatures.class).build();
	}

	@Test
	public void retrieveODataMetaData() {
		WebTarget target = client.target(ODATA_SERVICE_URL).path("/$metadata");
		Response response = target.request(MediaType.APPLICATION_XML_TYPE).get();
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);

		System.out.println("retrieveODataMetaData() statusCode:" + statusCode);
		System.out.println("retrieveODataMetaData() content:" + content);
		assert (statusCode == 200);
	}

	@Test
	public void retrieveFile1() {
		WebTarget target = client.target(REST_SERVICE_URL).path("/tools/fileupload/File/1");
		Response response = target.request(MediaType.WILDCARD_TYPE).get();
		int statusCode = response.getStatus();
		String content = response.readEntity(String.class);

		System.out.println("retrieveFile1() statusCode:" + statusCode);
		//System.out.println("retrieveFile1() content:" + content);
		assert (statusCode == 200);
	}
}
