package handler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;

import storage.cloud.dropbox.Dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWebAuth.ProviderException;

@Path("")
public class RootController {

  private static Logger LOG = Logger.getLogger(RootController.class);

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
  public Response backFromDropbox(@QueryParam("state") String token, @QueryParam("code") String authenticationCode) {
    Viewable result;

    // If returning from Dropbox authentication
    if (token != null && authenticationCode != null && !AuthorisationController.dbxStates.isEmpty()) {
      // Retrieve the Dropbox 'State' from storage.
      Dropbox dropbox = AuthorisationController.dbxStates.remove(1);

      // Reconstruct the parameter map - Jersey consumed it :-(
      HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
      String[] state = { token };
      String[] code = { authenticationCode };
      hashMap.put("state", state);
      hashMap.put("code", code);

      // Try to read the access token and catch any error that occurs.
      String accessToken = null;
      try {
        accessToken = dropbox.getAccessTokenRedirect(hashMap);
      } catch (BadRequestException e) {
        /* Bad Parameters */
        LOG.warn("Dropbox BadRequestException: Parameters not well formed. " + e.getMessage());
        return Response.status(400).entity("Parameters not well formed.").build();
      } catch (BadStateException e) {
        /* Bad State */
        LOG.warn("Dropbox BadStateException: Redirecting to authentication. " + e.getMessage());
        try {
          return Response.seeOther(new URI("")).build();
        } catch (URISyntaxException e1) {
          LOG.error("Dropbox BadStateException URISyntaxException: Could not redirect.");
          return Response.status(500).entity("Sorry... can't re-direct you.").build();
        }
      } catch (CsrfException e) {
        /* Cross Site Request Forgery Detected (CSRF Mismatch) */
        LOG.error("Dropbox CsrfException: CSRF Mismatch. " + e.getMessage());
        return Response.status(403).entity("CSRF Check Failed").build();
      } catch (NotApprovedException e) {
        /* User Declined Permission */
        LOG.info("Dropbox NotApprovedException: authorisation not approved by user.");
        return Response.status(200).entity("User denied access to Dropbox.").build();
      } catch (ProviderException e) {
        /* Problems with Dropbox */
        LOG.warn("Dropbox ProviderException: error communicating with Dropbox. " + e.getMessage());
        return Response.status(503).entity("Could not communicate with Dropbox").build();
      } catch (DbxException e) {
        /* General Catch All */
        LOG.warn("Dropbox DbxException: general exception thrown. " + e.getMessage());
        return Response.status(503).entity("Could not communicate with Dropbox").build();
      }
      result = new Viewable("/test.ftl", accessToken);
    } else {
      result = new Viewable("/test.ftl");
    }

    return Response.ok(result).build();
  }
}