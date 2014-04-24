package net.gplatform.sudoor.server.tools.fileupload;

import net.gplatform.sudoor.server.jaxrs.cache.Cacheable;

import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;


/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-11-18
 * Time: 下午4:31
 * To change this template use File | Settings | File Templates.
 */

@Path("/tools/fileupload/File")
public class FileResource {
	@Autowired
	private FileRepository fileRepository;

    @Cacheable(cc="public, max-age=5184000")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@Path("{fileId}")
	public Response getFile(@PathParam("fileId") String fileId) {
		File f = fileRepository.findOne(new Long(fileId));
	    if(f == null){
		    return Response.noContent().build();
	    }else{
		    return Response.ok(f.getData()).build();
	    }
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addFile(@FormDataParam("file") FormDataContentDisposition fileDetail, @FormDataParam("file") InputStream isFile) {
		File f = new File();
		f.setName(fileDetail.getFileName());
		try {
			f.setData(IOUtils.toByteArray(isFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileRepository.saveAndFlush(f);
		URI  createdUri = URI.create("tools/fileupload/File/" + f.getId());
		f.setData(null);
		f.setUri(createdUri.getPath());
		return Response.created(createdUri).entity(f).build();
	}
}
