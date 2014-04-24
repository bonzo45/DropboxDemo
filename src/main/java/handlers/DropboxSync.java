package handlers;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import dropbox.RESTDropbox;

@Path("/sync")
public class DropboxSync {
  
  @PUT
  @Path("{file_path}")
  public Response serverToDropbox(@QueryParam("access_token") String accessToken, @PathParam("file_path") String filePath) {
    RESTDropbox dropbox = new RESTDropbox(accessToken);
    dropbox.upload(filePath, "/" + filePath);
    return Response.status(200).entity("Probably went alright...").build();
  }
}