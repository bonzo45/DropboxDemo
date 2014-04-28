package handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;

import dropbox.RESTDropbox;

/**
 * All of the handlers required for authenticating a Dropbox account with this
 * web application.
 * 
 */
@Path("/auth")
public class AuthorisationController {

  /**
   * Return JSON containing this application's authorisation link.
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

  // Generate a random string to use as a CSRF token.
  private static String generateCSRFToken() {
    byte[] b = new byte[18];
    new SecureRandom().nextBytes(b);
    return Base64.encodeBase64URLSafeString(b);
  }

  @GET
  @Path("redirect_to_dropbox")
  public Response redirectAuthorisation(@Context HttpServletRequest request)
      throws URISyntaxException {
    RESTDropbox dropbox = new RESTDropbox();

    // Get Session
    HttpSession session = request.getSession(true);
    // Generate new Session Key
    String sessionKey = "dropbox-auth-csrf-token";
    // Return here when Dropbox has finished authenticating.
    String returnURI = "http://localhost:8080/DropboxDemo/dropbox/auth/back_from_dropbox";

    String dropboxRedirect = dropbox.getAuthorisationRedirect(session, sessionKey, returnURI);
    // Re-direct user to dropbox, setting the session key as a cookie so we can
    // check it later.
    return Response.seeOther(new URI(dropboxRedirect))
        .cookie(new NewCookie("csrf", sessionKey)).build();
  }

  @GET
  @Path("back_from_dropbox")
  public Response backFromDropbox(@Context HttpServletRequest request,
      @CookieParam(value = "csrf") String sessionKey,
      @QueryParam("state") String dropboxSessionKey,
      @QueryParam("code") String authenticationCode) {
    RESTDropbox dropbox = new RESTDropbox();

    // Get Session
    HttpSession session = request.getSession(true);
 
    // Check Session Key matches
    if (sessionKey.equals(dropboxSessionKey)) {
      
    }
    
    // TODO: http://dropbox.github.io/dropbox-sdk-java/api-docs/v1.7.x/com/dropbox/core/DbxWebAuth.html

    
    return null;
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