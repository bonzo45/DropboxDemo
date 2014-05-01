package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import models.DirectoryMetadata;
import storage.LocalStorage;

@Path("/directory")
public class DirectoryController {

  private static Logger LOG = Logger.getLogger(DirectoryController.class);

  /**
   * Returns JSON detailing all of the files in a directory.
   * 
   * @param directory
   *          - path to the directory e.g. "images/directory1"
   * @return
   */
  @GET
  @Path("{directory}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFiles(@PathParam("directory") String directoryString) {
    DirectoryMetadata directory = LocalStorage.getDirectory(directoryString);

    // If the directory is invalid
    if (directory == null) {
      return Response.status(404).entity("{error: \"Invalid Directory\"}").build();
    }

    return Response.ok(directory.toJSON()).build();
  }
}