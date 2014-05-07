package mediator.cloud;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import javax.ws.rs.core.Response;

import storage.cloud.CloudFile;
import storage.cloud.CloudFileStore;
import storage.local.LocalFile;

public class CloudWebMediator implements CloudWebMediatorInterface {

  private CloudFileStore storage;

  public CloudWebMediator(CloudFileStore storage) {
    this.storage = storage;
  }

  @Override
  public Response download(CloudFile source, LocalFile dest) {
    try {
      storage.download(source, dest);
      return Response.ok().build();

    } catch (AccessDeniedException e) {
      return Response.status(500).build();

    } catch (FileNotFoundException e) {
      return Response.status(404).build();

    } catch (ProviderException e) {
      return Response.status(500).build();

    } catch (IOException e) {
      return Response.status(500).build();

    }
  }

  @Override
  public Response upload(LocalFile source, CloudFile dest) {
    try {
      storage.upload(source, dest);
      return Response.ok().build();

    } catch (AccessDeniedException e) {
      return Response.status(500).build();

    } catch (FileNotFoundException e) {
      return Response.status(404).build();

    } catch (ProviderException e) {
      return Response.status(500).build();

    } catch (IOException e) {
      return Response.status(500).build();

    }
  }

}
