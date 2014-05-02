package mediator;

import jackson.JsonConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import javax.ws.rs.core.Response;

import models.FileMetadata;
import storage.LocalFileStore;
import storage.SamFile;

public class LocalWebMediator implements LocalWebMediatorInterface {

  private LocalFileStore storage;

  public LocalWebMediator(LocalFileStore storage) {
    this.storage = storage;
  }

  @Override
  public Response newFile(String filePath, InputStream inputStream, FileMetadata metadata) {
    try {
      storage.newFile(filePath, inputStream, metadata);
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
  public Response persistMetadata(SamFile file) {
    try {
      storage.persistMetadata(file);
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
  public Response getFile(String filePath) {
    try {
      SamFile file = storage.getFile(filePath);
      String content = JsonConverter.getJSONString(file.getMetadata());
      return Response.ok().entity(content).build();
      
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