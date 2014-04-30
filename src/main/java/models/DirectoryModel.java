package models;

import jackson.JsonConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * An object storing the information associated with a Directory (a list of files).
 */
public class DirectoryModel {

  // Models corresponding to all of the files in the directory.
  private List<FileModel> files;

  public DirectoryModel() {
    files = new ArrayList<FileModel>();
  }

  /**
   * Add a file to this model.
   * 
   * @param file
   */
  public void addFile(FileModel file) {
    files.add(file);
  }

  /**
   * Returns a JSON representation of a directory.
   * 
   * @return
   */
  public String toJSON() {
    String result = "";
    for (FileModel file : files) {
      result += JsonConverter.getJSONString(file);
      result += ",";
    }

    if (result.equals("")) {
      return "[]";
    }

    return "[" + result.substring(0, result.length() - 1) + "]";
  }

}