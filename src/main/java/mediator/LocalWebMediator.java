package mediator;

import jackson.StockJsonConverter;
import jackson.WebJsonConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.security.ProviderException;

import javax.ws.rs.core.Response;

import model.DirectoryMetadata;
import model.FileMetadata;
import storage.AbstractFile;
import storage.local.LocalFile;
import storage.local.LocalFileStore;

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
  public Response persistMetadata(LocalFile file) {
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
      AbstractFile file = storage.getFile(filePath);
      String content = StockJsonConverter.getJSONString(file.getMetadata());
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

  @Override
  public Response getDirectory(String path) {
    try {
      DirectoryMetadata metadata = storage.getDirectory(path);
      WebJsonConverter converter = new WebJsonConverter();
      String content = converter.getJSONString(metadata);
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