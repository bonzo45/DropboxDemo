package storage.cloud.dropbox;

import model.FileMetadata;
import storage.cloud.CloudFile;
import util.FileUtil;

public class DropboxFile extends CloudFile {

  public DropboxFile(String relativeFilePath) {
    String fileName = FileUtil.extractName(relativeFilePath);
    String filePath = "/" + FileUtil.extractPath(relativeFilePath);
    metadata = new FileMetadata(fileName, filePath);
  }

}
