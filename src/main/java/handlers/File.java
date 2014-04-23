package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import storage.LocalStorage;

@Path("/files")
public class File {

  // Returns the persistent access token
  @GET
  @Path("{file_name}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getFile(@PathParam("file_name") String fileName) {
    System.err.println("File Information: " + fileName);
    return LocalStorage.getMetaData(fileName).toJSON();
  }
}