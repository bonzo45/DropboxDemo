package handlers;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import storage.LocalStorage;

@Path("/upload")
public class Upload {
  
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(
      @FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

    LocalStorage.newFile(contentDispositionHeader.getFileName(), fileInputStream);

    String output = "File saved... maybe...";

    return Response.status(200).entity(output).build();

  }
}