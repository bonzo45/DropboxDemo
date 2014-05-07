package mediator;

import javax.ws.rs.core.Response;

import storage.cloud.CloudFile;
import storage.local.LocalFile;

public interface CloudWebMediatorInterface {

  /**
   * Wraps around a CloudFileStore download, building an appropriate HTTP response.
   * 
   * @param source
   * @param dest
   * @return
   */
  public Response download(CloudFile source, LocalFile dest);

  /**
   * Wraps around a CloudFileStore upload, building an appropriate HTTP response.
   * @param source
   * @param dest
   * @return
   */
  public Response upload(LocalFile source, CloudFile dest);

}
