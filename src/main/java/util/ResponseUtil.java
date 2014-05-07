package util;

import javax.ws.rs.core.Response;

public class ResponseUtil {

  public static boolean isOk(Response response) {
    int code = response.getStatus();
    return (200 <= code && code < 300);
  }
  
  public static boolean isRedirect(Response response) {
    int code = response.getStatus();
    return (300 <= code && code < 400);
  }
}
