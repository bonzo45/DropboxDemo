package handlers;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import dropbox.RESTDropbox;
import storage.LocalStorage;

@Path("/files/{file_name}")
public class File {

  // Returns the metadata for a file
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getFile(@PathParam("file_name") String fileName) {
    System.err.println("File Information: " + fileName);
    return LocalStorage.getMetaData(fileName).toJSON();
  }
  
  // Uploads a file
  @POST
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile(
      @FormDataParam("file") InputStream fileInputStream,
      @FormDataParam("file") FormDataContentDisposition contentDispositionHeader,
      @PathParam("file_name") String fileName) {

    LocalStorage.newFile(fileName, fileInputStream);
    String output = "File saved... maybe...";
    return Response.status(200).entity(output).build();
  }
  
  // Pushes a file to Dropbox
  @PUT
  @Path("to_dropbox")
  public Response serverToDropbox(@QueryParam("access_token") String accessToken, @PathParam("file_name") String fileName) {
    RESTDropbox dropbox = new RESTDropbox(accessToken);
    dropbox.upload(fileName, "/" + fileName);
    return Response.status(200).entity("Probably went alright...").build();
  }
  
  // Pulls a file from Dropbox
  @PUT
  @Path("from_dropbox")
  public Response dropboxToServer(@QueryParam("access_token") String accessToken, @PathParam("file_name") String fileName) {
    RESTDropbox dropbox = new RESTDropbox(accessToken);
    dropbox.download("/" + fileName, fileName);
    return Response.status(200).entity("Probably went alright...").build();
  }
}