package storage;

import model.FileMetadata;

public abstract class AbstractFile {

  protected FileMetadata metadata;

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

}