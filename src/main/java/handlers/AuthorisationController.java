package handlers;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

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
  @Path("auth_redirect")
  public Response redirectAuthorisation(@Context HttpServletRequest request) {
    RESTDropbox dropbox = new RESTDropbox();

    HttpSession session = request.getSession(true);
    String sessionKey = "dropbox-auth-csrf-token";
    String redirectURI = "http://localhost:8080/DropboxDemo/";

    return dropbox.getAuthorisationLink(session, sessionKey, redirectURI);
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