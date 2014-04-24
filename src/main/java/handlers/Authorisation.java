package handlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dropbox.RESTDropbox;

//Sets the path to base URL + /hello
@Path("/auth")
public class Authorisation {

  // Returns this applications authorisation link...
  @GET
  @Path("auth_link")
  @Produces(MediaType.APPLICATION_JSON)
  public String getAuthorisationLinkJSON() {
    RESTDropbox dropbox = new RESTDropbox();
    String auth_link = dropbox.getAuthorisationLink();
    return "{\"auth_link\": \"" + auth_link + "\"}";
  }
  
	// Returns the persistent access token
	@GET
	@Path("access_token/{authorisation_code}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccessToken(@PathParam("authorisation_code") String authorisationCode) {
		RESTDropbox dropbox = new RESTDropbox();
		return "{\"access_token\": \"" + dropbox.getAccessToken(authorisationCode) + "\"}";
	}
}