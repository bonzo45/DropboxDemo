package storage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public abstract class SamFile {

  public abstract InputStream getInputStream() throws FileNotFoundException;
  public abstract OutputStream getOutputStream() throws FileNotFoundException;
  
  public abstract String getFileName();
  public abstract String getFilePath();
  public String getFullPath() {
    return getFilePath() + getFileName();
  }
  
  public abstract void addCopy(String place, String path);
  
  public abstract void generateMetadata(Map<String, String> copies);
  
  @Override
  public String toString() {
    return getFullPath();
  }
}