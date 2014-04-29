package handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;

import dropbox.RESTDropbox;

/**
 * All of the handlers required for authenticating a Dropbox account with this
 * web application.
 * 
 */
@Path("/auth")
public class AuthorisationController {

  /**
   * A way of storing Dropbox (DbxWebAuth) objects to persist state between the
   * two calls. Usage: DbxWebAuth object is added when the user is re-directed
   * to dropbox. DbxWebAuth object is removed when they are returned to the
   * site.
   * 
   * Known Issues: If the user is never returned the object is never deleted ->
   * MEMORY LEAK Ignored in this demo as each following state overwrites the
   * last.
   */
  public static ConcurrentHashMap<Integer, RESTDropbox> dbxStates = new ConcurrentHashMap<>();

  /**
   * Return JSON containing this application's authorisation link.
   * 
   * NOTE: If using in a Web App, use redirectAuthorisation instead.
   * 
   * @return
   */
  @GET
  @Path("auth_link")
  @Produces(MediaType.APPLICATION_JSON)
  public String getAuthorisationLinkJSON() {
    RESTDropbox dropbox = new RESTDropbox();
    String auth_link = dropbox.getAuthorisationLink();
    return "{\"auth_link\": \"" + auth_link + "\"}";
  }

  /**
   * Redirects the user to the Dropbox authentication page. This page then
   * returns the user to the application.
   * 
   * @param request
   *          - Servlet request (used by Dropbox for some strange reason?)
   * @return
   * @throws URISyntaxException
   */
  @GET
  @Path("redirect_to_dropbox")
  public Response redirectAuthorisation(@Context HttpServletRequest request)
      throws URISyntaxException {
    RESTDropbox dropbox = new RESTDropbox();

    // Get Session
    HttpSession session = request.getSession(true);
    // Session Key's Name
    String sessionKey = "dropbox-auth-csrf-token";
    // Return here when Dropbox has finished authenticating.
    String returnURI = "http://localhost:8080/DropboxDemo/dropbox";

    // Get redirect URL
    String dropboxRedirect = dropbox.getAuthorisationRedirect(session,
        sessionKey, returnURI);
    // Store the Dropbox State so we can do a CSRF check when they return.
    dbxStates.put(1, dropbox);
    // Re-direct user to dropbox.
    return Response.seeOther(new URI(dropboxRedirect)).build();
  }

  /**
   * Return a persistent access token. NOTE: This should not be needed in
   * production as the access token should be stored.
   * 
   * @param authorisationCode
   * @return
   */
  @GET
  @Path("access_token/{authorisation_code}")
  @Produces(MediaType.APPLICATION_JSON)
  public String getAccessToken(
      @PathParam("authorisation_code") String authorisationCode) {
    RESTDropbox dropbox = new RESTDropbox();
    return "{\"access_token\": \"" + dropbox.getAccessToken(authorisationCode)
        + "\"}";
  }
}