package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import mediator.LocalWebMediator;
import mediator.LocalWebMediatorInterface;

import org.apache.log4j.Logger;

import storage.local.LocalStorage;

@Path("directory")
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
  @Path("{directory: .*}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDirectory(@PathParam("directory") String path) {
    LOG.info("Directory Requested: " + path);
    LocalStorage localStore = new LocalStorage();
    LocalWebMediatorInterface mediator = new LocalWebMediator(localStore);
    Response response = mediator.getDirectory(path);
    return response;
  }
}