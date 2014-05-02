package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import storage.dropbox.RESTDropbox;

/**
 * This simple web application has no user log in or authentication. If it did this path should be changed to /user/{user_name}. Rather than simply supplying the access token it
 * could be looked up from the user name.
 * 
 */
@Path("/account_details")
public class AccountController {

  private static Logger LOG = Logger.getLogger(Root.class);

  /**
   * Returns JSON String containing the Account Details of a Dropbox Account.
   * 
   * @param accessToken
   * @return
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public String getAccountDetails(@QueryParam("access_token") String accessToken) {
    RESTDropbox dropbox = new RESTDropbox(accessToken);
    return dropbox.getAccountDetails().toJson();
  }
}