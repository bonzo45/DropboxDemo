package handlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dropbox.RESTDropbox;

/**
 * This simple web application has no user log in or authentication. If it did
 * this path should be changed to /user/{user_name} rather than simply supplying
 * the access token. The access token could be looked up from the user name.
 * 
 */
@Path("/account_details")
public class AccountController {

  /**
   * Returns JSON String containing the Account Details of a Dropbox Account.
   * 
   * @param accessToken
   * @return
   */
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String getAccountDetails(@QueryParam("access_token") String accessToken) {
    RESTDropbox dropbox = new RESTDropbox(accessToken);
    return dropbox.getAccountDetailsAsJson();
  }
}