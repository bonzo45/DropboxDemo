package handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import storage.dropbox.RESTDropbox;

/**
 * All of the handlers required for authenticating a Dropbox account with this web application.
 * 
 */
@Path("auth")
public class AuthorisationController {

  private static Logger LOG = Logger.getLogger(RESTDropbox.class);

  /**
   * A way of storing Dropbox (DbxWebAuth) objects to persist state between the two calls.
   * 
   * Usage: - DbxWebAuth object is added when the user is re-directed to dropbox. - DbxWebAuth object is removed when they are returned to the site.
   * 
   * Known Issues: If the user is never returned the object is never deleted -> MEMORY LEAK
   * 
   * Ignored in this demo as each following state overwrites the last.
   */
  public static Map<Integer, RESTDropbox> dbxStates = new ConcurrentHashMap<Integer, RESTDropbox>();

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
    RESTDropbox dropbox = new RESTDropbox();

    // Get Session
    HttpSession session = request.getSession(true);
    // Where in the session object should the key be stored?
    String sessionKey = "dropbox-auth-csrf-token";
    // Where shall we return from the Dropbox Authentication?
    String returnURI = "http://localhost:8080/DropboxDemo/dropbox";

    // Get Dropbox Authentication URL
    String dropboxRedirect = dropbox.getAuthorisationRedirect(session, sessionKey, returnURI);

    // Store the state so we can do a CSRF check when the user returns.
    dbxStates.put(1, dropbox);

    // Return the re-direct
    try {
      return Response.seeOther(new URI(dropboxRedirect)).build();
    } catch (URISyntaxException e) {
      String error = "Dropbox Redirect URISyntaxException: URI Dropbox has generated is invalid.";
      LOG.error(error, e);
      return Response.status(500).entity(error).build();
    }
  }
}