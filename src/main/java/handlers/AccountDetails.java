package handlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dropbox.core.DbxAccountInfo;

import dropbox.RESTDropbox;

@Path("/account_details")
public class AccountDetails {

  // Returns account details
  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String getAccountDetails(@QueryParam("access_token") String accessToken) {
    RESTDropbox dropbox = new RESTDropbox();
    DbxAccountInfo info = dropbox.getAccountDetails(accessToken);
    String result = "";
    result += "\"name\": \"" + info.displayName + "\",";
    result += "\"country\": \"" + info.country + "\",";
    result += "\"user_id\": \"" + info.userId + "\"";
    return "{" + result + "}";
  }
}