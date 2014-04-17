package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import storage.LocalStorage;

@Path("/files/{file_name}")
public class File {

  // Returns the persistent access token
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getFile(@PathParam("file_name") String fileName) {
    return LocalStorage.getFile(fileName).toJSON();
  }
}