package handlers;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.glassfish.jersey.server.mvc.Viewable;

import dropbox.RESTDropbox;

@Path("/")
public class Root {
  /**
   * When the user returns from Dropbox Authentication they are sent here.
   * 
   * @param token
   *          - used for CSRF detection
   * @param authenticationCode
   *          - Authentication Code used to get Access Token
   * @return
   */
  @GET
  public Viewable backFromDropbox(@QueryParam("state") String token,
      @QueryParam("code") String authenticationCode) {

    // If returning from Dropbox authentication
    if (token != null && authenticationCode != null) {
      // Retrieve the Dropbox 'State' from storage.
      RESTDropbox dropbox = AuthorisationController.dbxStates.remove(1);

      // Reconstruct the parameter map - Jersey consumed it :-(
      HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
      String[] state = { token };
      String[] code = { authenticationCode };
      hashMap.put("state", state);
      hashMap.put("code", code);

      String accessToken = dropbox.getAccessTokenRedirect(hashMap);
    } else {

    }

    Viewable result = new Viewable("/test.ftl");
    return result;
  }
}
