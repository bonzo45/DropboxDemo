package util;

import java.io.File;

public class FileUtil {

  /**
   * Separates the file name from the rest of the path.
   * 
   * @param filePath
   * @return
   */
  public static String extractName(String filePath) {
    File f = new File(filePath);
    return f.getName();
  }

  /**
   * Separates the file path from the file name.
   * 
   * @param filePath
   * @return
   */
  public static String extractPath(String filePath) {
    File f = new File(filePath);
    String result = f.getParent();
    if (result == null) {
      return "";
    }
    return result;
  }
}