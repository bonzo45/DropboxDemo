package handlers;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import dropbox.RESTDropbox;

//Sets the path to base URL + /hello
@Path("/access_token")
public class AccessToken {

	// Returns the persistent access token
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getAccessToken(@QueryParam("authorisation_code") String authorisationCode) {
		RESTDropbox dropbox = new RESTDropbox();
		return "{\"access_token\": \"" + dropbox.getAccessToken(authorisationCode) + "\"}";
	}
}