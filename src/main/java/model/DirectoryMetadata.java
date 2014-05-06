package model;

import jackson.StockJsonConverter;

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

  /**
   * Returns a JSON representation of a directory.
   * 
   * @return
   */
  public String toJSON() {
    String result = "";
    for (FileMetadata file : files) {
      result += StockJsonConverter.getJSONString(file);
      result += ",";
    }

    if (result.equals("")) {
      return "[]";
    }

    return "[" + result.substring(0, result.length() - 1) + "]";
  }

  
  public List<FileMetadata> getFiles() {
    return files;
  }

  public void setFiles(List<FileMetadata> files) {
    this.files = files;
  }

}