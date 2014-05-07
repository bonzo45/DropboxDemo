package handler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import mediator.cloud.auth.DropboxAuthWebMediator;
import mediator.cloud.auth.DropboxAuthWebMediatorInterface;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;

import storage.cloud.dropbox.Dropbox;

@Path("")
public class RootController {

  private static Logger LOG = Logger.getLogger(RootController.class);

  /**
   * When the user returns from Dropbox Authentication they are sent here.
   * 
   * @param state
   *          - used for CSRF detection
   * @param authenticationCode
   *          - Authentication Code used to get Access Token
   * @return
   */
  @GET
  public Response backFromDropbox(@QueryParam("state") String state, @QueryParam("code") String authenticationCode) {

    // If returning from Dropbox authentication
    if (state != null && authenticationCode != null && !AuthorisationController.dbxStates.isEmpty()) {
      LOG.info("User has returned from Dropbox authentication.");
      // Retrieve the Dropbox 'State' from storage.
      Dropbox dropbox = AuthorisationController.dbxStates.remove(1);
      DropboxAuthWebMediatorInterface mediator = new DropboxAuthWebMediator(dropbox);
      return mediator.getAccessTokenRedirect(state, authenticationCode);

    } else {
      return Response.ok(new Viewable("/index.ftl")).build();
    }
  }
}