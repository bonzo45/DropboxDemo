package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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