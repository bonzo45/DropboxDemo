package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * An object storing the information associated with a Directory (a list of files).
 */
public class DirectoryMetadata {

  // Models corresponding to all of the files in the directory.
  private List<FileMetadata> files;

  public DirectoryMetadata() {
    files = new ArrayList<FileMetadata>();
  }

  @JsonCreator
  public DirectoryMetadata(@JsonProperty("files") List<FileMetadata> files) {
    this.files = files;
  }
  
  /**
   * Add a file to this model.
   * 
   * @param file
   */
  public void addFile(FileMetadata file) {
    files.add(file);
  }
  
  public List<FileMetadata> getFiles() {
    return files;
  }

  public void setFiles(List<FileMetadata> files) {
    this.files = files;
  }

}