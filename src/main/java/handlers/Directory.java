package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import storage.LocalStorage;

@Path("/directory")
public class Directory {

  // Returns all the files in a directory
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getFiles(@PathParam("directory") String directory) {
    return LocalStorage.getDirectory("").toJSON();
  }
}