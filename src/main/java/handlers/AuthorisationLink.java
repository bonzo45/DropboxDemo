package handlers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dropbox.RESTDropbox;

@Path("/auth_link")
public class AuthorisationLink {

	// Returns this applications authorisation link...
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAuthorisationLinkJSON() {
		RESTDropbox dropbox = new RESTDropbox();
		String auth_link = dropbox.getAuthorisationLink();
		return "{\"auth_link\": \"" + auth_link + "\"}";
	}
}