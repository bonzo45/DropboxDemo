package mediator.local;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import model.FileMetadata;
import storage.local.LocalFile;

public interface LocalWebMediatorInterface {

  public Response newFile(String filePath, InputStream inputStream, FileMetadata metadata);

  public Response persistMetadata(LocalFile source);

  public Response getFile(String filePath);

  public Response getDirectory(String path);
}
