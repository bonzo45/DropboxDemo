package mediator;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import models.FileMetadata;
import storage.SamFile;

public interface LocalWebMediatorInterface {

  public Response newFile(String filePath, InputStream inputStream, FileMetadata metadata);
  public Response persistMetadata(SamFile source);
  public Response getFile(String filePath);
  public Response getDirectory(String path);
}
