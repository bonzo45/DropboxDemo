package handlers;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
*/

import storage.LocalStorage;

@Path("/files")
public class Directory {

  // Returns the persistent access token
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getFiles(@PathParam("directory") String directory) {
    return LocalStorage.getDirectory("").toJSON();
  }
  
  /*
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(
      @FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {

    LocalStorage.newFile(contentDispositionHeader.getFileName(), fileInputStream);

    String output = "File saved... maybe...";

    return Response.status(200).entity(output).build();

  }
  */
}