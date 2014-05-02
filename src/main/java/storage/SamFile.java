package storage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import models.FileMetadata;

public abstract class SamFile {

  protected FileMetadata metadata;

  public abstract InputStream getInputStream() throws FileNotFoundException;

  public abstract OutputStream getOutputStream() throws FileNotFoundException;

  public String getFileName() {
    return metadata.getName();
  }

  public String getFilePath() {
    return metadata.getPath();
  }

  public String getFullPath() {
    return getFilePath() + getFileName();
  }

  public FileMetadata getMetadata() {
    return metadata;
  };

  public void setMetadata(FileMetadata metadata) {
    this.metadata = metadata;
  };

  public abstract void addCopy(String place, String path);

  @Override
  public String toString() {
    return getFullPath();
  }
  
}