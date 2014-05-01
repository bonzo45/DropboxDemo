package things;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class SamFile {

  public abstract InputStream getInputStream();
  public abstract OutputStream getOutputStream();
  
  public abstract String getFileName();
  public abstract String getFilePath();
  public String getFullPath() {
    return getFilePath() + getFileName();
  }
  
  @Override
  public String toString() {
    return getFullPath();
  }
}
