package models;

import java.util.ArrayList;
import java.util.List;

public class DirectoryModel {

  private List<FileModel> files;

  public DirectoryModel() {
    files = new ArrayList<FileModel>();
  }

  public void addFile(FileModel file) {
    files.add(file);
  }
  
  public String toJSON() {
    String result = "";
    for (FileModel file : files) {
      result += file.toJSON();
      result += ",";
    }
    return "[" + result.substring(0, result.length() - 1) + "]";
  }
  
}