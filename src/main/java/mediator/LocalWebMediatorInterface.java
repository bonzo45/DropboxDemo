package mediator;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import storage.SamFile;
import storage.local.SamLocalFile;
import models.FileMetadata;

public interface LocalWebMediatorInterface {

  public Response newFile(String filePath, InputStream inputStream, FileMetadata metadata);
  public Response persistMetadata(SamFile source);
}
