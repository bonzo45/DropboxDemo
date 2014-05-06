package storage.cloud.dropbox;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import model.FileMetadata;
import storage.SamFile;
import util.FileUtil;

public class DropboxFile extends SamFile {

  public DropboxFile(String relativeFilePath) {
    String fileName = FileUtil.extractName(relativeFilePath);
    String filePath = "/" + FileUtil.extractPath(relativeFilePath);
    metadata = new FileMetadata(fileName, filePath);
  }
  
  @Override
  public InputStream getInputStream() throws FileNotFoundException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public OutputStream getOutputStream() throws FileNotFoundException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addCopy(String place, String path) {
    // TODO Auto-generated method stub
    
  }

}
