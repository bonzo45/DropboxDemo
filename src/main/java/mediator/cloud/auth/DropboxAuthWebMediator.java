package mediator.cloud.auth;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth.BadRequestException;
import com.dropbox.core.DbxWebAuth.BadStateException;
import com.dropbox.core.DbxWebAuth.CsrfException;
import com.dropbox.core.DbxWebAuth.NotApprovedException;
import com.dropbox.core.DbxWebAuth.ProviderException;

import storage.cloud.dropbox.Dropbox;

public class DropboxAuthWebMediator implements DropboxAuthWebMediatorInterface {

  private static Logger LOG = Logger.getLogger(DropboxAuthWebMediator.class);
  
  private Dropbox dropbox;

  public DropboxAuthWebMediator(Dropbox dropbox) {
    this.dropbox = dropbox;
  }

  @Override
  public Response getAuthorisationRedirect(HttpSession session, String sessionKey, String returnURI) {
    // Get Dropbox Authentication URL
    String dropboxRedirect = dropbox.getAuthorisationRedirect(session, sessionKey, returnURI);

    // Return the re-direct
    try {
      return Response.seeOther(new URI(dropboxRedirect)).build();
    } catch (URISyntaxException e) {
      LOG.error("URI Dropbox has generated is invalid!", e);
      return Response.status(500).build();
    }
  }

  @Override
  public Response getAccessTokenRedirect(String state, String code) {
    // Reconstruct the parameter map - Jersey consumed it :-(
    HashMap<String, String[]> hashMap = new HashMap<String, String[]>();
    String[] s = {state};
    String[] c = {code};
    hashMap.put("state", s);
    hashMap.put("code", c);

    // Try to read the access token and catch any error that occurs.
    String accessToken = null;
    try {
      accessToken = dropbox.getAccessTokenRedirect(hashMap);
      return Response.ok(new Viewable("/index.ftl", accessToken)).build();
      
    } catch (BadRequestException e) {
      LOG.error("Parameters not well formed.", e);
      return Response.status(400).build();
      
    } catch (BadStateException e) {
      LOG.warn("Bad State.", e);
      try {
        return Response.seeOther(new URI("")).build();
      } catch (URISyntaxException e1) {
        LOG.error("Could not redirect.", e);
        return Response.status(500).build();
      }
      
    } catch (CsrfException e) {
      LOG.error("CSRF Mismatch.", e);
      return Response.status(403).build();
      
    } catch (NotApprovedException e) {
      LOG.info("Authorisation not approved by user.", e);
      return Response.status(200).build();
      
    } catch (ProviderException e) {
      LOG.warn("Error communicating with Dropbox.", e);
      return Response.status(503).build();
      
    } catch (DbxException e) {
      LOG.warn("General exception thrown.", e);
      return Response.status(503).build();
      
    }
  }

}
