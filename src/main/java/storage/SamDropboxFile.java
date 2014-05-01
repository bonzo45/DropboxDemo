package storage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import util.FileUtil;

public class SamDropboxFile extends SamFile {

  private String fileName;
  private String filePath;
  
  public SamDropboxFile(String relativeFilePath) {
    this.fileName = FileUtil.extractName(relativeFilePath);
    this.filePath = FileUtil.extractPath(relativeFilePath);
  }
  
  @Override
  public InputStream getInputStream() throws FileNotFoundException {
    throw new UnsupportedOperationException();
  }

  @Override
  public OutputStream getOutputStream() throws FileNotFoundException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  @Override
  public String getFilePath() {
    return filePath;
  }

  @Override
  public void addCopy(String place, String path) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void generateMetadata(Map<String, String> copies) {
    throw new UnsupportedOperationException();
  }
}