package mediator.cloud.auth;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Response;

public interface DropboxAuthWebMediatorInterface {

  public Response getAuthorisationRedirect(HttpSession session, String sessionKey, String returnURI);
  
  public Response getAccessTokenRedirect(String state, String code);
}
