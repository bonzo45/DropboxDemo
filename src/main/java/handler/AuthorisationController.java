package handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import mediator.cloud.auth.DropboxAuthWebMediator;
import mediator.cloud.auth.DropboxAuthWebMediatorInterface;

import org.apache.log4j.Logger;

import storage.cloud.dropbox.Dropbox;
import util.ResponseUtil;

/**
 * All of the handlers required for authenticating a Dropbox account with this web application.
 * 
 */
@Path("auth")
public class AuthorisationController {

  private static Logger LOG = Logger.getLogger(Dropbox.class);

  /**
   * A way of storing Dropbox (DbxWebAuth) objects to persist state between the two calls.
   * 
   * Usage: - DbxWebAuth object is added when the user is re-directed to dropbox. - DbxWebAuth object is removed when they are returned to the site.
   * 
   * Known Issues: If the user is never returned the object is never deleted -> MEMORY LEAK
   * 
   * Ignored in this demo as each following state overwrites the last.
   */
  public static Map<Integer, Dropbox> dbxStates = new ConcurrentHashMap<Integer, Dropbox>();

  /**
   * Redirects the user to the Dropbox authentication page. This page then returns the user to the application.
   * 
   * @param request
   *          - used to extract the session for Dropbox to store the key in.
   * @return
   */
  @GET
  @Path("redirect_to_dropbox")
  public Response redirectAuthorisation(@Context HttpServletRequest request) {
    LOG.info("Redirecting to Dropbox Authorisation");
    Dropbox dropbox = new Dropbox();
    DropboxAuthWebMediatorInterface mediator = new DropboxAuthWebMediator(dropbox);

    // Get Session
    HttpSession session = request.getSession(true);
    // Where in the session object should the key be stored?
    String sessionKey = "dropbox-auth-csrf-token";
    // Where shall we return from the Dropbox Authentication?
    String returnURI = "http://localhost:8080/DropboxDemo/dropbox";

    Response result = mediator.getAuthorisationRedirect(session, sessionKey, returnURI);
    
    if (ResponseUtil.isRedirect(result)) {
      // Store the state so we can do a CSRF check when the user returns.
      dbxStates.put(1, dropbox);
    }
    
    return result;
  }
}