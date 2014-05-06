package storage.dropbox;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import models.FileMetadata;
import storage.SamFile;
import util.FileUtil;

public class SamDropboxFile extends SamFile {
  
  public SamDropboxFile(String relativeFilePath) {
    String fileName = FileUtil.extractName(relativeFilePath);
    String filePath = FileUtil.extractPath(relativeFilePath);
    metadata = new FileMetadata(fileName, filePath);
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
  public void addCopy(String place, String path) {
    throw new UnsupportedOperationException();
  }
}