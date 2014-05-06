package storage.cloud.dropbox;

import util.FileUtil;

public class DropboxFile {
  
  public DropboxFile(String relativeFilePath) {
    String fileName = FileUtil.extractName(relativeFilePath);
    String filePath = "/" + FileUtil.extractPath(relativeFilePath);
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